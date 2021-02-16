package bgu.spl.net.impl.BGRSServer;

public abstract class User {

    //data member
    protected Boolean isLogin = false;
    protected String username;
    protected String password;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIfLogin() {
        return isLogin;
    }

    public Boolean setLogin() {
        return (isLogin = true);
    }

    public Boolean setLogout() {
        return (isLogin = false);
    }

    public Boolean verifyPassword(String _password) {
        if (_password.equals(password)) {
            return true;
        }
        return false;
    }
}
