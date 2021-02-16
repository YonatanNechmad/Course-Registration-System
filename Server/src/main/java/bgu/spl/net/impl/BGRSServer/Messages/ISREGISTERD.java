package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class ISREGISTERD extends Massage {

    private int courseNumber;

    public ISREGISTERD(int _courseNumber) {
        super.OPCode = 9;
        courseNumber = _courseNumber;
    }

    public Massage function(BGRSProtocol _myProtocol) {
        myProtocol = _myProtocol;
        if (myProtocol.getIsLogin() && myProtocol.getIsStudent()) {
            Boolean result = data.IsRegistred(courseNumber, myProtocol.getUser().getUsername());
            if (result != null) {
                ACK T = new ACK(OPCode, myProtocol);
                if (result) {
                    String s = "REGISTERED";
                    T.setRelevantObject(s);
                } else {
                    String s = "NOT REGISTERED";
                    T.setRelevantObject(s);
                }
                return T;
            }
        }
        return new ERR(OPCode);
    }
}
