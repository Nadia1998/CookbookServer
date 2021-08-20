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
import rs.ac.bg.fon.nprog.server.so.korisnik.PrijavaKorisnika;

class IzmeniReceptTest {
	private IzmeniRecept operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new IzmeniRecept();
		DBBroker.getInstance().driverUpload();
    	DBBroker.getInstance().connect(); 
	}

	@AfterEach
	void tearDown() throws Exception {
		operacija=null;
		DBBroker.getInstance().disconnect();
	}

	@Test
	final void testIzvrsiKonkretnuOperaciju() throws Exception {
		Recept zaIzmenu=new Recept();
		Korisnik k=new Korisnik(0,"Luka","Antic","x","x");
		List<Sastojak> sastojci=new ArrayList<>();
		sastojci.add(new Sastojak(1, "Mleko", 100, EnumMera.ML, zaIzmenu));
		zaIzmenu.setReceptId(1);
		zaIzmenu.setNaziv("Recept");
		zaIzmenu.setVremePripreme(EnumVremePripreme.BRZO);
		zaIzmenu.setNivoTezine(EnumNivoTezine.POCETNICKO);
		zaIzmenu.setVrstaJela(EnumVrsteJela.DESERT);
		zaIzmenu.setKategorijaRecepta(EnumKategorijaRecepta.KOLACI);
		zaIzmenu.setOpisRecepta("opis");
		zaIzmenu.setKorisnik(k);
		zaIzmenu.setSastojci(sastojci);
		
		ServerskiOdgovor so  = operacija.izvrsiKonkretnuOperaciju(zaIzmenu);
		
		assertNotNull(so);
		Recept izmenjen=(Recept) so.getOdgovor();
		assertEquals(zaIzmenu.getReceptId(),izmenjen.getReceptId());
		assertTrue(so.isUspesno());
		
	}

}
