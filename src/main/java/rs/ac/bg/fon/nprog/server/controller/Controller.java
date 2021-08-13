package rs.ac.bg.fon.nprog.server.controller;

import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Kontroler u kome se nalaze sve sistemske operacije.
 * @author Nadia
 */
public class Controller {
	/**
	 * Instanca kontrolera kao Kontroler potrebna za Singlton patern.
	 */
	private static Controller instance;
	/**
	 * Instanca brokera baze podataka kao DBBroker.
	 */
	private DBBroker db;

	/**
	 * Konstruktor koji incijalizuje objekat i nista vise.
	 */
	public Controller() {
		db = new DBBroker();
	}

	/**
	 * Vraca jedinstvenu instancu klase Kontroler.
	 * 
	 * @return instance kao Kontroler.
	 */
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	/**
	 * Prijavljuje korisnika na sistem.
	 * 
	 * @param neulogovan Korisnik za logovanje kao Korisnik
	 * 
	 * @return k ulogovan korisnik kao Korisnik 
	 * 
	 * @throws Exception Izuzetak ukoliko ne uspe povezivanje sa bazom
	 * 
	 */

	public Korisnik login(Korisnik neulogovan) throws Exception {

		db.driverUpload();
		db.connect();
		List<Korisnik> lista = db.vratiKorisnike();
		for (Korisnik k : lista) {
			if (neulogovan.getKorisnickoIme().equals(k.getKorisnickoIme())) {
				if (neulogovan.getLozinka().equals(k.getLozinka())) {
					return k;
				}
			}
		}
		return null;
	}

	/**
	 * Cuva recept.
	 * 
	 * @param receptNesacuvan Recept za cuvanje kao Recept.
	 * 
	 * @return sacuvan Recept koji je sacuvan kao Recept.
	 * 
	 * @throws Exception Ukoliko ne uspe povezivanje sa bazom.
	 * 
	 */

	public Recept sacuvajRecept(Recept receptNesacuvan) throws Exception {
		db.driverUpload();
		db.connect();
		int receptId = db.vratiIDRecepta();
		receptNesacuvan.setReceptId(receptId);
		Recept sacuvan = db.sacuvajRecept(receptNesacuvan);
		return sacuvan;
	}
	/**
	 * Cuva listu sastojaka.
	 * 
	 * @param sastojci Lista sastojaka kao List.
	 * 
	 * @return boolean Odgovor o sacuvanoj listi kao boolean.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */

	public boolean sacuvajSastojke(ArrayList<Sastojak> sastojci) throws Exception {
		db.driverUpload();
		db.connect();
		for (Sastojak s : sastojci) {
			int sastojakID;
			sastojakID = db.vratiIDSastojka();
			s.setSastojakId(sastojakID);
			db.sacuvajSastojak(s);

		}
		return true;

	}

	/**
	 * Vraca listu recepata.
	 * 
	 * @return recepti Lista recepata kao List.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */
	public ArrayList<Recept> vratiRecepte() throws Exception {
		ArrayList<Recept> recepti;
		db.connect();
		db.driverUpload();
		recepti = db.vratiRecepte();
		return recepti;

	}
	/**
	 * Vraca listu sastojaka recepta.
	 * 
	 * @param r Recept kao Recept.
	 * 
	 * @return sastojci Lista sastojaka kao List.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */

	public ArrayList<Sastojak> vratiSastojkeRecepta(Recept r) throws Exception {
		ArrayList<Sastojak> sastojci;
		db.connect();
		db.driverUpload();
		sastojci = db.vratiSastojkeRecepta(r);
		return sastojci;
	}
	/**
	 * Brise recept.
	 * 
	 * @param r Recept kao Recept.
	 * 
	 * @return uspesno Uspesnost cuvanja kao boolean.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */

	public boolean obrisiRecept(Recept r) throws Exception {
		db.connect();
		db.driverUpload();
		boolean uspesno = db.obrisiRecept(r);

		return uspesno;
	}
	/**
	 * Menja recept.
	 * 
	 * @param receptZaIzmenu Recept koji se menja kao Recept.
	 * 
	 * @return izmenjen Recept izmenjen kao Recept.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom
	 */

	public Recept izmeniRecept(Recept receptZaIzmenu) throws Exception {
		db.connect();
		db.driverUpload();
		Recept izmenjen = db.izmeniRecept(receptZaIzmenu);
		if (izmenjen == null)
			return null;
		return izmenjen;
	}
	/**
	 * Nalazi recept po imenu.
	 * 
	 * @param naziv Naziv recepta kao String.
	 * 
	 * @return recepti Lista recepata kao List.
	 * 
	 * @throws Exception Ukoliko ne uspe povezivanje sa bazom.
	 */

	public List<Recept> nadjiReceptPoImenu(String naziv) throws Exception {
		ArrayList<Recept> recepti = new ArrayList<>();
		db.connect();
		db.driverUpload();
		try {
			recepti = db.filterPremaNazivu(naziv);
		} catch (Exception ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		return recepti;
	}
	/**
	 * Nalazi recept po vremenu pripreme.
	 * 
	 * @param vremePripreme Vreme pripreme kao EnumVremePripreme.
	 * 
	 * @return recepti Lista recepata kao List.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */

	public List<Recept> nadjiReceptPoVP(EnumVremePripreme vremePripreme) throws Exception {
		ArrayList<Recept> recepti = new ArrayList<>();
		db.connect();
		db.driverUpload();
		try {
			recepti = db.filterPremaVP(vremePripreme);
		} catch (Exception ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		return recepti;
	}

	/**
	 * Nalazi recept po vrsti jela.
	 * 
	 * @param vrstaJela Vrsta jela kao EnumVrstaJela.
	 * 
	 * @return recepti Lista recepata kao List.
	 * 
	 * @throws Exception  Ukoliko ne uspe povezivanje sa bazom.
	 */
	public List<Recept> nadjiReceptPoVJ(EnumVrsteJela vrstaJela) throws Exception {
		ArrayList<Recept> recepti = new ArrayList<>();
		db.connect();
		db.driverUpload();
		try {
			recepti = db.filterPremaVJ(vrstaJela);
		} catch (Exception ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		return recepti;
	}
}
