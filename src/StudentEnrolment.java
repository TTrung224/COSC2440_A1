public class StudentEnrolment {
    final private Student student;
    final private Course course;
    final private String semester;

    public StudentEnrolment(Student student, Course course, String semester) {
        this.student = student;
        this.course = course;
        this.semester = semester;
    }

    protected Student getStudent() {
        return student;
    }

    protected Course getCourse() {
        return course;
    }

    protected String getSemester() {
        return semester;
    }

}
