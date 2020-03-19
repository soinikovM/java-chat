import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow  extends JFrame  implements ActionListener, TCPConnectionListner {
    private static final String IP_ADR = "187.121.171.123"; //вводите свой IP
    private static final int PORT = 9800;
    private static final int WIDTH = 600;
    private static final int HIGHT = 450;

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("nickname");
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname,BorderLayout.NORTH);
        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADR, PORT);
        } catch (IOException e) {
            printMSG("Connection exception: " + e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + ": " + msg);
    }

    public void connectionReady(TCPConnection tcpConnection) {
        printMSG("Connection ready...");
    }

    public void receiveString(TCPConnection tcpConnection, String value) {
        printMSG(value);
    }

    public void disconnect(TCPConnection tcpConnection) {
        printMSG("Connection close...");
    }

    public void exception(TCPConnection tcpConnection, Exception e) {
        printMSG("Connection exception: " + e);
    }

    private synchronized void printMSG (final String msg){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
