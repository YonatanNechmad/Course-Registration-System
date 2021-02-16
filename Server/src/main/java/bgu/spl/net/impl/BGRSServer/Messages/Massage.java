package bgu.spl.net.impl.BGRSServer.Messages;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;
import bgu.spl.net.impl.BGRSServer.Database;

public  abstract class Massage {

    protected Short OPCode;
    protected Database data=Database.getInstance();
    protected BGRSProtocol myProtocol;

    public  Short numOfReturnMessage() {return OPCode;}

    public abstract Massage function(BGRSProtocol _myProtocol);
}
