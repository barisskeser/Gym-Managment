package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jdk.internal.icu.impl.StringPrepDataReader;

import java.io.*;
import java.sql.*;

public class Main extends Application {

    String selected_item = null; // Seçilen salon adını tutmaktadır.

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXML dosyasından pencere oluşturulmaktadır.
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 600, 400);

        // look up özelliği kullanılarak fxml de bulunan nesne id değerine göre alınıyor.
        ComboBox gymsBox = (ComboBox) scene.lookup("#gymsBox");
        Button toMember = (Button) scene.lookup("#toMember");
        Button toCoach = (Button) scene.lookup("#toCoach");
        Label label = (Label) scene.lookup("#gymName");

        // Salon adları eklenmektedir.
        gymsBox.getItems().add("Marmara Gym");
        gymsBox.getItems().add("Teknoloji Gym");
        gymsBox.getSelectionModel().select(0); // İlk değer seçili olarak getirilmekte.

        // Yeni salon seçildiğinde adının labelda görünmesi sağlanıyor.
        gymsBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                // gymBox nesnesinde bir değişiklik olduğunda çalışmaktadır.

                // Label değerini seçilen değere göre yazmaktadır.
                label.setText(gymsBox.getSelectionModel().getSelectedItem().toString());
            }
        });

        // Üye işlemleri ekranına geçiş
        toMember.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // toMember buttonuna tıklandığında çalışmaktadır.


                try {
                    // Hedef sayfa için bir nesne oluşturuluyor.
                    MemberPage memberPage = new MemberPage();

                    // Seçilen değer selected_gym.txt adlı dosyaya yazılıyor.
                    try {
                        selected_item = gymsBox.getSelectionModel().getSelectedItem().toString(); // Seçilen değer alınıyor.
                        try (BufferedWriter yaz = new BufferedWriter(new FileWriter("selected_gym.txt"));){
                            yaz.write(selected_item); // Yazma işlemi
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        memberPage.start(new Stage()); // Hedef pencerenin start methodu çalıştırılarak ekrana görünümü getiriliyor.
                        primaryStage.close(); // Mevcut pencere kapatılıyor.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){

                    // Hata olduğunda uyarı mesajı

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Uyarı");
                    alert.setContentText("Salon seçilmedi!");
                    alert.showAndWait();
                }
            }
        });

        // Antranör işlemleri ekranına geçiş
        toCoach.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // toCoach buttonuna tıklandığında çalışmaktadır.

                // Hedef sayfa için nesne oluşturuluyor.
                CoachPage coachPage = new CoachPage();

                // Seçilen değer selected_gym.txt adlı dosyaya yazılıyor.
                try {
                    selected_item = gymsBox.getSelectionModel().getSelectedItem().toString(); // Seçilen değer alınıyor.
                    try (BufferedWriter yaz = new BufferedWriter(new FileWriter("selected_gym.txt"));){
                        yaz.write(selected_item); // Yazma işlemi
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    coachPage.start(new Stage()); // Yeni pencere ekrana getiriliyor.
                    primaryStage.close(); // Mevcut pencere kapatılıyor.
                } catch (Exception e) {

                    // Hata olduğunda uyarı mesajı.

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Uyarı");
                    alert.setContentText("Salon seçilmedi!");
                    alert.showAndWait();
                }
            }
        });


        primaryStage.setTitle("Spor Salonları");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
