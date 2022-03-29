import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EnrolmentSystem implements StudentEnrolmentManager{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String filePath;

        EnrolmentSystem enrolmentSystem = new EnrolmentSystem();
        System.out.println("______Enrolment System______");
        boolean flag;
        do {
            System.out.print("Enter a file path for system database (press enter to use default.csv file): ");
            filePath = sc.nextLine();
            flag = enrolmentSystem.readData((filePath.equals("")) ? "default.csv" : filePath);
        } while(!flag);

        while (true) {
            String selection;
            int menuInputError=0;

            do {
                if (menuInputError!=0)
                    System.out.println("Please enter number 1 to 4 only.");
                System.out.println("-------------------");
                System.out.println(
                        """
                           Menu:
                             1) Enroll
                             2) Delete enrolment
                             3) Update enrolment
                             4) Report
                             5) Exit""");
                System.out.print(">>> ");
                selection = sc.nextLine().trim();
                menuInputError++;

            } while (!selection.equals("1")
                    &&!selection.equals("2")
                    &&!selection.equals("3")
                    &&!selection.equals("4")
                    &&!selection.equals("5"));

            switch (selection) {
                case "1" -> {
                    boolean enrollFlag;
                    do {
                        System.out.print("Student ID: ");
                        String studentID = sc.nextLine().trim().toUpperCase();
                        System.out.print("Course ID of course you want to add: ");
                        String courseID = sc.nextLine().trim().toUpperCase();
                        System.out.print("Semester that you want to enroll for this course: ");
                        String sem = sc.nextLine().trim().toUpperCase();
                        enrollFlag = enrolmentSystem.add(studentID, courseID, sem);
                    } while (!enrollFlag);
                }

                case "2" -> {
                    boolean deleteFlag;
                    do {
                        System.out.print("Student ID: ");
                        String studentID = sc.nextLine().trim().toUpperCase();
                        System.out.print("Course ID of course you want to drop: ");
                        String courseID = sc.nextLine().trim().toUpperCase();
                        System.out.print("Semester that you enrolled for this course: ");
                        String sem = sc.nextLine().trim().toUpperCase();
                        deleteFlag = enrolmentSystem.delete(studentID, courseID, sem);
                    } while (!deleteFlag);
                }

                case "3" -> {
                    Student student;
                    do {
                        System.out.print("Student ID: ");
                        String studentID = sc.nextLine().trim().toUpperCase();
                        System.out.println();
                        student = enrolmentSystem.isStudentExisted(studentID);
                        if (student == null)
                            System.out.println("This student ID is not exist");
                    } while (student == null);

                    String updateSelection;
                    int updateSelectionError=0;
                    do {
                        if (updateSelectionError!=0)
                            System.out.println("Please enter number from 1 to 3 only.");
                        System.out.println("-------------------");
                        System.out.println(
                                """
                                        What update do you want to make:
                                          1) Add a course enrolment
                                          2) Delete a course enrolment
                                          3) Return back to menu"""
                        );
                        System.out.print(">>> ");
                        updateSelection = sc.nextLine().trim();
                        updateSelectionError++;

                    } while (!updateSelection.equals("1") && !updateSelection.equals("2") && !updateSelection.equals("3"));

                    if (updateSelection.equals("1"))
                        enrolmentSystem.update(student, "1");
                    else if (updateSelection.equals("2"))
                        enrolmentSystem.update(student, "2");
                }

                case "4" -> {
                    String reportSelection;
                    String fileExportSelection;
                    int reportSelectionError=0;
                    do {
                        if (reportSelectionError!=0)
                            System.out.println("Please enter number from 1 to 4 only.");
                        System.out.println("-------------------");
                        System.out.println(
                                """
                                        What report do you want to get:
                                          1) Courses for 1 student in 1 semester
                                          2) Students of 1 course in 1 semester
                                          3) Courses offered in 1 semester
                                          4) Back to menu"""
                        );
                        System.out.print(">>> ");
                        reportSelection = sc.nextLine().trim();
                        reportSelectionError++;
                    } while (!reportSelection.equals("1")
                            && !reportSelection.equals("2")
                            && !reportSelection.equals("3")
                            && !reportSelection.equals("4"));

                    if (reportSelection.equals("4"))
                        break;

                    Report report;
                    do {
                        System.out.println("Do you want to get a csv report file (Y/N): ");
                        System.out.print(">>> ");
                        fileExportSelection = sc.nextLine().trim().toUpperCase();
                    } while (!fileExportSelection.equals("Y") && !fileExportSelection.equals("N"));

                    if (reportSelection.equals("1")) {
                        report = new allCourseForStudent(enrolmentSystem);
                    } else if (reportSelection.equals("2")) {
                        report = new allStudentOfCourse(enrolmentSystem);
                    } else {
                        report = new allCourseInSem(enrolmentSystem);
                    }
                    if (fileExportSelection.equals("N"))
                        report.report(enrolmentSystem);
                    else report.reportFile(enrolmentSystem);
                }

                case "5" -> {
                    System.out.println("Thank you for using, see you again!");
                    System.exit(0);
                }
            }
        }
    }

    // ArrayList to store list of all students, courses and studentEnrolments
    private ArrayList<StudentEnrolment> studentEnrolmentList;
    private ArrayList<Student> studentList;
    private ArrayList<Course> coursesList;

    // Constructor for the EnrolmentSystem
    public EnrolmentSystem() {
        studentEnrolmentList = new ArrayList<>();
        studentList = new ArrayList<>();
        coursesList = new ArrayList<>();
    }

    protected ArrayList<StudentEnrolment> getStudentEnrolmentList() {
        return studentEnrolmentList;
    }

    // This method loop through the studentList to check if student is existed or not
    // if the student existed, it will return the student instance, else it returns null
    protected Student isStudentExisted(String studentID){
        for(Student student: studentList)
            if(student.getId().equals(studentID))
                return student;
        return null;
    }

    // This method loop through the courseList to check if course is existed or not
    // if the course existed, it will return the course instance, else it returns null
    protected Course isCourseExisted(String courseID){
        for(Course course: coursesList)
            if(course.getId().equals(courseID))
                return course;
        return null;
    }

    // This method to check the user's input semester format
    protected String isSemValid(String sem){
        if (sem.length()!=5)
            return null;

        ArrayList<String> validSem = new ArrayList<>();
        validSem.add("A");
        validSem.add("B");
        validSem.add("C");

        // This if to check the last keyword of semester
        if (!validSem.contains(sem.substring(4)))
            return null;

        // Try to parse the first 4 keyword into int to check if they are numbers or not
        try {
            Integer.parseInt(sem.substring(0,4));
        }
        catch (Exception e){
            return null;
        }
        return sem;
    }

    // This method will loop through the studentEnrolmentList to check if a studentEnrolment has existed or not
    private boolean isStudentEnrolmentExisted(Student student, Course course, String semester){
        for(StudentEnrolment item: this.studentEnrolmentList){
            if (student == item.getStudent()
                    && course == item.getCourse()
                    && semester.equals(item.getSemester())){
                return true;
            }
        }
        return false;
    }

    // This method to create and add student enrolment into the system by String parameters input
    @Override
    public boolean add(String studentID, String courseID, String sem){
        //Check validation of Strings and objects
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        //Cases and return messages of cases
        if (student == null) {
            System.out.println("Invalid student");
            return false;
        }
        else if (course == null) {
            System.out.println("Invalid course");
            return false;
        }
        else if (semester == null) {
            System.out.println("Invalid semester");
            return false;
        }
        //Check if the studentEnrolment have existed or not
        else if (isStudentEnrolmentExisted(student, course, semester)) {
            System.out.println("Same enrolment existed");
            return false;
        }
        else{
            studentEnrolmentList.add(new StudentEnrolment(student, course, semester));
            System.out.println("Enroll successful");
            return true;
        }
    }

    // This method to remove studentEnrolment from the system by String parameters input
    @Override
    public boolean delete(String studentID, String courseID, String sem) {
        StudentEnrolment studentEnrolment = this.getOne(studentID, courseID, sem);
            if(studentEnrolment != null) {
                this.studentEnrolmentList.remove(studentEnrolment);
                System.out.println("Delete successful");
                return true;
            }
        // If no studentEnrolment suitable to delete, it means there are no such studentEnrolment
        return false;
    }

    //This method will get a Student instance and option of the update to add or delete an enrolment of that student
    @Override
    public void update(Student student, String option) {
        Scanner sc = new Scanner(System.in);

        //Print all the courses enrolled by the student
        System.out.println("Your enrolled courses:");
        for (StudentEnrolment item : this.studentEnrolmentList) {
            if (item.getStudent() == student)
                System.out.println(item.getCourse().getId() + ": "
                        + item.getCourse().getName()
                        + " (credit: " + item.getCourse().getNumberOfCredits() + ")"
                        + " - " + item.getSemester());
        }

        //Case that add enrolment
        if (option.equals("1")) {
            boolean flag;
            //Get user input
            do {
                System.out.println("Student ID: " + student.getId());
                System.out.print("Course ID of course you want to add: ");
                String courseID = sc.nextLine().trim().toUpperCase();
                System.out.print("Semester that you want to enroll for this course: ");
                String sem = sc.nextLine().trim().toUpperCase();
                flag = this.add(student.getId(), courseID, sem);
            }
            while (!flag);

        //Case that delete enrolment
        } else {
            boolean flag;
            //Get user input
            do {
                System.out.println("Student ID: " + student.getId());
                System.out.print("Course ID of course you want to delete: ");
                String courseID = sc.nextLine().trim().toUpperCase();
                System.out.print("Semester that you enrolled for this course: ");
                String sem = sc.nextLine().trim().toUpperCase();
                flag = this.delete(student.getId(), courseID, sem);
            }
            while (!flag);
        }
    }

    //This method get string of studentID, courseID and semester to return a student enrolment
    @Override
    public StudentEnrolment getOne(String studentID, String courseID, String sem) {
        Student student = isStudentExisted(studentID);
        Course course = isCourseExisted(courseID);
        String semester = isSemValid(sem);

        //These if to check the validation off input String parameters
        if (student == null) {
            System.out.println("Invalid student");
            return null;
        }
        else if (course == null) {
            System.out.println("Invalid course");
            return null;
        }
        else if (semester == null) {
            System.out.println("Invalid semester");
            return null;
        }
        //If everything is validated, loop through the studentEnrolmentList to find the appropriate
        else {
            for (StudentEnrolment item : studentEnrolmentList) {
                if (item.getStudent() == student && item.getCourse() == course && item.getSemester().equals(semester)) {
                    return item;
                }
            }
        }
        System.out.println("Information given is not valid");
        return null;
    }

    @Override
    public ArrayList<StudentEnrolment> getAll() {
        return studentEnrolmentList;
    }

    private boolean readData(String filePath){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] colValue = line.split(",");
                Student stu = null;
                Course cou = null;

                int studentCount = 0;
                for (Student student: studentList){
                    if(colValue[0].trim().equals(student.getId())) {
                        studentCount++;
                        stu = student;
                        break;
                    }
                }
                if (studentCount == 0) {
                    stu = new Student(colValue[0].trim(), colValue[1].trim(), colValue[2].trim());
                    studentList.add(stu);
                }

                int courseCount = 0;
                for (Course course: coursesList){
                    if(colValue[3].trim().equals(course.getId())) {
                        courseCount++;
                        cou = course;
                        break;
                    }
                }

                if (courseCount == 0) {
                    cou = new Course(colValue[3].trim(), colValue[4].trim(), Integer.parseInt(colValue[5].trim()));
                    coursesList.add(cou);
                }

                studentEnrolmentList.add(new StudentEnrolment(stu, cou, colValue[6]));
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found");
            return false;
        } catch (IOException e){
            System.out.println("Invalid file or data");
            return false;
        }
        return true;
    }
}
