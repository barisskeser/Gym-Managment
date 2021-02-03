package sample;

import java.util.ArrayList;

public class Gym {
    // Salon bilgilerini alarak nesne oluşturmak üzere kullanılmış bir sınıf.
    private String name;
    private String coach;
    private String student;
    private String time;

    public Gym(String name, String coach, String student, String time) {
        this.name = name;
        this.coach = coach;
        this.student = student;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getCoach() {
        return coach;
    }

    public String getStudent() {
        return student;
    }

    public String getTime() {
        return time;
    }
}
