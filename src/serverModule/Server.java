//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     Server.java                   
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

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	
	private static ServerSocket listener;
	public static Messenger messenger; // represente la base donne. elle recroupe tous les compte et les 15 dernier utulisateur
	

	/*
	 * Application Serveur
	 * */
	public static void main(String[] args) throws Exception {
		
		/* on charge la base de donnee*/
		File dataBase = new File("saveMessenger.bin");
		if(dataBase.exists()) {
			messenger = SaveAndLoadManager.LoadMessenger();
		}
		else {
			 messenger = new Messenger();
			 SaveAndLoadManager.saveMessenger();
		}
		
		/*declaration des variables*/
		Scanner sc = new Scanner(System.in);   //la variable sc sauvegarde les saisies claviers.
		int clientNumber = 0;                  //la variable clientNumber sauvegarde le nombre de client connecte au serveur.
		int serverPort;                        //la variable serverPort sauvegarde le numero de port.
		String serverAddress;                  //la variable serverAddress sauvegarde le adresse ip du serveur.
		
		
        // lecture de l adresse IP locale
            serverAddress = "127.0.0.1"; 
        
		/* adresse et port du serveur*/  
		
		System.out.println("*-*- Veuillez entrer un numéro de port compris entre 5000 et 5050 :");
		try {
			serverPort = sc.nextInt();
		}catch(Exception e){
			serverPort=0; //vous avez saisi un numéro de port invalide
		}// Au cas ou le numéro de port saisi n'est pas un int
		
		sc.nextLine();
		
		
		/* Boucle permettant de verifier le numero de port rentre par le client*/
		while(serverPort<= 5000 || serverPort>= 5050) {
			System.out.println("*-*- Port invalide!\nVeuillez entrer un numéro de port compris entre 5000 et 5050 :");
			try {
				serverPort = sc.nextInt();
			}catch(Exception e){
				serverPort=0; //vous avez saisi un numéro de port invalide
			}//Au cas ou le numéro de port saisi n'est pas un int
			sc.nextLine();
			
		}

		/* création de la connexion pour communiquer avec les clients*/
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIp = InetAddress.getByName(serverAddress);

		/* Association de l'adresse et du port à la connexion*/
		listener.bind(new InetSocketAddress(serverIp, serverPort));

		System.out.format("*-*- Le serveur est connecté avec les informations suivantes:  %s : %d%n", serverAddress, serverPort);

		try {
			while (true) {
				// attend qu'un prochain client se connecte
				// une nouvelle connexion: on incrémente le compteur clientNumber

				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		} finally

		{
			// fermeture de la connexion
			listener.close();
			sc.close();
		}
	}
}
