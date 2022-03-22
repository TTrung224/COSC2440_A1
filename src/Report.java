import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

abstract class Report {
    public abstract void report(EnrolmentSystem enrolmentSystem);

    public abstract void reportFile(EnrolmentSystem enrolmentSystem);
}

class allCourseForStudent extends Report{
    Student student;

    public allCourseForStudent(EnrolmentSystem enrolmentSystem) {
        setStudent(enrolmentSystem);
    }

    private void setStudent(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        Student stu;
        do {
            System.out.println("Student ID: ");
            String studentID = sc.nextLine();
            stu = enrolmentSystem.isStudentExisted(studentID);
            if (stu == null)
                System.out.println("This student ID is not exist");
        } while (stu == null);

        this.student = stu;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        System.out.printf("Courses of student %s:\n", student.getId());
        int count = 0;
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getStudent()== this.student) {
                System.out.println(item.getCourse() + item.getSemester());
                count++;
            }
        }
        if (count == 0)
            System.out.printf("Student %s have not enroll in any courses\n", student.getId());
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        System.out.println("Enter file path to export report: ");
        String filePath = sc.nextLine();
        System.out.printf("Courses of student %s:\n", student.getId());
        try(PrintWriter pw = new PrintWriter(filePath)){
            StringBuilder sb = new StringBuilder();
            for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getStudent() == this.student) {
                    count++;
                    System.out.println(item.getCourse());
                    sb.append(item.getCourse().getId());
                    sb.append(",");
                    sb.append(item.getCourse().getName());
                    sb.append(",");
                    sb.append(item.getCourse().getNumberOfCredits());
                    sb.append(",");
                    sb.append(item.getSemester());
                    pw.println(sb);
                }
            }
            if (count == 0) {
                File file = new File(filePath);
                file.delete();
                System.out.printf("Student %s have not enroll in any courses\n", student.getId());
            }
            else
                System.out.println("report file generated");
        } catch (FileNotFoundException e){
            reportFile(enrolmentSystem);
        }
    }
}

class allStudentOfCourse extends Report{
    Course course;
    String semester;

    public allStudentOfCourse(EnrolmentSystem enrolmentSystem) {
        setVariable(enrolmentSystem);
    }

    private void setVariable(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        Course course;
        String sem;
        do {
            System.out.println("Course ID: ");
            String courseID = sc.nextLine();
            System.out.println("Semester: ");
            sem = sc.nextLine();
            course = enrolmentSystem.isCourseExisted(courseID);
            sem = enrolmentSystem.isSemValid(sem);
            if (course == null)
                System.out.println("This student ID is not exist");
            else if (sem == null)
                System.out.println("invalid semester input");
        } while (course == null || sem == null);
        this.course = course;
        this.semester = sem;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        System.out.printf("Students of course %s:\n", course.getId());
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getCourse() == this.course && item.getSemester().equals(semester))
                count++;
                System.out.println(item.getStudent());
        }
        if (count == 0)
            System.out.printf("This course %s have no student \n", course.getId());
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        System.out.println("Enter file path to export report: ");
        String filePath = sc.nextLine();
        System.out.printf("Students of course %s:\n", course.getId());
        try (PrintWriter pw = new PrintWriter(filePath)) {
            StringBuilder sb = new StringBuilder();
            for (StudentEnrolment item : enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getCourse() == this.course && item.getSemester().equals(semester)) {
                    count++;
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
                System.out.printf("This course %s have no student \n", course.getId());
            } else
                System.out.println("report file generated");
        } catch (FileNotFoundException e) {
            reportFile(enrolmentSystem);
        }
    }
}

class allCourseInSem extends Report {
    String semester;

    public allCourseInSem(EnrolmentSystem enrolmentSystem) {
        setVariable(enrolmentSystem);
    }

    private void setVariable(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        String sem;
        do {
            System.out.println("Semester: ");
            sem = sc.nextLine();
            sem = enrolmentSystem.isSemValid(sem);
            if (sem == null)
                System.out.println("invalid semester input");
        } while (sem == null);
        this.semester = sem;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        int count = 0;
        System.out.printf("Courses offered in semester %s:\n", semester);
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getSemester().equals(semester))
                count++;
            System.out.println(item.getStudent());
        }
        if (count == 0)
            System.out.printf("This course %s have no student \n", course.getId());
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {

    }
}
