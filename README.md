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

License
-----------
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
