package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.StockDAO;
import distrito2presta.Model.Stock;

public class StockManager extends Manager {

	public void load(Stock stock) throws Exception {
		try{
			StockDAO dao= new StockDAO(pool.getConnection());
			dao.load(stock);
			dao.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+stock+")");
		}
	}
	
}
