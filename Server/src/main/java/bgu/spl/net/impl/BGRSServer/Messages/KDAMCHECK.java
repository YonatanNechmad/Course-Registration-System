package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class KDAMCHECK extends Massage {

    int courseNumber;
    //LinkedList<Integer> myKdamCourse;

    public KDAMCHECK (int _courseNum){
        super.OPCode=6;
        courseNumber=_courseNum;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol = _myProtocol;
        if (myProtocol.getIsLogin()&&myProtocol.getIsStudent()) {
            String myKdamCourse = data.KdamChack(courseNumber);
            if (myKdamCourse != null) {
                ACK ack1 = new ACK(OPCode, myProtocol);
                ack1.setRelevantObject(myKdamCourse);
                return ack1;
            }
        }
        return new ERR(OPCode);
    }

}
