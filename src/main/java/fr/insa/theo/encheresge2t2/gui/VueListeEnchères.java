package fr.insa.theo.encheresge2t2.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class VueListeEnchères extends GridPane {

    private Connection con;
    private final Label lObjet;
    private final Label lUtilisateur;
    private final Label lDate;
    private final Label lPrix;
    private Label lTitre1;
    private Label lUtilisateur1;
    private Label lPrix1;
    private Label lDate1;

    public VueListeEnchères() throws SQLException {

        Utils.addSimpleBorder(this);
        this.setAlignment(Pos.TOP_CENTER);
        this.setGridLinesVisible(true);

        try {
            this.con = GestionBdD.defautConnect();
        } catch (ClassNotFoundException | SQLException ex) {
            this.add(new Label("PAS DE BDD"), 0, 0);
        }

        this.setStyle("-fx-color:#c91833;-fx-font-size:20px");
        this.lObjet = new Label(" Objet");
        this.lUtilisateur = new Label(" Utilisateur");
        this.lDate = new Label(" Date");
        this.lPrix = new Label(" Prix");

        this.add(this.lObjet, 0, 0);
        this.add(this.lPrix, 2, 0);
        this.add(this.lUtilisateur, 4, 0);
        this.add(this.lDate, 6, 0);

        try ( Statement st = con.createStatement()) {
            try ( ResultSet C = st.executeQuery("""
                Select count(*) as NmbrEncheres from encheres2
                             """)) {
                while (C.next()) {
                    int NOMBREENCH = C.getInt("NmbrEncheres");

                    for (int i = 1; i <= NOMBREENCH; i++) {
                        try ( PreparedStatement A = con.prepareStatement("select prenom, titre, quand, nom, montant from objet2, encheres2, "
                                + "  utilisateur2  where utilisateur2.id = encheres2.de and objet2.id  = encheres2.sur and encheres2.id = ?")) {
                            A.setInt(1, i);

                            ResultSet res = A.executeQuery();

                            if (res.next()) {

                                String TITRE = res.getString("titre");
                                String ChaineTitre =  " " + TITRE +" ";

                                Timestamp QUAND = res.getTimestamp("quand");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String date = dateFormat.format(QUAND);
                                String ChaineTemps = " " + date + " ";
                                
                                String NOM = res.getString("nom");
                                String PRENOM = res.getString("prenom");
                                String ChaineNomComplet = " " + PRENOM + " " + NOM + " ";

                                int MONTANT = res.getInt("montant");
                                String ChainePrix = " " + MONTANT + "€ ";

                                this.lTitre1 = new Label(ChaineTitre);
                                this.lUtilisateur1 = new Label(ChaineNomComplet);
                                this.lPrix1 = new Label(ChainePrix);
                                this.lDate1 = new Label(ChaineTemps);

                                this.add(this.lTitre1, 0, i);
                                this.add(this.lPrix1, 2, i);
                                this.add(this.lUtilisateur1, 4, i);
                                this.add(this.lDate1, 6, i);

                            }

                        }
                    }

                }

            }
        }
    }

    public Connection getCon() {
        return con;
    }

}
