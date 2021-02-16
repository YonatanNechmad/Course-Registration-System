package bgu.spl.net.impl.BGRSServer.Messages;
import java.lang.String;

import bgu.spl.net.impl.BGRSServer.BGRSProtocol;
//import com.sun.org.apache.xpath.internal.operations.String;

public class ACK extends Massage {

    private Short successMassage;
    private String relevantObject;
    private boolean isObjectToSend=false;


    public ACK(Short _successMassage, BGRSProtocol _myprotocol){
        super.OPCode=12;
        successMassage=_successMassage;
        myProtocol=_myprotocol;
    }

    public Massage function(BGRSProtocol _myProtocol){
        return null;
    }

    public Short numOfReturnMessage(){
        return successMassage;
    }

    public boolean getIsObjectToSend(){return isObjectToSend;}

    public String getRelevantObject(){
        return relevantObject;
    }
    public void setRelevantObject(String _relevant){
        relevantObject =_relevant;
        isObjectToSend=true;
    }

}
