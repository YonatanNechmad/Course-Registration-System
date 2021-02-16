package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class COURSEREG extends Massage {

    int courseNumber;

    public COURSEREG(int _courseNumber){
        OPCode=5;
        courseNumber=_courseNumber;

    }
    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol=_myProtocol;
        if (myProtocol.getIsLogin()) {
            if(data.Coursereg(courseNumber,myProtocol.getUser())){
                ACK ack1=new ACK(OPCode,myProtocol);
                return ack1;
            }
        }
        return new ERR(OPCode);
    }
}
