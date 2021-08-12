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

/*
 * @author Nadia
 */
public class Controller {
	
	private static Controller instance;
	
	private DBBroker db;

	
	public Controller() {
		db = new DBBroker();
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

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

	public Recept sacuvajRecept(Recept receptNesacuvan) throws ClassNotFoundException, SQLException {
		db.driverUpload();
		db.connect();
		int receptId = db.vratiIDRecepta();
		receptNesacuvan.setReceptId(receptId);
		Recept sacuvan = db.sacuvajRecept(receptNesacuvan);
		return sacuvan;
	}

	public boolean sacuvajSastojke(ArrayList<Sastojak> sastojci) throws ClassNotFoundException, SQLException {
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

//    public boolean savuvajSastojke(ArrayList<Sastojak> sastojci) throws ClassNotFoundException, SQLException {
//        boolean sacuvano = false;
//        db.driverUpload();
//        db.connect();
//        try {
//            for (Sastojak s : sastojci) {
//                int sastojakID = db.vratiIDSastojka();
//                s.setSastojakId(sastojakID);
//                db.sacuvajSastojak(s);
//            }
//            db.commit();
//            sacuvano = true;
//        } catch (SQLException e) {
//            db.rollback();
//        }
//        return sacuvano;
//    }
	public ArrayList<Recept> vratiRecepte() throws SQLException, ClassNotFoundException {
		ArrayList<Recept> recepti;
		db.connect();
		db.driverUpload();
		recepti = db.vratiRecepte();
		return recepti;

	}

	public ArrayList<Sastojak> vratiSastojkeRecepta(Recept r) throws SQLException, ClassNotFoundException {
		ArrayList<Sastojak> sastojci;
		db.connect();
		db.driverUpload();
		sastojci = db.vratiSastojkeRecepta(r);
		return sastojci;
	}

	public boolean obrisiRecept(Recept r) throws SQLException, ClassNotFoundException, Exception {
		db.connect();
		db.driverUpload();
		boolean uspesno = db.obrisiRecept(r);

		return uspesno;
	}

	public Recept izmeniRecept(Recept receptZaIzmenu) throws SQLException, ClassNotFoundException, Exception {
		db.connect();
		db.driverUpload();
		Recept izmenjen = db.izmeniRecept(receptZaIzmenu);
		if (izmenjen == null)
			return null;
		return izmenjen;
	}

	public List<Recept> nadjiReceptPoImenu(String naziv) throws SQLException, ClassNotFoundException {
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

	public List<Recept> nadjiReceptPoVP(EnumVremePripreme vremePripreme) throws SQLException, ClassNotFoundException {
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

	public List<Recept> nadjiReceptPoVJ(EnumVrsteJela vrstaJela) throws SQLException, ClassNotFoundException {
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
