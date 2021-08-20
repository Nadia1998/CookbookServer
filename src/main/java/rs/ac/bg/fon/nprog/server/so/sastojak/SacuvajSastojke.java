package rs.ac.bg.fon.nprog.server.so.sastojak;

import java.util.List;

import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class SacuvajSastojke extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so = new ServerskiOdgovor();
		List<Sastojak> sastojci = (List<Sastojak>) object;
		if(sastojci==null || sastojci.isEmpty()) {
			so.setUspesno(false);
			so.setOdgovor(null);
		}
		for (Sastojak s : sastojci) {
			int sastojakID;
			sastojakID = DBBroker.getInstance().vratiIDSastojka();
			s.setSastojakId(sastojakID);
			DBBroker.getInstance().sacuvajSastojak(s);
		}
		so.setUspesno(true);
		so.setOdgovor(true);
		return so;
	}

}
