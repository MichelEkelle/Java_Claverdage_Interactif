//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     Client.java                   
// \Auteurs  :  Michel EKELLE   1924828 
//              Eric KENMOGNE   1928406
//              Youva BOUTORA   1986737
// 
// \version  Du : 2020-06-02
// \Description: 
//      Projet: SYSTEME DE CLAVARDAGE INTERACTIF
// 		Code source partie CLIENT
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package clientModule;
import java.net.Socket;
import java.util.Scanner;


import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Client {
	public static boolean endListener=false;
	public static Socket socket;
	
	
	/**
	 * Application Client
	 * */
	public static void main(String[] args) throws Exception {
		
				Scanner sc = new Scanner(System.in);
				
				// declaration des variables Adresses et port du serveur
				String serverAdress;
				int port;
				
				// on lance l'inteface utulisateur
				interfaceUtilisateur();
				
				System.out.println("-*-*- Veuillez entrer l'adresse IP du serveur :");
				serverAdress = sc.nextLine();
				//serverAdress = "127.0.0.1";

				while(!IPvalid(serverAdress)) {
					System.out.println("-*-*- IP invalide! \nVeuillez entrer une adresse IP valide :");
					serverAdress = sc.nextLine();
				}
				
				
				System.out.println("-*-*- Veuillez entrer un numéro de port compris entre 5000 et 5050 :");
				try {
					port = sc.nextInt();
				}catch(Exception e){
					port=0; //vous avez saisi un numéro de port invalide
				}// Au cas ou le numéro de port saisi n'est pas un int
				
				sc.nextLine();
				
				
				/*Boucle permettant de verifier le numero de port rentre par le client */
				
				while(port<= 5000 || port>= 5050) {
					System.out.println("-*-*- Port invalide! \nVeuillez entrer un numéro de port compris entre 5000 et 5050 :");
					try {
						port = sc.nextInt();
					}catch(Exception e){
						port=0;
					}
					sc.nextLine();	
				}
				
				
				/*Creation d une nouvelle connexion avec le serveur*/
				socket=new Socket(serverAdress, port);
				System.out.format("-*-*- Connection au serveur avec les informations suivantes:  %s : %d%n", serverAdress, port);
				
				/*creation d un canal entrant et sortant pour recevoir et envoyer les messages  du/vers le serveur sur le canal*/	
				
				DataInputStream in = new DataInputStream(socket.getInputStream()); // canal reception
				DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // canal envoie
				
				
				
				/*Attente de la reception d'un message de connection envoye par le serveur sur le canal*/
				String MessageFromServer = in.readUTF();
				System.out.println(MessageFromServer);
				
				
				/*connection du client a son compte utulisateur */
				connection(in,out);
				
				// on lance le thread qui va s'occuper de la reception des messages du serveur lors de la conversation
				new ServerHandler(in).start();
				
				// on debute la communication bloquante avec les utulisateurs connectes
				envoieMessage(out);
				
		
		// fermmeture de la connexion a la fin
		socket.close();
		sc.close();

	}
	
/**
 * La methode IPvalid permet de verifier la validite d une adresse IP saisi par le client, 
 * il verifie que l adrsse contient des chiffres et qu il a une valeur de  4 octets
 * 
 * */
	
	static boolean IPvalid(String IP) {
		String[] splittedIP;
		splittedIP = IP.split("\\.");
		if(splittedIP.length!=4) {
			return false;
		}
		int[] intIP = new int[4]; 
		for(int i=0; i<4; i++) {
			try {
		        intIP[i] = Integer.parseInt(splittedIP[i]);
		    } catch (NumberFormatException | NullPointerException nfe) {
		        return false;
		    }
			if(intIP[i]<0 || intIP[i]>255) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Cette methode qui va gerer les envoies de messages de l'utulisateur
	 * */
	static void envoieMessage(DataOutputStream out) throws Exception{
		System.out.println("-*-* Vous pouvez commencer la communication. ");
		Scanner sc = new Scanner(System.in);
		String message = sc.nextLine();
		
		
		while(!message.toUpperCase().equals("EXIT")) {
			if(message.length() <= 200) 
			{
				out.writeUTF(message);
			}else {
				System.out.println("-*-*- Veuillez entrer un message de taille maximal de 200 carateres");
			}
			message = sc.nextLine();
		}
		
		out.writeUTF("EXIT");
		Client.endListener= true;
	}
	
	/**
	 * cette fonction permet de gerer la connection du cient a son compte utulisateur
	 * **/
	public static void connection(DataInputStream in,DataOutputStream out)  throws Exception{
		Scanner sc = new Scanner(System.in);
		String messageFromServer = in.readUTF(); // on recuppere le message du serveur
		while(!messageFromServer.equals("CONNECTED")) {
		
			 switch (messageFromServer.toUpperCase()) {
	            case "USERNAME":
	            	System.out.println("-*-*- Entrez votre nom utulisateur: ");
	            	String userName = sc.nextLine();
	            	out.writeUTF(userName);
	            	
	                     break;
	            case "PASSWORD":  
	            	System.out.println("-*-*- Entrez votre mot de passe: ");
	            	String passWord = sc.nextLine();
	            	out.writeUTF(passWord);
	            	
	                     break;
	            case "PASSWORD_ERROR":  
	            	System.out.println("-*-*- Mot de passe incorrect: Entrez  a nouveau: ");
	            	String passWordError = sc.nextLine();
	            	out.writeUTF(passWordError);
	            	
	                     break;
	            case "CONNECTED": 
	                     break;
	            default: 
	                     break;
	        }
			
			messageFromServer = in.readUTF();
			System.out.println(messageFromServer);
		}
		
		System.out.println(" -*-*- Connection a votre compte reussie !! ");
	}
	
	/**
	 * methode qui permet d'afficher le menu sur la console
	 * */
	public static void interfaceUtilisateur() {
		System.out.println("********************************************************************");
		System.out.println("* BIEN VENU A L'APPLICATION DE CLAVERDAGE OPENSOURCE < PolyTchat > *");
		System.out.println("*------------------------------------------------------------------*");
		System.out.println("*                                                                  *");
		System.out.println("*                         MENU D'UTILISATION :                     *");
		System.out.println("* EXIT: permet de quitter le claverdage                            *");
		System.out.println("********************************************************************");
		System.out.println("");
		
	}

}

