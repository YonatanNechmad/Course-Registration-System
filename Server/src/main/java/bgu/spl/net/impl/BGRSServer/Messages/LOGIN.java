package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;
import bgu.spl.net.impl.BGRSServer.User;

public class LOGIN extends Massage {

    private String username ;
    private String password;

    public LOGIN(String _username , String _password ){
        super.OPCode=03;
        username=_username;
        password=_password;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol = _myProtocol;
        if (!myProtocol.getIsLogin()) {
            User myUser = data.login(username, password);
            if (myUser != null) {
                myProtocol.setUser(myUser);
                myProtocol.setLogin();
                return new ACK(OPCode, myProtocol);
            }
        }
        return new ERR(OPCode);
    }
}

