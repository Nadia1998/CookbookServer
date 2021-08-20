package rs.ac.bg.fon.nprog.server.controller;

import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;
import rs.ac.bg.fon.nprog.server.so.korisnik.PrijavaKorisnika;
import rs.ac.bg.fon.nprog.server.so.recept.IzmeniRecept;
import rs.ac.bg.fon.nprog.server.so.recept.NadjiReceptPoImenu;
import rs.ac.bg.fon.nprog.server.so.recept.NadjiReceptPremaVremenuPripreme;
import rs.ac.bg.fon.nprog.server.so.recept.NadjiReceptPremaVrstiJela;
import rs.ac.bg.fon.nprog.server.so.recept.ObrisiRecept;
import rs.ac.bg.fon.nprog.server.so.recept.SacuvajRecept;
import rs.ac.bg.fon.nprog.server.so.recept.VratiRecepte;
import rs.ac.bg.fon.nprog.server.so.sastojak.SacuvajSastojke;
import rs.ac.bg.fon.nprog.server.so.sastojak.VratiSastojkeOdredjenogRecepta;
import rs.ac.bg.fon.nprog.library.domen.EnumKategorijaRecepta;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;
import com.google.gson.JsonParser;


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

	public ServerskiOdgovor login(Korisnik neulogovan) throws Exception {
		OpstaSO oso=new PrijavaKorisnika();
		return oso.izvrsiOperaciju(neulogovan);	
	}

	public ServerskiOdgovor sacuvajRecept(Recept receptNesacuvan) throws Exception {
		OpstaSO oso=new SacuvajRecept();
		return oso.izvrsiOperaciju(receptNesacuvan);	
	}
	
	public ServerskiOdgovor sacuvajSastojke(ArrayList<Sastojak> sastojci) throws Exception {  
		OpstaSO oso=new SacuvajSastojke();
		return oso.izvrsiOperaciju(sastojci);
	}

	public ServerskiOdgovor vratiRecepte() throws Exception {	
		OpstaSO oso=new VratiRecepte();
		return oso.izvrsiOperaciju(null);		
	}

	public ServerskiOdgovor vratiSastojkeRecepta(Recept r) throws Exception {
		OpstaSO oso=new VratiSastojkeOdredjenogRecepta();
		return oso.izvrsiOperaciju(r);
	}

	public ServerskiOdgovor obrisiRecept(Recept r) throws Exception {
		OpstaSO oso=new ObrisiRecept();
		ServerskiOdgovor so=oso.izvrsiOperaciju(r);
        if(so.isUspesno()) {
        	sacuvajUJSON(r);
        }
		return so;
	}

	public ServerskiOdgovor izmeniRecept(Recept receptZaIzmenu) throws Exception {
		OpstaSO oso=new IzmeniRecept();
		return oso.izvrsiOperaciju(receptZaIzmenu);
	}


	public ServerskiOdgovor nadjiReceptPoImenu(String naziv) throws Exception {
		OpstaSO oso=new NadjiReceptPoImenu();
		return oso.izvrsiOperaciju(naziv);
	}


	public ServerskiOdgovor nadjiReceptPoVP(EnumVremePripreme vremePripreme) throws Exception {
		OpstaSO oso=new NadjiReceptPremaVremenuPripreme();
		return oso.izvrsiOperaciju(vremePripreme);
	}


	public ServerskiOdgovor nadjiReceptPoVJ(EnumVrsteJela vrstaJela) throws Exception {
		OpstaSO oso=new NadjiReceptPremaVrstiJela();
		return oso.izvrsiOperaciju(vrstaJela);
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
}
