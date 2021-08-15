
package rs.ac.bg.fon.nprog.server.CookbookServer;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import rs.ac.bg.fon.nprog.library.domen.EnumKategorijaRecepta;
import rs.ac.bg.fon.nprog.library.domen.EnumMera;
import rs.ac.bg.fon.nprog.library.domen.EnumNivoTezine;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import rs.ac.bg.fon.nprog.server.controller.Controller;
import rs.ac.bg.fon.nprog.server.db.DBBroker;

/**
 * Unit test for simple App.
 */
public class ControllerTest {

	DBBroker db = new DBBroker();

	@BeforeEach
	void setUp() throws Exception {
		db.driverUpload();
		db.connect();
	}

	@AfterEach
	void tearDown() throws Exception {
		db.disconnect();
	}

	@Test
	public void loginTest() throws Exception {
		Korisnik neulogovan = new Korisnik(0, "Luka", "Antic", "x", "x");
		Korisnik ulogovan = Controller.getInstance().login(neulogovan);
		assertNotNull(ulogovan);
		assertEquals(neulogovan, ulogovan);
	}

	@Test
	public void sacuvajReceptTest() throws Exception {
		Sastojak s=new Sastojak();
		s.setSastojakId(1);
		s.setKolicina(130);
		s.setNaziv("brasno");
		s.setMera(EnumMera.G);
		List<Sastojak> sastojci=new ArrayList<>();
		sastojci.add(s);
		Korisnik k = new Korisnik(0, "Luka", "Antic", "x", "x");
		Recept recept = new Recept(sastojci, k, 1, "Omlet", EnumVremePripreme.BRZO, EnumNivoTezine.HOBBY_KUVANJE,
				EnumVrsteJela.GLAVNO_JELO, EnumKategorijaRecepta.TRADICIONALNA_KUHINJA, "Zanimljivo jelo");
		Recept sacuvan = Controller.getInstance().sacuvajRecept(recept);
		assertNotNull(sacuvan);
		assertEquals(recept.getNaziv(), sacuvan.getNaziv());
		assertEquals(recept.getKorisnik().getKorisnikId(), sacuvan.getKorisnik().getKorisnikId());
	}

	@Test
	public void sacuvajSastojkeTest() throws Exception {
		Korisnik k = new Korisnik();
		k.setKorisnikId(0);
		Recept recept = new Recept(null, k, 1, "Omlet", EnumVremePripreme.BRZO, EnumNivoTezine.HOBBY_KUVANJE,
				EnumVrsteJela.GLAVNO_JELO, EnumKategorijaRecepta.TRADICIONALNA_KUHINJA, "Zanimljivo jelo");
		Sastojak s = new Sastojak(-1, "Mleko", 250, EnumMera.ML, recept);
		ArrayList<Sastojak> lista = new ArrayList<>();
		lista.add(s);
		assertTrue(Controller.getInstance().sacuvajSastojke(lista));

	}

	@Test
	public void vratirecepteTest() throws Exception {
		List<Recept> lista = new ArrayList<>();
		lista = Controller.getInstance().vratiRecepte();
		assertNotNull(lista);

	}

	@Test
	public void vratiSastojkeReceptaTest() throws Exception {
		List<Recept> lista = new ArrayList<>();
		lista = Controller.getInstance().vratiRecepte();
		ArrayList<Sastojak> sastojci = Controller.getInstance().vratiSastojkeRecepta(lista.get(0));
		assertNotNull(sastojci);

	}

	@Test
	public void obrisiReceptTest() throws Exception {
		List<Sastojak> sastojci=new ArrayList<>();
		Sastojak s = new Sastojak(-1, "Mleko", 250, EnumMera.ML, null);
		sastojci.add(s);
		Korisnik k = new Korisnik();
		k.setKorisnikId(0);
		Recept recept = new Recept(sastojci, k, 1, "Omlet", EnumVremePripreme.BRZO, EnumNivoTezine.HOBBY_KUVANJE,
				EnumVrsteJela.GLAVNO_JELO, EnumKategorijaRecepta.TRADICIONALNA_KUHINJA, "Zanimljivo jelo");
		Recept sacuvan = Controller.getInstance().sacuvajRecept(recept);
		boolean obrisan = Controller.getInstance().obrisiRecept(recept);
		assertEquals(obrisan, true);
		assertNotNull(sacuvan);

	}

	@Test
	public void izmeniReceptTest() throws Exception {
		Sastojak s=new Sastojak();
		s.setSastojakId(1);
		s.setKolicina(130);
		s.setNaziv("brasno");
		s.setMera(EnumMera.G);
		List<Sastojak> sastojci=new ArrayList<>();
		sastojci.add(s);
		Korisnik k = new Korisnik();
		k.setKorisnikId(0);
		Recept recept = new Recept(sastojci, k, 1, "Omlet", EnumVremePripreme.BRZO, EnumNivoTezine.HOBBY_KUVANJE,
				EnumVrsteJela.GLAVNO_JELO, EnumKategorijaRecepta.TRADICIONALNA_KUHINJA, "Zanimljivo jelo");
		Recept sacuvan = Controller.getInstance().sacuvajRecept(recept);
		Recept zaIzmenu = new Recept(null, k, 1, "Omlet", EnumVremePripreme.BRZO, EnumNivoTezine.POCETNICKO,
				EnumVrsteJela.GLAVNO_JELO, EnumKategorijaRecepta.TRADICIONALNA_KUHINJA, "Zanimljivo jelo");
		Recept izmenjen = Controller.getInstance().izmeniRecept(zaIzmenu);
		assertEquals(zaIzmenu.getReceptId(), izmenjen.getReceptId());
		assertNotNull(izmenjen);
	}

	@Test
	public void nadjiReceptPoImenuTest() throws Exception {
		String ime = "Kolač";
		List<Recept> recepti = Controller.getInstance().nadjiReceptPoImenu(ime);
		assertNotNull(recepti);
	}

	@Test
	public void nadjiReceptPoVPTest() throws Exception {
		EnumVremePripreme vp = EnumVremePripreme.BRZO;
		List<Recept> recepti = Controller.getInstance().nadjiReceptPoVP(vp);
		assertNotNull(recepti);
		assertEquals(recepti.get(1).getVremePripreme(), vp);
	}

	@Test
	public void nadjiReceptPoVJTest() throws Exception {
		EnumVrsteJela vj = EnumVrsteJela.DESERT;
		List<Recept> recepti = Controller.getInstance().nadjiReceptPoVJ(vj);
		assertNotNull(recepti);
		assertEquals(recepti.get(0).getVrstaJela(), vj);
	}

}
