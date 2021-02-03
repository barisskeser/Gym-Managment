package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NewCoachController implements Initializable {

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

    // FXML üzerinden nesneler çağrılıyor
    @FXML
    TextField nameField, surnameField, ageField, tcField;

    @FXML
    ComboBox branchBox, gymBox;

    // Yeni antrenör kayıt buton Action'ı
    @FXML
    public void onSave(ActionEvent actionEvent){

        // coach tablosu oluşturmak için sql sorgusu
        String sql = "create table if not exists coach(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT," +
                "surname TEXT, branch TEXT, tc TEXT, age INTEGER, gym TEXT)";

        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement()){

            statement.execute(sql);

            //Veri eklemek için sql sorgusu
            String sqlInsert = "insert into coach(name, surname, branch, tc, age, gym) values(?,?,?,?,?,?)";
            try (PreparedStatement preparedStatement = baglanti.prepareStatement(sqlInsert)){
                String selected_branch = branchBox.getSelectionModel().getSelectedItem().toString();
                String selected_gym = gymBox.getSelectionModel().getSelectedItem().toString();

                if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || tcField.getText().isEmpty() || ageField.getText().isEmpty())) {

                    // Polymorphism ile coach nesnesi oluşturma
                    Entry coach = new Coach(nameField.getText(), surnameField.getText(), selected_branch, tcField.getText()
                    , Integer.parseInt(ageField.getText()));

                    // Kayıt işlemi için veriler alınıyor
                    preparedStatement.setString(1, coach.getFirstName());
                    preparedStatement.setString(2, coach.getLastName());
                    preparedStatement.setString(3, coach.getBranch());
                    preparedStatement.setString(4, coach.getTc());
                    preparedStatement.setInt(5, coach.getAge());
                    preparedStatement.setString(6, selected_gym);

                    preparedStatement.executeUpdate(); // Değişiklik işleniyor
                    System.out.println("Antranör eklendi.");
                }else{
                    System.out.println("Antranör Eklenemedi.");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Pencere ilk açıldığında yapılacak işlemler
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // ComboBoxlara veriler ekleniyor
        branchBox.getItems().add("Fitness");
        gymBox.getItems().add("Marmara Gym");
        gymBox.getItems().add("Teknoloji Gym");
    }
}
