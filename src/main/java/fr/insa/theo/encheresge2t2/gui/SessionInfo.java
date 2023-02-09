
package fr.insa.theo.encheresge2t2.gui;
import java.sql.Connection;
import java.util.Optional;

//auteur : Mr Beuvron

public class SessionInfo {

    public Optional<Utilisateur> curUser;
    private Connection conBdD;

    public SessionInfo() {
        this.curUser = Optional.empty();
        this.conBdD = null;
    }

    public boolean userConnected() {
        return this.curUser.isPresent();
    }

    public Optional<Utilisateur> getCurUser() {
        return this.curUser;
    }

    public void setCurUser(Optional<Utilisateur> curUser) {
        this.curUser = curUser;
    }

    public int getUserID() {
        return this.curUser.orElseThrow().getId();
    }

    /**
     * @return the conBdD
     */
    public Connection getConBdD() {
        return conBdD;
    }

    /**
     * @param conBdD the conBdD to set
     */
    public void setConBdD(Connection conBdD) {
        this.conBdD = conBdD;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.curUser.orElseThrow().getNom();
    }

}
