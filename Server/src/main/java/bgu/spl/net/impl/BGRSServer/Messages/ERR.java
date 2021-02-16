package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;

public class ERR extends Massage {

    Short numOfError;

    public ERR(short _numOfError){
        super.OPCode=12;
        numOfError=_numOfError;
    }

    public Massage function(BGRSProtocol _myProtocol){
        myProtocol=_myProtocol;
        return null;
    }
    public Short numOfReturnMessage(){
        return numOfError;
    }


}
