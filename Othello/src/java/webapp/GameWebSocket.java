package webapp;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import oracle.jrockit.jfr.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

@ServerEndpoint("/endpoint")
public class GameWebSocket {

    /**
     * @OnOpen allows us to intercept the creation of a new session. The session
     * class allows us to send data to the user. In the method onOpen, we'll let
     * the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + " has opened a connection");

    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(String message, Session session) throws org.json.simple.parser.ParseException {
        System.out.println("Message from " + session.getId() + ": " + message);
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap();
            }

        };

        Map json = (Map) parser.parse(message, containerFactory);
        long type = (Long) json.get("type");
        JSONObject obj;
        if (type == 2) {
            String gameId = (String) json.get("gameId");
            long playerIndex = (Long) json.get("playerId");
            long x = (Long) json.get("x");
            long y = (Long) json.get("y");
            Game g = GameController.idToGame.get(gameId);
            if (g != null) {
                boolean wasValid=g.insertDisk((int)x, (int)y);

                obj = new JSONObject();
                String matrix = "";
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        matrix += g.matrix[i][j];
                    }
                }
                obj.put("type", 2);
                obj.put("wasValid",wasValid);

                obj.put("turn", new Integer(g.turn));
                obj.put("matrix", matrix);
                obj.put("hasDone", g.isGameFinished());
                obj.put("whiteCount",g.whiteCount);
                obj.put("blackCount",g.blackCount);
                String whoWon = "";

                if (g.isGameFinished()) {
                    whoWon = g.getWhoWon();

                }
                obj.put("whoWon", whoWon);
                if(!wasValid){
                    if(g.turn==1){
                        sendMessage(obj, g.session1);
                        
                    }
                    else{
                        sendMessage(obj, g.session2);
                        
                    }
                }
                else{
                    sendMessage(obj, g.session1);
                    sendMessage(obj, g.session2);
                }
            } else {
                System.out.println("No game " + gameId + " is present");

            }

        } else {
            String gameId = (String) json.get("gameId");
            long playerIndex = (Long) json.get("playerId");
            Game g = GameController.idToGame.get(gameId);
            if (g != null) {

                if (playerIndex == 1) {
                    g.session1 = session;
                    g.isJoin1 = true;
                } else {
                    g.session2 = session;
                    g.isJoin2 = true;
                }
                if (g.isJoin1 && g.isJoin2) {
                    System.out.println("Both players Joined !");
                    obj = new JSONObject();
                    String matrix = "";
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            matrix += g.matrix[i][j];
                        }
                    }
                    obj.put("type", 1);

                    obj.put("turn", new Integer(g.turn));
                    obj.put("matrix", matrix);
                    String otherPlayer = "";//other players Name

                    otherPlayer = g.player2;

                    obj.put("otherPlayer", otherPlayer);
                   // g.session1.isOpen()
                    sendMessage(obj, g.session1);  //send to player1
                    otherPlayer = g.player1;
                    obj.put("otherPlayer", otherPlayer);
                    
                    sendMessage(obj, g.session2);

                } else {
                    //only 1 player joinedone person joined!
                    System.out.println("only 1 player joined as of now !");
                }

            } else {
                System.out.println("No game " + gameId + " is present");

            }

        }

    }

    public void sendMessage(JSONObject message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException ex) {
            Logger.getLogger(GameWebSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncodeException ex) {
            Logger.getLogger(GameWebSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
    }

}
