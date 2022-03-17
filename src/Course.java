public class Course {
    final private String id, name;
    final private int numberOfCredits;

    public Course(String id, String name, int numberOfCredits) {
        this.id = id;
        this.name = name;
        this.numberOfCredits = numberOfCredits;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

}
