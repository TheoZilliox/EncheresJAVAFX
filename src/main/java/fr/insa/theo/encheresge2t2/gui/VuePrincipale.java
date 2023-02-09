package fr.insa.theo.encheresge2t2.gui;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VuePrincipale extends BorderPane {
    
    private final Button menu1;
    private final Button menu2;
    private final Button menu3;
    private final Button menu4;
    private final Button menu5;
    private final VBox VBox;
    private final HBox HBox;
    private SessionInfo SessionInfo;
    
 public VuePrincipale() {
     
        this.VBox = new VBox();
        VBox.setStyle("-fx-background-color:#FAB7B7;-fx-font-size:20px");
        this.HBox = new HBox();
                    
        this.menu1 = new Button("Se connecter");
        this.menu2 = new Button("Créer un compte");
        this.menu3 = new Button("Déposer un objet en vente");
        this.menu4 = new Button("Liste des objets");
        this.menu5 = new Button("Liste des enchères");

        
        menu1.setStyle("-fx-background-color:#5df0dc;-fx-font-size:20px");
        menu2.setStyle("-fx-background-color:#50e65f;-fx-font-size:20px");
        menu3.setStyle("-fx-background-color:#f2f233;-fx-font-size:20px");
        menu4.setStyle("-fx-background-color:#c91833;-fx-font-size:20px");
        menu5.setStyle("-fx-background-color:#a750e6;-fx-font-size:20px");


        HBox.getChildren().addAll(menu1, menu2, menu3, menu4, menu5);
        VBox.getChildren().add(HBox);
        VBox.setPadding(new Insets(10));
       
        this.setTop(VBox);
        
        menu1.setOnAction((actionEvent) -> {
            this.setCenter(new VueConnexion());
        } );
        
        menu2.setOnAction((actionEvent) -> {
            this.setCenter(new VueInscription());
        } );
        
        
       menu3.setOnAction((actionEvent) -> {
            try {
                this.setCenter(new VuePlacerObjet());
            } catch (SQLException ex) {
                //Logger.getLogger(VuePrincipale.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
       
       menu4.setOnAction((actionEvent) -> {
            try {
                this.setCenter(new VueListeObjet());
            } catch (SQLException ex) {
                //Logger.getLogger(VuePrincipale.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
        
       menu5.setOnAction((actionEvent) -> {
            try {
                this.setCenter(new VueListeEnchères());
            } catch (SQLException ex) {
               // Logger.getLogger(VuePrincipale.class.getName()).log(Level.SEVERE, null, ex);

            }
        } );
    
    }

    
    
    public SessionInfo getSessionInfo(){
        return SessionInfo;
    }
    
    

}
