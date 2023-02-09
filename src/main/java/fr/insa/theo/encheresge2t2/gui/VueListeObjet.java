package fr.insa.theo.encheresge2t2.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class VueListeObjet extends GridPane {

    private Connection con;
    private Label lTitre;
    private Label lDescription;
    private Label lUtilisateur;
    private Label lPrix;
    private Button Bouton;
    private Label lFin;
    private Label lTitre1;
    private Label lDescription1;
    private Label lUtilisateur1;
    private Label lPrix1;
    private Label lFin1;
    private Button Bouton2;
    private Label lCat;
    private Label lCat1;
    private Label lObjet;
    private TextField tfObjet;
    private Label lEmail;
    private TextField tfEmail;
    private Label lMotDePasse;
    private PasswordField tfMotDePasse;
    private Label lNouveauPrix;
    private TextField tfNouveauPrix;
    private Label lMessage;

    public VueListeObjet() throws SQLException {

        Utils.addSimpleBorder(this);
        //this.setGridLinesVisible(true);
        this.setAlignment(Pos.TOP_CENTER);

        try {
            this.con = GestionBdD.defautConnect();
        } catch (ClassNotFoundException | SQLException ex) {
            this.add(new Label("PAS DE BDD"), 0, 0);
        }

        this.setStyle("-fx-color:#c91833;-fx-font-size:20px");
        this.lTitre = new Label(" Titre");
        this.lDescription = new Label(" Description");
        this.lUtilisateur = new Label(" Utilisateur");
        this.lPrix = new Label(" Prix");
        this.lFin = new Label(" Fin de la vente");
        this.lCat = new Label(" Catégorie");

        this.add(this.lTitre, 0, 0);
        this.add(this.lDescription, 2, 0);
        this.add(this.lUtilisateur, 4, 0);
        this.add(this.lPrix, 6, 0);
        this.add(this.lFin, 8, 0);
        this.add(this.lCat, 10, 0);

        this.Bouton = new Button("PLACER UNE ENCHERE");
        this.Bouton2 = new Button("VALIDER");

        try ( Statement st = con.createStatement()) {
            try ( ResultSet C = st.executeQuery("""
                Select count(*) as NmbrObjet from objet2
                             """)) {
                while (C.next()) {
                    int NOMBREOBJ = C.getInt("nmbrObjet");

                    for (int i = 1; i <= NOMBREOBJ; i++) {
                        try ( PreparedStatement A = con.prepareStatement("select titre, "
                                + "description,objet2.cat,prixbase,fin,utilisateur2.nom,utilisateur2.prenom"
                                + " from objet2 join utilisateur2 on utilisateur2.id = objet2.proposepar where objet2.id = ?")) {
                            A.setInt(1, i);
                            ResultSet res = A.executeQuery();

                            if (res.next()) {

                                String NOM = res.getString("nom");
                                String PRENOM = res.getString("prenom");
                                String ChaineNomComplet = " " + PRENOM + " " + NOM + " ";

                                String TITRE = res.getString("titre");
                                String ChaineTitre = " " + TITRE + " ";

                                String DESCRIPTION = res.getString("description");
                                String ChaineDescription = " " + DESCRIPTION + " ";

                                int PRIXBASE = res.getInt("prixbase");
                                String ChainePrixBase = " " + PRIXBASE + "€ ";

                                Timestamp FIN = res.getTimestamp("fin");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String date = dateFormat.format(FIN);
                                String ChaineFin = "" + date;

                                int MaCat = res.getInt("cat");

                                String CATEGORIE = "Pas de catégorie";

                                if (MaCat == 1) {
                                    CATEGORIE = " Vetements ";
                                }
                                if (MaCat == 2) {
                                    CATEGORIE = " Bricolage ";
                                }
                                if (MaCat == 3) {
                                    CATEGORIE = " Sport ";
                                }
                                if (MaCat == 4) {
                                    CATEGORIE = " Cosmétique ";
                                }
                                if (MaCat == 5) {
                                    CATEGORIE = " Cutlure ";
                                }
                                if (MaCat == 6) {
                                    CATEGORIE = " Technologie ";
                                }
                                if (MaCat == 7) {
                                    CATEGORIE = " Jardin ";
                                }
                                if (MaCat == 8) {
                                    CATEGORIE = " Jouets ";
                                }
                                if (MaCat == 9) {
                                    CATEGORIE = " Ecole ";
                                }
                                if (MaCat == 10) {
                                    CATEGORIE = " Accessoires pour animaux ";
                                }

                                this.lTitre1 = new Label(ChaineTitre);
                                this.lDescription1 = new Label(ChaineDescription);
                                this.lUtilisateur1 = new Label(ChaineNomComplet);
                                this.lPrix1 = new Label(ChainePrixBase);
                                this.lFin1 = new Label(ChaineFin);
                                this.lCat1 = new Label(CATEGORIE);

                                this.add(this.lTitre1, 0, i);
                                this.add(this.lDescription1, 2, i);
                                this.add(this.lUtilisateur1, 4, i);
                                this.add(this.lPrix1, 6, i);
                                this.add(this.lFin1, 8, i);
                                this.add(this.lCat1, 10, i);

                            }

                        }
                    }
                    int RANG0 = NOMBREOBJ + 1;    //partie peu élégante nous le concevons.
                    int RANG1 = NOMBREOBJ + 5;
                    int RANG2 = NOMBREOBJ + 6;
                    int RANG3 = NOMBREOBJ + 7;
                    int RANG4 = NOMBREOBJ + 8;
                    int RANG5 = NOMBREOBJ + 9;
                    this.add(this.Bouton, 0, RANG0, 2, 3);

                    this.Bouton.setOnAction((t) -> {
                        this.lObjet = new Label("Sur quel objet enchérir ?");
                        this.lNouveauPrix = new Label("Quel est votre offre (€) ?");
                        this.lEmail = new Label("Adresse mail");
                        this.lMotDePasse = new Label("Mot de passe");
                        this.tfObjet = new TextField();
                        this.tfNouveauPrix = new TextField();
                        this.tfNouveauPrix.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                            if (newValue.matches("\\d*")) {
                                int value = Integer.parseInt(newValue);
                            } else {
                                tfNouveauPrix.setText(oldValue);
                            }
                        });
                        this.tfEmail = new TextField();
                        this.tfMotDePasse = new PasswordField();

                        this.add(this.lObjet, 0, RANG1);
                        this.add(this.lNouveauPrix, 0, RANG2);
                        this.add(this.lEmail, 0, RANG3);
                        this.add(this.lMotDePasse, 0, RANG4);
                        this.add(this.tfObjet, 1, RANG1);
                        this.add(this.tfNouveauPrix, 1, RANG2);
                        this.add(this.tfEmail, 1, RANG3);
                        this.add(this.tfMotDePasse, 1, RANG4);
                        this.add(this.Bouton2, 1, RANG5, 2, 3);

                    });

                    this.Bouton2.setOnAction((t) -> {

                        String NomObjet = this.tfObjet.getText();
                        String NouveauPrix = this.tfNouveauPrix.getText();
                        String MonEmail = this.tfEmail.getText();
                        String MonMotDePasse = this.tfMotDePasse.getText();

                        try ( PreparedStatement ST = con.prepareStatement("select prixbase,email,pass,utilisateur2.id as idu, objet2.id as ido from utilisateur2 "
                                + ",objet2 where   "
                                + " utilisateur2.email = ? and utilisateur2.pass = ? and objet2.titre = ?")) {
                            ST.setString(1, MonEmail);
                            ST.setString(2, MonMotDePasse);
                            ST.setString(3, NomObjet);
                            ResultSet res = ST.executeQuery();
                            Timestamp monQuand = new Timestamp(System.currentTimeMillis());
                            if (res.next()) {
                                int MonDe = res.getInt("idu");
                                int MonSur = res.getInt("ido");
                                int PRIXBASE = res.getInt("prixbase");
                                if (PRIXBASE < Integer.parseInt(NouveauPrix)) {
                                    GestionBdD.CreeEncheres(this.getCon(), monQuand,
                                            Integer.parseInt(NouveauPrix), MonSur, MonDe);
                                    this.lMessage = new Label(" Enchère déposé ! ");
                                    this.add(this.lMessage, 2, RANG5);
                                } else {
                                    this.lMessage = new Label("Prix insuffisant ! ");
                                    this.add(this.lMessage, 2, RANG5);
                                }

                            } else {
                                Utils.showErrorInAlert("Problème", "Votre enchère ne peut pas être prise en compte", "");
                            }

                        } catch (SQLException ex) {
                            System.out.println("pb : " + ex.getLocalizedMessage());
                            Utils.showErrorInAlert("PROBLEME", "sql", ex.getLocalizedMessage());
                        }

                    });
                }

            }
        }
    }

    public Connection getCon() {
        return con;
    }

}
