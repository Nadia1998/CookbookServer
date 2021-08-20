package rs.ac.bg.fon.nprog.server.so.recept;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class SacuvajRecept extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		Recept receptNesacuvan=(Recept) object;
		int receptId = DBBroker.getInstance().vratiIDRecepta();
		receptNesacuvan.setReceptId(receptId);
		Recept sacuvan = DBBroker.getInstance().sacuvajRecept(receptNesacuvan);
		if(sacuvan!=null) {
			so.setUspesno(true);
			so.setOdgovor(sacuvan);
		} else {
			so.setUspesno(false);
			so.setOdgovor(null);
		}
		return so;
	}

}
