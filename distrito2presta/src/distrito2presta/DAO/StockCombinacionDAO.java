package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.StockCombinacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockCombinacionDAO extends DAO {
	
	private static String SQLLOAD="SELECT CODARTICULO, CODCARACT, VALOR, SUM(STOCK1) TOTAL FROM CAREXIST WHERE CODARTICULO=? AND CODCARACT=? AND VALOR=? GROUP BY CODARTICULO, CODCARACT, VALOR";
	
	public StockCombinacionDAO(Connection con) {
		super(con);	
	}

	public void load(StockCombinacion stock) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, stock.combinacion().producto().reference);
			stmt.setInt(2, stock.combinacion().codcaract());
			stmt.setString(3, stock.combinacion().valorCaract());
			
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
