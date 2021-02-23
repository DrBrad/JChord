package unet.uncentralized.chord.Server;

import java.util.HashMap;

public class Storage {

    private HashMap<Long, String> storage = new HashMap<>();

    public void put(long key, String var){
        storage.put(key, var);
    }

    public String get(long key){
        if(storage.containsKey(key)){
            return storage.get(key);
        }
        return null;
    }
}
