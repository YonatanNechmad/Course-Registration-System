//
// Created by yonatan nechmad on 04/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_SERVERREADER_H
#define BOOST_ECHO_CLIENT_SERVERREADER_H

#include <string>
#include <iostream>
#include <boost/asio.hpp>
# include <mutex>
#include "connectionHandler.h"
# include <cstddef>
#include <condition_variable>

using namespace ::std;

class serverReader {

private:
    ConnectionHandler * conH;
    std:: condition_variable * exit;
    int * know;
    std::mutex * mutex_;
public:
    serverReader(ConnectionHandler & _conH,condition_variable& _exit,int& _know,class mutex& _mutex);
    void run();
    short bytesToShort(char* bytesArr);
};


#endif //BOOST_ECHO_CLIENT_SERVERREADER_H
