package unet.uncentralized.chord.CSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CSocket extends Socket {

    public CSocket(){
        super();
    }

    public CSocket(InetAddress address, int port)throws IOException {
        super(address, port);
    }

    @Override
    public DataInputStream getInputStream()throws IOException {
        return new DataInputStream(super.getInputStream());
    }

    @Override
    public DataOutputStream getOutputStream()throws IOException {
        return new DataOutputStream(super.getOutputStream());
    }

    @Override
    public void close()throws IOException {
        if(!super.isInputShutdown()){
            super.shutdownInput();
        }

        if(!super.isOutputShutdown()){
            super.shutdownOutput();
        }

        super.close();
    }
}
