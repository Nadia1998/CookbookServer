package rs.ac.bg.fon.nprog.server.controller;

import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit.Parser;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;


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

	

	public Recept sacuvajRecept(Recept receptNesacuvan) throws Exception {
		db.driverUpload();
		db.connect();
		int receptId = db.vratiIDRecepta();
		receptNesacuvan.setReceptId(receptId);
		Recept sacuvan = db.sacuvajRecept(receptNesacuvan);
		return sacuvan;
	}
	
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


	public ArrayList<Recept> vratiRecepte() throws Exception {
		ArrayList<Recept> recepti;
		db.connect();
		db.driverUpload();
		recepti = db.vratiRecepte();
		return recepti;

	}


	public ArrayList<Sastojak> vratiSastojkeRecepta(Recept r) throws Exception {
		ArrayList<Sastojak> sastojci;
		db.connect();
		db.driverUpload();
		sastojci = db.vratiSastojkeRecepta(r);
		return sastojci;
	}

	public boolean obrisiRecept(Recept r) throws Exception {
		db.connect();
		db.driverUpload();
		boolean uspesno = db.obrisiRecept(r);
        if(uspesno) {
        	sacuvajUJSON(r);
        }
		return uspesno;
	}

	private void sacuvajUJSON(Recept r) {
		JSONObject receptObrisan = new JSONObject();
        receptObrisan.put("Korisnik:", r.getKorisnik().getIme());
        receptObrisan.put("Naziv:", r.getNaziv());
        receptObrisan.put("Nivo tezine:", r.getNivoTezine().toString());
        receptObrisan.put("Vreme pripreme:", r.getVremePripreme().toString());
        receptObrisan.put("Vrsta jela:", r.getVrstaJela().toString());
        receptObrisan.put("Kategorija recepta:", r.getKategorijaRecepta().toString());
        receptObrisan.put("Opis recepta:", r.getOpisRecepta());
        
        JsonParser jsonParser = new JsonParser();

        try {
            Object obj = jsonParser.parse(new FileReader("C:\\Users\\Nadia\\eclipse-workspace\\CookbookServer\\obrisani.json"));
            

            FileWriter file = new FileWriter("C:\\Users\\Nadia\\eclipse-workspace\\CookbookServer\\obrisani.json",true);
            file.write(receptObrisan.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}


	public Recept izmeniRecept(Recept receptZaIzmenu) throws Exception {
		db.connect();
		db.driverUpload();
		Recept izmenjen = db.izmeniRecept(receptZaIzmenu);
		if (izmenjen == null)
			return null;
		return izmenjen;
	}


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
