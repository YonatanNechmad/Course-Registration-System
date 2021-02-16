package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;
import bgu.spl.net.impl.BGRSServer.Student;
import bgu.spl.net.impl.BGRSServer.User;

public class STEDENTREG extends Massage {

    private User myUser ;

    public STEDENTREG(String _username , String _password ){
        super.OPCode=02;
        myUser=new Student(_username,_password);
    }

    public Massage function(BGRSProtocol _myProtocol) {
        super.myProtocol=_myProtocol;
        if (!myProtocol.getIsLogin() && data.register(myUser)) {
                return new ACK(OPCode,myProtocol);
            }
        return new ERR(OPCode);
    }

}

