package rs.ac.bg.fon.nprog.server.so.korisnik;

import java.util.List;

import rs.ac.bg.fon.nprog.library.domen.Korisnik;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class PrijavaKorisnika extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so = new ServerskiOdgovor();
		Korisnik neulogovan = (Korisnik) object;
		List<Korisnik> lista = DBBroker.getInstance().vratiKorisnike();
		Korisnik ulogovan=null;
		if (lista.isEmpty()) {
			so.setUspesno(false);
		} else {
			for (Korisnik k : lista) {
				if (neulogovan.getKorisnickoIme().equals(k.getKorisnickoIme())) {
					if (neulogovan.getLozinka().equals(k.getLozinka())) {
						ulogovan = k;
					}
				}
			}
		}
		if (ulogovan == null) {
			so.setOdgovor(ulogovan);
		} else {
			so.setUspesno(true);
		}
		so.setOdgovor(ulogovan);
		return so;
	}

}
