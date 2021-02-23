package unet.uncentralized.chord.Server;

import unet.uncentralized.chord.CSocket.CServerSocket;
import unet.uncentralized.chord.CSocket.CSocket;

import java.net.InetAddress;

public class Chord {

    private RoutingTable routingTable = new RoutingTable();
    private Storage storage = new Storage();

    public Chord(int port)throws Exception {
        bind(port);
        routingTable.setMe(new NodeProperties(InetAddress.getLocalHost(), port));
        routingTable.setPredecessor(new NodeProperties(InetAddress.getLocalHost(), port));
        routingTable.setSuccessor(new NodeProperties(InetAddress.getLocalHost(), port));
    }

    public Chord(int port, InetAddress entryAddress, int entryPort)throws Exception {
        bind(port);
        routingTable.setMe(new NodeProperties(InetAddress.getLocalHost(), port));
        routingTable.setPredecessor(new NodeProperties(InetAddress.getLocalHost(), port));
        routingTable.setSuccessor(new NodeProperties(InetAddress.getLocalHost(), port));

        routingTable.findSuccessor(new NodeProperties(entryAddress, entryPort));

        routingTable.setFingers();
    }

    private void bind(int port){
        new Thread(new Runnable(){
            private CSocket socket;

            @Override
            public void run(){
                CServerSocket server = null;
                try{
                    server = new CServerSocket(port);

                    while((socket = server.accept()) != null){
                        new Tunnel(storage, routingTable, socket).start();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    try{
                        server.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String toString(){
        String builder = "   ";
        if(routingTable.getFingers().size() > 0){
            for(NodeProperties finger : routingTable.getFingers()){
                String k = finger.getKey()+"";
                builder += "  |  "+k.substring(0, 2);
            }
        }

        String me = routingTable.getMe().getKey()+"",
                predecessor = routingTable.getPredecessor().getKey()+"",
                successor = routingTable.getSuccessor().getKey()+"";

        return me.substring(0, 2)+
                "  |  "+predecessor.substring(0, 2)+
                "  |  "+successor.substring(0, 2)+builder;
    }
}
