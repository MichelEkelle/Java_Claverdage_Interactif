//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     ServerHandler.java                   
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

import java.io.DataInputStream;
import java.io.IOException;

public class ServerHandler extends Thread{
	private String response;
	private DataInputStream recieve;
	
	public ServerHandler(DataInputStream in) {
		recieve = in;
	}
	
	/**
	 * Ce Thread s'occupe de la reception des message venant du serveur
	 * */
	public void run() {
		try {
			// creation d'un canal entrant pour recevoir des messages du serveur
			
			while(!Client.endListener) {
				if(this.recieve.available()!=0) {
					response =this.recieve.readUTF();
					System.out.println(response);
				}
			}
			
		} catch (IOException e) {
			System.out.println(" erreur du serveur ");
		}
		
	}
}
