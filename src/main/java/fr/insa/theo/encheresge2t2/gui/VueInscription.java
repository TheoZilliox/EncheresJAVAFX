package fr.insa.theo.encheresge2t2.gui;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class VueInscription extends GridPane {

    private Connection con;
    private Label lNom;
    private TextField tfNom;
    private Label lPrenom;
    private TextField tfPrenom;
    private Label lEmail;
    private TextField tfEmail;
    private Label lCodepostal;
    private TextField tfCodepostal;
    private Label lMotDePasse;
    private PasswordField tfMotDePasse;
    private Label lMotDePasseC;
    private PasswordField tfMotDePasseC;
    private Label lMessage;
    private Button Bouton;

    public VueInscription() {
        Utils.addSimpleBorder(this);
        //this.setGridLinesVisible(true);
        this.setAlignment(Pos.TOP_CENTER);
        try {
            this.con = GestionBdD.defautConnect();
        } catch (Exception ex) {
            this.add(new Label("PAS DE BDD"), 0, 0);
        }

        this.lNom = new Label("Nom : ");
        this.tfNom = new TextField();
        this.add(this.lNom, 0, 0);
        this.add(this.tfNom, 1, 0);

        this.lPrenom = new Label("Prénom : ");
        this.tfPrenom = new TextField();
        this.add(this.lPrenom, 0, 1);
        this.add(this.tfPrenom, 1, 1);

        this.lEmail = new Label("Email : ");
        this.tfEmail = new TextField();
        this.add(this.lEmail, 0, 2);
        this.add(this.tfEmail, 1, 2);

        this.lCodepostal = new Label("Code Postal : ");
        this.tfCodepostal = new TextField();
        this.add(this.lCodepostal, 0, 3);
        this.add(this.tfCodepostal, 1, 3);

        this.lMotDePasse = new Label("Mot de passe : ");
        this.tfMotDePasse = new PasswordField();
        this.add(this.lMotDePasse, 0, 4);
        this.add(this.tfMotDePasse, 1, 4);

        this.lMotDePasseC = new Label("Confirmer le mot de passe : ");
        this.tfMotDePasseC = new PasswordField();
        this.add(this.lMotDePasseC, 0, 5);
        this.add(this.tfMotDePasseC, 1, 5);

        this.setStyle("-fx-color:#50e65f;-fx-font-size:20px");

        this.Bouton = new Button("JE M'INSCRIS");
        this.add(this.Bouton, 0, 6, 2, 3);
        this.Bouton.setOnAction((t) -> {
            //LoginForm lf = new LoginForm(this);
            String MonNom = this.tfNom.getText();
            String MonPrenom = this.tfPrenom.getText();
            String MonCodePostal = this.tfCodepostal.getText();
            String MonEmail = this.tfEmail.getText();
            String MonMotDePasse = this.tfMotDePasse.getText();
            String MonMotDePasseC = this.tfMotDePasseC.getText();

            if (MonMotDePasse.equals(MonMotDePasseC)) {

                try {

                    GestionBdD.creeUtilisateur(this.getCon(), MonNom, MonPrenom, MonEmail, MonMotDePasse, MonCodePostal);
                    this.lMessage = new Label(" Compte crée ");
                    this.add(this.lMessage, 7, 5);
                } catch (SQLException ex) {
                    Utils.showErrorInAlert("Probleme BdD", "creation impossible", ex.getLocalizedMessage());
                }

            } else {
                this.lMessage = new Label(" Mot de passe incorrect ");
                this.add(this.lMessage, 7, 5);

            }

        });

    }

    public Connection getCon() {
        return con;
    }

}
