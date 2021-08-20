package rs.ac.bg.fon.nprog.server.so.korisnik;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;

class PrijavaKorisnikaTest {
    private PrijavaKorisnika operacija;
	@BeforeEach
	void setUp() throws Exception {
		operacija = new PrijavaKorisnika();
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
		Korisnik k=new Korisnik(0,"Luka","Antic","x","x");
		ServerskiOdgovor so  = operacija.izvrsiKonkretnuOperaciju(k);
		assertNotNull(so);
		assertEquals(k,so.getOdgovor());
		assertTrue(so.isUspesno());
	}

}
