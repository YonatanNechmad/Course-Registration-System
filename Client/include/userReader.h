//
// Created by yonatan nechmad on 02/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_USERREADER_H
#define BOOST_ECHO_CLIENT_USERREADER_H

#include <string>
#include <iostream>
#include <boost/asio.hpp>
# include <mutex>
#include "connectionHandler.h"
# include <cstddef>
#include <condition_variable>

using namespace ::std;


class userReader {

private:
    ConnectionHandler * conH;
    std:: condition_variable * exit;
    int * know;
    std::mutex * mutex_;

public:
    userReader(ConnectionHandler & _conH,condition_variable& _exit,int& _know,class mutex& _mutex);
    void run();
    void shortToBytes(short num, vector<char> & bytesArr);
    void shortToBytes(int num, vector<char> & bytesArr);
    short makeOpcode (string & _opcodeS);

};

#endif //BOOST_ECHO_CLIENT_USERREADER_H
