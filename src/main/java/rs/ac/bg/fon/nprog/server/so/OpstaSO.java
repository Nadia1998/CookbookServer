package rs.ac.bg.fon.nprog.server.so;

import java.sql.SQLException;
import java.util.logging.Level;
import rs.ac.bg.fon.nprog.library.transfer.ServerskiOdgovor;
import rs.ac.bg.fon.nprog.server.db.DBBroker;
/**
 * Ovo je apstraktna klasa koju nasledjuju sve SO.
 * @author Nadia
 *
 */
public abstract class OpstaSO {
	

	/**
	 * Otvara konekciju sa bazom, izvrsava konkretnu operaciju .
	 * 
	 * @param object Objekat nad kojim se izvrsava operacija kao Object.
	 * @return Odgovor od servera kao ServerskiOdgovor.
	 * 
	 */
	


	public ServerskiOdgovor izvrsiOperaciju(Object object) {
		ServerskiOdgovor so = new ServerskiOdgovor();
		try {
			DBBroker.getInstance().driverUpload();
			DBBroker.getInstance().connect();
			so = izvrsiKonkretnuOperaciju(object);

		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(OpstaSO.class.getName()).log(Level.SEVERE, null, ex);
			so.setUspesno(false);
			so.setPoruka(ex.getMessage());

		} finally {
			try {
				DBBroker.getInstance().disconnect();
			} catch (SQLException ex) {
				java.util.logging.Logger.getLogger(OpstaSO.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return so;
	}

	/**
	 * Metoda koja je zasluzna za izvrsavanje konkretne sistemske operacije.
	 * 
	 * @param object Objekat kao Object.
	 * @return Odgovor od servera kao ServerskiOdgovor.
	 * @throws java.lang.Exception ukoliko dodje do greske u toku izvrsavanja
	 *                             operacije.
	 */

	protected abstract ServerskiOdgovor izvrsiKonkretnuOperaciju(Object object) throws Exception;

}
