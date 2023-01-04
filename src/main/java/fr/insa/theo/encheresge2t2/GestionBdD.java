/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;   //préciser qu'on a importé

// @author tzilliox01
public class GestionBdD {

    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String passe)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, passe);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "passe");
    }

    public static void main(String[] args) {
        try ( Connection con = defautConnect()) {
            deleteSchema(con);
            creeSchema(con);
            creeBDB(con);

            //demandeNouvelUtilisateur(con);
            //afficheUtilisateur(con);
            //demandeNouvelObjet(con);
            //AfficheObjet(con);
            //DemandeEncheres(con);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public static void creeSchema(Connection con)
            throws SQLException {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transactionSS
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation des tables

            // ResultSet resultSet = statement.executeQuery( sql: "create table objet2"); 
            //while (resultSet.next())
            st.executeUpdate(
                    """
                     
                            create table objet2 (
                                id integer not null primary key
                                generated always as identity,
                                titre varchar(200) not null,
                                description varchar(100) not null,
                                debut timestamp not null,
                                prixbase integer not null, 
                                proposepar integer not null,
                                cat integer not null,
                                fin timestamp
                                )
                                                                                                          
                              """);

            st.executeUpdate(
                    """
                            create table utilisateur2 (
                            id integer not null primary key
                            generated always as identity,
                            nom varchar(50) not null,
                            prenom varchar(30) not null,
                            email varchar(100) not null unique,
                            pass varchar(30) not null,
                            codepostal varchar(30) not null
                            )
                            
                        """);

            st.executeUpdate(
                    """
                            create table categorie2 (
                            id integer not null primary key
                            generated always as identity,
                            nom varchar(30) not null unique
                            )
                         
                         """);

            st.executeUpdate(
                    """
                            create table encheres2   (
                            id integer not null primary key
                            generated always as identity,
                            quand timestamp not null,
                            montant integer not null,
                            sur integer not null,
                            de integer not null
                            )
                         
                         """);
            st.executeUpdate(
                    """
                        alter table objet2
                            add constraint fk_objet2_proposepar 
                            foreign key (proposepar) references utilisateur2(id)
                            
                         """);

            st.executeUpdate(
                    """
                                  alter table objet2
                                  add constraint fk_objet2_cat
                                    foreign key (cat) references categorie2(id)
                                  """);

            st.executeUpdate(
                    """
                            alter table encheres2
                            add constraint fk_encheres2_sur 
                            foreign key (sur) references objet2(id)
                             """);

            st.executeUpdate(
                    """  
                            alter table encheres2
                            add constraint fk_encheres2_de
                            foreign key (de) references utilisateur2(id)
                             """);

//           
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;
        } finally {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
    }

    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        """
                alter table objet2
                drop constraint fk_objet2_proposepar 
                 """);
            } catch (SQLException ex) {
                System.out.println("pas de fk_objet2_proposepar :" + ex.getLocalizedMessage());
            }

            try {
                st.executeUpdate(
                        """
                alter table objet2
                drop constraint fk_objet2_cat
                """);
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate(
                        """
                alter table encheres2
                drop constraint fk_encheres2_sur 
                """);
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate(
                        """
                alter table encheres2
                drop constraint fk_encheres2_de
               """);
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate(
                        """
                    drop table utilisateur2
                    """);
            } catch (SQLException ex) {
                System.out.println("pas de drop table utilisateur2 : " + ex.getLocalizedMessage());
            }
            try {
                st.executeUpdate(
                        """
                    drop table encheres2
                    """);
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                    drop table objet2
                    """);
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                    drop table categorie2
                    """);
            } catch (SQLException ex) {
            }
        }
    }

    public static void creeUtilisateur(Connection con, String monNom, String monPrenom, String monEmail, String monMDP, String monCodePostal) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement("insert into utilisateur2 (nom, prenom, email, pass, codepostal) "
                + " values (?,?,?,?,?) ")) {
            pst.setString(1, monNom);
            pst.setString(2, monPrenom);
            pst.setString(3, monEmail);
            pst.setString(4, monMDP);
            pst.setString(5, monCodePostal);
            pst.executeUpdate();
        }

    }

    public static void demandeNouvelUtilisateur(Connection con) throws SQLException {

        //cette fonction récolte les variables nécéssaires à la création d'un nouvel utilisateur, et puis
        //les créer avec creeUtilisateur(), appelé dans le main
        Scanner console = new Scanner(System.in);
        System.out.println("------------------------");
        System.out.println("Création d'un utilisateur");
        System.out.println("Rentrez votre prénom : ");
        String monPrenom = Lire.S();
        System.out.println("Rentrez votre nom de famille : ");
        String monNom = console.nextLine();
        System.out.println("Rentrez votre code postal : ");
        String monCodePostal = console.nextLine();
        System.out.println("Rentrez votre email : ");
        String monEmail = console.nextLine();
        System.out.println("Rentrez votre mdp : ");
        String monMDP = console.nextLine();
        creeUtilisateur(con, monNom, monPrenom, monEmail, monMDP, monCodePostal);

    }

    public static void afficheUtilisateur(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour effectuer une recherche, il faut utiliser un "executeQuery"
            // un executeQuery retourne un ResultSet qui contient le résultat
            try ( ResultSet CHAINE = st.executeQuery(
                    """
                        select id,nom,prenom,email,pass,codepostal
                            from utilisateur2
                    """
            )) {
                // un ResultSet se manipule un peu comme un fichier :
                // - il faut le fermer quand on ne l'utilise plus
                //   d'où l'utilisation du try(...) ci-dessus
                System.out.println("Utilisateurs :");
                System.out.println("               ");
                // ici, on veut lister toutes les lignes, d'où le while
                while (CHAINE.next()) {
                    int id = CHAINE.getInt("id");
                    String nom = CHAINE.getString("nom");
                    String prenom = CHAINE.getString("prenom");
                    String email = CHAINE.getString("email");
                    String pass = CHAINE.getString("pass");
                    String codepostal = CHAINE.getString("codepostal");
                    String Ligne = id + " - " + nom + " - " + prenom + " - " + email + " - " + pass + " - " + codepostal;
                    System.out.println(Ligne);
                }
            }
        }
    }

    public static void creeObjet(Connection con, String monTitre, String maDescription, Timestamp monDebut,
            int monPrixbase, int monProposepar, int maCat, Timestamp maFin) throws SQLException {

        try ( PreparedStatement pst = con.prepareStatement("insert into objet2 (titre, description, debut, prixbase, proposepar, cat, fin)"
                + " values (?,?,?,?,?,?,?) ")) {
            pst.setString(1, monTitre);
            pst.setString(2, maDescription);
            pst.setTimestamp(3, monDebut);    //timestamp
            pst.setInt(4, monPrixbase);
            pst.setInt(5, monProposepar);
            pst.setInt(6, maCat);
            pst.setTimestamp(7, maFin);    //timestamp
            pst.executeUpdate();

            /*
                   // je peux alors récupérer les clés créées comme un result set :
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    // et comme ici je suis sur qu'il y a une et une seule clé, je
                    // fait un simple next 
                    rid.next();
                    // puis je récupère la valeur de la clé créé qui est dans la
                    // première colonne du ResultSet
                    int id = rid.getInt(1);
                    return id;
            
                }

             */
        }
    }

    public static void demandeNouvelObjet(Connection con) throws SQLException {

        //cette fonction récolte les variables nécéssaires à la création d'un nouvel objet dans la bdd, et puis
        //les créer avec creeObjet(), appelé dans le main
        Scanner console = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("Création d'un objet");
        System.out.println("Rentrez le titre : ");
        String monTitre = Lire.S();

        System.out.println("Rentrez la description : ");
        String maDescription = console.nextLine();

        Timestamp monDebut = new Timestamp(System.currentTimeMillis());
        LocalDateTime cur = LocalDateTime.now();
        LocalDateTime plusUnMois = cur.plusMonths(1);
        Timestamp maFin = Timestamp.valueOf(plusUnMois);

        System.out.println("Rentrez votre prix inital : ");
        int monPrixbase = Lire.i();

        System.out.println("Quel est votre id ?");

        int monProposepar = Lire.i();                     //récolter l'id utilisteur ? (recolter email/nom pour en chercher l'id)

        System.out.println("Choissisez la catégorie");       //créer une boucle pour forcer le choix d'une catégorie
        System.out.println("1) Vetements ");
        System.out.println("2) Bricolage ");
        System.out.println("3) Sport");
        System.out.println("4) Cosmétique");
        System.out.println("5) Culture ");
        System.out.println("6) Technologie");
        System.out.println("7) Jardin");
        System.out.println("8) Jouets");
        System.out.println("9) Ecole");
        System.out.println("10) Accesoires pour animaux");

        int maCat = ConsoleFdB.entreeEntier("Votre choix : ");
        creeObjet(con, monTitre, maDescription, monDebut, monPrixbase, monProposepar, maCat, maFin);
    }

    public static void AfficheObjet(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour effectuer une recherche, il faut utiliser un "executeQuery"
            // un executeQuery retourne un ResultSet qui contient le résultat
            try ( ResultSet CHAINE = st.executeQuery(
                    """
                        select id,titre,description,prixbase, fin
                            from objet2
                    """
            )) {

                System.out.println("Objets :");
                System.out.println("               ");
                while (CHAINE.next()) {
                    int ID = CHAINE.getInt("id");
                    String TITRE = CHAINE.getString("titre");
                    String DESCRIPTION = CHAINE.getString("description");
                    int PB = CHAINE.getInt("prixbase");
                    //Timestamp FIN = CHAINE.getTimestamp("fin");
                    String Ligne = ID + " - " + TITRE + " - " + DESCRIPTION + " - " + PB;
                    System.out.println(Ligne);
                }
            }
        }
    }

    public static void creeCategorie(Connection con, String monNomCat) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement("insert into categorie2 (nom)"
                + " values (?) ")) {
            pst.setString(1, monNomCat);
            pst.executeUpdate();

        }

    }

    public static void demanderCategorie(Connection con) throws SQLException {

        System.out.println("-------------------------");
        System.out.println("Création d'une catégorie");
        //Scanner console = new Scanner(System.in); 
        System.out.println("Rentrez le nom de la catégorie : ");
        String monNomCat = Lire.S();
        creeCategorie(con, monNomCat);

    }

    public static void CreeEncheres(Connection con, Timestamp monQuand, int monMontant, int monSur, int monDe) throws SQLException {

        try ( PreparedStatement pst = con.prepareStatement("insert into encheres2 (quand, montant, sur, de)"
                + " values (?,?,?,?) ")) {
            pst.setTimestamp(1, monQuand);
            pst.setInt(2, monMontant);
            pst.setInt(3, monSur);
            pst.setInt(4, monDe);

            pst.executeUpdate();

        }
    }
    
        public static void DemandeEncheres(Connection con) throws SQLException {

        System.out.println("---------------------------");
        System.out.println("Création d'une enchère");
        System.out.println("Quel est votre id ?");
        int monDe = Lire.i();

        System.out.println("Sur quel objet souhaitez vous enchérir? Saisir un titre");
        String monObj = Lire.S();
        try ( PreparedStatement st
                = con.prepareStatement("select id,prixbase from objet2 where objet2.titre = ?")) {
            st.setString(1, monObj);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                System.out.println("debug if");
                int monSur = res.getInt("id");
                int PB = res.getInt("prixbase");
                System.out.println("Rentrez le prix que vous souhaitez débourser : ");
                int monOffre = Lire.i();
                if (monOffre <= PB) {
                    System.out.println("Votre offre est insuffisante !");
                } else {
                    int monMontant = monOffre;

                    Timestamp monQuand = new Timestamp(System.currentTimeMillis());

                    System.out.println("Votre offre est enregistrée");

                    CreeEncheres(con, monQuand, monMontant, monSur, monDe);
                    System.out.println("Enrengistré");
                }

            } else {
                System.out.println("erreur : cet objet n'existe pas");
                System.out.println("debug else");
            }
        }
    }

    public static void creeBDB(Connection con) throws SQLException {
        String a = "White";
        String b = "Walter";
        String c = "ww@gmail.com";
        String d = "bluemeth";
        String e = "87101";

        String f = "Bateman";
        String g = "Patrick";
        String h = "americanpsycho@yahoo.com";
        String i = "PaulAllen212";
        String j = "10001";

        String k = "Chad";
        String l = "Giga";
        String m = "chad@gmail.com";
        String n = "0000";
        String o = "10115";

        String p = "Vetements";
        String q = "Bricolage";
        String r = "Sport";

        String s = "Blouse";
        String t = "Utile pour cuisiner des spécialités locales";
        Timestamp u = new Timestamp(System.currentTimeMillis());
        LocalDateTime U = LocalDateTime.now();
        LocalDateTime plusUnMois = U.plusMonths(1);
        Timestamp y = Timestamp.valueOf(plusUnMois);
        int v = 2600;
        int w = 1;
        int x = 1;

        String z = "Hache";
        String A = "Parfait pour découper votre bois en hiver";
        LocalDateTime C = LocalDateTime.now();
        LocalDateTime D = C.plusMonths(1);
        LocalDateTime E = C.plusMonths(2);
        Timestamp F = Timestamp.valueOf(D);
        Timestamp G = Timestamp.valueOf(E);
        int H = 5000;
        int I = 2;
        int J = 2;

        String K = "Halteres";
        String L = "Ils pèsent 10 kilos chacun";
        LocalDateTime M = LocalDateTime.now();
        LocalDateTime N = M.plusMonths(2);
        LocalDateTime O = M.plusMonths(3);
        Timestamp P = Timestamp.valueOf(N);
        Timestamp Q = Timestamp.valueOf(O);
        int R = 3000;
        int S = 3;
        int T = 3;

        String uu = "Cosmétique";
        String V = "Culture";
        String W = "Technologie";
        String X = "Jardin";
        String Y = "Jouets";
        String Z = "Ecole";
        String ZA = "Accesoires pour animaux";

        creeUtilisateur(con, a, b, c, d, e);
        creeUtilisateur(con, f, g, h, i, j);
        creeUtilisateur(con, k, l, m, n, o);
        creeCategorie(con, p);
        creeCategorie(con, q);
        creeCategorie(con, r);
        creeCategorie(con, uu);
        creeCategorie(con, V);
        creeCategorie(con, W);
        creeCategorie(con, X);
        creeCategorie(con, Y);
        creeCategorie(con, Z);
        creeCategorie(con, ZA);
        creeObjet(con, s, t, u, v, w, x, y);
        creeObjet(con, z, A, F, H, I, J, G);
        creeObjet(con, K, L, P, R, S, T, Q);

    }
    
}
