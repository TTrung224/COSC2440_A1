import java.util.ArrayList;

public interface StudentEnrolmentManager{
    public boolean add(String studentID, String courseID, String sem);
    public void update(Student student, String option);
    public boolean delete(String studentID, String courseID, String sem);
    public StudentEnrolment getOne(String studentID, String courseID, String sem);
    public ArrayList<StudentEnrolment> getAll();
}
