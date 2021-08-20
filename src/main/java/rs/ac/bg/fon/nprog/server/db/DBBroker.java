package rs.ac.bg.fon.nprog.server.db;

import rs.ac.bg.fon.nprog.library.domen.EnumKategorijaRecepta;
import rs.ac.bg.fon.nprog.library.domen.EnumMera;
import rs.ac.bg.fon.nprog.library.domen.EnumNivoTezine;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nadia
 */
public class DBBroker {
    private static DBBroker instance;
	Connection conn;

	public void driverUpload() throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
	}

	public void connect() throws SQLException {
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/cookbook1", "root", "wordpress");
	}

	public void disconnect() throws SQLException {
		conn.close();
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}
	public static DBBroker getInstance() {
		if(instance==null)
			instance=new DBBroker();
		return instance;
	}

	public int vratiIDRecepta() {
		int maxId = 0;
		String upit = " SELECT max(receptID) as maxId FROM recept";

		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(upit);
			while (rs.next()) {
				maxId = rs.getInt("maxId");
			}

		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("Nije nasao max");
		}

		return ++maxId;

	}

	public int vratiIDSastojka() {
		int maxId = 0;
		String upit = " SELECT max(sastojakID) as maxId FROM sastojak";

		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(upit);
			while (rs.next()) {
				maxId = rs.getInt("maxId");
			}

		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("Nije nasao max");
		}

		return ++maxId;

	}

	public List<Korisnik> vratiKorisnike() {
		List<Korisnik> lista = new ArrayList<>();
		String sql = "select * from korisnik";
		try {
			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				Korisnik korisnik = new Korisnik(rs.getInt("korisnikID"), rs.getString("ime"), rs.getString("prezime"),
						rs.getString("korisnickoIme"), rs.getString("lozinka"));
				lista.add(korisnik);

			}
			statement.close();
			rs.close();
			return lista;

		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
		}
		return lista;
	}

	public Recept sacuvajRecept(Recept receptNesacuvan) throws SQLException {
		String upit = "INSERT INTO recept VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(upit);
		ps.setInt(1, receptNesacuvan.getReceptId());
		ps.setString(2, receptNesacuvan.getNaziv());
		ps.setString(3, receptNesacuvan.getVremePripreme().toString());
		ps.setString(4, receptNesacuvan.getNivoTezine().toString());
		ps.setString(5, receptNesacuvan.getVrstaJela().toString());
		ps.setString(6, receptNesacuvan.getKategorijaRecepta().toString());
		ps.setString(7, receptNesacuvan.getOpisRecepta());
		ps.setInt(8, (int) receptNesacuvan.getKorisnik().getKorisnikId());
		List<Sastojak> sastojci = receptNesacuvan.getSastojci();

		try {
			ps.execute();
			for (Sastojak s : sastojci) {
				s.setRecept(receptNesacuvan);
				sacuvajSastojak(s);
			}

			System.out.println("upis je okej");

		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("Greska prilikom pravljenja recepta");
			return null;
		}

		return receptNesacuvan;
	}

	public void sacuvajSastojak(Sastojak s) throws SQLException {
		String upit = "INSERT INTO sastojak VALUES (?,?,?,?,?)";
		int idSastojka = vratiIDSastojka();
		s.setSastojakId(idSastojka);
		PreparedStatement ps = conn.prepareStatement(upit);
		ps.setInt(1, s.getSastojakId());
		ps.setString(2, s.getNaziv());
		ps.setInt(3, s.getKolicina());
		ps.setString(4, s.getMera().toString());
		ps.setInt(5, s.getRecept().getReceptId());
		ps.execute();
	}

	public ArrayList<Recept> vratiRecepte() {
		List<Recept> lista = new ArrayList<>();
		String upit = "SELECT * FROM recept r JOIN korisnik k ON r.korisnikID=k.korisnikID";
		Statement statement;

		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(upit);
			while (rs.next()) {
				Recept r = new Recept();
				r.setReceptId(rs.getInt("receptID"));
				r.setNaziv(rs.getString("naziv"));
				r.setVremePripreme(EnumVremePripreme.fromStringToEnum(rs.getString("vremePripreme")));
				r.setNivoTezine(EnumNivoTezine.fromStringToEnum(rs.getString("nivoTezine")));
				r.setVrstaJela(EnumVrsteJela.fromStringToEnum(rs.getString("vrstaJela")));
				r.setKategorijaRecepta(EnumKategorijaRecepta.fromStringToEnum(rs.getString("kategorijaRecepta")));
				r.setOpisRecepta(rs.getString("opisRecepta"));
				int idKorisnika = rs.getInt("korisnikID");

				Korisnik k = new Korisnik();
				k.setIme(rs.getString("ime"));
				k.setKorisnickoIme(rs.getString("prezime"));
				k.setPrezime(rs.getString("korisnickoIme"));
				k.setLozinka(rs.getString("lozinka"));

				r.setKorisnik(k);
				lista.add(r);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
		}
		return (ArrayList<Recept>) lista;
	}

	public ArrayList<Sastojak> vratiSastojkeRecepta(Recept r) {
		ArrayList<Sastojak> sastojci = new ArrayList<>();

		String upit = "SELECT * FROM sastojak WHERE receptID=" + r.getReceptId();
		Statement statement;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(upit);

			while (rs.next()) {
				Sastojak s = new Sastojak();
				s.setSastojakId(rs.getInt("sastojakID"));
				s.setNaziv(rs.getString("naziv"));
				s.setKolicina(rs.getInt("kolicina"));
				s.setMera(EnumMera.valueOf(rs.getString("mera")));
				s.setRecept(r);
				sastojci.add(s);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
		}
		return sastojci;
	}

	public boolean obrisiRecept(Recept r) throws Exception {
		boolean uspesno = false;
		obrisiSastojkeRecepta(r.getReceptId());
		String upit = "DELETE FROM recept WHERE receptID=?";
		try {
			PreparedStatement stat = conn.prepareStatement(upit);
			stat.setInt(1, r.getReceptId());
			stat.executeUpdate();
			// resetujID();
			uspesno = true;
		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);

			throw new Exception("SQL greska");
		}
		return uspesno;
	}

	public boolean obrisiSastojkeRecepta(int receptID) throws Exception {
		boolean uspesno = false;
		String upit = "DELETE from sastojak where receptID=?";
		try {
			PreparedStatement stat = conn.prepareStatement(upit);
			stat.setInt(1, receptID);
			stat.executeUpdate();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
			throw new Exception("SQL greska");
		}
	}

	public Recept izmeniRecept(Recept receptZaIzmenu) throws Exception {
		Recept izmenjen = new Recept();
		try {
			List<Sastojak> lista = receptZaIzmenu.getSastojci();
			Statement stat = conn.createStatement();
			String upit1 = "UPDATE recept SET vremePripreme ='" + receptZaIzmenu.getVremePripreme().toString()
					+ "' WHERE receptID =" + receptZaIzmenu.getReceptId();
			String upit2 = "UPDATE recept SET nivoTezine ='" + receptZaIzmenu.getNivoTezine().toString()
					+ "' WHERE receptID =" + receptZaIzmenu.getReceptId();
			String upit3 = "UPDATE recept SET vrstaJela ='" + receptZaIzmenu.getVrstaJela().toString()
					+ "' WHERE receptID =" + receptZaIzmenu.getReceptId();
			String upit4 = "UPDATE recept SET kategorijaRecepta ='" + receptZaIzmenu.getKategorijaRecepta().toString()
					+ "' WHERE receptID =" + receptZaIzmenu.getReceptId();
			String upit5 = "UPDATE recept SET opisRecepta ='" + receptZaIzmenu.getOpisRecepta() + "' WHERE receptID ="
					+ receptZaIzmenu.getReceptId();
			obrisiSastojkeRecepta(receptZaIzmenu.getReceptId());

			if (lista != null) {
				for (Sastojak s : lista) {
					sacuvajSastojak(s);
				}
			}
			stat.executeUpdate(upit1);
			stat.executeUpdate(upit2);
			stat.executeUpdate(upit3);
			stat.executeUpdate(upit4);
			stat.executeUpdate(upit5);
			izmenjen = receptZaIzmenu;
		} catch (SQLException ex) {
			Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);

			throw new Exception("SQL greska");
		}
		return izmenjen;
	}

	public Korisnik vratiKorisnikaPremaID(int id) throws Exception {
		List<Korisnik> korisnici = vratiKorisnike();
		for (Korisnik k : korisnici) {
			if (k.getKorisnikId() == id) {
				return k;
			}
		}
		throw new Exception("Nema korisnika sa tim imenom");
	}

	public ArrayList<Recept> filterPremaNazivu(String naziv) throws SQLException, Exception {
		ArrayList<Recept> receptiPremaNazivu = new ArrayList<>();
		String upit = "SELECT * FROM recept r WHERE r.naziv LIKE '%" + naziv
				+ "%'";
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(upit);
		while (rs.next()) {
			Recept r = new Recept();
			r.setReceptId(rs.getInt("r.receptID"));
			r.setNaziv(rs.getString("r.naziv"));
			r.setVremePripreme(EnumVremePripreme.fromStringToEnum(rs.getString("r.vremePripreme")));
			r.setNivoTezine(EnumNivoTezine.fromStringToEnum(rs.getString("r.nivoTezine")));
			r.setVrstaJela(EnumVrsteJela.fromStringToEnum(rs.getString("r.vrstaJela")));
			r.setKategorijaRecepta(EnumKategorijaRecepta.fromStringToEnum(rs.getString("r.kategorijaRecepta")));
			r.setOpisRecepta(rs.getString("r.opisRecepta"));
			Korisnik k = vratiKorisnikaPremaID(rs.getInt("r.korisnikID"));
			r.setKorisnik(k);
			List<Sastojak> sastojci = vratiSastojkeRecepta(r);
			r.setSastojci(sastojci);
			receptiPremaNazivu.add(r);
		}
		if (receptiPremaNazivu.isEmpty()) {
			throw new Exception("Nema recepta sa tim imenom");
		}
		return receptiPremaNazivu;

	}

	public ArrayList<Recept> filterPremaVP(EnumVremePripreme vremePripreme) throws SQLException, Exception {
		ArrayList<Recept> receptiPremaVP = new ArrayList<>();
		String upit = "SELECT * FROM recept r WHERE r.vremePripreme LIKE '%"
				+ vremePripreme.toString() + "%'";
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(upit);
		while (rs.next()) {
			Recept r = new Recept();
			r.setReceptId(rs.getInt("r.receptID"));
			r.setNaziv(rs.getString("r.naziv"));
			r.setVremePripreme(EnumVremePripreme.fromStringToEnum(rs.getString("r.vremePripreme")));
			r.setNivoTezine(EnumNivoTezine.fromStringToEnum(rs.getString("r.nivoTezine")));
			r.setVrstaJela(EnumVrsteJela.fromStringToEnum(rs.getString("r.vrstaJela")));
			r.setKategorijaRecepta(EnumKategorijaRecepta.fromStringToEnum(rs.getString("r.kategorijaRecepta")));
			r.setOpisRecepta(rs.getString("r.opisRecepta"));
			Korisnik k = vratiKorisnikaPremaID(rs.getInt("r.korisnikID"));
			r.setKorisnik(k);
			List<Sastojak> sastojci = vratiSastojkeRecepta(r);
			r.setSastojci(sastojci);
			receptiPremaVP.add(r);
		}
		if (receptiPremaVP.isEmpty()) {
			throw new Exception("Nema recepta sa tim vremenom pripreme");
		}
		return receptiPremaVP;
	}

	public ArrayList<Recept> filterPremaVJ(EnumVrsteJela vrstaJela) throws SQLException, Exception {
		ArrayList<Recept> receptiPremaVJ = new ArrayList<>();
		String upit = "SELECT * FROM recept r WHERE r.vrstaJela LIKE '"+ vrstaJela.toString() + "'";
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(upit);
		while (rs.next()) {
			Recept r = new Recept();
			r.setReceptId(rs.getInt("r.receptID"));
			r.setNaziv(rs.getString("r.naziv"));
			r.setVremePripreme(EnumVremePripreme.fromStringToEnum(rs.getString("r.vremePripreme")));
			r.setNivoTezine(EnumNivoTezine.fromStringToEnum(rs.getString("r.nivoTezine")));
			r.setVrstaJela(EnumVrsteJela.fromStringToEnum(rs.getString("r.vrstaJela")));
			r.setKategorijaRecepta(EnumKategorijaRecepta.fromStringToEnum(rs.getString("r.kategorijaRecepta")));
			r.setOpisRecepta(rs.getString("r.opisRecepta"));
			Korisnik k = vratiKorisnikaPremaID(rs.getInt("r.korisnikID"));
			r.setKorisnik(k);
			List<Sastojak> sastojci = vratiSastojkeRecepta(r);
			r.setSastojci(sastojci);
			receptiPremaVJ.add(r);
		}
		rs.close();
		s.close();
		System.out.println(receptiPremaVJ.size());
		if (receptiPremaVJ.isEmpty()) {
			throw new Exception("Nema recepta koje spada u tu vrstu jela");
		}
		return receptiPremaVJ;
	}
}