public class Student {
    final private String id, name, birthDate;

    public Student(String id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (Birthdate: " + birthDate + ")";
    }

    protected String getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected String getBirthDate() {
        return birthDate;
    }
}