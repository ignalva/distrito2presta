package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockDAO extends DAO {
	
	private static String SQLLOAD="SELECT CODARTICULO, SUM(STOCK1) TOTAL FROM EXISTENC WHERE CODARTICULO=? GROUP BY CODARTICULO";
	
	public StockDAO(Connection con) {
		super(con);
	}

	public void load(Stock stock) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, stock.producto().reference);
			
			ResultSet rs = stmt.executeQuery();
			stock.quantity=rs.next()?(int)rs.getDouble("TOTAL"):0;
			
			rs.close();
			stmt.close();	
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+stock+")");
		}
	}

}
