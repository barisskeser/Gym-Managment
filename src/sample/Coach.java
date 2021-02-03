package sample;

import java.util.ArrayList;

public class Coach extends Entry {
    // Antranör bilgilerini alarak nesne oluşturulmak üzere kullanılmış bir sınıf.
    public Coach(String firstName, String lastName, String branch, String tc, int age) {
        super(firstName, lastName, branch, tc, age);
    }

    @Override
    public String toString() {
        return "\nFirst Name : " + getFirstName()
                + "\nLast Name : " + getLastName()
                + "\nAge : " + getAge()
                + "\nTC : " + getTc()
                + "\nBranch : " + getBranch();
    }

}
