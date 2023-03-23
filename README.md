JChord
===========
A Java implementation of the Chord protocol, using TCP sockets with no UDP or RMI.

I have added a new method in the code for the option to handle fingers in what may be a better method in my opinion.
This method handles fingers by determining the opposite position from itself and then incremently goes closer and closer to itself.
Think of a Pizza and you split the pizza in half and then you go to the right by 1/4 then to the right 1/8 etc... This runs by finding
the closest to the successor rather than asking for successors 32 times.

This method DOES NOT tunnel through the network to preserve threads, however it would be very easy to implement a method with tunneling, assuming you don't run
more than 6 nodes on a given computer.

Features
-----------
This implementation includes the following

- [x] Fingers
- [x] Storage
- [ ] Safe Node Leaving - Handovers
