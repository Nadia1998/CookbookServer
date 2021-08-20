package rs.ac.bg.fon.nprog.server.so.recept;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;

class NadjiReceptPremaVremenuPripremeTest {

	private NadjiReceptPremaVremenuPripreme operacija;

	@BeforeEach
	void setUp() throws Exception {
		operacija = new NadjiReceptPremaVremenuPripreme();
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
		EnumVremePripreme vp=EnumVremePripreme.BRZO;
		ServerskiOdgovor so=operacija.izvrsiKonkretnuOperaciju(vp);
		List<Recept> recepti=(List<Recept>) so.getOdgovor();
		
		assertNotNull(so);
		assertTrue(so.isUspesno());
		assertNotNull(recepti);
		
	}


}
