/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.theo.encheresge2t2.gui;

//Auteur : Mr.Beuvron

public class Utilisateur {
    
    private final int id;
    private String nom;
    private String pass;

    public Utilisateur(int id, String nom, String pass) {
        this.id = id;
        this.nom = nom;
        this.pass = pass;
    }


    public int getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }

    
    public void setNom(String nom) {
        this.nom = nom;
    }

 
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
}