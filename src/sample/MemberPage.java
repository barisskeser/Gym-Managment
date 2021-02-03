package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class MemberPage extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        // FXML üzerinden pencere oluşturuluyor.
        Parent root = FXMLLoader.load(getClass().getResource("Member.fxml"));
        Scene scene = new Scene(root, 630, 450);

        // look up özelliği kullanılarak fxml de bulunan nesne id değerine göre alınıyor.
        Button toNewMember = (Button) scene.lookup("#toNewMember");
        Button onBack = (Button) scene.lookup("#onBack");

        // Geri dönme işlemi
        onBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                // Main ekranına geçiş işlemi

                Main main = new Main();
                try {
                    main.start(new Stage());
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Yeni üye oluşturma ekranına geçiş
        toNewMember.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                NewMember newMember = new NewMember();
                try {
                    newMember.start(new Stage());
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.setScene(scene);
        stage.show();
    }

}
