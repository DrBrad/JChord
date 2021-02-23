package unet.uncentralized.chord.Server;

import java.security.MessageDigest;

public class Key {

    public static long generate(String request)throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] buffer = messageDigest.digest(request.getBytes());

        long key = ((buffer[0] & 0xFFL) << 56) |
                ((buffer[1] & 0xFFL) << 48) |
                ((buffer[2] & 0xFFL) << 40) |
                ((buffer[3] & 0xFFL) << 32) |
                ((buffer[4] & 0xFFL) << 24) |
                ((buffer[5] & 0xFFL) << 16) |
                ((buffer[6] & 0xFFL) <<  8) |
                ((buffer[7] & 0xFFL) <<  0);

        return Math.abs(key);
    }

    public static boolean between(long key, long from, long to){
        if(from > to){
            return key > from || key <= to;
        }else if(from < to){
            return key > from && key <= to;
        }else{
            return true;
        }
    }
}
