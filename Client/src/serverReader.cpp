//
// Created by yonatan nechmad on 04/01/2021.
//

#include "serverReader.h"

serverReader::serverReader(ConnectionHandler & _conH ,condition_variable & _exit ,int &_know ,class mutex &_mutex):conH(&_conH),exit(&_exit),know(&_know),mutex_(&_mutex){}

void serverReader::run () {
    char * ERRorACK = new char[2];
    char * opcodeReturnMsg = new char[2];

    while (!conH->getShouldTerminate()) {

        short ERRorACKShort;
        short opcodeReturnMsgShort;

        conH->getBytes(ERRorACK,2);
        ERRorACKShort=bytesToShort(ERRorACK);
        conH->getBytes(opcodeReturnMsg,2);
        opcodeReturnMsgShort=bytesToShort(opcodeReturnMsg);

        if (ERRorACKShort==13){
            cout<<"ERROR "<<opcodeReturnMsgShort<<endl;
            if(opcodeReturnMsgShort==4){
                unique_lock<mutex> locker(*mutex_);
                *know=2;
                exit->notify_all();
            }
        } else{
            string data;
            if(opcodeReturnMsgShort==4){
                cout<<"ACK 4"<<endl;
                conH->setShouldTerminate();
                *know=1;
                unique_lock<mutex> locker (*mutex_);
                exit->notify_all();
                //conH.close();
                break;
            }
            if(!conH->getFrameAscii(data,'\0')){
                conH->setShouldTerminate();
                cout<<"disconnect.."<<endl;
                continue;
            }
            else {
                string line ="ACK " + std::to_string(opcodeReturnMsgShort);
                if ((opcodeReturnMsgShort == 6) | (opcodeReturnMsgShort == 9) | (opcodeReturnMsgShort == 7) | (opcodeReturnMsgShort == 8) |
                        ( opcodeReturnMsgShort == 11)){
                line= line+ "\n"+ data ;
                }
                cout << line <<endl;
            }
            }


    }
    delete[] opcodeReturnMsg;
    delete[] ERRorACK;
}
short serverReader::bytesToShort(char* bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}
