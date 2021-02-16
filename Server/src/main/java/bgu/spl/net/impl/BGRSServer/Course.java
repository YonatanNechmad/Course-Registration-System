package bgu.spl.net.impl.BGRSServer;

import java.util.*;

public class Course {

    //data members
    private int courseNum;
    private String courseName;
    private LinkedList<Integer> kdamCourseList;
    private int numOfMaxStudent;
    private int numStudentInCourse;
    LinkedList<String> myStudent;
    //default CTOR
    public Course() {}

    public Course(int _courseNum , String _courseName, LinkedList<Integer> _kdamCourseList, int _numOfMaxStudent) {
        courseNum = _courseNum;
        courseName = _courseName;
        kdamCourseList = _kdamCourseList;
        numOfMaxStudent = _numOfMaxStudent;
        numStudentInCourse=0;
        myStudent=new LinkedList<>();
    }

    public int getCourseNum(){
        return courseNum;
    }
    public String getCourseName(){
        return courseName;
    }
    public LinkedList<Integer> getkdamCourseList(){
        return kdamCourseList;
    }
    public int getnumOfMaxStudent(){
        return numOfMaxStudent;
    }
    public boolean isFull (){
        return (numOfMaxStudent<=numStudentInCourse);
    }
    public void addStudent(String _stud){
        myStudent.add(_stud);
        numStudentInCourse++;
    }
    public int getNumStudentInCourse(){
        return numOfMaxStudent- numStudentInCourse;
    }

    public LinkedList<String> getMyStudentList(){
        Collections.sort(myStudent);
        return myStudent;
    }

    public void UnregisterStudent(String student){
        myStudent.remove(student);
            numStudentInCourse=numStudentInCourse-1;
    }
}


