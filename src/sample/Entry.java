package sample;

public class Entry {

    // Member ve Coach sınıflarının ortak parametreleri için oluşturulmuş bir sınıf.

    private String firstName;
    private String lastName;
    private String tc;
    private int age;
    private String branch;

    public Entry(String firstName, String lastName, String branch, String tc, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
        this.tc = tc;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTc() {
        return tc;
    }

    public int getAge() {
        return age;
    }

    public String getBranch() {
        return branch;
    }
}
