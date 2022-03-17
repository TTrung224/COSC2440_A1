import java.util.ArrayList;

public class EnrolmentSystem implements StudentEnrolmentManager{
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

    @Override
    public void add(String studentID, String courseID, String sem){
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        if (student == null)
            System.out.println("invalid student");
        else if (course == null)
            System.out.println("invalid course");
        else if (semester == null)
            System.out.println("invalid semester");
        else{
            studentEnrolmentList.add(new StudentEnrolment(student, course, semester));
            System.out.println("enrol successful");
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void delete(String studentID, String courseID, String sem) {
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        if (student == null)
            System.out.println("invalid student");
        else if (course == null)
            System.out.println("invalid course");
        else if (semester == null)
            System.out.println("invalid semester");
        else {
            boolean implementFlag = false;
            for (StudentEnrolment item : studentEnrolmentList) {
                if (item.getStudent() == student && item.getCourse() == course && item.getSemester().equals(semester)) {
                    studentEnrolmentList.remove(item);
                    System.out.println("Delete successful");
                    implementFlag = true;
                    break;
                }
            }
            if(!implementFlag)
                System.out.println("information given is not valid");
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

interface StudentEnrolmentManager{
    public void add(String studentID, String courseID, String sem);
    public void update();
    public void delete(String studentID, String courseID, String sem);
    public StudentEnrolment getOne();
    public ArrayList<StudentEnrolment> getAll();
}