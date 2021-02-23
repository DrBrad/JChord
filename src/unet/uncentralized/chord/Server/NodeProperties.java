package unet.uncentralized.chord.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import static unet.uncentralized.chord.Server.Key.*;

public class NodeProperties {

    private long key;
    private InetAddress address;
    private int port;

    public NodeProperties(){

    }

    //GENERATE KEY - USED FOR JOINING
    public NodeProperties(InetAddress address, int port)throws Exception {
        //this.key = key;
        this.key = generate(address.getHostAddress()+":"+port);
        this.address = address;
        this.port = port;
    }

    //GET KEY WITHOUT GENERATION
    public NodeProperties(long key, InetAddress address, int port)throws Exception {
        this.key = key;
        this.address = address;
        this.port = port;
    }

    public void setKey(long key){
        this.key = key;
    }

    public long getKey(){
        return key;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }

    public InetAddress getAddress(){
        return address;
    }

    public void setPort(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    public void serialize(DataOutputStream out)throws Exception {
        out.writeLong(key);

        if(address instanceof Inet4Address){
            out.writeByte(0x04);

        }else if(address instanceof Inet6Address){
            out.writeByte(0x06);
        }

        out.write(address.getAddress());
        out.writeInt(port);
    }

    public void deserialize(DataInputStream in)throws Exception {
        key = in.readLong();
        byte[] buffer = null;

        switch(in.readByte()){
            case 0x04:
                buffer = new byte[4];
                break;

            case 0x06:
                buffer = new byte[16];
                break;
        }

        in.read(buffer);
        address = InetAddress.getByAddress(buffer);

        port = in.readInt();
    }
}
