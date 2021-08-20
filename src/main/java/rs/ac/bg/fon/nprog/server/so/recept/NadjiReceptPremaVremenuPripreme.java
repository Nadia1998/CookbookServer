package rs.ac.bg.fon.nprog.server.so.recept;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import rs.ac.bg.fon.nprog.library.domen.EnumVremePripreme;
import rs.ac.bg.fon.nprog.library.domen.Recept;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.controller.Controller;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
import rs.ac.bg.fon.nprog.server.so.OpstaSO;

public class NadjiReceptPremaVremenuPripreme extends OpstaSO {

	@Override
	protected ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception {
		ServerskiOdgovor so=new ServerskiOdgovor();
		ArrayList<Recept> recepti = new ArrayList<>();
		EnumVremePripreme vp = (EnumVremePripreme) object;
		recepti = DBBroker.getInstance().filterPremaVP(vp);
        if(recepti==null) {
        	so.setUspesno(false);
        }else {
        	so.setUspesno(true);
        }
		so.setOdgovor(recepti);
		return so;
	}

}
