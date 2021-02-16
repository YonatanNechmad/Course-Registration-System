# Course-Registration-System
The program is a registration system at BG University. focused on communication between tasks. The communication between the server and the client(s) will be performed by two protocols: Thread-Per-Client (TPC) and Reactor servers. 


Testing run commands:

put Course.txt file on server folder

To use reactor server:

mvn exec:java -Dexec.mainClass=”bgu.spl.net.impl.BGRSServer.ReactorMain” -

Dexec.args=”<port> <No of threads>”


To use thread per client server:

mvn exec:java -Dexec.mainClass=”bgu.spl.net.impl.BGRSServer.TPCMain” -

Dexec.args=”<port>”

after than :

(on client folder)

make

BGRSclient <ip> <port> 



