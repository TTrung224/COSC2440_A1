import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Student {
    final private String id, name;
    final private LocalDate birthDate;

    public Student(String id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}