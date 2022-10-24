/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tzilliox01
 */
public class GestionBdD {

    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "passe");
    }

    public static void creeSchema(Connection con)
            throws SQLException {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
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
                                début timestamp not null,
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
                            nomtest varchar(50) not null,
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
                          
                          
                    
            
            
            
            
            
//            st.executeUpdate(
//                    """
//                    create table aime (
//                        u1 integer not null,
//                        u2 integer not null
//                    )
//                    """);
//            // je defini les liens entre les clés externes et les clés primaires
//            // correspondantes
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u1
//                        foreign key (u1) references utilisateur(id)
//                    """);
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u2
//                        foreign key (u2) references utilisateur(id)
//                    """);
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
            con.commit();
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
//            try {
//                st.executeUpdate(
//                        """
//                    alter table aime
//                        drop constraint fk_aime_u1
//                             """);
//                System.out.println("constraint fk_aime_u1 dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the constraint was not created
//            }
//            try {
//                st.executeUpdate(
//                        """
//                    alter table aime
//                        drop constraint fk_aime_u2
//                    """);
//                System.out.println("constraint fk_aime_u2 dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the constraint was not created
//            }
            // je peux maintenant supprimer les tables
//            try {
//                st.executeUpdate(
//                        """
//                    drop table aime
//                    """);
//                System.out.println("dable aime dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the table was not created
//            }
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

    public static void ChoixUtilisateur() throws SQLException {
    }

    public static void ChoixCategorie() throws SQLException {
    }
    
    public static void main(String[] args) {
        try(Connection con = defautConnect()) {
            deleteSchema(con);
            creeSchema(con);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

}
