/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class VuePlacerObjet extends GridPane {

    private Connection con;
    private Label lTitre;
    private TextField tfTitre;
    private Label lMotDePasse;
    private PasswordField tfMotDePasse;
    private Label lDescription;
    private TextArea tfDescription;
    private Label lPrixBase;
    private TextField tfPrixBase;
    private Label lEmail;
    private TextField tfEmail;
    private ComboBox ChoixCat;
    private Button Bouton;
    private Label lMessageErreur;
    private Label lConfirmé;
    private Label lCat;

    public VuePlacerObjet() throws SQLException {

        Utils.addSimpleBorder(this);
        //this.setGridLinesVisible(true);
        this.setAlignment(Pos.TOP_CENTER);

        try {
            this.con = GestionBdD.defautConnect();
        } catch (Exception ex) {
            this.add(new Label("PAS DE BDD"), 0, 0);
        }

        this.ChoixCat = new ComboBox();

        ChoixCat.getItems().add("Vetements");
        ChoixCat.getItems().add("Bricolage");
        ChoixCat.getItems().add("Sport");
        ChoixCat.getItems().add("Cosmétique");
        ChoixCat.getItems().add("Culture");
        ChoixCat.getItems().add("Technologie");
        ChoixCat.getItems().add("Jardin");
        ChoixCat.getItems().add("Jouets");
        ChoixCat.getItems().add("Ecole");
        ChoixCat.getItems().add("Accessoires pour animaux");

        this.lEmail = new Label("Email : ");
        this.tfEmail = new TextField();
        this.add(this.lEmail, 0, 0);
        this.add(this.tfEmail, 1, 0);

        this.lMotDePasse = new Label("Mot de passe ");
        this.tfMotDePasse = new PasswordField();
        this.add(this.lMotDePasse, 0, 1);
        this.add(this.tfMotDePasse, 1, 1);

        this.lTitre = new Label("Titre : ");
        this.tfTitre = new TextField();
        this.add(this.lTitre, 0, 2);
        this.add(this.tfTitre, 1, 2);

        this.lDescription = new Label("Description de l'objet : ");
        this.tfDescription = new TextArea();
        this.add(this.lDescription, 0, 3);
        this.add(this.tfDescription, 1, 3);

        this.lPrixBase = new Label("Prix (€) : ");
        this.tfPrixBase = new TextField();
        this.tfPrixBase.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Integer.parseInt(newValue);
            } else {
                tfPrixBase.setText(oldValue);
            }
        });
        this.add(this.lPrixBase, 0, 4);
        this.add(this.tfPrixBase, 1, 4);

        this.lCat = new Label("Catégories :");
        this.add(this.lCat, 0, 5);
        HBox hbox = new HBox(ChoixCat);
        this.add(hbox, 1, 5);

        this.Bouton = new Button("DEPOSER VOTRE OBJET");
        this.add(this.Bouton, 0, 6, 2, 3);
        this.setStyle("-fx-color:#f2f233;-fx-font-size:20px");

        this.Bouton.setOnAction((t) -> {
            try {
                miseEnLigne();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VuePlacerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    public Connection getCon() {
        return con;
    }

    public int RecolterIdCategorie(String CategorieTextuelle) throws SQLException, ClassNotFoundException {
        this.con = GestionBdD.defautConnect();

        try ( PreparedStatement st = con.prepareCall("select id from categorie2 where nom = ?")) {
            st.setString(1, CategorieTextuelle);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return res.getInt("id");
            } else {
                Utils.showErrorInAlert("Erreur", "Il faut choisir une catégoire", "Vous devez en selectionner une !");
                return 0;
            }

        }

    }

    private void miseEnLigne() throws ClassNotFoundException {

        String MonTitre = this.tfTitre.getText();
        String MaDescription = this.tfDescription.getText();
        String MonEmail = this.tfEmail.getText();
        //int ID = this.main.getSessionInfo().getUserID();   //n'a pas pu être établi
        String MonMotDePasse = this.tfMotDePasse.getText();
        String MonPrixBase = this.tfPrixBase.getText();
        Timestamp t1 = new Timestamp(System.currentTimeMillis());
        LocalDateTime cur = LocalDateTime.now();
        LocalDateTime plusUnMois = cur.plusMonths(1);
        Timestamp t2 = Timestamp.valueOf(plusUnMois);

        try ( PreparedStatement st = con.prepareStatement("select id from utilisateur2 where utilisateur2.email = ? and utilisateur2.pass = ?")) {
            st.setString(1, MonEmail);
            st.setString(2, MonMotDePasse);

            ResultSet res = st.executeQuery();

            if (res.next()) {
                int MonProposePar = res.getInt("id");
                GestionBdD.creeObjet(this.getCon(), MonTitre, MaDescription,
                        t1, Integer.parseInt(MonPrixBase), MonProposePar,
                        RecolterIdCategorie(this.ChoixCat.getValue().toString()), t2);
                this.lConfirmé = new Label("  Objet déposé ! ");
                this.add(this.lConfirmé, 0, 9);

            } else {

                this.lMessageErreur = new Label("Email ou mot de passe incorrect ! ");
                this.add(this.lMessageErreur, 8, 0);

            }

        } catch (SQLException ex) {
            Utils.showErrorInAlert("Pb BDD", "PB", ex.getLocalizedMessage());
        }

    }

}
