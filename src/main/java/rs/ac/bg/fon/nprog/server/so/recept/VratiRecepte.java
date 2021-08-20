package rs.ac.bg.fon.nprog.server.so.recept;

import java.util.ArrayList;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class VratiRecepte extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		ArrayList<Recept> recepti;
		recepti = DBBroker.getInstance().vratiRecepte();
		if(recepti.isEmpty()) {
			so.setUspesno(false);
		}else {
			so.setOdgovor(recepti);
			so.setUspesno(true);
		}
		return so;
	}

}
