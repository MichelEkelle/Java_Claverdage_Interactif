//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     Messenger.java                   
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



/**
 * Cette classe permet de  concerver tous les comptes utilisateurs
 *  et les 15 derniers message
 * */
public class Messenger implements Serializable {
	private static  final long serialVersionUID = 1L;
	private static final int NomberMessage = 15;
	private  int latestMessage = -1;         // nous donne la position du dernier message (-1 si pas de message dans la liste)
	private  HashMap<String, User> accountList = new HashMap<String, User>(); // la cle est le loging du client
	private  LinkedList<String> messageList = new LinkedList<String>(); // permet de sauvegarder les 15 derniers messages
	 
	
	 /**
	  * permet de retourner un utilisateur en partir de son username
	  * */
	 public User getUserByUsername(String username){
		 return this.accountList.get(username);
	 }
	 
	 /**
	  * permet de retourner un utilisateur en partir de son numero
	  * */
	 public User getUserByNumber(int number){
		 User users=null;
		 for(Map.Entry<String, User> entry :  accountList.entrySet()) {
			 User user =entry.getValue();
			    if(user.getConnectionNumber()==number) users = user;
			}
		 return users;
	 }
	 
	 
	 /**
	  * permet d'ajouter un utilisateur
	  * */
	 public void addUser(User user) {
		 this.accountList.put(user.getUsername(), user);
	 }
	 
	 /**
	  * permet de retourner la liste de tous les utilisateurs qui sont connecte
	  * */
	 public  LinkedList<User> getUserList(){
		 LinkedList<User> userList = new LinkedList<User>();
		 for(Map.Entry<String, User> entry :  accountList.entrySet()) {
			 User user =entry.getValue();
			    if(user.getSocket()!= null)userList.add(user);
			}
		 return userList;
	 }
	 
	 /**
	  * permet d'ajouter les message dans la liste des messages
	  * */
	 public void insertMessage(String message) {
		 this.latestMessage = (this.latestMessage+1)%(NomberMessage+1);
		 messageList.add(this.latestMessage, message);
	 }
	 
	 /**
	  * permet de retourner la liste ordonnée des 15 derniers messages
	  * */
	 public LinkedList<String> getOrderedmessage(){
		 LinkedList<String> list = new LinkedList<String>();
		 if( this.messageList.isEmpty()) return list;
		 int taille = messageList.size();
		 for(int i=1;i<=taille;i++) {
			 int index=(this.latestMessage+i)%(taille);
			 list.add(messageList.get(index));
		 }
		 return list;
	 }

}
