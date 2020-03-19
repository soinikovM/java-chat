public interface TCPConnectionListner {
    void connectionReady(TCPConnection tcpConnection);
    void receiveString(TCPConnection tcpConnection, String value);
    void disconnect(TCPConnection tcpConnection);
    void exception(TCPConnection tcpConnection, Exception e);
}
