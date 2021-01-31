//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     User.java                   
// \Auteurs  :  Michel EKELLE   1924828 
//              Eric KENMOGNE   1928406
//              Youva BOUTORA   1986737
// 
// \version  Du : 2020-06-02
// \Description: 
//      Projet: SYSTEME DE CLAVARDAGE INTERACTIF
// 		Code source partie SERVEUR
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package serverModule;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable{
	private static  final long serialVersionUID = 1L;
    private String username = ""; 
    private String password = ""; 
    private transient Socket socket;
    private int connectionNumber=-1;

    public User(String username, String password, int number){
        this.username = username;
        this.password = password;
        this.connectionNumber = number;
        socket=null;
        
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @param set the socket
     */
    public void setUserSocket(Socket socket) {
        this.socket = socket;
    }
    /**
     * @param set the connectionNumber
     */
    public void setConnectionNumber(int number) {
        this.connectionNumber = number;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }
    /**
     * @return the connectionNumber
     */
    public int getConnectionNumber() {
        return connectionNumber;
    }
}
