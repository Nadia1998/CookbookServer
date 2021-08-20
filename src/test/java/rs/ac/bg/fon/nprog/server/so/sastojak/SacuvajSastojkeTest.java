package rs.ac.bg.fon.nprog.server.so.sastojak;

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
import rs.ac.bg.fon.nprog.server.so.recept.SacuvajRecept;

class SacuvajSastojkeTest {

	private SacuvajSastojke operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new SacuvajSastojke();
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
	
		Recept r=new Recept();
		Korisnik k=new Korisnik(0,"Luka","Antic","x","x");
		List<Sastojak> sastojci=new ArrayList<>();
		sastojci.add(new Sastojak(1, "Mleko", 100, EnumMera.ML, r));
		r.setReceptId(1);
		r.setNaziv("Recept");
		r.setVremePripreme(EnumVremePripreme.BRZO);
		r.setNivoTezine(EnumNivoTezine.POCETNICKO);
		r.setVrstaJela(EnumVrsteJela.DESERT);
		r.setKategorijaRecepta(EnumKategorijaRecepta.KOLACI);
		r.setOpisRecepta("opis");
		r.setKorisnik(k);
		r.setSastojci(sastojci);
		Sastojak s=new Sastojak(12, "Secer",100, EnumMera.G, r);
		List<Sastojak> zaDodavanje=new ArrayList<>();
		zaDodavanje.add(s);
	
		ServerskiOdgovor so = operacija.izvrsiKonkretnuOperaciju(zaDodavanje);
        boolean sacuvan=(boolean) so.getOdgovor();
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertEquals(so.getOdgovor(),sacuvan);

	}

}
