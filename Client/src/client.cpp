//
// Created by yonatan nechmad on 02/01/2021.
//

#include <stdlib.h>
#include "connectionHandler.h"
# include <thread>
#include <iostream>
#include <condition_variable>
#include "userReader.h"
#include "serverReader.h"

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host =argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    // make new thread to read from server
    // make new thread to read from keyboard
    int know=0;
    std::mutex mutex;
    std:: condition_variable exit;
    userReader userReader1(connectionHandler,exit,know,mutex);

    std::thread uReader(&userReader::run, &userReader1); // thread to read from keyboard

    serverReader serverReader1(connectionHandler,exit,know,mutex);

    std::thread sReader(&serverReader::run, &serverReader1);

    uReader.join();
    sReader.join();
}







