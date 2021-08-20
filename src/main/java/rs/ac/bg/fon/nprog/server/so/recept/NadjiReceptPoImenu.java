package rs.ac.bg.fon.nprog.server.so.recept;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class NadjiReceptPoImenu extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		ArrayList<Recept> recepti = new ArrayList<>();
		String naziv=(String) object;
		if(naziv==null) {
			throw new Exception("Nije iskucan naziv");
		}
		recepti = DBBroker.getInstance().filterPremaNazivu(naziv);
		if(recepti==null || recepti.isEmpty()) {
			so.setUspesno(false);
		}else {
			so.setUspesno(true);
			so.setOdgovor(recepti);
		}
		return so;
	}

}
