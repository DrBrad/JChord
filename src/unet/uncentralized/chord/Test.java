package unet.uncentralized.chord;

import unet.uncentralized.chord.Server.Chord;

import java.net.InetAddress;

import static unet.uncentralized.chord.Client.*;

public class Test {

    /*
        CORRECT KEYS

           ME  |  PR  |  SU
        S: 74  |  71  |  82
        A: 37  |  35  |  71
        B: 35  |  83  |  37
        C: 71  |  37  |  74
        D: 82  |  74  |  83
        E: 83  |  82  |  35
    */

    //AS THE ORIGINAL PAPER FOR CHORD STATES THE FINGERS ARE DEFINED AS 2 - 4 - 8 - 16 -32
    //I HAVE IMPLEMENTED THAT METHOD HOWEVER I FEEL THAT IT MAY BE BETTER TO ROUTE USING
    //THIS METHOD A METHOD OF DETERMINING THE HALF POINT OF THE CIRCLE AND THEN MOVING CLOCK WISE FOR
    //SMALLER & SMALLER CHUNKS - I HAVE ADDED THE CODE FOR THIS BELOW IF YOU WISH TO TRY IT OUT

    //THIS VERSION DOESN'T INCLUDE HANDOVERS OR LEAVES - YOU CAN IMPLEMENT THOSE PRETTY EASILY THOUGH

    public static void main(String[] args){
        try{
            Chord server = new Chord(1099);

            Chord cli1 = new Chord(1098, InetAddress.getLocalHost(), 1099);
            Chord cli2 = new Chord(1097, InetAddress.getLocalHost(), 1099);
            Chord cli3 = new Chord(1096, InetAddress.getLocalHost(), 1099);
            Chord cli4 = new Chord(1095, InetAddress.getLocalHost(), 1099);
            Chord cli5 = new Chord(1094, InetAddress.getLocalHost(), 1099);
            //Chord cli6 = new Chord(1093, InetAddress.getLocalHost(), 1099);

            Thread.sleep(2000); //THIS DELAY IS TO ENSURE WE ONLY PRINT AFTER THE NODES ARE ALIGNED...

            System.out.println("   ME  |  PR  |  SU     |  F2  |  F4  |  F8  | F16  | F32");
            System.out.println("S: "+server.toString());
            System.out.println("A: "+cli1.toString());
            System.out.println("B: "+cli2.toString());
            System.out.println("C: "+cli3.toString());
            System.out.println("D: "+cli4.toString());
            System.out.println("E: "+cli5.toString());
            //System.out.println("F: "+cli6.toString());




            //TEST STORAGE

            put(InetAddress.getLocalHost(), 1099, "key", "VARIABLE");

            System.out.println("RETREIVE: "+get(InetAddress.getLocalHost(), 1099, "key"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    public void setFingers(){
        try{
            fingers.clear();

            long h = 4611686018427387903l;
            for(int i = 0; i < 5; i++){
                long position = (me.getKey() > h) ? me.getKey()-h : me.getKey()+h;
                NodeProperties finger = getClosestNode(successor, position);

                if(finger.getKey() != me.getKey() && !fingers.containsKey(finger.getKey())){
                    fingers.put(finger.getKey(), finger);
                }

                h = h/2;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    */
}
