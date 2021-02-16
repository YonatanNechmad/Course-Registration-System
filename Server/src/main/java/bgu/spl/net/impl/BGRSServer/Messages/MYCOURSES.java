package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class MYCOURSES extends Massage {

    public MYCOURSES(){
        super.OPCode=11;
    }
    public Massage function (BGRSProtocol _myProtocol){
        super.myProtocol=_myProtocol;
        if(myProtocol.getIsLogin()&&myProtocol.getIsStudent()){
            String myCourses = data.MyCourses(myProtocol.getUser());
            if(myCourses!=null){
                ACK ack1 = new ACK(OPCode, myProtocol);
                ack1.setRelevantObject(myCourses);
                return ack1;
            }
        }
        return new ERR(OPCode);
    }

}
