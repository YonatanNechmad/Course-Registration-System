package bgu.spl.net.impl.BGRSServer;


import bgu.spl.net.impl.BGRSServer.Messages.Massage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

    // data member
    private ConcurrentHashMap<Integer, Course> CourseInfo; // find course by course number
    private ConcurrentHashMap<String, User> userInfo;    // find user by username

    private static class SingletonHolder {
        private static Database instance = new Database();
    }

    //to prevent user from creating new Database
    private Database() {
        userInfo = new ConcurrentHashMap<>();
        CourseInfo = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingletonHolder.instance;
    }

    //methods
    public boolean register(User _user) {
        synchronized (userInfo) {
            if (userInfo.containsKey(_user.getUsername())) {
                return false;
            }
            userInfo.putIfAbsent(_user.getUsername(), _user);
            return true;
        }
    }

    public User login(String _user, String password) {
        synchronized (userInfo) {
            if (userInfo.containsKey(_user)) {
                if ((!userInfo.get(_user).isLogin) & (userInfo.get(_user).verifyPassword(password))) {
                    User user = userInfo.get(_user);
                    user.setLogin();
                    return user;
                }
            }
            return null;
        }
    }

    public boolean logout(User _user) {
        String myUsername = _user.getUsername();
        if ((userInfo.containsKey(myUsername)) && (_user.getIfLogin())) {
            _user.setLogout();
            return true;
        }
        return false;
    }

    public boolean Coursereg(int NumCourse, User student) {
        Course course = CourseInfo.get(NumCourse);
        if (course != null) {
            synchronized (course) {
                if (student instanceof Student && student.isLogin) {
                    if (!((Student) student).IfRegToCourse(course)) {
                        if (!course.isFull()) {
                            LinkedList<Integer> getkdamCourseList = course.getkdamCourseList();
                            if (((Student) student).hasTheKdamCourse(getkdamCourseList)) {
                                ((Student) student).addCourse(course);
                                course.addStudent(student.getUsername());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    public String KdamChack(int courseNum) {
        if (CourseInfo.get(courseNum) == null) {
            return null;
        }
        String result = new String();
        Course currCourse = CourseInfo.get(courseNum);
        result = currCourse.getkdamCourseList().toString().replaceAll(" ", "");
        return result;
    }

    public String Coursestat(int courseNum, User admin) {
        String result = null;
        if (admin instanceof Admin & CourseInfo.get(courseNum) != null) {
            Course course = CourseInfo.get(courseNum);
            String CourseName = "Course: (" + courseNum + ") " + course.getCourseName() + '\n';
            String SeatsAv = "Seats Available: " + course.getNumStudentInCourse() + "/" + course.getnumOfMaxStudent() + '\n';
            String studentsReg = "Students Registered: [";
            for (String studentName : course.getMyStudentList()) {
                studentsReg = studentsReg + studentName + ",";
            }
            if (studentsReg.length() > 1) {
                studentsReg = studentsReg.substring(0, studentsReg.length() - 1); //IT WAS 2 INSTADE 1
            }
            result = CourseName + SeatsAv + studentsReg + "]";
        }
        return result;

    }


    public String StudentStat(String NameOfStudent, User admin) {
        String result = null;
        if (admin instanceof Admin & userInfo.get(NameOfStudent) != null && userInfo.get(NameOfStudent) instanceof Student) {
            Student student = (Student) userInfo.get(NameOfStudent);
            String courseList = student.getMyCourses().toString().replaceAll(" ", "");
            result = "Student: " + NameOfStudent + '\n' + "Courses: " + courseList;
        }
        return result;
    }

    public Boolean IsRegistred(int course, String NameOfStudent) {
        Boolean result = null;
        Course c = CourseInfo.get(course);
        Student student = (Student) userInfo.get(NameOfStudent);
        if (c != null & userInfo.get(NameOfStudent) != null && student instanceof Student) {
            result = student.IfRegToCourse(c);
        }
        return result;
    }

    public boolean Unregister(int course, User student) {
        boolean result = false;
        Course currCourse1 = CourseInfo.get(course);
        if (currCourse1 != null) {
            synchronized (currCourse1) {
                Student currStudent = (Student) student;
                if (userInfo.get(student.getUsername()) != null & currStudent.IfRegToCourse(currCourse1)) {
                    currStudent.UnregisterCourse(currCourse1);
                    currCourse1.UnregisterStudent(student.getUsername());
                    result = true;
                }
            }
        }
        return result;
    }

    public String MyCourses(User student) {
        if (userInfo.get(student.getUsername()) == null) {
            return null;
        }
        Student currStudnt = (Student) student;
        String result = "[";
        for (Integer currCurse : currStudnt.getMyCourses()) {
            result = result + currCurse + ",";
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result + "]";
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    boolean initialize(String coursesFilePath) {
        try {
            File myFile = new File(coursesFilePath);
            Scanner reader = new Scanner(myFile);
            while (reader.hasNextLine()) {
                String CurrentLine = reader.nextLine();

                Integer courseNum = Integer.parseInt(CurrentLine.substring(0, CurrentLine.indexOf('|'))); //1

                CurrentLine = CurrentLine.substring(CurrentLine.indexOf('|') + 1); //2

                String CourseName = CurrentLine.substring(0, CurrentLine.indexOf('|'));

                CurrentLine = CurrentLine.substring(CurrentLine.indexOf('|') + 1); //3

                String kdamCourseString = CurrentLine.substring(1, CurrentLine.indexOf('|') - 1);

                LinkedList<Integer> kdamCourse = new LinkedList<>();
                int myCourse;
                while (kdamCourseString.length() > 0) {
                    if (kdamCourseString.contains(",")) {
                        myCourse = Integer.parseInt(kdamCourseString.substring(0, kdamCourseString.indexOf(",")));
                        kdamCourseString = kdamCourseString.substring(kdamCourseString.indexOf(",") + 1);
                        kdamCourse.add(myCourse);

                    } else {
                        myCourse = Integer.parseInt(kdamCourseString);
                        kdamCourse.add(myCourse);
                        kdamCourseString = "";
                    }
                }

                CurrentLine = CurrentLine.substring(CurrentLine.indexOf('|') + 1);
                int MasStudent = java.lang.Integer.parseInt(CurrentLine);
                Course course1 = new Course(courseNum, CourseName, kdamCourse, MasStudent);
                CourseInfo.putIfAbsent(courseNum, course1);
            }

        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
}
