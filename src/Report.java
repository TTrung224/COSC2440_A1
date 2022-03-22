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

    public void setStudent(Student student, EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Student ID: ");
            String studentID = sc.nextLine();
            student = enrolmentSystem.isStudentExisted(studentID);
            if (student == null)
                System.out.println("This student ID is not exist");
        } while (student == null);

        this.student = student;
    }

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {
        System.out.printf("Courses of student %s:\n", student.getId());
        for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()){
            if (item.getStudent()== this.student)
                System.out.println(item.getCourse());
        }
    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter file path to export report: ");
        String filePath = sc.nextLine();
        System.out.printf("Courses of student %s:\n", student.getId());
        try(PrintWriter pw = new PrintWriter(filePath)){
            StringBuilder sb = new StringBuilder();
            for (StudentEnrolment item: enrolmentSystem.getStudentEnrolmentList()) {
                if (item.getStudent() == this.student) {
                    System.out.println(item.getCourse());
                    sb.append(item.getCourse().getId());
                    sb.append(",");
                    sb.append(item.getCourse().getName());
                    sb.append(",");
                    sb.append(item.getCourse().getNumberOfCredits());
                    pw.println(sb);
                }
            }
            System.out.println("report file exported");
        } catch (FileNotFoundException e){
            reportFile(enrolmentSystem);
        }
    }
}

class allStudentOfCourse extends Report{

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {

    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {

    }
}

class allCourseInSem extends Report {

    @Override
    public void report(EnrolmentSystem enrolmentSystem) {

    }

    @Override
    public void reportFile(EnrolmentSystem enrolmentSystem) {

    }
}
