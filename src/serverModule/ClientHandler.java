//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     ClientHandler.java                   
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientHandler extends Thread{
	private Socket socket;
	private int clientNumber;
	
	public ClientHandler(Socket socket,int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		System.out.println("Nouvelle connection avec le client# "+clientNumber+ " au port# "+socket);
	}
	
	/**
	 * un thread se charge d'envoyer au client un message de bienvenue
	 * */
	public void run() {
		try {
			// creation d'un canal sortant pour envoyer des messages au client
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			// creation d'un canal ENTRANT pour envoyer des messages au client
			DataInputStream in = new DataInputStream(socket.getInputStream()); 
			
			// envoie d'un message au client pour confirmer la connection au serveur
			out.writeUTF("Connection au serveur reussie - vous etes le client# "+ this.clientNumber);
			
			/*connection au compte du client*/
			connection(out,in ,socket);
			
			/*affichage des dernier message de communication*/
			sendLastMessage(out);
			
			/*debut de la communication avec le client*/
			communication(in);
			
		} catch (IOException e) {
			System.out.println(" erreur capturer pour le client#"+ this.clientNumber +": "+e);
		}
		finally {
			try {
				// fermeture de la connexion avec le client
				socket.close();
			}
			catch(IOException e) {
				System.out.println("Impossible de fermer le socket!!!!!");
			}
			System.out.println("Fermeture de la connexion avec le client#"+ this.clientNumber);
		}
	}
	
	/**
	 * Cette methode permet de verifier la connection du client a son compte existant ou de lui creer un nouveau compte si non
	 * */
	public  User connection(DataOutputStream out,DataInputStream in ,Socket socket) throws IOException{

    	out.writeUTF("USERNAME");
    	String username = in.readUTF(); // on recuppere le userName du client
    	
    	User newUser=Server.messenger.getUserByUsername(username);
    	
    	if(newUser == null) { // nouveau utulisateur
    		out.writeUTF("PASSWORD");
        	String password = in.readUTF(); // on recuppere le password
        	newUser = new User(username,password,this.clientNumber); // creation d'un nouveau compte
        	Server.messenger.addUser(newUser);
    	}
    	else {
    		out.writeUTF("PASSWORD");
        	String password =  in.readUTF();
    		while(!password.equals(newUser.getPassword()) ) {
    			out.writeUTF("PASSWORD_ERROR");
    			password =  in.readUTF();
    		}
    		newUser.setConnectionNumber(this.clientNumber);
    	}
    	newUser.setUserSocket(socket);
    	SaveAndLoadManager.saveMessenger( ); // on sauvegarde dans la BD
    	out.writeUTF("CONNECTED");
    	return newUser;
    }
	
	/**
	 * Permet de recevoir le message venant du client et de le redistribuer a tous les autres autulisateurs connectés
	 * */
	
	public void communication(DataInputStream recieve) {
		String response;
				try {
					while(recieve.available()==0);
						response =recieve.readUTF(); // On attend le message du client
					
					while(!response.toUpperCase().equals("EXIT")) {
							/* on ajoute le message dans liste des message*/
						String message = printMessage (response, socket);
							Server.messenger.insertMessage(message); 
							
							/*On envoie le message a tous les clients connecte*/
							for(User user : Server.messenger.getUserList()) {
								if(user.getConnectionNumber()!=this.clientNumber) {
									// creation d'un canal sortant pour envoyer des messages au client
									DataOutputStream out = new DataOutputStream(user.getSocket().getOutputStream());
									out.writeUTF(message);
								}
								
							}
							SaveAndLoadManager.saveMessenger(); // on sauvegarde dans la BD
						while(recieve.available()!=0);
								response =recieve.readUTF();
						
					}
					// on reinitialise le client
					User user = Server.messenger.getUserByNumber(this.clientNumber);
					user.setUserSocket(null);
					user.setConnectionNumber(-1);
					SaveAndLoadManager.saveMessenger(); // on sauvegarde la BD
					
				} catch (IOException e) {
					System.out.println(" erreur du serveur ");
				}
	}
	
	public void sendLastMessage(DataOutputStream out) throws IOException{
		for(String message: Server.messenger.getOrderedmessage()) {
			out.writeUTF(message);
		}
	}
	
	/**
	 * Cette methode permet d'imprimer dans le bon format de date
	 */
	static String printDateTime() {
		DateFormat formatDate = new SimpleDateFormat ("yyyy-MM-dd @ hh:mm:ss");
		Date day = new Date();
		return formatDate.format(day);
	}
	
    /**
	 * cette Methode permet d'imprimer le message du client avec toutes informations personnel du client
	 * 
	 */
	public String printMessage (String message, Socket socket) {
		
		return "[ Utilisateur"+this.clientNumber+" - " + socket.getInetAddress().getHostAddress()+ ":"
				   +  socket.getPort() + " - " + printDateTime() + "] : " +message;
	}
}
