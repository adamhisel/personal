

package appBackend.teamChat;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/chat/{username}")
@Component
public class ChatServer {
    private static Map<Session, String> sessionUsernameMap = new Hashtable();
    private static Map<String, Session> usernameSessionMap = new Hashtable();
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    public ChatServer() {
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("teamName") String teamName) throws IOException {
        this.logger.info("[onOpen] " + username);
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {
            sessionUsernameMap.put(session, username);
            usernameSessionMap.put(username, session);
            this.sendMessageToPArticularUser(username, "Welcome to the"+teamName+"server, " + username);
            this.broadcast("User: " + username + " has Joined the Chat");
        }

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String username = (String)sessionUsernameMap.get(session);
        this.logger.info("[onMessage] " + username + ": " + message);
        if (message.startsWith("@")) {
            String[] split_msg = message.split("\\s+");
            StringBuilder actualMessageBuilder = new StringBuilder();

            for(int i = 1; i < split_msg.length; ++i) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }

            String destUserName = split_msg[0].substring(1);
            String actualMessage = actualMessageBuilder.toString();
            this.sendMessageToPArticularUser(destUserName, "[DM from " + username + "]: " + actualMessage);
            this.sendMessageToPArticularUser(username, "[DM from " + username + "]: " + actualMessage);
        } else {
            this.broadcast(username + ": " + message);
        }

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = (String)sessionUsernameMap.get(session);
        this.logger.info("[onClose] " + username);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        this.broadcast(username + " disconnected");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = (String)sessionUsernameMap.get(session);
        this.logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    private void sendMessageToPArticularUser(String username, String message) {
        try {
            ((Session)usernameSessionMap.get(username)).getBasicRemote().sendText(message);
        } catch (IOException var4) {
            this.logger.info("[DM Exception] " + var4.getMessage());
        }

    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException var5) {
                this.logger.info("[Broadcast Exception] " + var5.getMessage());
            }

        });
    }
}