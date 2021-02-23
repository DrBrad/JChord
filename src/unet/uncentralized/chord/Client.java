package unet.uncentralized.chord;

import unet.uncentralized.chord.CSocket.CSocket;
import unet.uncentralized.chord.Server.NodeProperties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import static unet.uncentralized.chord.Server.Key.*;

public class Client {

    public static boolean put(InetAddress address, int port, String key, String var)throws Exception {
        NodeProperties entry = new NodeProperties(address, port);

        long hkey = generate(key);

        for(int i = 0; i < 20; i++){
            CSocket socket = null;

            try{
                socket = new CSocket(entry.getAddress(), entry.getPort());

                DataInputStream in = socket.getInputStream();
                DataOutputStream out = socket.getOutputStream();

                //socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                //socket.setSoTimeout(5000);

                //FIND CLOSEST
                out.writeByte(0x05);
                out.writeLong(hkey);

                switch(in.readByte()){
                    case 0x00:
                        out.writeInt(var.getBytes().length);
                        out.write(var.getBytes());
                        socket.close();

                        return true;

                    case 0x01:
                        entry.deserialize(in);
                        break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    socket.close();
                }catch(IOException e){
                }
            }
        }
        return false;
    }

    public static String get(InetAddress address, int port, String key)throws Exception {
        NodeProperties entry = new NodeProperties(address, port);

        long hkey = generate(key);

        for(int i = 0; i < 20; i++){
            CSocket socket = null;

            try{
                socket = new CSocket(entry.getAddress(), entry.getPort());

                DataInputStream in = socket.getInputStream();
                DataOutputStream out = socket.getOutputStream();

                //socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                //socket.setSoTimeout(5000);

                //FIND CLOSEST
                out.writeByte(0x06);
                out.writeLong(hkey);

                switch(in.readByte()){
                    case 0x00:
                        byte[] buffer = new byte[in.readInt()];
                        in.read(buffer);

                        String var = new String(buffer);

                        socket.close();

                        return var;

                    case 0x01:
                        entry.deserialize(in);
                        break;

                    case 0x02:
                        socket.close();
                        return null;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    socket.close();
                }catch(IOException e){
                }
            }
        }

        return null;
    }
}
