package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.ValorCaracteristica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValorCaracteristicaDAO extends DAO {

	private static String SQLLOAD="SELECT VALORTRADUC FROM IDIOMACARVALID WHERE CODIDIOMA=? AND CODCLASE=? AND CODOBJETO IS NULL AND CODCARACT=? AND DIMENSION=? AND VALORCARVALID=?";
	
	public ValorCaracteristicaDAO(Connection con) {
		super(con);
	}

	public void load(ValorCaracteristica valor) throws Exception{
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);			
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setInt(2, ValorCaracteristica.CODCLASE());
			stmt.setInt(3, valor.codcaract());
			stmt.setInt(4, valor.dimension());
			stmt.setString(5, valor.valor());
			
			ResultSet rs = stmt.executeQuery();			
			if(rs.next()) 
				valor.setId(rs.getString("VALORTRADUC"));
			else
				Sincro.LOG.warning("No existe "+ valor +"!!");

			rs.close();
			stmt.close();	
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+valor+")");
		}
	}

}
