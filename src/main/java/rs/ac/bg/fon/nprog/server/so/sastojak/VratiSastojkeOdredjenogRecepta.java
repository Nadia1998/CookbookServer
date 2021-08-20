package rs.ac.bg.fon.nprog.server.so.sastojak;

import java.util.ArrayList;

import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.domen.Sastojak;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class VratiSastojkeOdredjenogRecepta extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		Recept odredjen=(Recept) object;
		ArrayList<Sastojak> sastojci=new ArrayList<>();
		sastojci = DBBroker.getInstance().vratiSastojkeRecepta(odredjen);
		if(sastojci==null || sastojci.isEmpty()) {
			so.setUspesno(false);
		} else {
			so.setUspesno(true);
			so.setOdgovor(sastojci);
		}
		return so;
	}

}
