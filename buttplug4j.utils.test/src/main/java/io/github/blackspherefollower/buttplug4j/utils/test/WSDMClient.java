package io.github.blackspherefollower.buttplug4j.utils.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.blackspherefollower.buttplug4j.client.ButtplugClient;
import io.github.blackspherefollower.buttplug4j.client.IConnectedEvent;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugConsts;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugMessage;
import io.github.blackspherefollower.buttplug4j.protocol.ButtplugProtocolException;
import io.github.blackspherefollower.buttplug4j.protocol.messages.Error;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class WSDMClient {

        private WebSocketClient client;
        private Session session;
        private WSDHeader header;
        public ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
        private CompletableFuture<Boolean> connected = new CompletableFuture<>();

        public int battery = 100;

        class WSDHeader {
            @JsonProperty("identifier")
            public String identifier;
            @JsonProperty("address")
            public String address;
            @JsonProperty("version")
            public int version = 0;
        }

        public WSDMClient(final URI url, final String identifier, final String address) throws Exception {
            header = new WSDHeader();
            header.identifier = identifier;
            header.address = address;
            client = new WebSocketClient();

            client.start();
            client.connect(this, url, new ClientUpgradeRequest()).get(2, TimeUnit.SECONDS);
            connected.get(5, TimeUnit.SECONDS);
        }

        protected void cleanup() {

            if (session != null) {
                session.close();
            }

            try {
                LifeCycle.stop(client);
            } catch (RuntimeException ignored) {
            }
            client = null;
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            this.session = null;
            cleanup();
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            this.session = session;

            // Don't block the WS thread
            new Thread(() -> {
                try {
                    session.getRemote().sendStringByFuture(new ObjectMapper().writeValueAsString(header)).get(1, TimeUnit.SECONDS);
                } catch (JsonProcessingException | ExecutionException | InterruptedException |TimeoutException e) {
                    System.out.println("Failed to send header: " + e.getMessage());
                }
            }).start();
        }

        @OnWebSocketFrame
        public void onFrame(final Frame frame) {
            if( frame.getType().isData()) {
                System.out.println("Got frame: " + frame);
                byte[] data = new byte[frame.getPayloadLength()];
                        frame.getPayload().get(data);
                onMessage(null, new String(data, StandardCharsets.UTF_8));
            }
        }

        @OnWebSocketMessage
        public void onMessage(final Session sess, final String message) {
            System.out.println("Got message: " + message);
            if(message.startsWith("DeviceType;")) {
                new Thread(() -> {
                    try {
                        session.getRemote().sendBytesByFuture(ByteBuffer.wrap(("Z:10:" + header.address + ";").getBytes(StandardCharsets.UTF_8))).get(1, TimeUnit.SECONDS);
                        connected.complete(true);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        throw new RuntimeException(e);
                    }

                }).start();
                return;
            }
            if(message.startsWith("Battery;")) {
                new Thread(() -> {
                    try {
                        session.getRemote().sendBytesByFuture(ByteBuffer.wrap((battery + ";").getBytes(StandardCharsets.UTF_8))).get(1, TimeUnit.SECONDS);
                        connected.complete(true);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        throw new RuntimeException(e);
                    }

                }).start();
                return;
            }
            messages.add(message);
        }

        @OnWebSocketError
        public void onWebSocketError(final Throwable cause) {
            System.out.println("Got error: " + cause.getMessage());
        }

        protected void sendMessage(final String msg) throws ExecutionException, InterruptedException, TimeoutException {
            session.getRemote().sendStringByFuture(msg).get(1, TimeUnit.SECONDS);
        }
    }
