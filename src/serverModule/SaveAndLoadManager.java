//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                      Travail pratique numero1 ETE 2020
//						
// \Fichier     SaveAndLoadManager.java                   
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveAndLoadManager {
	/**
	 * Permet de sauvegarder la base de donne (messenger)
	 * */
    public static void saveMessenger( ) {
    	
        try (FileOutputStream messenger = new FileOutputStream("saveMessenger.bin")) {
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(messenger));
            
            os.writeObject(Server.messenger);
            os.close();
        } 
        catch (FileNotFoundException e) {
          	 System.out.println("ERROR:  FICHIER INEXISTANT");
          }
       catch (IOException e) {
       	 System.out.println("ERROR:  Entree-sortie");
 	    }   
    }

    /**
	 * Permet de restaurer la base de donne (messenger)
	 * */
    public static Messenger LoadMessenger() {
    	Messenger mess;
        try (FileInputStream messenger = new FileInputStream("saveMessenger.bin")) {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(messenger));

            mess = (Messenger) is.readObject();
            is.close();
            return mess;

        } catch (ClassNotFoundException e) {
          	 System.out.println("ERROR:  class not found");
        }
        catch (FileNotFoundException e) {
         	 System.out.println("ERROR:  FICHIER INEXISTANT");
         }
        catch (IOException e) {
       	 System.out.println("ERROR:  Entree-sortie");
 	    } 
        return null;
    }
}
