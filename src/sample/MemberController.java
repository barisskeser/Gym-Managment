package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MemberController implements Initializable {

    // FXML de bulunan nesneler tanımlanıyor.

    @FXML
    TextField nameField, surnameField, tcField, ageField;

    @FXML
    Label averageMember;

    @FXML
    ComboBox coachBox, gymBox, branchBox, timeBox;

    @FXML
    Pane groupPane;

    @FXML
    ListView memberList;

    String avaibleGym, selected_coach, selected_gym;

    // Listelenen verilerin id değerlerini tutmaktadır.
    private ArrayList<Integer> dataID;


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

    int idx = -1; // Listeden seçilen değerin index değeri


    // Veri güncelleme işlemi
    @FXML
    public void onUpdate(ActionEvent actionEvent) {
        // Güncelleme için sql sorgusu
        String sql = "update member set name=?, surname=?, tc=?, age=?, coach=?, branch=?, gym=?, time = ? where id = ?";


        try (Connection baglanti = this.connect();
             PreparedStatement pstmt = baglanti.prepareStatement(sql)) {

            // ComboBoxlarda bulunan verilerin seçili olup olmadığı kontrolü
            int idxCoach = coachBox.getSelectionModel().getSelectedIndex();
            int idxBranch = branchBox.getSelectionModel().getSelectedIndex();
            int idxGym = gymBox.getSelectionModel().getSelectedIndex();
            int idxTime = timeBox.getSelectionModel().getSelectedIndex();
            if (idxCoach != -1 && idxBranch != -1 && idxGym != -1 && idxTime != -1) {

                // Girilen bilgileri alarak bilginin güncellenmesi işlemi.
                selected_coach = coachBox.getSelectionModel().getSelectedItem().toString();
                String selected_branch = branchBox.getSelectionModel().getSelectedItem().toString();
                selected_gym = gymBox.getSelectionModel().getSelectedItem().toString();
                String selected_time = timeBox.getSelectionModel().getSelectedItem().toString();
                idx = memberList.getSelectionModel().getSelectedIndex();
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, surnameField.getText());
                pstmt.setString(3, tcField.getText());
                pstmt.setInt(4, Integer.parseInt(ageField.getText()));
                pstmt.setString(5, selected_coach);
                pstmt.setString(6, selected_branch);
                pstmt.setString(7, selected_gym);
                pstmt.setString(8, selected_time);
                pstmt.setInt(9, dataID.get(idx));

                pstmt.executeUpdate(); // Değişikliğin veritabanına uygulanması.
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        fillListView(); // Değişiklik sonrası verilerin tekrar listelenmesi
    }


    // Veri silme işlemi
    @FXML
    public void onDelete(ActionEvent actionEvent) {
        // Veri silme için sql sorgusu
        String sql = "delete from member where id = ?";
        try (Connection baglanti = this.connect();
             PreparedStatement pstmt = baglanti.prepareStatement(sql)) {

            // Listeden seçilen değerin index bilgisi alınıyor
            idx = memberList.getSelectionModel().getSelectedIndex();

            // Seçilen indexe göre verinin id değeri alınıyor ve siliniyor.
            pstmt.setInt(1, dataID.get(idx));

            pstmt.executeUpdate(); // Değişiklik veritabanına uygulanıyor.
            idx = -1; // Seçim olmadığından index -1 yapılıyor.
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        fillListView(); // Değişiklik sonrası verilerin tekrar listelenmesi

        groupPane.setVisible(false); // Veri görüntüleme alanı görünmez yapılıyor.
    }

    // Ekran ilk geldiğinde yapılacak işlemler
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Önceki sayfada seçilen spor salonu bilgisi dosyadan okunuyor.
        try (BufferedReader oku = new BufferedReader(new FileReader("selected_gym.txt"));) {
            avaibleGym = oku.readLine();
            selected_gym = avaibleGym;
        } catch (Exception e) {
            e.printStackTrace();
        }

        groupPane.setVisible(false); // Veri giriş alanı görünmez yapılıyor.
        tcField.setEditable(false); // Tc alını değiştirilimez yapılıyor.

        fillListView(); // Spor salonundaki üyeler listView e getiriliyor.

        // İlgili alanlara veri ekleme işlemi
        gymBox.getItems().add("Marmara Gym");
        gymBox.getItems().add("Teknoloji Gym");
        branchBox.getItems().add("Fitness");

        // Listeden veri seçilmesi durumu
        memberList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onItemSelected(mouseEvent);
            }
        });

        // Antranör değişikliği yapıldığında seansların bulunduğu timeBox güncelleniyor.
        coachBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (coachBox.getSelectionModel().getSelectedIndex() != -1) {
                    selected_coach = coachBox.getSelectionModel().getSelectedItem().toString();
                    timeBox.getItems().clear();
                    fillTimeBox();
                }
            }
        });

        // Salon değişikliği yapıldığında antranörlerin bulunduğu coachBox güncelleniyor.
        gymBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (gymBox.getSelectionModel().getSelectedIndex() != -1) {
                    selected_gym = gymBox.getSelectionModel().getSelectedItem().toString();
                    timeBox.getItems().clear();
                    fillCoachBox();
                }

            }
        });
    }

    int averageAge; // Üyelerin yaş ortalaması
    int lenMember; // Üye sayısı

    // Listeyi doldurma fonksiyonu
    public void fillListView() {
        averageAge = 0;
        lenMember = 0;
        memberList.getItems().clear(); // Liste temizleniyor.
        dataID = new ArrayList<>();

        // Verileri seçilen salona göre çekmek için sql sorgusu
        String selectSQL = "select * from member where gym = '" + avaibleGym + "'";

        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet rs = statement.executeQuery(selectSQL);) {

            // Veri okuma işlemi
            while (rs.next()) {
                averageAge += rs.getInt("age");
                lenMember++;

                // Verilerin listView a eklenmesi ve id değerlerinin listeye eklenmesi.
                dataID.add(rs.getInt("id"));
                memberList.getItems().add(rs.getString("name") + " " + rs.getString("surname"));
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Üyelerin yaş ortalamasının görünmesi.
        if (lenMember != 0){
            averageMember.setText("Üyelerin yaş ortalaması : " + String.valueOf(averageAge / lenMember));
        }

        // Güncelleme işlemi sonrası seçilen verinin korunması
        memberList.getSelectionModel().select(idx);
    }

    // Kayıtlı antranörlere göre coachBox ı doldurma fonksiyonu
    public void fillCoachBox() {

        // coach tablosundan seçilen salona göre veri çekmek için sql sorgusu
        String sql = "select * from coach where gym='" + selected_gym + "'";
        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            coachBox.getItems().clear(); // coachBox temizlenmesi

            // Okunan verilerin combo boxa eklenmesi.
            while (rs.next()) {
                coachBox.getItems().add(rs.getString("name") + " " + rs.getString("surname"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> times; // Boş seansları tutmak için liste

    // Seçilen antranöre göre seansların getirilme fonksiyonu
    public void fillTimeBox() {

        // Seçilen antranöre göre member tablosundan okuma için sql sorgusu
        String sql = "select * from member where coach='" + selected_coach + "'";

        // Öncelikle tüm seanslar ekleniyor.
        times = new ArrayList<>();
        times.add("10:00 - 12:00");
        times.add("12:00 - 14:00");
        times.add("14:00 - 16:00");
        times.add("16:00 - 18:00");
        times.add("18:00 - 20:00");
        try (Connection baglanti = this.connect();
             Statement statement = baglanti.createStatement();
             ResultSet read = statement.executeQuery(sql)) {

            // Üyelerin aldığı seanslar listeden kaldırılıyor. Böylelikle seçilmemiş olanlar listeleniyor.
            while (read.next()) {
                int idxTime = times.indexOf(read.getString("time"));
                times.remove(idxTime);
            }
            if (coachBox.getSelectionModel().getSelectedIndex() != -1){
                // timeBox dolduruluyor.
                for (String time : times) {
                    timeBox.getItems().add(time);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Listeden nesne seçilme işlemi
    @FXML
    public void onItemSelected(MouseEvent event) {

        // Seçilen nesnenin index değeri
        idx = memberList.getSelectionModel().getSelectedIndex();

        if (idx != -1) { // Eğer seçim yapıldıysa
            groupPane.setVisible(true); // Veri görüntüleme alanı görünür yapılıyor.

            // Seçilen değerin bilgilerini id ye göre getirmek için sql sorgusu
            String sql = "select * from member where id=" + dataID.get(idx) + "";
            try (Connection baglanti = this.connect();
                 Statement statement = baglanti.createStatement();
                 ResultSet read = statement.executeQuery(sql);) {

                // verilerin okunarak ilgili alana konulması işlemi
                while (read.next()) {
                    nameField.setText(read.getString("name"));
                    surnameField.setText(read.getString("surname"));
                    tcField.setText(read.getString("tc"));
                    ageField.setText(read.getString("age"));
                    coachBox.setValue(read.getString("coach"));
                    branchBox.setValue(read.getString("branch"));
                    gymBox.setValue(read.getString("gym"));

                    selected_coach = read.getString("coach");
                    timeBox.getItems().clear();
                    if (selected_coach != null) {
                        timeBox.getItems().add(read.getString("time"));
                    }
                    timeBox.setValue(read.getString("time"));
                    fillTimeBox(); // timeBox dolduruluyor.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
