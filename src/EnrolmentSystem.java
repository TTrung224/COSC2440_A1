import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EnrolmentSystem implements StudentEnrolmentManager{
    public static void main(String[] args) throws IOException {
        while (true) {
            Scanner sc = new Scanner(System.in);
            EnrolmentSystem enrolmentSystem = new EnrolmentSystem();
            System.out.println("___Enrolment System___");
            boolean flag;
            do {
                System.out.print("enter a file path for system database (press enter to use default.csv file: ");
                String filePath = sc.nextLine();
                flag = enrolmentSystem.readData((filePath.equals("")) ? "default.csv" : filePath);
            } while(!flag);

            String selection;

            do {
                System.out.println(
                        """
                           Menu:
                             1) Enroll
                             2) Delete enrolment
                             3) Update enrolment
                             4) Report
                        """);
                System.out.print(">>> ");
                selection = sc.nextLine();
            } while (!selection.equals("1")&&!selection.equals("2")&&!selection.equals("3")&&!selection.equals("4"));
            switch (selection){
                case "1": {
                    boolean enrollFlag;
                    do {
                        System.out.println("Student ID: ");
                        String studentID = sc.nextLine();
                        System.out.print("course ID of course you want to add: ");
                        String courseID = sc.nextLine();
                        System.out.print("\nSemester that you want to enroll for this course: ");
                        String sem = sc.nextLine();
                        enrollFlag = enrolmentSystem.add(studentID, courseID, sem);
                    } while (enrollFlag);
                    break;
                }

                case "2": {
                    boolean deleteFlag;
                    do {
                        System.out.println("Student ID: ");
                        String studentID = sc.nextLine();
                        System.out.print("course ID of course you want to add: ");
                        String courseID = sc.nextLine();
                        System.out.print("\nSemester that you want to enroll for this course: ");
                        String sem = sc.nextLine();
                        deleteFlag = enrolmentSystem.delete(studentID, courseID, sem);
                    } while (deleteFlag);
                    break;
                }

                case "3": {
                    Student student;
                    do {
                        System.out.println("Student ID: ");
                        String studentID = sc.nextLine();
                        student = enrolmentSystem.isStudentExisted(studentID);
                        if (student == null)
                            System.out.println("This student ID is not exist");
                    } while (student == null);


                    System.out.println("Your enrolled courses:");
                    for (StudentEnrolment item : enrolmentSystem.studentEnrolmentList) {
                        if (item.getStudent() == student)
                            System.out.println(item.getCourse().getId() + ": "
                                    + item.getCourse().getName() + " - "
                                    + item.getSemester());
                    }

                    String updateSelection;
                    do {
                        System.out.println("-------------------");
                        System.out.println(
                                """
                                        What update you want to make:
                                          1) Add a course enrolment
                                          2) Delete a course enrolment
                                        """
                        );
                        System.out.print(">>> ");
                        updateSelection = sc.nextLine().trim();

                    } while (!updateSelection.equals("1") && !updateSelection.equals("2"));

                    if (updateSelection.equals("1"))
                        enrolmentSystem.update(student, "1");
                    else enrolmentSystem.update(student, "2");
                    break;
                }

                case "4": {

                }

            }
        }
    }

    private ArrayList<StudentEnrolment> studentEnrolmentList;
    private ArrayList<Student> studentList;
    private ArrayList<Course> coursesList;

    public EnrolmentSystem() {
        studentEnrolmentList = new ArrayList<>();
        studentList = new ArrayList<>();
        coursesList = new ArrayList<>();
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

    protected Student isStudentExisted(String studentID){
        for(Student student: studentList)
            if(student.getId().equals(studentID))
                return student;
        return null;
    }

    protected Course isCourseExisted(String courseID){
        for(Course course: coursesList)
            if(course.getId().equals(courseID))
                return course;
        return null;
    }

    protected String isSemValid(String sem){
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
            System.out.println("enroll successful");
            return true;
        }
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
    public void update(Student student, String option) {
        Scanner sc = new Scanner(System.in);
        if (option.equals("1")) {
            boolean flag;
            do {
                System.out.println("Student ID: " + student.getId());
                System.out.print("course ID of course you want to add: ");
                String courseID = sc.nextLine();
                System.out.print("\nSemester that you want to enroll for this course: ");
                String sem = sc.nextLine();
                flag = this.add(student.getId(), courseID, sem);
            }
            while (!flag);
        } else {
            boolean flag;
            do {
                System.out.println("Student ID: " + student.getId());
                System.out.print("course ID of course you want to delete: ");
                String courseID = sc.nextLine();
                System.out.println();
                System.out.print("Semester that you enrolled for this course: ");
                String sem = sc.nextLine();
                flag = this.delete(student.getId(), courseID, sem);
            }
            while (!flag);
        }
    }

    @Override
    public StudentEnrolment getOne(String studentID, String courseID, String sem) {
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        if (student == null) {
            System.out.println("invalid student");
            return null;
        }
        else if (course == null) {
            System.out.println("invalid course");
            return null;
        }
        else if (semester == null) {
            System.out.println("invalid semester");
            return null;
        }
        else {
            for (StudentEnrolment item : studentEnrolmentList) {
                if (item.getStudent() == student && item.getCourse() == course && item.getSemester().equals(semester)) {
                    return item;
                }
            }
        }
        System.out.println("information given is not valid");
        return null;
    }

    @Override
    public ArrayList<StudentEnrolment> getAll() {
        return studentEnrolmentList;
    }

    private boolean readData(String filePath) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] colValue = line.split(",");
                Student student = new Student(colValue[0].trim(), colValue[1].trim(), colValue[2].trim());
                studentList.add(student);
                Course course = new Course(colValue[3].trim(), colValue[4].trim(), Integer.parseInt(colValue[5].trim()));
                coursesList.add(course);
                studentEnrolmentList.add(new StudentEnrolment(student, course, colValue[6]));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
            return false;
        } catch (IOException e){
            System.out.println("invalid file");
            return false;
        }
        return true;
    }
}
