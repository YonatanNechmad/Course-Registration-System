package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class UNREGISTER extends Massage {


    private int courseNumber;

    public UNREGISTER(int _courseNumber) {
        super.OPCode = 10;
        courseNumber = _courseNumber;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol = _myProtocol;
        if (myProtocol.getIsLogin()&&myProtocol.getIsStudent()) {
            if (data.Unregister(courseNumber, myProtocol.getUser())) {
                return new ACK(OPCode, myProtocol);
            }
        }
        return new ERR(OPCode);
    }
}
