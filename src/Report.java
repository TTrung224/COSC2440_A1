import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

abstract class Report {
    private String filePath;
    public abstract void report(EnrolmentSystem enrolmentSystem);
    public abstract void reportFile(EnrolmentSystem enrolmentSystem);

    public void setFilePath(){
        Scanner sc = new Scanner(System.in);
        boolean flag;
        do {
            System.out.print("Enter file path to export report (.csv): ");
            filePath = sc.nextLine();
            flag = filePath.contains(".csv");
        } while(!flag);
    }

    public String getFilePath() {
        return filePath;
    }
}

class allCourseForStudent extends Report{
    private final Student student;
    private final String semester;

    public allCourseForStudent(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        Student stu;
        String sem;
        do {
            System.out.print("Student ID: ");
            String studentID = sc.nextLine().trim().toUpperCase();
            System.out.print("Semester: ");
            String semInput = sc.nextLine().trim().toUpperCase();
            System.out.println();
            stu = enrolmentSystem.isStudentExisted(studentID);
            sem = enrolmentSystem.isSemValid(semInput);
            if (stu == null)
                System.out.println("This student ID is not exist");
            else if (sem == null)
                System.out.println("Invalid semester input");

        } while (stu == null || sem == null);

        this.student = stu;
        this.semester = sem;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        System.out.printf("Courses of student %s in semester %s:\n", student.getId(), semester);
        int count = 0;
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getStudent()== this.student && item.getSemester().equals(semester)) {
                System.out.println(item.getCourse());
                count++;
            }
        }
        if (count == 0)
            System.out.printf("Student %s have not enroll in any courses in semester %s\n", student.getId(), semester);
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        super.setFilePath();
        String filePath = super.getFilePath();
        try(PrintWriter pw = new PrintWriter(filePath)){
            System.out.println("-------------------");
            System.out.printf("Courses of student %s in semester %s:\n", student.getId(), semester);
            for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getStudent() == this.student && item.getSemester().equals(semester)) {
                    count++;
                    StringBuilder sb = new StringBuilder();
                    System.out.println(item.getCourse());
                    sb.append(item.getCourse().getId());
                    sb.append(",");
                    sb.append(item.getCourse().getName());
                    sb.append(",");
                    sb.append(item.getCourse().getNumberOfCredits());
                    pw.println(sb);
                }
            }
            if (count == 0) {
                File file = new File(filePath);
                file.delete();
                System.out.printf("Student %s have not enroll in any courses in semester %s\n", student.getId(), semester);
            }
            else
                System.out.println("Report file generated");
        } catch (FileNotFoundException e){
            reportFile(enrolmentSystem);
        }
    }
}

class allStudentOfCourse extends Report{
    private final Course course;
    private final String semester;

    public allStudentOfCourse(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        Course course;
        String sem;
        do {
            System.out.print("Course ID: ");
            String courseID = sc.nextLine().trim().toUpperCase();
            System.out.print("Semester: ");
            String semInput = sc.nextLine().trim().toUpperCase();
            course = enrolmentSystem.isCourseExisted(courseID);
            sem = enrolmentSystem.isSemValid(semInput);
            if (course == null)
                System.out.println("This course ID is not exist");
            else if (sem == null)
                System.out.println("Invalid semester input");
        } while (course == null || sem == null);
        this.course = course;
        this.semester = sem;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        System.out.printf("Students of course %s in semester %s:\n", course.getId(), semester);
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getCourse() == this.course && item.getSemester().equals(semester)) {
                count++;
                System.out.println(item.getStudent());
            }
        }
        if (count == 0)
            System.out.printf("This course %s has no student in semester %s \n", course.getId(), semester);
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        super.setFilePath();
        String filePath = super.getFilePath();

        try (PrintWriter pw = new PrintWriter(filePath)) {
            System.out.println("-------------------");
            System.out.printf("Students of course %s in semester %s:\n", course.getId(), semester);
            for (StudentEnrolment item : enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getCourse() == this.course && item.getSemester().equals(semester)) {
                    count++;
                    StringBuilder sb = new StringBuilder();
                    System.out.println(item.getStudent());
                    sb.append(item.getStudent().getId());
                    sb.append(",");
                    sb.append(item.getStudent().getName());
                    sb.append(",");
                    sb.append(item.getStudent().getBirthDate());
                    pw.println(sb);
                }
            }
            if (count == 0) {
                File file = new File(filePath);
                file.delete();
                System.out.printf("This course %s has no student in semester %s \n", course.getId(), semester);
            } else
                System.out.println("Report file generated");
        } catch (FileNotFoundException e) {
            reportFile(enrolmentSystem);
        }
    }
}

class allCourseInSem extends Report {
    private final String semester;

    public allCourseInSem(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        String sem;
        do {
            System.out.print("Semester: ");
            String semInput = sc.nextLine().trim().toUpperCase();
            sem = enrolmentSystem.isSemValid(semInput);
            if (sem == null)
                System.out.println("Invalid semester input");
        } while (sem == null);
        this.semester = sem;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        ArrayList<Course> courseList = new ArrayList<>();
        System.out.printf("Courses offered in semester %s:\n", semester);
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getSemester().equals(semester) && !courseList.contains(item.getCourse())) {
                count++;
                System.out.println(item.getCourse());
                courseList.add(item.getCourse());
            }
        }
        if (count == 0)
            System.out.printf("This semester %s has no course \n", semester);
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        super.setFilePath();
        String filePath = super.getFilePath();

        ArrayList<Course> courseList = new ArrayList<>();
        try (PrintWriter pw = new PrintWriter(filePath)) {
            System.out.println("-------------------");
            System.out.printf("Courses offered in semester %s:\n", semester);
            for (StudentEnrolment item : enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getSemester().equals(semester) && !courseList.contains(item.getCourse())) {
                    courseList.add(item.getCourse());
                    count++;
                    StringBuilder sb = new StringBuilder();
                    System.out.println(item.getCourse());
                    sb.append(item.getCourse().getId());
                    sb.append(",");
                    sb.append(item.getCourse().getName());
                    sb.append(",");
                    sb.append(item.getCourse().getNumberOfCredits());
                    pw.println(sb);
                }
            }
            if (count == 0) {
                File file = new File(filePath);
                file.delete();
                System.out.printf("This semester %s has no course \n", semester);
            } else
                System.out.println("Report file generated");
        } catch (FileNotFoundException e) {
            reportFile(enrolmentSystem);
        }
    }
}
