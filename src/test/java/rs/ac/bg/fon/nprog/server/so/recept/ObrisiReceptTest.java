package rs.ac.bg.fon.nprog.server.so.recept;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.nprog.library.domen.EnumKategorijaRecepta;
import rs.ac.bg.fon.nprog.library.domen.EnumMera;
import rs.ac.bg.fon.nprog.library.domen.EnumNivoTezine;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;

class ObrisiReceptTest {

	private ObrisiRecept operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new ObrisiRecept();
		DBBroker.getInstance().driverUpload();
		DBBroker.getInstance().connect();
	}

	@AfterEach
	void tearDown() throws Exception {
		operacija = null;
		DBBroker.getInstance().disconnect();
	}

	@Test
	final void testIzvrsiKonkretnuOperaciju() throws Exception {
		Recept zaBrisanje=new Recept();
		Korisnik k=new Korisnik(0,"Luka","Antic","x","x");
		List<Sastojak> sastojci=new ArrayList<>();
		sastojci.add(new Sastojak(1, "Mleko", 100, EnumMera.ML, zaBrisanje));
		zaBrisanje.setReceptId(3);
		zaBrisanje.setNaziv("x");
		zaBrisanje.setVremePripreme(EnumVremePripreme.BRZO);
		zaBrisanje.setNivoTezine(EnumNivoTezine.POCETNICKO);
		zaBrisanje.setVrstaJela(EnumVrsteJela.DESERT);
		zaBrisanje.setKategorijaRecepta(EnumKategorijaRecepta.KOLACI);
		zaBrisanje.setOpisRecepta("opis");
		zaBrisanje.setKorisnik(k);
		zaBrisanje.setSastojci(sastojci);
		ServerskiOdgovor so=operacija.izvrsiKonkretnuOperaciju(zaBrisanje);
		
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertTrue((boolean)so.getOdgovor());
		
	}

}
