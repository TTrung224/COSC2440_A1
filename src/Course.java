public class Course {
    final private String id, name;
    final private int numberOfCredits;

    public Course(String id, String name, int numberOfCredits) {
        this.id = id;
        this.name = name;
        this.numberOfCredits = numberOfCredits;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (Credit: " + numberOfCredits + ")";
    }

    protected String getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected int getNumberOfCredits() {
        return numberOfCredits;
    }

}
