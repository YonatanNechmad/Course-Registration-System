package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class STUDENTSTAT extends Massage {

    String username;

    public STUDENTSTAT (String _username){
        super.OPCode=8;
        username=_username;
    }

    public Massage function (BGRSProtocol _myProtocol){
        super.myProtocol=_myProtocol;
        if(myProtocol.getIsLogin()&&myProtocol.getIsAdmin()){
            String studentStat = data.StudentStat(username,myProtocol.getUser());
            if(studentStat!=null){
                ACK ack1 = new ACK(OPCode, myProtocol);
                ack1.setRelevantObject(studentStat);
                return ack1;
            }
        }
        return new ERR(OPCode);
    }


}
