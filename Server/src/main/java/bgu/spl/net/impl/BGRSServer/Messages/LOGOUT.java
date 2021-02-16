package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class LOGOUT extends Massage {


    public LOGOUT(){
        super.OPCode=04;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol=_myProtocol;
        if (myProtocol.getIsLogin()) {
            if (data.logout(_myProtocol.getUser())) {
                myProtocol.setLogout();
                return new ACK(OPCode, myProtocol);
            }
        }
        return new ERR(OPCode);
    }

}
