package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Admin;
import bgu.spl.net.impl.BGRSServer.Messages.Massage;
import bgu.spl.net.impl.BGRSServer.Student;
import bgu.spl.net.impl.BGRSServer.User;
import bgu.spl.net.api.MessagingProtocol;

public class BGRSProtocol implements MessagingProtocol<Massage> {

    private boolean shouldTerminate = false;
    boolean isStudent=false;
    boolean isAdmin=false;
    boolean isLogin=false;
    User myUser=null;


    public Massage process(Massage msg) {
        return msg.function(this);
    }

    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setUser(User _user) {
        myUser = _user;
        if (_user instanceof Student) {
            isStudent = true;
            isAdmin = false;
        }
        if (_user instanceof Admin) {
            isStudent = false;
            isAdmin = true;
        }

    }

    public void setLogin() {
        isLogin = true;
    }
    public void setLogout() {
        isLogin = false;
        myUser=null;
        shouldTerminate=true;
    }
    public User getUser (){
        return myUser;
    }
    public boolean getIsStudent() {
        return isStudent;
    }
    public boolean getIsAdmin() {
        return isAdmin;
    }
    public boolean getIsLogin(){return isLogin;}


}