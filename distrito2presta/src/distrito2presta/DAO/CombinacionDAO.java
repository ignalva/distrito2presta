package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.Producto;
import distrito2presta.Model.ValorCaracteristica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CombinacionDAO extends DAO {
	
	private static String SQLLOAD="SELECT PRECIO FROM VENTA WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=? AND CODTARIFA IS NULL";
	private static String SQLEXIST="SELECT CODCARACT FROM VENTA WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=? AND CODTARIFA IS NULL UNION SELECT CODCARACT FROM CAREXIST WHERE CODARTICULO=? AND CODCARACT=? AND VALOR=? UNION SELECT CODCARACT FROM CARVALIDFOTO WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=?";
	private static String SQLGET="SELECT VALORCARACT FROM VENTA WHERE CODARTICULO=? AND CODCARACT=? AND CODTARIFA IS NULL UNION SELECT VALOR VALORCARACT FROM CAREXIST WHERE CODARTICULO=? AND CODCARACT=? UNION SELECT VALORCARACT FROM CARVALIDFOTO WHERE CODARTICULO=? AND CODCARACT=? ORDER BY 1 ASC";
	private static String SQLGETCODCARACT="SELECT CODCARACT FROM CARVALID WHERE CODOBJETO=? AND CODCLASE=?";
	
	public CombinacionDAO(Connection con) {
		super(con);
	}

	public void load(Combinacion combinacion) throws Exception{
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			
			stmt.setString(1, combinacion.producto().reference);
			stmt.setInt(2, combinacion.codcaract());
			stmt.setString(3, combinacion.valorCaract());
			
			ResultSet rs = stmt.executeQuery();
			combinacion.price=rs.next()?new BigDecimal(rs.getDouble("PRECIO")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue():null;
			
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+combinacion+")");
		}
	}

	private int getCodCaract(Producto producto) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLGETCODCARACT);
			stmt.setString(1, producto.reference);
			stmt.setInt(2, ValorCaracteristica.CODCLASE());
			ResultSet rs = stmt.executeQuery();
			int codcaract=rs.next()?rs.getInt("CODCARACT"):0;
			rs.close();
			stmt.close();
			return codcaract;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".getCodCaract("+producto+")");
		}
	}
	
	public boolean exist(Combinacion combinacion) throws Exception {
		try{
			int codcaract=getCodCaract(combinacion.producto());
			
			if (codcaract==0) return false;
			
			PreparedStatement stmt = con.prepareStatement(SQLEXIST);
			stmt.setString(1, combinacion.producto().reference);
			stmt.setInt(2, combinacion.codcaract());
			stmt.setString(3, combinacion.valorCaract());
			stmt.setString(4, combinacion.producto().reference);
			stmt.setInt(5, combinacion.codcaract());
			stmt.setString(6, combinacion.valorCaract());
			stmt.setString(7, combinacion.producto().reference);
			stmt.setInt(8, combinacion.codcaract());
			stmt.setString(9, combinacion.valorCaract());
			
			ResultSet rs = stmt.executeQuery();
			boolean exist=rs.next()?rs.getInt("CODCARACT")==codcaract:false;
			
			rs.close();
			stmt.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+combinacion+")");
		}
	}

	public List<Combinacion> get(Producto producto) throws Exception {
		try{
			List<Combinacion> combinaciones = new ArrayList<Combinacion>();
			int codcaract=getCodCaract(producto);
			
			if (codcaract==0) return combinaciones;
			
			PreparedStatement stmt = con.prepareStatement(SQLGET);
			stmt.setString(1, producto.reference);
			stmt.setInt(2, codcaract);
			stmt.setString(3, producto.reference);
			stmt.setInt(4, codcaract);
			stmt.setString(5, producto.reference);
			stmt.setInt(6, codcaract);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				combinaciones.add(new Combinacion(producto,codcaract,rs.getString("VALORCARACT")));
			
			rs.close();
			stmt.close();
			return combinaciones;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+producto+")");
		}
	}
	
}
