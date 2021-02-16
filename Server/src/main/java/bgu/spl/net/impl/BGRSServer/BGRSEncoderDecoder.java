package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Messages.*;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSEncoderDecoder implements MessageEncoderDecoder<Massage> {

    private byte[] bytes = new byte[1 << 16]; //start with 8k
    private int len = 0;
    private boolean foundOpcode=false;
    private Short Opcode=null;
    private String firstObject=null;
    private String secondObject=null;
    private int counter=0;
    private short ACK=12;
    private short ERR=13;
    private byte[] ERRInBytes= shortToBytes(ERR);
    private byte[] ACKInBytes= shortToBytes(ACK);
    
    public Massage decodeNextByte(byte nextByte) {

        if(!foundOpcode && len==1 ){
            pushByte(nextByte);
            Opcode=bytesToShort(bytes);
            foundOpcode=true;
            len=0;
            if (Opcode == 4 | Opcode == 11) { // nothing
                return orderEmpty();
            }
            return null;
        }
        else if (!foundOpcode){
            pushByte(nextByte);
            return null;
        }
        else if(foundOpcode){
            if (Opcode == 1 | Opcode == 2 | Opcode == 3) { // username and password
                if (nextByte=='\0') {
                    if (counter==0) {
                        firstObject =new String(bytes, 0, len, StandardCharsets.UTF_8);
                        len = 0;
                        counter++;
                        return null;
                    }
                    else if (counter==1){
                        secondObject = new String(bytes, 0, len, StandardCharsets.UTF_8);
                        return orderUsernamePassword();
                    }
                }
                else{
                    pushByte(nextByte);
                    return null;
                    }
            }
            if (Opcode == 5 | Opcode == 6 | Opcode == 7 | Opcode == 9 | Opcode == 10) { // course number
                if(counter==0) {
                    pushByte(nextByte);
                    counter=counter+1;
                    return null;
                }
                else {
                    counter=0;
                    pushByte(nextByte);
                    byte [] b=new byte[2];
                    b[0]=bytes[0];
                    b[1]=bytes[1];
                    short toSend=bytesToShort(b);
                    return orderCourseNumber(toSend);

                }
            }
            if (Opcode == 8) { // username
                if (nextByte=='\0'){
                    firstObject=new String(bytes, 0, len, StandardCharsets.UTF_8);
                    return orderUsername();
                }
                else{
                    pushByte(nextByte);
                }
            }
        }
        return null;
    }

    public byte[] encode(Massage message) { // ack or err should be encoding

        Short numOfReturnMessage = message.numOfReturnMessage();
        byte[] numOfReturnMessageInBytes= shortToBytes(numOfReturnMessage);

        if (message instanceof ERR) {

            return append(ERRInBytes,numOfReturnMessageInBytes,null,false);
        }
        else { // massage is ACK
            ACK ack = (ACK) message;
            if (!ack.getIsObjectToSend()) {
                return append(ACKInBytes,numOfReturnMessageInBytes,null,true);
            } else {
                String relevantObject = ack.getRelevantObject();
                byte[] returnMassage=(relevantObject).getBytes();
                return append(ACKInBytes,numOfReturnMessageInBytes,returnMassage,true);
            }
        }
    }


    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Massage orderUsernamePassword (){
        String username1=firstObject;
        String password1=secondObject;

        if (Opcode == 1){
            cleaner();
            return new ADMINREG(username1,password1);
        }
         if(Opcode == 2){
             cleaner();
             return new STEDENTREG(username1,password1);
         }
         if(Opcode == 3){
             cleaner();
             return new LOGIN(username1,password1);
         }
         cleaner();
         return null;
    }
    private Massage orderUsername (){
        String username1=firstObject;
        cleaner();
        return new STUDENTSTAT(username1);
    }

    private Massage orderEmpty (){

        if (Opcode == 4) {
            cleaner();
            return new LOGOUT();
        }
        if(Opcode == 11){
            cleaner();
            return new MYCOURSES();
        }
        return null;
    }

    private Massage orderCourseNumber (short courseNum){

        //int courseNumber =Integer.parseInt(firstObject);
        // Integer.decode(firstObject);

        if(Opcode == 5){
            cleaner();
            return new COURSEREG(courseNum);
        }
        if(Opcode == 6){
            cleaner();
            return new KDAMCHECK(courseNum);
        }
        if(Opcode == 7){
            cleaner();
            return new COURSESTAT(courseNum);
        }
        if(Opcode == 9){
            cleaner();
            return new ISREGISTERD(courseNum);
        }
        if(Opcode == 10){
            cleaner();
            return new UNREGISTER(courseNum);
        }
        return null;
    }

    private void cleaner(){
        bytes=new byte[1 << 16]; //???
        foundOpcode=false;
        Opcode=null;
        firstObject=null;
        secondObject=null;
        len=0;
        counter=0;
    }

  public short bytesToShort(byte[] byteArr)
  {
      short result = (short)((byteArr[0] & 0xff) << 8);
      result += (short)(byteArr[1] & 0xff);
      return result;
  }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private byte[] append (byte[] arrA ,byte[] arrB,byte[] arrC,boolean EorA){
        byte [] result = new byte[4];
        if (arrC == null&&!EorA) {
            //byte [] result = new byte[4];
            result[0]=arrA[0];
            result[1]=arrA[1];
            result[2]=arrB[0];
            result[3]=arrB[1];
            return result;
        }
        else if(arrC == null&&EorA){
            result = new byte[5];
            result[0]=arrA[0];
            result[1]=arrA[1];
            result[2]=arrB[0];
            result[3]=arrB[1];
            result[4]=0;
            return result;
        }
        else{
            result=new byte[5+arrC.length];
            result[0]=arrA[0];
            result[1]=arrA[1];
            result[2]=arrB[0];
            result[3]=arrB[1];

            for(int j=0;j<arrC.length;j++){
                result[j+4]=arrC[j];
            }
            result[result.length-1]='\0';
            return result;

        }
    }

}