package io.github.blackspherefollower.buttplug4j.client;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ButtplugClientWSServerMockTest {

    @Disabled
    @Test
    public void TestConnect() throws Exception {
        ButtplugClientWSServerExample server = new ButtplugClientWSServerExample(54321);
        server.join();
    }

    class ButtplugClientWSServerExample {

        private final Server server;
        private final ServerConnector connector;

        public ButtplugClientWSServerExample(int port) throws Exception {
            server = new Server();
            connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);

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
                        if(endpointClass == ButtplugClientWSEndpoint.class) {
                            ButtplugClientWSServer client = new ButtplugClientWSServer("Java WS Server Buttplug Client");
                            client.setOnConnected(new IConnectedEvent() {
                                @Override
                                public void onConnected(ButtplugClientWSEndpoint client) {
                                    new Thread(() -> {
                                        try {
                                            if( client instanceof ButtplugClientWSServer ) {
                                                Session s = ((ButtplugClientWSServer) client).getSession();
                                                s.getPathParameters().forEach((k,v) -> {
                                                    System.out.printf("param: %s -> %s\n", k, v);
                                                });
                                            }

                                            client.startScanning();

                                        Thread.sleep(5000);
                                        client.requestDeviceList();
                                        for (
                                                ButtplugClientDevice dev : client.getDevices()) {
                                            if (dev.getScalarVibrateCount() > 0) {
                                                dev.sendScalarVibrateCmd(0.5).get();
                                            }
                                        }

                                        Thread.sleep(1000);

                                        assertTrue(client.stopAllDevices());

                                        Thread.sleep(60000);
                                        for (
                                                ButtplugClientDevice dev : client.getDevices()) {
                                            if (dev.getScalarVibrateCount() > 0) {
                                                dev.sendScalarVibrateCmd(0.5).get();
                                            }
                                        }

                                        Thread.sleep(1000);

                                        assertTrue(client.stopAllDevices());

                                        Thread.sleep(60000);
                                        for (
                                                ButtplugClientDevice dev : client.getDevices()) {
                                            if (dev.getScalarVibrateCount() > 0) {
                                                dev.sendScalarVibrateCmd(0.5).get();
                                            }
                                        }

                                        Thread.sleep(1000);

                                        assertTrue(client.stopAllDevices());

                                        client.disconnect();
                                        } catch (ExecutionException e) {
                                            throw new RuntimeException(e);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).start();
                                }
                            });
                            return (T)client;
                        }
                        return null;
                    }
                }).build());
            });
            server.start();
        }

        public URI getURI()
        {
            return server.getURI();
        }

        public void stop() throws Exception
        {
            server.stop();
        }

        public void join() throws InterruptedException
        {
            server.join();
        }
    }
}