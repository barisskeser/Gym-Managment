package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NewCoach extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // FXML üzerinden pencere oluşturuluyor.
        Parent root = FXMLLoader.load(getClass().getResource("newCoach.fxml"));
        Scene scene = new Scene(root, 380, 330);

        // look up özelliği kullanılarak fxml de bulunan nesne id değerine göre alınıyor.
        Button onSave = (Button) scene.lookup("#onSave");

        // Kayıt işleminden sonra antranör sayfasına dönülüyor.
        onSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CoachPage coachPage = new CoachPage();
                try {
                    coachPage.start(new Stage());
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.setScene(scene);
        stage.setTitle("Yeni Antranör");
        stage.show();
    }
}
