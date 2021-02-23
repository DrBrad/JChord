package unet.uncentralized.chord.Server;

import unet.uncentralized.chord.CSocket.CSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Tunnel extends Thread {

    private RoutingTable routingTable;
    private Storage storage;

    private CSocket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Tunnel(Storage storage, RoutingTable routingTable, CSocket socket){
        this.storage = storage;
        this.routingTable = routingTable;
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();

            //socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            //socket.setSoTimeout(5000);

            switch(in.readByte()){
                case 0x00: //FIND SUCCESSOR
                    findSuccessor();
                    break;

                case 0x01: //SET PREDECESSOR
                    setPredecessor();
                    break;

                case 0x02:
                    getSuccessor();
                    break;

                case 0x05:
                    put();
                    break;

                case 0x06:
                    get();
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

    private void findSuccessor()throws Exception {
        long key = in.readLong();

        NodeProperties closest = routingTable.getClosest(key);
        if(closest == null){
            out.writeByte(0x01);
            routingTable.getSuccessor().serialize(out);
            return;
        }

        //IF BETWEEN IT AND PRED WE GET BUG
        if(closest.getKey() == routingTable.getMe().getKey()){
            out.writeByte(0x00);
            closest.serialize(out); //WRITE ME
            routingTable.getSuccessor().serialize(out);

            if(in.readByte() == 0x00){
                routingTable.getSuccessor().deserialize(in);

                if(routingTable.getPredecessor().getKey() == routingTable.getMe().getKey()){
                    out.writeByte(0x00);
                    routingTable.setPredecessor(new NodeProperties(routingTable.getSuccessor().getKey(), routingTable.getSuccessor().getAddress(), routingTable.getSuccessor().getPort()));

                }else{
                    out.writeByte(0x01);
                }
            }

            routingTable.setFingers();

        }else{
            //TUNNEL INIT
            out.writeByte(0x01);
            closest.serialize(out);
        }
    }

    private void setPredecessor()throws Exception {
        routingTable.getPredecessor().deserialize(in);
        out.write(0x00);
        routingTable.setFingers();
    }

    private void getSuccessor()throws Exception {
        //out.writeByte(0x00);
        routingTable.getSuccessor().serialize(out);
    }

    private void put()throws Exception {
        long key = in.readLong();

        NodeProperties closest = routingTable.getClosest(key);
        if(closest == null){
            out.writeByte(0x01);
            routingTable.getSuccessor().serialize(out);
            return;
        }

        //IF BETWEEN IT AND PRED WE GET BUG
        if(closest.getKey() == routingTable.getMe().getKey()){
            out.writeByte(0x00);

            byte[] buffer = new byte[in.readInt()];
            in.read(buffer);
            String var = new String(buffer);
            storage.put(key, var);

            System.out.println(routingTable.getMe().getKey()+" - STORED - "+key+" : "+var);

        }else{
            //TUNNEL INIT
            out.writeByte(0x01);
            closest.serialize(out);
        }
    }

    private void get()throws Exception {
        long key = in.readLong();

        NodeProperties closest = routingTable.getClosest(key);
        if(closest == null){
            out.writeByte(0x01);
            routingTable.getSuccessor().serialize(out);
            return;
        }

        //IF BETWEEN IT AND PRED WE GET BUG
        if(closest.getKey() == routingTable.getMe().getKey()){
            String var = storage.get(key);
            if(var != null){
                out.writeByte(0x00);

                out.writeInt(var.getBytes().length);
                out.write(var.getBytes());

            }else{
                out.writeByte(0x02);
            }

        }else{
            //TUNNEL INIT
            out.writeByte(0x01);
            closest.serialize(out);
        }
    }
}