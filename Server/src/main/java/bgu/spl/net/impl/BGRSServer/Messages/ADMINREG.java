package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.Admin;
import bgu.spl.net.impl.BGRSServer.BGRSProtocol;
import bgu.spl.net.impl.BGRSServer.User;

public class ADMINREG extends Massage {

    private User myUser ;

    public ADMINREG(String _username , String _password){
        super.OPCode=01;
        myUser=new Admin(_username,_password);
    }

    public Massage function(BGRSProtocol _myProtocol) {
        super.myProtocol=_myProtocol;
        if (!myProtocol.getIsLogin() && data.register(myUser)) {
                return new ACK(OPCode,myProtocol);
        }
        return new ERR(OPCode);
    }

}
