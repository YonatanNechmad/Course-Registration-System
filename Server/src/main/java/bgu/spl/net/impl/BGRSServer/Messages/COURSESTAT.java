package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class COURSESTAT extends Massage {

    int courseNumber;

    public COURSESTAT(int _courseNumber){
        super.OPCode=7;
        courseNumber=_courseNumber;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol=_myProtocol;
        if(myProtocol.getIsLogin() && myProtocol.getIsAdmin()) {
            String courseStat = data.Coursestat(courseNumber, myProtocol.getUser());
            if (courseStat != null) {
                ACK ack1 = new ACK(OPCode, myProtocol);
                ack1.setRelevantObject(courseStat);
                return ack1;
            }
        }
        return new ERR(OPCode);
    }
}
