package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewMemberController implements Initializable {

    // FXML üzerindeki nesnenlerin tanımlanması
    @FXML
    TextField nameField, surnameField, tcField, ageField;

    @FXML
    ComboBox coachBox, gymBox, branchBox, timeBox;


    String selected_coach = null;
    String selected_gym = null;
    String selected_branch = null;
    String selected_time = null;

    // Veritabanı bağlantısı
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:gym.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Verileri veritabanına kaydetme
    @FXML
    public void onSave(ActionEvent actionEvent) {

        // Eğer seçili ise ComboBox üzerindeki veriler değişkenlere aktarılıyor
        if (coachBox.getSelectionModel().getSelectedIndex() != -1 && gymBox.getSelectionModel().getSelectedIndex() != -1 &&
                timeBox.getSelectionModel().getSelectedIndex() != -1 && branchBox.getSelectionModel().getSelectedIndex() != -1) {

            selected_branch = branchBox.getSelectionModel().getSelectedItem().toString();
            selected_time = timeBox.getSelectionModel().getSelectedItem().toString();
            selected_gym = gymBox.getSelectionModel().getSelectedItem().toString();
            selected_coach = coachBox.getSelectionModel().getSelectedItem().toString();
        }

        // üye bilgilerini tutan tablo oluşturuluyor
        String createSqlMember = "CREATE TABLE IF NOT EXISTS member (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, surname TEXT, branch TEXT, tc TEXT, " +
                "age INTEGER , coach, gym TEXT, time TEXT)";

        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();) {
            statement.execute(createSqlMember);

            // üye tablosuna veri eklemek için sql sorgusu
            String sql = "insert into member(name, surname, branch, tc, age, coach, gym, time) values(?,?,?,?,?,?,?,?)";
            try (PreparedStatement preparedStatement = baglanti.prepareStatement(sql)) {

                if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || tcField.getText().isEmpty() ||
                        ageField.getText().isEmpty() || selected_time == null || selected_coach == null ||
                        selected_gym == null || selected_branch == null)) {

                    // Girilen verilerle member nesnesi oluşturuluyor
                    Member member = new Member(nameField.getText(), surnameField.getText(), selected_branch,
                            tcField.getText(), Integer.parseInt(ageField.getText()), selected_gym, selected_coach);

                    // Veriler veritabanına kaydediliyor.
                    preparedStatement.setString(1, member.getFirstName());
                    preparedStatement.setString(2, member.getLastName());
                    preparedStatement.setString(3, member.getBranch());
                    preparedStatement.setString(4, member.getTc());
                    preparedStatement.setInt(5, member.getAge());
                    preparedStatement.setString(6, member.getCoach());
                    preparedStatement.setString(7, member.getGym());
                    preparedStatement.setString(8, selected_time);
                    preparedStatement.executeUpdate();

                } else {

                    // Eksik veri durumunda uyarı mesajı

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Uyarı");
                    alert.setContentText("Eksik veri girildi! Üye bilgileri kaydedilemedi!");
                    alert.showAndWait();
                }
            } catch (Exception e) {

                // Eksik veri durumunda uyarı mesajı

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setContentText("Eksik veri girildi! Üye bilgileri kaydedilemedi!");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // gym tablosu oluşturmak için sql sorgusu
        String createSqlGym = "CREATE TABLE IF NOT EXISTS gym (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, coach TEXT, student TEXT, time TEXT)";

        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();) {
            statement.execute(createSqlGym);

            // gym tablosuna veri eklemek için sql sorgusu

            String sql = "insert into gym (name, coach, student, time) values (?,?,?,?)";

            try (PreparedStatement preparedStatement = baglanti.prepareStatement(sql)) {

                if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() ||
                        selected_gym == null || selected_coach == null || selected_time == null)) {

                    // Gym nesnesi oluşturma
                    Gym gym = new Gym(selected_gym, selected_coach, nameField.getText() + " " + surnameField.getText(), selected_time);

                    // gym tablosuna verilerin eklenmesi
                    preparedStatement.setString(1, gym.getName());
                    preparedStatement.setString(2, gym.getCoach());
                    preparedStatement.setString(3, gym.getStudent());
                    preparedStatement.setString(4, gym.getTime());
                    preparedStatement.executeUpdate();

                } else {

                    // Eksik veri girişi durumunda uyarı mesajı

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Uyarı");
                    alert.setContentText("Eksik veri girildi! Salon bilgileri kaydedilemedi!");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // Pencere ilk açıldığında yapılacak işlemler
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        coachBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                // coachBox da değişiklik olduğunda yapılacak işlemler

                if (coachBox.getSelectionModel().getSelectedIndex() != -1) {
                    // Bir değer seçildiyse timeBox ı seçilen antranörün boş saatleriyle dolduruyor.
                    selected_coach = coachBox.getSelectionModel().getSelectedItem().toString();
                    fillTimeBox();
                    selected_time = null;
                }
            }
        });

        gymBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                // gymBox da değişiklik olması durumunda yapılacak işlemler

                if (gymBox.getSelectionModel().getSelectedIndex() != -1) {

                    // Bir salon seçildiğinde salondaki antranörler coachBoxa getiriliyor.
                    selected_gym = gymBox.getSelectionModel().getSelectedItem().toString();
                    fillCoachBox();
                    selected_coach = null;
                    selected_time = null;
                    selected_branch = null;
                }
            }
        });
    }

    // Boş saatleri tutacak liste
    ArrayList<String> times;

    // Time Box'ı dolduracak fonksiyon
    public void fillTimeBox() {

        // member tablosundan veri çekmek için sql sorgusu
        String sql = "select * from member where coach='" + selected_coach + "'";

        // Tüm saatler listeye ekleniyor.
        times = new ArrayList<>();
        times.add("10:00 - 12:00");
        times.add("12:00 - 14:00");
        times.add("14:00 - 16:00");
        times.add("16:00 - 18:00");
        times.add("18:00 - 20:00");
        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet read = statement.executeQuery(sql)) {

            // tablo okunuyor ve dolu saatler listeden kaldırılıyor
            while (read.next()) {
                int idx = times.indexOf(read.getString("time"));
                times.remove(idx);
            }

            // timeBox boşaltılıyor ve boş saatler ekleniyor.
            timeBox.getItems().clear();
            for (String time : times) {
                timeBox.getItems().add(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // coachBox nesnesini dolduran fonksiyon
    public void fillCoachBox() {

        // Coach tablosundan mevcut salona göre bilgileri getiren sql sorgusu
        String sql = "select * from coach where gym='" + selected_gym + "'";
        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet read = statement.executeQuery(sql)) {

            // coachBox temizleniyor ve gelen bilgiler ekleniyor.
            coachBox.getItems().clear();
            while (read.next()) {
                coachBox.getItems().add(read.getString("name") + " " + read.getString("surname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
