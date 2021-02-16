package bgu.spl.net.impl.BGRSServer;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;


public class Student extends User {

    private LinkedList<Integer> myCourses; // LinkedList!

    public Student(String _username, String _password) {
        super.username = _username;
        super.password = _password;
        myCourses = new LinkedList<>();
    }

    public Student(User _user) {
        super.username = _user.getUsername();
        super.password = _user.getPassword();
        super.isLogin = _user.getIfLogin();
    }

    public boolean IfRegToCourse(Course _numofcourse){
        int numofcourse=_numofcourse.getCourseNum();
        boolean result=false;
        if(myCourses.contains(numofcourse)){result=true;}
        return  result;
    }
    public boolean addCourse(Course course) {
        Integer _course=course.getCourseNum();
        myCourses.add(_course);
        return true;
    }

    public boolean getIsRegisterCourse(Integer _course) {
        if (myCourses.contains(_course)) return true;
        return false;
    }

    public LinkedList<Integer> getMyCourses() {
        return myCourses;
    }

public boolean hasTheKdamCourse (LinkedList<Integer> kdamCourseListOfCourse){
        for(Integer numofcourse:kdamCourseListOfCourse ){
            if (!myCourses.contains(numofcourse)){
                return false;
            }
        }
        return true;
}
public void UnregisterCourse(Course _course){
    Integer course=_course.getCourseNum();
      if(myCourses.contains(course)) {myCourses.remove(course);}
}

}

