package io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.server;

import io.github.blackspherefollower.buttplug4j.client.ButtplugClient;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDevice;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClientDeviceFeature;
import io.github.blackspherefollower.buttplug4j.client.IConnectedEvent;
import io.github.blackspherefollower.buttplug4j.connectors.javax.websocket.common.ButtplugClientWSEndpoint;
import io.github.blackspherefollower.buttplug4j.utils.test.IntifaceEngineWrapper;
import io.github.blackspherefollower.buttplug4j.utils.test.WSDMClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;
import org.junit.jupiter.api.Test;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugClientWSServerMockTest {

    @Test
    public void TestConnect() throws Exception {
        int lport = (int) (Math.random() * 63000) + 1025;
        CompletableFuture<Boolean> testDone = new CompletableFuture<>();
        ButtplugClientWSServerExample server = new ButtplugClientWSServerExample(lport, testDone);
        Thread.sleep(500);

        try (IntifaceEngineWrapper wrapper = new IntifaceEngineWrapper(lport)) {
            Thread.sleep(500);
            WSDMClient wsdev = new WSDMClient(new URI("ws://localhost:" + wrapper.dport), "LVS-Fake", "A9816725B");
            testDone.get(10, TimeUnit.SECONDS);
            server.join();
            assertEquals(wsdev.messages.poll(), "Vibrate:10;");
            assertEquals(wsdev.messages.poll(), "Vibrate:0;");
        }

    }

    static class ButtplugClientWSServerExample {

        private final Server server;
        private final ServerConnector connector;

        public ButtplugClientWSServerExample(int port, CompletableFuture<Boolean> testDone) throws Exception {
            server = new Server();
            connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);
            CompletableFuture<Boolean> firstDevice = new CompletableFuture<>();

            // Setup the basic application "context" for this application at "/"
            // This is also known as the handler tree (in jetty speak)
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/*");
            server.setHandler(context);

            // Configure specific websocket behavior
            JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
            {
                // Configure default max size
                wsContainer.setDefaultMaxTextMessageBufferSize(65535);

                // Add websockets
                wsContainer.addEndpoint(ServerEndpointConfig.Builder.create(ButtplugClientWSEndpoint.class, "/{user}").configurator(new ServerEndpointConfig.Configurator() {
                    @Override
                    public <T> T getEndpointInstance(Class<T> endpointClass) {
                        if (endpointClass == ButtplugClientWSEndpoint.class) {
                            ButtplugClientWSServer client = new ButtplugClientWSServer("Java WS Server Buttplug Client");
                            client.setDeviceAdded(dev -> firstDevice.complete(true));
                            client.setOnConnected(new IConnectedEvent() {
                                @Override
                                public void onConnected(ButtplugClient client) {
                                    new Thread(() -> {
                                        try {
                                            if (client instanceof ButtplugClientWSServer) {
                                                Session s = ((ButtplugClientWSServer) client).getSession();
                                                s.getPathParameters().forEach((k, v) -> {
                                                    System.out.printf("param: %s -> %s\n", k, v);
                                                });
                                            }

                                            client.startScanning();

                                            firstDevice.get(5, TimeUnit.SECONDS);
                                            for (ButtplugClientDevice dev : client.getDevices()) {
                                                for (ButtplugClientDeviceFeature feat : dev.getDeviceFeatures().values()) {
                                                    if (feat.HasVibrate()) {
                                                        feat.VibrateFloat(0.5F).get();
                                                    }
                                                }
                                            }

                                            Thread.sleep(100);

                                            assertTrue(client.stopAllDevices());

                                            client.disconnect();
                                            server.stop();
                                            testDone.complete(true);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).start();
                                }
                            });
                            return (T) client;
                        }
                        return null;
                    }
                }).build());
            });
            server.start();
        }

        public URI getURI() {
            return server.getURI();
        }

        public void stop() throws Exception {
            server.stop();
        }

        public void join() throws InterruptedException {
            server.join();
        }
    }
}