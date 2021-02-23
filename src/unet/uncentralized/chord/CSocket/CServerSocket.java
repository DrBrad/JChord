package unet.uncentralized.chord.CSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class CServerSocket extends ServerSocket {

    public CServerSocket(int port)throws IOException {
        super(port);
    }

    @Override
    public CSocket accept()throws IOException {
        if(isClosed()){
            throw new SocketException("Socket is closed");
        }
        if(!isBound()){
            throw new SocketException("Socket is not bound yet");
        }

        CSocket socket = new CSocket();
        implAccept(socket);
        return socket;
    }
}
