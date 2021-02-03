package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class NewMember extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // FXML üzerinden pencere oluşturuluyor.
            Parent root = FXMLLoader.load(getClass().getResource("newMember.fxml"));
            Scene scene = new Scene(root, 350, 430);

            // look up özelliği kullanılarak fxml de bulunan nesne id değerine göre alınıyor.
            ComboBox gym = (ComboBox) scene.lookup("#gymBox");
            ComboBox branch = (ComboBox) scene.lookup("#branchBox");
            Button onSave = (Button) scene.lookup("#onSave");

            // Kaydetme işleminden sonra üye sayfasına dönüş
            onSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    MemberPage memberPage = new MemberPage();
                    try {
                        memberPage.start(new Stage());
                        stage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Salon ve branş değerleri combo box a ekleniyor.
            gym.getItems().add("Marmara Gym");
            gym.getItems().add("Teknoloji Gym");
            branch.getItems().add("Fitness");

            stage.setScene(scene);
            stage.setTitle("Yeni Üye");
            stage.show();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
