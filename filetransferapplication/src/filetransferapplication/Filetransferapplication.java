/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransferapplication;

/**
 *
 * @author User
 */
public class Filetransferapplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        FileServer server = new FileServer();
        server.createAndListenSocket();
        
    }
    
}
