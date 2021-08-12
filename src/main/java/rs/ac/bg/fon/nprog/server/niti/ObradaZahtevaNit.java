package rs.ac.bg.fon.nprog.server.niti;

import rs.ac.bg.fon.nprog.server.controller.Controller;
import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.EnumVrsteJela;
import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rs.ac.bg.fon.nprog.library.konstante.Operacije;
import rs.ac.bg.fon.nprog.library.transfer.KlijentskiZahtev;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;

/**
 *
 * @author Nadia
 */
public class ObradaZahtevaNit extends Thread {

	private final Socket s;
	boolean kraj = false;
	private Korisnik korisnik = new Korisnik(-1, "neopoznato", "nepoznato", "nepoznato", "nepoznato");

	public ObradaZahtevaNit(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			while (!kraj) {
				KlijentskiZahtev kz = primiZahtev();
				ServerskiOdgovor so = new ServerskiOdgovor();

				switch (kz.getOperacija()) {
				case Operacije.PRIJAVI_KORISNIKA:
					Korisnik neulogovan = (Korisnik) kz.getParametar();
					Korisnik ulogovan;

					ulogovan = Controller.getInstance().login(neulogovan);
					so.setOdgovor(ulogovan);
					if (ulogovan == null) {
						so.setUspesno(false);
					} else {
						so.setUspesno(true);
					}
					break;
				case Operacije.ZAPAMTI_RECEPT:
					Recept receptNesacuvan = (Recept) kz.getParametar();
					Recept receptSacuvan;

					receptSacuvan = Controller.getInstance().sacuvajRecept(receptNesacuvan);

					if (receptSacuvan == null) {
						so.setUspesno(false);
					} else {
						so.setUspesno(true);
					}
					break;
				case Operacije.ZAPAMTI_SASTOJKE:
					ArrayList<Sastojak> sastojci = (ArrayList<Sastojak>) kz.getParametar();
					boolean uspesno = Controller.getInstance().sacuvajSastojke(sastojci);
					so.setOdgovor(uspesno);
					break;
				case Operacije.VRATI_RECEPTE:
					ArrayList<Recept> recepti = Controller.getInstance().vratiRecepte();
					if (recepti == null) {
						so.setUspesno(false);
					} else {
						so.setUspesno(true);
					}
					so.setOdgovor(recepti);
					break;
				case Operacije.VRATI_SASTOJKE_RECEPTA:
					Recept r = (Recept) kz.getParametar();
					ArrayList<Sastojak> sastojciRecepta = Controller.getInstance().vratiSastojkeRecepta(r);
//                        if (sastojciRecepta == null) {
//                            so.setUspesno(false);
//                        } else {
//                            so.setUspesno(true);
//                        }
					so.setOdgovor(sastojciRecepta);
					break;

				case Operacije.OBRISI_RECEPT:
					Recept recept = (Recept) kz.getParametar();
					boolean obrisano = Controller.getInstance().obrisiRecept(recept);
					if (obrisano) {
						so.setUspesno(true);
					}
					so.setOdgovor(obrisano);
					break;
				case Operacije.IZMENI_RECEPT:
					Recept receptZaIzmenu = (Recept) kz.getParametar();
					Recept izmenjen = Controller.getInstance().izmeniRecept(receptZaIzmenu);
					if (izmenjen != null) {
						so.setUspesno(true);
					} else {
						so.setUspesno(false);
					}
					so.setOdgovor(izmenjen);
					break;
				case Operacije.NADJI_RECEPT_PREMA_NAZIVU:
					String naziv = (String) kz.getParametar();
					List<Recept> receptiPoImenu = Controller.getInstance().nadjiReceptPoImenu(naziv);
					if (!receptiPoImenu.isEmpty()) {
						so.setOdgovor(receptiPoImenu);
						so.setUspesno(true);
					} else {
						so.setUspesno(false);
					}
					break;
				case Operacije.NADJI_RECEPT_PREMA_VREMENU_PRIPREME:
					EnumVremePripreme vremePripreme = (EnumVremePripreme) kz.getParametar();
					List<Recept> receptiPoVP = Controller.getInstance().nadjiReceptPoVP(vremePripreme);
					if (!receptiPoVP.isEmpty()) {
						so.setOdgovor(receptiPoVP);
						so.setUspesno(true);
					} else {
						so.setUspesno(false);
					}
					break;
				case Operacije.NADJI_RECEPT_PREMA_VRSTI_JELA:
					EnumVrsteJela vrstaJela = (EnumVrsteJela) kz.getParametar();
					List<Recept> receptiPoVJ = Controller.getInstance().nadjiReceptPoVJ(vrstaJela);
					if (!receptiPoVJ.isEmpty()) {
						so.setOdgovor(receptiPoVJ);
						so.setUspesno(true);
					} else {
						so.setUspesno(false);
					}
					break;
				case Operacije.IZBACI_KLIJENTA:
					so.setOdgovor(true);
					kraj = true;

					break;
					default: kraj = true;
					break;
						
				}
				posaljiOdgovor(so);

			}
		} catch (Exception ex) {
			Logger.getLogger(ObradaZahtevaNit.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public KlijentskiZahtev primiZahtev() {
		KlijentskiZahtev kz = new KlijentskiZahtev();

		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			kz = (KlijentskiZahtev) ois.readObject();
		} catch (Exception ex) {
			kraj = true;
			return null;
			// Logger.getLogger(ObradaZahtevaNit.class.getName()).log(Level.SEVERE, null,
			// ex);
		}

		return kz;
	}

	private void posaljiOdgovor(ServerskiOdgovor so) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(so);
		} catch (IOException ex) {
			kraj = true;
			// Logger.getLogger(ObradaZahtevaNit.class.getName()).log(Level.SEVERE, null,
			// ex);
		}

	}

	public void zaustavi() {
		kraj = true;

	}

}
