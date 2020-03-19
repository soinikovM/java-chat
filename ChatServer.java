import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListner{
    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Сервер запущен...");
        try(ServerSocket serverSocket = new ServerSocket(9800)){
            while(true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException z){
                    System.out.println("TCPConnection exception: " + z);
                }
            }
        }catch (IOException e){ throw new RuntimeException(e); }
    }

    @Override
    public synchronized void connectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendAllConnections("Client " + tcpConnection + "connected.");
    }

    @Override
    public synchronized void receiveString(TCPConnection tcpConnection, String value) {
        sendAllConnections(value);
    }

    @Override
    public synchronized void disconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendAllConnections("Client " + tcpConnection + "disconnected.");
    }

    @Override
    public synchronized void exception(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendAllConnections(String value){
        System.out.println(value);
        final int count = connections.size();
        for (int i = 0; i < count; i++) {
            connections.get(i).sendString(value);
        }
    }
}
