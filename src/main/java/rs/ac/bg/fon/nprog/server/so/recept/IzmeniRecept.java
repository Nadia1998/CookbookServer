package rs.ac.bg.fon.nprog.server.so.recept;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class IzmeniRecept extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		Recept receptZaIzmenu=(Recept) object;
		Recept izmenjen = DBBroker.getInstance().izmeniRecept(receptZaIzmenu);
		if (izmenjen == null) {
			so.setUspesno(false);
		} else {
			so.setUspesno(true);
		}
		so.setOdgovor(izmenjen);
		return so;
	}

}
