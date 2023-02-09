/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//Auteur : Mr.Beuvron

public class Utils {

    public static Stage dansStage(Parent compo) {
        Scene sc = new Scene(compo);
        Stage fenetre = new Stage();
        fenetre.setScene(sc);
        fenetre.show();
        return fenetre;
    }

    public static void showErrorInAlert(String titre, String message, String detail) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.setContentText(detail);
        alert.showAndWait();

    }
     public static void addSimpleBorder(Region c) {
        addSimpleBorder(c, Color.BLACK, BorderWidths.DEFAULT.getTop());
    }
    
    public static void addSimpleBorder(Region c,Color couleur,double epaisseur) {
        c.setBorder(new Border(new BorderStroke(couleur,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(epaisseur))));
    }


}

    

