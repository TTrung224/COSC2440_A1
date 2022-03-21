import java.util.ArrayList;

public interface StudentEnrolmentManager{
    public boolean add(String studentID, String courseID, String sem);
    public void update(String studentID);
    public boolean delete(String studentID, String courseID, String sem);
    public StudentEnrolment getOne();
    public ArrayList<StudentEnrolment> getAll();
}
