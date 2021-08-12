package rs.ac.bg.fon.nprog.server.niti;


import rs.ac.bg.fon.nprog.server.gui.FrmServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nadia
 */
public class PokreniServerNit extends Thread {

    FrmServer forma;
    private boolean pokrenut;
    private ServerSocket ss;
    
    public PokreniServerNit(FrmServer f) {
        this.forma = f;
    }

    public PokreniServerNit() {
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(9000);
            forma.serverJePokrenut();
            pokrenut = true;
            while (!isInterrupted()) {
                Socket s = ss.accept();
                System.out.println("Prihvatio klijenta");
                ObradaZahtevaNit obradaZahteva = new ObradaZahtevaNit(s);
                obradaZahteva.start();
            }
        } catch (IOException ex) {
            System.out.println("Kraj izvrsavanja niti servera");
        }
    }

    public void zaustaviServer() {
        forma.serverNijePokrenut();
        try {
            ss.close();
            this.interrupt();

        } catch (IOException ex) {
            Logger.getLogger(PokreniServerNit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
