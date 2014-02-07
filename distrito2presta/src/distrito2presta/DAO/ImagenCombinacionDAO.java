package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.ImagenCombinacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImagenCombinacionDAO extends DAO {
	
	private static String SQLLOAD="SELECT EXCLUIRWEB, FOTO FROM CARVALIDFOTO WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=? AND CODIGO=?";
	private static String SQLEXIST="SELECT CODIGO FROM CARVALIDFOTO WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=? AND CODIGO=?";
	private static String SQLGET="SELECT CODIGO FROM CARVALIDFOTO WHERE CODARTICULO=? AND CODCARACT=? AND VALORCARACT=? AND EXCLUIRWEB='F'";
	
	public ImagenCombinacionDAO(Connection con) {
		super(con);
	}

	public boolean load(ImagenCombinacion imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			boolean ok=false;

			stmt.setString(1, imagen.producto().reference);
			stmt.setInt(2, imagen.combinacion().codcaract());
			stmt.setString(3, imagen.combinacion().valorCaract());
			stmt.setInt(4, imagen.codigo());
			
			ResultSet rs = stmt.executeQuery();
			if (ok=rs.next()){
				String excluirweb=rs.getString("EXCLUIRWEB");
				imagen.excluirweb(excluirweb!=null && excluirweb.equals("T"));
				imagen.save(rs.getBlob("FOTO"));
			}
			else
				Sincro.LOG.warning("No existe "+ imagen +"!!");
			
			rs.close();
			stmt.close();	
			return ok;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+imagen+")");
		}
	}

	public boolean exist(ImagenCombinacion imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLEXIST);
			stmt.setString(1, imagen.producto().reference);
			stmt.setInt(2, imagen.combinacion().codcaract());
			stmt.setString(3, imagen.combinacion().valorCaract());
			stmt.setInt(4, imagen.codigo());

			boolean exist=stmt.executeQuery().next();
			stmt.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+imagen+")");
		}
	}

	public List<ImagenCombinacion> get(Combinacion combinacion) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLGET);
			stmt.setString(1, combinacion.producto().reference);
			stmt.setInt(2, combinacion.codcaract());
			stmt.setString(3, combinacion.valorCaract());
			
			List<ImagenCombinacion> list=new ArrayList<ImagenCombinacion>();
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) list.add(new ImagenCombinacion(combinacion,rs.getInt("CODIGO")));
			
			rs.close();
			stmt.close();	
			return list;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+combinacion+")");
		}
	}

}
