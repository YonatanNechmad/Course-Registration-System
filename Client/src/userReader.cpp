//
// Created by yonatan nechmad on 02/01/2021.
//

#include "userReader.h"
#include <stdlib.h>
#include "connectionHandler.h"
#include <mutex>
#include <string>
#include <vector>
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <boost/lexical_cast.hpp>

using namespace ::std;

userReader::userReader(ConnectionHandler &_conH, condition_variable &_exit, int &_know, class mutex &_mutex) : conH(
        &_conH), exit(&_exit), know(&_know), mutex_(&_mutex) {};

void userReader::run() {

    while (!conH->getShouldTerminate()) { // raise condition ??
        //   const short bufsize = 1024;
        //   char buf[bufsize];
        //   std::cin.getline(buf, bufsize);
        //   std::string myInput(buf);
        //  int len = myInput.length();
        string myInput;
        getline(cin, myInput);
        vector<string> splitStringVec;
        boost::split(splitStringVec, myInput, boost::is_any_of(" "));
        string opcodeS = splitStringVec[0];
        const char *username;
        const char *password;
        string courseNumber;
        short opcode = makeOpcode(opcodeS);
        char zero = '\0';
        vector<char> byteVector; //the one that we will send


        //insert the opcose to the vector
        shortToBytes(opcode, byteVector);//will work for 4 and 11

        if ((opcode == 1) | (opcode == 2) | (opcode == 3)) { //username +password
            username = splitStringVec[1].c_str();
            for (int i = 0; (i < username[i]) != '\0'; i++) {
                byteVector.push_back(username[i]);
            }
            byteVector.push_back(zero);
            password = splitStringVec[2].c_str();
            for (int i = 0; (i < password[i]) != '\0'; i++) {
                byteVector.push_back(password[i]);
            }
            byteVector.push_back(zero);
        }

        else if ((opcode == 5) | (opcode == 6) | (opcode == 7) | (opcode == 9) | (opcode == 10)) {
            int coursNum = stoi(splitStringVec[1]);
            shortToBytes(coursNum, byteVector);
        }
        else if (opcode == 8) {
            username = splitStringVec[1].c_str();
            for (int i = 0; (i < username[i]) != '\0'; i++) {
                byteVector.push_back(username[i]);
            }
            byteVector.push_back(zero);
        }
        //send
        conH->sendBytes(&byteVector[0], byteVector.size());

        if (opcode == 4) {
            std::unique_lock<std::mutex> locker(*mutex_);
            while (*know == 0) { exit->wait(locker); }
            if (*know == 1) {
                conH->setShouldTerminate();
            } 
            else {
                *know = 0;
            }
        }
    }


}

void userReader::shortToBytes(short num, vector<char> &bytesVc) {
    bytesVc.push_back((num >> 8) & 0xFF);
    bytesVc.push_back(num & 0xFF);

}


void userReader::shortToBytes(int num, vector<char> &bytesVc) {
    bytesVc.push_back((num >> 8) & 0xFF);
    bytesVc.push_back(num & 0xFF);

}

short userReader::makeOpcode(string &_opcodeS) {
    short opcode;
    if (_opcodeS == "ADMINREG") return opcode = 1;
    if (_opcodeS == "STUDENTREG") return opcode = 2;
    if (_opcodeS == "LOGIN") return opcode = 3;
    if (_opcodeS == "LOGOUT")return opcode = 4;
    if (_opcodeS == "COURSEREG")return opcode = 5;
    if (_opcodeS == "KDAMCHECK")return opcode = 6;
    if (_opcodeS == "COURSESTAT")return opcode = 7;
    if (_opcodeS == "STUDENTSTAT")return opcode = 8;
    if (_opcodeS == "ISREGISTERED")return opcode = 9;
    if (_opcodeS == "UNREGISTER")return opcode = 10;
    if (_opcodeS == "MYCOURSES")return opcode = 11;
    return 0;
}

