package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.ValorCaracteristicaDAO;
import distrito2presta.Model.ValorCaracteristica;

public class ValorCaracteristicaManager extends Manager {

	public boolean load(ValorCaracteristica valor) throws Exception {
		try {
			ValorCaracteristicaDAO dao = new ValorCaracteristicaDAO(pool.getConnection());
			dao.load(valor);
			dao.close();
			
			Sincro.LOG.info(valor.toString());

			return valor.id!=null;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+valor+")");
		}
	}


}
