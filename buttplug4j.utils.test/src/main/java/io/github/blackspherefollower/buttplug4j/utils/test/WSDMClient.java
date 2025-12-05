package io.github.blackspherefollower.buttplug4j.utils.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class WSDMClient {

    private final WSDHeader header;
    private final CompletableFuture<Boolean> connected = new CompletableFuture<>();
    public ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
    public int battery = 100;
    private WebSocket client;

    public WSDMClient(final URI url, final String identifier, final String address) throws Exception {
        header = new WSDHeader();
        header.identifier = identifier;
        header.address = address;

        HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(url, new WebSocketClient(this))
                .join();
        connected.get(10, TimeUnit.SECONDS);

    }

    public void onClose(int statusCode, String reason) {
        this.client = null;
    }

    public void onConnect(WebSocket client) {
        this.client = client;
        // Don't block the WS thread
        new Thread(() -> {
            try {
                client.sendText(new ObjectMapper().writeValueAsString(header), true).get(1, TimeUnit.SECONDS);
            } catch (JsonProcessingException | ExecutionException | InterruptedException | TimeoutException e) {
                System.out.println("Failed to send header: " + e.getMessage());
            }
        }).start();
    }

    public void onMessage(final String message) {
        System.out.println("Got message: " + message);
        if (message.startsWith("DeviceType;")) {
            new Thread(() -> {
                try {
                    sendMessage("Z:10:" + header.address + ";");
                    if (!connected.isDone()) {
                        connected.complete(true);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }

            }).start();
            return;
        }
        if (message.startsWith("Battery;")) {
            new Thread(() -> {
                try {
                    sendMessage(battery + ";");
                    connected.complete(true);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }

            }).start();
            return;
        }
        messages.add(message);
    }

    protected void sendMessage(final String msg) throws ExecutionException, InterruptedException, TimeoutException {
        client.sendBinary(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)), true).get(1, TimeUnit.SECONDS);
    }

    static class WSDHeader {
        @JsonProperty("identifier")
        public String identifier;
        @JsonProperty("address")
        public String address;
        @JsonProperty("version")
        public int version = 0;
    }

    private static class WebSocketClient implements WebSocket.Listener {
        WSDMClient wsdmclient;

        public WebSocketClient(WSDMClient wsdmclient) {
            this.wsdmclient = wsdmclient;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
            wsdmclient.onConnect(webSocket);
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("onText received " + data);
            wsdmclient.onMessage(data.toString());
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("Bad day! " + webSocket.toString());
            error.printStackTrace();
            WebSocket.Listener.super.onError(webSocket, error);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("onClose received " + statusCode + " " + reason);
            wsdmclient.onClose(statusCode, reason);
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer message, boolean last) {
            System.out.println("onBinary received " + message);
            wsdmclient.onMessage(StandardCharsets.UTF_8.decode(message).toString());
            return WebSocket.Listener.super.onBinary(webSocket, message, last);
        }
    }
}
