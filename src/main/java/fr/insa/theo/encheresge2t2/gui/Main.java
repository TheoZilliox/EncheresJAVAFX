package fr.insa.theo.encheresge2t2.gui;

import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws SQLException {
        Scene sc = new Scene(new VuePrincipale());
        stage.setWidth(800);
        stage.setHeight(800);
        stage.setScene(sc);
        stage.setTitle("Encheres Massotte&Zilliox");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
   

}