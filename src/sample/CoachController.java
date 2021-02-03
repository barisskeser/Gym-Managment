package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CoachController implements Initializable {

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

    // FXML üzerindeki nesneler çağrılıyor
    @FXML
    TextField nameField, ageField, tcField, surnameField;

    @FXML
    ListView coachList, studentList;

    @FXML
    Pane groupPane;

    // 2 listenin seçilen indexlerini tutacak değerler
    int idxCoach = -1, idxStudent = -1;

    // 2 listedeki verilerin id değerlerini tutacak listeler
    ArrayList<Integer> dataID, studentID;

    // Mevcut salon bilgisi
    String avaibleGym;

    // Pencere ilk açıldığında çalışacak fonksiyon
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Önceki sayfadan seçilen salon adının dosyadan okunması
        try (BufferedReader oku = new BufferedReader(new FileReader("selected_gym.txt"));){
            avaibleGym = oku.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }

        fillCoach(); // Antranör listesinin doldurulması

        // Antranör listesine tıklama işlemi
        coachList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onCoachSelected(mouseEvent);
            }
        });

        // Öğrenci listesine tıklama işlemi
        studentList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onStudentSelected(mouseEvent);
            }
        });

        // Detayların bulunduğu alan gizleniyor.
        groupPane.setVisible(false);

        // Tc alanı değiştirilemez yapılıyor.
        tcField.setEditable(false);
    }

    // Antranör seçildiğinde yapılan işlemler
    public void onCoachSelected(MouseEvent event){

        // Seçilen değerin indexi alınıyor
        idxCoach = coachList.getSelectionModel().getSelectedIndex();

        if (idxCoach != -1) { // Eğer bir seçim yapılırsa
            groupPane.setVisible(true); // Detaylar bölümü görünür yapılıyor

            // seçilen değerin veritabanından çekilmesi için sql sorgusu
            String sql = "select * from coach where id="+dataID.get(idxCoach)+"";
            try (Connection baglanti = this.connect();
                 Statement statement = baglanti.createStatement();
                 ResultSet read = statement.executeQuery(sql);) {

                // Veriler okunarak detaylar bölümüne yazılıyor
                while (read.next()){
                    nameField.setText(read.getString("name"));
                    surnameField.setText(read.getString("surname"));
                    tcField.setText(read.getString("tc"));
                    ageField.setText(read.getString("age"));
                }

                // Öğrenci listesi dolduruluyor
                fillStudent();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Öğrenci seçildiğinde index değeri alınıyor.
    public void onStudentSelected(MouseEvent mouseEvent){
        idxStudent = studentList.getSelectionModel().getSelectedIndex();
    }

    // Güncelleme işlemi
    @FXML
    public void onUpdate(ActionEvent actionEvent){

        // Güncelleme için sql sorgusu
        String sql = "update coach set name = ?, surname = ?, age = ? where id = ?";
        try (Connection baglanti = this.connect();
             PreparedStatement preparedStatement = baglanti.prepareStatement(sql)){

            // Güncelleme için veriler alınıyor
            idxCoach = coachList.getSelectionModel().getSelectedIndex();
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, surnameField.getText());
            preparedStatement.setInt(3, Integer.parseInt(ageField.getText()));
            preparedStatement.setInt(4, dataID.get(idxCoach));

            preparedStatement.executeUpdate(); // Değişiklik işleniyor

        }catch (Exception e){
            e.printStackTrace();
        }

        fillCoach(); // Güncelleme sonrası veriler yeniden listeleniyor
    }

    // Antranör silme işlemi
    @FXML
    public void onDeleteCoach(ActionEvent actionEvent){

        // Antranör üzerindeki öğrencileri silme işlemi için sql sorgusu
        String sqlStudent = "update member set coach =?, time= ? where coach = ?";
        try (Connection baglanti = this.connect();
             PreparedStatement preparedStatement = baglanti.prepareStatement(sqlStudent)){

            // Girilen antrenör adına ait değerler güncelleniyor
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, coachList.getSelectionModel().getSelectedItem().toString());

            preparedStatement.executeUpdate(); // Değişiklik işleniyor
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Antrenör silme için sql sorgusu
        String sql = "delete from coach where id = ?";
        try (Connection baglanti = this.connect();
             PreparedStatement preparedStatement = baglanti.prepareStatement(sql)){

            // Girilen id'e ait değer siliniyor.
            preparedStatement.setInt(1, dataID.get(idxCoach));

            preparedStatement.executeUpdate(); // Değişiklik işleniyor
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Silme sonrası seçilen index -1 yapılıyor, antranör ve öğrenci tekrar listeleniyor.
        idxCoach = -1;

        fillCoach();
        fillStudent();
    }

    // Öğrenciyi kaldırma işlemi
    @FXML
    public void onDeleteStudent(ActionEvent actionEvent){
        if (idxStudent != -1){

            // Eğer bir öğrenci seçildiyse
            // Seçilen öğrencinin coach ve time değerleri null yapılıyor
            String sql = "update member set coach = ?, time = ? where id = ?";
            try (Connection baglanti = this.connect();
                 PreparedStatement preparedStatement = baglanti.prepareStatement(sql)){

                // null değerleri ve id değeri giriliyor
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setInt(3, studentID.get(idxStudent));

                preparedStatement.executeUpdate(); // değişiklik uygulanıyor

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        idxStudent = -1; // seçilen öğrenci indexi -1 yapılıyor
        fillStudent(); // Öğrenciler tekrar listeleniyor
    }

    // Antranörlerin listelenmesi
    public void fillCoach(){
        coachList.getItems().clear(); // liste temizleniyor

        // Mevcut salonun verilerini çekmek için sql sorgusu
        String sqlSelect = "select * from coach where gym='"+avaibleGym+"'";

        // Eklenecek değerlerin id lerini tutacak liste
        dataID = new ArrayList<>();
        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet rs = statement.executeQuery(sqlSelect)){

            // veriler okunuyor ve ad soyad değeri coachList'e id değeri dataID listesine ekleniyor
            while (rs.next()){
                dataID.add(rs.getInt("id"));
                coachList.getItems().add(rs.getString("name") + " " + rs.getString("surname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Güncelleme sonrası aynı verinin tekrar seçilmesi için kullanıldı
        coachList.getSelectionModel().select(idxCoach);
    }

    // Öğrencilerin listelenmesi
    public void fillStudent(){
        studentList.getItems().clear(); // Listenin temizlenmesi

        if (idxCoach != -1){
            // seçilen antranörü seçmiş öğrencileri getirmek için sql sorgusu
            String studentSql = "select * from member where coach = '" + coachList.getItems().get(idxCoach) + "'";
            try (Connection baglanti = this.connect();
                 Statement statement = baglanti.createStatement();
                 ResultSet readStudent = statement.executeQuery(studentSql)) {

                // Öğrencilerin id değerlerini tutacak liste
                studentID = new ArrayList<>();

                // Veriler listeye yazılıyor ve id değerleri alınıyor.
                while (readStudent.next()){
                    studentID.add(readStudent.getInt("id"));
                    studentList.getItems().add(readStudent.getString("name") + " " +
                            readStudent.getString("surname") + " -> " + readStudent.getString("time"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
