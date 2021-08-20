package rs.ac.bg.fon.nprog.server.so.recept;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;

class NadjiReceptPremaVrstiJelaTest {

	private NadjiReceptPremaVrstiJela operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new NadjiReceptPremaVrstiJela();
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
		EnumVrsteJela vj=EnumVrsteJela.DESERT;
		ServerskiOdgovor so=operacija.izvrsiKonkretnuOperaciju(vj);
		List<Recept> recepti=(List<Recept>) so.getOdgovor();
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertNotNull(recepti);
		
	}

}
