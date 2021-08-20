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
import rs.ac.bg.fon.nprog.server.so.recept.IzmeniRecept;

class VratiSastojkeOdredjenogReceptaTest {

	private VratiSastojkeOdredjenogRecepta operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new VratiSastojkeOdredjenogRecepta();
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
		Korisnik k = new Korisnik(0, "Luka", "Antic", "x", "x");
		Recept r=new Recept(null, k, 1, null, null, null, null, null, null);

		ServerskiOdgovor so = operacija.izvrsiKonkretnuOperaciju(r);
        List<Sastojak> lista=(List<Sastojak>) so.getOdgovor();
        
		assertNotNull(so);
		assertNotNull(lista);
		assertTrue(so.isUspesno());

	}

}
