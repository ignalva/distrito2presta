package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.StockCombinacionDAO;
import distrito2presta.Model.StockCombinacion;

public class StockCombinacionManager extends Manager {
	
	public void load(StockCombinacion stock) throws Exception {
		try{
			StockCombinacionDAO dao= new StockCombinacionDAO(pool.getConnection());
			dao.load(stock);
			dao.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+stock+")");
		}
	}

}
