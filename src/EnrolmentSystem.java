import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class EnrolmentSystem implements StudentEnrolmentManager{
    public static void main(String[] args){
        EnrolmentSystem enrolmentSystem = new EnrolmentSystem();
        Student s1 = new Student("s3891724","Trung", LocalDate.parse("2002-04-22"));
        enrolmentSystem.addStudent(s1);
        Course c1 = new Course("COSC2044","Further Programming", 12);
        enrolmentSystem.addCourse(c1);

        enrolmentSystem.add("s3891724", "COSC2044", "2021C");
        System.out.println(enrolmentSystem.getStudentEnrolmentList());
        enrolmentSystem.delete("s3891724","COSC2044","2021C");
        System.out.println(enrolmentSystem.getStudentEnrolmentList());

    }

    private ArrayList<StudentEnrolment> studentEnrolmentList;
    private ArrayList<Student> studentList;
    private ArrayList<Course> coursesList;

    public EnrolmentSystem() {
        studentEnrolmentList = new ArrayList<>();
        studentList = new ArrayList<>();
        coursesList = new ArrayList<>();
    }

    public void addStudent(Student student){
        studentList.add(student);
    }

    public void addCourse(Course course){
        coursesList.add(course);
    }

    public ArrayList<StudentEnrolment> getStudentEnrolmentList() {
        return studentEnrolmentList;
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public ArrayList<Course> getCoursesList() {
        return coursesList;
    }

    private Student isStudentExisted(String studentID){
        for(Student student: studentList)
            if(student.getId().equals(studentID))
                return student;
        return null;
    }

    private Course isCourseExisted(String courseID){
        for(Course course: coursesList)
            if(course.getId().equals(courseID))
                return course;
        return null;
    }

    private String isSemValid(String sem){
        if (sem.length()!=5)
            return null;

        ArrayList<String> validSem = new ArrayList<>();
        validSem.add("A");
        validSem.add("B");
        validSem.add("C");
        if (!validSem.contains(sem.substring(4)))
            return null;

        try {
            Integer.parseInt(sem.substring(0,4));
        }
        catch (Exception e){
            return null;
        }
        return sem;
    }

    private void systemAdd(String studentID){
        Scanner sc = new Scanner(System.in);

        boolean flag;
        do {
            System.out.println("Student ID: " + studentID);
            System.out.print("course ID of course you want to add: ");
            String courseID = sc.nextLine();
            System.out.println();
            System.out.print("Semester that you want to enrol for this course: ");
            String sem = sc.nextLine();
            flag = this.add(studentID, courseID, sem);
        }
        while (!flag);
    }

    @Override
    public boolean add(String studentID, String courseID, String sem){
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        if (student == null) {
            System.out.println("invalid student");
            return false;
        }
        else if (course == null) {
            System.out.println("invalid course");
            return false;
        }
        else if (semester == null) {
            System.out.println("invalid semester");
            return false;
        }
        else{
            studentEnrolmentList.add(new StudentEnrolment(student, course, semester));
            System.out.println("enrol successful");
            return true;
        }
    }

    @Override
    public void update(String studentID) {
        Student student = isStudentExisted(studentID);
        if (student == null)
            System.out.println("This student ID is not exist");
        else {
            System.out.println("Your enroled courses:");
            for (StudentEnrolment item : studentEnrolmentList) {
                if (item.getStudent() == student)
                    System.out.println(item.getCourse().getId() + ": "
                            + item.getCourse().getName() + " - "
                            + item.getSemester());
            }

            String selection;
            Scanner sc = new Scanner(System.in);

            boolean flag = true;
            do {
                System.out.println("-------------------");
                System.out.println("""
                        What update you want to make:
                          1) Add a course enrolment
                          2) Delete a course enrolment""");
                System.out.print(">>> ");
                selection = sc.nextLine();
                selection = selection.trim();

                if (selection.equals("1") || selection.equals("2")) {
                    if (selection.equals("1")) {
                        this.systemAdd(studentID);
                    } else {
                        this.systemDelete(studentID);
                    }
                } else {
                    flag = false;
                }
            } while (!flag);
        }
    }

    private void systemDelete(String studentID){
        Scanner sc = new Scanner(System.in);

        boolean flag;
        do {
            System.out.println("Student ID: " + studentID);
            System.out.print("course ID of course you want to delete: ");
            String courseID = sc.nextLine();
            System.out.println();
            System.out.print("Semester that you enroled for this course: ");
            String sem = sc.nextLine();
            flag = this.delete(studentID, courseID, sem);
        }
        while (!flag);
    }

    @Override
    public boolean delete(String studentID, String courseID, String sem) {
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        if (student == null) {
            System.out.println("invalid student");
            return false;
        }
        else if (course == null) {
            System.out.println("invalid course");
            return false;
        }
        else if (semester == null) {
            System.out.println("invalid semester");
            return false;
        }
        else {
            for (StudentEnrolment item : studentEnrolmentList) {
                if (item.getStudent() == student && item.getCourse() == course && item.getSemester().equals(semester)) {
                    studentEnrolmentList.remove(item);
                    System.out.println("Delete successful");
                    return true;
                }
            }
            System.out.println("information given is not valid");
            return false;
        }
    }

    @Override
    public StudentEnrolment getOne() {
        return null;
    }

    @Override
    public ArrayList<StudentEnrolment> getAll() {
        return null;
    }
}
