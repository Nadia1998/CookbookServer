package rs.ac.bg.fon.nprog.server.so.recept;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.korisnik.PrijavaKorisnika;

class NadjiReceptPoImenuTest {
	private NadjiReceptPoImenu operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new NadjiReceptPoImenu();
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
		String ime="limunom";
		ServerskiOdgovor so=operacija.izvrsiKonkretnuOperaciju(ime);
		List<Recept> recepti=(List<Recept>) so.getOdgovor();
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertNotNull(recepti);
		
	}

}
