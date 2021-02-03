package sample;

public class Member extends Entry {

    // Üye bilgilerini alarak nesne oluşturulmak üzere kullanılmış bir sınıf.

    private String gym;
    private String coach;

    public Member(String firstName, String lastName, String branch, String tc, int age) {
        super(firstName, lastName, branch, tc, age);
    }

    public Member(String firstName, String lastName, String branch, String tc, int age, String gym, String coach) {
        super(firstName, lastName, branch, tc, age);
        this.gym = gym;
        this.coach = coach;
    }

    @Override
    public String toString() {

        return "\nFirst Name : " + getFirstName()
                + "\nLast Name : " + getLastName()
                + "\nAge : " + getAge()
                + "\nTC : " + getTc()
                + "\nBranch : " + getBranch()
                + "\nGym : " + getGym()
                + "\nCoach : " + getCoach() + "\n";
    }

    public String getGym() {
        return gym;
    }


    public String getCoach() {
        return coach;
    }
}
