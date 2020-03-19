import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private final Socket socket;
    private final Thread thread;
    private final TCPConnectionListner listner;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListner listner, String ipAdress, int port) throws IOException {
        this(listner, new Socket(ipAdress, port));
    }


    public TCPConnection(final TCPConnectionListner listner, Socket socket) throws IOException {
        this.listner = listner;
        this.socket = socket;
        socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    listner.connectionReady(TCPConnection.this);
                    while (!thread.isInterrupted()){
                        String msg = in.readLine();
                        listner.receiveString(TCPConnection.this, msg);
                    }

                } catch (IOException e) {
                    listner.exception(TCPConnection.this, e);
                } finally {
                    listner.disconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendString (String value){
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            listner.exception(TCPConnection.this, e);
            disconnect();
        }

    }

    public synchronized void disconnect(){
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listner.exception(TCPConnection.this, e);
        }

    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
