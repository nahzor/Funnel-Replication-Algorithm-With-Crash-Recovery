Roshan-Muralidharan-FR-Algorithm-With-Crash-Recovery
====================================================

<b>Project folders are included in the zip file:</b>

1.p2c
2.p2ms
3.p2s

<b>How to run this applicaiton:</b>

The main file in the folder p2ms should be run first.
The program would ask for a port number to run the master server and this value needs to be provided.

After running the master server, 5 servers should be run from the main.java file in the folder p2s.
The program would require 4 inputs, the file to be modified and the port numbers for the servers that communicate with the master server, the client and the other peer servers.

After this, the client program can be run. This can be run from the java file in the folder p2c.
The program requires inputs : Client ID, Port to run a server that receives crash notifications from the server and a message that needs to be written to the file.

<b>Reference:</b>

http://www.ymsir.com/papers/fr-ladis.pdf
