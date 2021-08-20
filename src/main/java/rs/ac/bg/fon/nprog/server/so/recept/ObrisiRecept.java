package rs.ac.bg.fon.nprog.server.so.recept;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class ObrisiRecept extends OpstaSO {
   
	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		Recept receptZaBrisanje=(Recept) object;
		boolean uspesno = DBBroker.getInstance().obrisiRecept(receptZaBrisanje);
		if(uspesno) {
			so.setUspesno(true);
			so.setOdgovor(uspesno);
		}else {
			so.setUspesno(false);
			so.setOdgovor(uspesno);
		}
		return so;
	}
	

}
