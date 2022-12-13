/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        try(Connection con = defautConnect()) {
            System.out.println("debug");
            //demandeNouvelUtilisateur(con);
            //demandeNouvelObjet(con);
            deleteSchema(con);
            //creeSchema(con);
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
                            mail varchar(100) not null unique,
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
                                    foreign key (cat) references encheres2(id)
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
           

//            // je defini les liens entre les clés externes et les clés primaires
//            // correspondantes

           
            
            con.commit();      // je confirme (commit) la transaction
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
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

    // vous serez bien contents, en phase de développement de pouvoir
    // "repartir de zero" : il est parfois plus facile de tout supprimer
    // et de tout recréer que d'essayer de modifier le schema et les données
    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate(
                """
                alter table objet2
                drop constraint fk_objet2_proposepar 
  //            foreign key (proposepar) references utilisateur2(id) à mettre ?
                """);
                } catch (SQLException ex) {}
                
        
               try {
                st.executeUpdate(
                 """
                alter table objet2
                drop constraint fk_objet2_cat
                foreign key (sur) references objet2(id)
                """);
                } catch (SQLException ex) {}
                
                try {
                st.executeUpdate(
                 """
                alter table encheres2
                drop constraint fk_encheres2_sur 
                foreign key (sur) references objet2(id)
                """);
                } catch (SQLException ex) {}
            
                   try {
                st.executeUpdate(
                 """
                alter table encheres2
                drop constraint fk_encheres2_de
                foreign key (de) references utilisateur2(id)
                """);
                } catch (SQLException ex) {}
            
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur2
                    """);
                System.out.println("table utilisateur2 dropped");
            }
            catch (SQLException ex) {}
            //*************************************************************************
            try {
                st.executeUpdate(
                        """
                    drop table encheres2
                    """);
                System.out.println("table encheres2 dropped");
            } 
            catch (SQLException ex) {}
            //*************************************************************************        
            try {
                st.executeUpdate(
                        """
                    drop table objet2
                    """);
                System.out.println("table objet2 dropped");
            } catch (SQLException ex) {}
            //*************************************************************************
            try {
                st.executeUpdate(
                        """
                    drop table categorie2
                    """);
                System.out.println("table categorie2 dropped");
            } catch (SQLException ex) {// nothing to do : maybe the table was not created
            }
            //*****************************************************************************
        }
    }

 /*   public static void menu(Connection con) {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Menu BdD Aime");
            System.out.println("=============");
            System.out.println("1) créer/recréer la BdD initiale");
            System.out.println("2) liste des utilisateurs");
            System.out.println("3) liste des liens 'Aime'");
            System.out.println("4) ajouter un utilisateur");
            System.out.println("5) ajouter un lien 'Aime'");
            System.out.println("6) ajouter n utilisateurs aléatoires");
            System.out.println("0) quitter");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                if (rep == 1) {
                    // TODO recreeTout(con);
                } else if (rep == 2) {
                    // TODO afficheTousLesUtilisateur(con);
                } else if (rep == 3) {
                    // TODO afficheAmours(con);
                } else if (rep == 4) {
                    // TODO demandeNouvelUtilisateur(con);
                } else if (rep == 5) {
                    // TODO demandeNouvelAime(con);
                } else if (rep == 6) {
                    System.out.println("création d'utilisateurs 'aléatoires'");
                    int combien = ConsoleFdB.entreeEntier("combien d'utilisateur : ");
                    for (int i = 0; i < combien; i++) {
                        boolean exist = true;
                        while (exist) {
                            String nom = "U" + ((int) (Math.random() * 10000));
                            try{
                                //TODO createUtilisateur(con, nom, "P" + ((int) (Math.random() * 10000)));
                                exist = false;
                            }
                           catch (NomExisteDejaException ex) {
                            }

                        }

                    }
                }
            } catch (SQLException ex) {
                throw new Error(ex);
            }
        }
    }
    */
    
    
    public static void creerUtilisateur(Connection con,String monNom,String monPrenom,String monCodePostal, String monEmail, String monMDP) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("insert into utilisateur2 (nom, prenom, email, codepostal, pass) "
                + " values (?,?,?,?,?) ")) {
            pst.setString(1, monNom);
            pst.setString(2, monPrenom);
            pst.setString(3, monCodePostal);
            pst.setString(4, monEmail);
            pst.setString(5, monMDP);
            pst.executeUpdate();
        }
    }
    
    
    public static void demandeNouvelUtilisateur(Connection con) throws SQLException{
        
        //cette fonction récolte les variables nécéssaires à la création d'un nouvel utilisateur, et puis
        //les créer avec creerUtilisateur(), appelé dans le main
        
        Scanner console = new Scanner(System.in); 
        
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
        creerUtilisateur(con, monNom, monPrenom, monCodePostal, monEmail, monMDP);

    }
    
    public static void demandeNouvelObjet(Connection con) throws SQLException{
        
        //cette fonction récolte les variables nécéssaires à la création d'un nouvel objet dans la bdd, et puis
        //les créer avec creerObjet(), appelé dans le main
        
        Scanner console = new Scanner(System.in); 
        
        System.out.println("Rentrez le titre : ");
        String monTitre = Lire.S();
        
        System.out.println("Rentrez la description : ");
        String maDescription = console.nextLine();
        
        //monDebut =                                //récolter les valeurs de temps
        
        System.out.println("Rentrez votre prix inital : ");
        String monPrixbase = console.nextLine();
        
        //int MonProposepar =                             //récolter l'id utilisteur ? 
        
        System.out.println("Choissisez la catégorie");       //créer une boucle pour forcer le xhoix d'une catégorie
        System.out.println("1) Vetements ");
        System.out.println("2) Livres");
        int maCat = ConsoleFdB.entreeEntier("Votre choix : ");
   
        //maFin =                                   //récolter monDébut + X temps à définir
        
        //creerObjet (con, monTitre, maDescription, monDebut, monPrixbase, monProposepar, maCat, maFin);

    }
    /*
    
    public static void creerObjet (Connection con,String monTitre,String maDescription,timestamp monDebut, int monPrixbase, int monProposepar, int maCat, timestamp maFin) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("insert into objet2 (titre, description, debut, prixbase, proposepar, cat, fin)"    
                + " values (?,?,?,?,?,?,?) ")) {
            pst.setString(1, monTitre);
            pst.setString(2, maDescription);
            pst.setString(3, monDebut);    //timestamp
            pst.setInt(4, monPrixbase);   
            pst.setInt(5, monProposepar);
            pst.setInt(6, maCat);    
            pst.setString(7, maFin);    //timestamp
            pst.executeUpdate();
        }
    }
    
    */
}
