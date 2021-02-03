package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CoachPage extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // FXML üzerinden pencere oluşturuluyor.
        Parent root = FXMLLoader.load(getClass().getResource("Coach.fxml"));
        Scene scene = new Scene(root, 516, 505);

        // look up özelliği kullanılarak fxml de bulunan nesne id değerine göre alınıyor.
        Button toNewCoach = (Button) scene.lookup("#toNewCoach");
        Button toBack = (Button) scene.lookup("#toBack");

        // Yeni antranör oluşturma ekranına geçiş
        toNewCoach.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                NewCoach newCoach = new NewCoach();
                try {
                    newCoach.start(new Stage());
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Geri dönme işlemi
        toBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main main = new Main();
                try {
                    main.start(new Stage());
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stop");
    }
}
