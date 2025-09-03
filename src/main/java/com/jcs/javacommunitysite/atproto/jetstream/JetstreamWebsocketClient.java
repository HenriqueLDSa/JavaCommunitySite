package com.jcs.javacommunitysite.atproto.jetstream;

import com.jcs.javacommunitysite.atproto.records.AtprotoRecord;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class JetstreamWebsocketClient extends WebSocketClient {
    private Map<String, JetstreamHandler> handlers = new HashMap<>();

    public JetstreamWebsocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Websocket opened!");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("MESSAGE RECEIVED:");
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Websocket closed! " + i + " " + s + " " + b);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Websocket error:");
        e.printStackTrace();
    }

    public <T extends AtprotoRecord> void registerJetstreamHandler(String recordCollection, JetstreamHandler<T> handler) {
        handlers.put(recordCollection, handler);
    }
}
