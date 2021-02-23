package unet.uncentralized.chord.Server;

import unet.uncentralized.chord.CSocket.CSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static unet.uncentralized.chord.Server.Key.*;

public class RoutingTable {

    private HashMap<Long, NodeProperties> fingers = new HashMap<>();
    private NodeProperties me, predecessor, successor;

    public void setMe(NodeProperties me){
        this.me = me;
    }

    public NodeProperties getMe(){
        return me;
    }

    public void setPredecessor(NodeProperties predecessor){
        this.predecessor = predecessor;
    }

    public NodeProperties getPredecessor(){
        return predecessor;
    }

    public void setSuccessor(NodeProperties successor){
        this.successor = successor;
    }

    public NodeProperties getSuccessor(){
        return successor;
    }

    public void setFingers(){
        try{
            fingers.clear();

            NodeProperties entry = new NodeProperties(successor.getAddress(), successor.getPort());
            int probe = 2;

            for(int i = 0; i < 33; i++){
                CSocket socket = null;
                try{
                    socket = new CSocket(entry.getAddress(), entry.getPort());
                    DataInputStream in = socket.getInputStream();
                    DataOutputStream out = socket.getOutputStream();

                    socket.setTcpNoDelay(true);

                    out.writeByte(0x02);

                    entry = new NodeProperties();
                    entry.deserialize(in);

                    if(i == probe){
                        if(entry.getKey() != me.getKey()){
                            fingers.put(entry.getKey(), entry);
                        }
                        probe = probe*2;
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //RETURNS GREEDY
    public NodeProperties getClosest(long key){ // POSSIBLE TO RETURN ME...
        if(between(key, predecessor.getKey(), me.getKey())){
            return predecessor;
        }

        if(between(key, me.getKey(), successor.getKey())){
            return me;
        }

        if(fingers.size() > 0){
            long lastFinger = successor.getKey();
            for(long finger : fingers.keySet()){
                if(between(key, successor.getKey(), finger)){
                    return fingers.get(lastFinger);
                }
                lastFinger = finger;
            }
        }

        return null;
    }

    public void findSuccessor(NodeProperties entry){
        for(int i = 0; i < 20; i++){
            CSocket socket = null;
            try{
                socket = new CSocket(entry.getAddress(), entry.getPort());
                DataInputStream in = socket.getInputStream();
                DataOutputStream out = socket.getOutputStream();

                socket.setTcpNoDelay(true);

                out.writeByte(0x00);
                out.writeLong(me.getKey());

                switch(in.readByte()){
                    case 0x00:
                        predecessor.deserialize(in);
                        successor.deserialize(in);

                        out.writeByte(0x00);
                        me.serialize(out);

                        if(in.readByte() == 0x01){
                            socket.close();
                            setPredecessor(successor, me);
                            return;
                        }

                        socket.close();
                        return;

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
    }

    public static void setPredecessor(NodeProperties to, NodeProperties change){
        CSocket socket = null;

        try{
            socket = new CSocket(to.getAddress(), to.getPort());

            DataInputStream in = socket.getInputStream();
            DataOutputStream out = socket.getOutputStream();

            //socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            //socket.setSoTimeout(5000);

            //FIND CLOSEST
            out.writeByte(0x01);
            change.serialize(out);

            if(in.readByte() == 0x00){
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


    public ArrayList<NodeProperties> getFingers(){
        return new ArrayList<>(fingers.values());
    }
}
