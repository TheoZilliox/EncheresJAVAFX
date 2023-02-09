package fr.insa.theo.encheresge2t2.gui;

import java.sql.Connection;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VueConnexion extends GridPane {

    private Connection con;
    private Label lEmail;
    private TextField tfEmail;
    private Label lMotDePasse;
    private PasswordField tfMotDePasse;
    private Button Bouton;
    private VuePrincipale main;

    public VueConnexion() {
        //this.main = main;
        Utils.addSimpleBorder(this);
        this.setAlignment(Pos.TOP_CENTER);
        try {
            this.con = GestionBdD.defautConnect();
        } catch (ClassNotFoundException | SQLException ex) {
            this.add(new Label("PAS DE BDD"), 0, 0);
        }

        this.lEmail = new Label("Email : ");
        this.tfEmail = new TextField();
        this.add(this.lEmail, 0, 0);
        this.add(this.tfEmail, 1, 0);

        this.lMotDePasse = new Label("Mot de passe : ");
        this.tfMotDePasse = new PasswordField();
        this.add(this.lMotDePasse, 0, 1);
        this.add(this.tfMotDePasse, 1, 1);

        this.setStyle("-fx-color:#5df0dc;-fx-font-size:20px");

        this.Bouton = new Button("JE ME CONNECTE");
        this.add(this.Bouton, 0, 6, 2, 3);
        this.Bouton.setOnAction((t) -> {
            try {
                doLogin();
            } catch (SQLException ex) {
                Logger.getLogger(VueConnexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public Connection getCon() {
        return con;
    }

    public void doLogin() throws SQLException { //la connexion n'a malheuresement pas pu être établi
        //this.main = main;
        String email = this.tfEmail.getText();
        String pass = this.tfMotDePasse.getText();

        //VuePrincipale main = new VuePrincipale();
        Optional<Utilisateur> user = GestionBdD.login(con, email, pass);
        this.main.getSessionInfo().setCurUser(user);
        System.out.println("ejdfet");

    }

}
