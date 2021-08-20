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

class SacuvajReceptTest {
	private SacuvajRecept operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new SacuvajRecept();
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
		Recept zaCuvanje = new Recept();
		Korisnik k = new Korisnik(0, "Luka", "Antic", "x", "x");
		List<Sastojak> sastojci = new ArrayList<>();
		sastojci.add(new Sastojak(1, "Mleko", 100, EnumMera.ML, zaCuvanje));
		zaCuvanje.setReceptId(4);
		zaCuvanje.setNaziv("x");
		zaCuvanje.setVremePripreme(EnumVremePripreme.BRZO);
		zaCuvanje.setNivoTezine(EnumNivoTezine.POCETNICKO);
		zaCuvanje.setVrstaJela(EnumVrsteJela.DESERT);
		zaCuvanje.setKategorijaRecepta(EnumKategorijaRecepta.KOLACI);
		zaCuvanje.setOpisRecepta("opis");
		zaCuvanje.setKorisnik(k);
		zaCuvanje.setSastojci(sastojci);
		ServerskiOdgovor so = operacija.izvrsiKonkretnuOperaciju(zaCuvanje);
        Recept sacuvan=(Recept) so.getOdgovor();
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertEquals(zaCuvanje.getNaziv(),sacuvan.getNaziv());

	}

}
