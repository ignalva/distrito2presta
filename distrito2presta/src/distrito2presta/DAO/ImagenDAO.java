package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Imagen;
import distrito2presta.Model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImagenDAO extends DAO {

	private static String SQLLOAD="SELECT EXCLUIRWEB, FOTO FROM FOTOGRAF WHERE CODARTICULO=? AND CODIGO=?";
	private static String SQLEXIST="SELECT CODIGO FROM FOTOGRAF WHERE CODARTICULO=? AND CODIGO=?";
	private static String SQLGET="SELECT CODIGO FROM FOTOGRAF WHERE CODARTICULO=? AND EXCLUIRWEB='F'";
	
	public ImagenDAO(Connection con) {
		super(con);
	}

	public boolean load(Imagen imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			boolean ok=false;

			stmt.setString(1, imagen.producto().reference);
			stmt.setInt(2, imagen.codigo());
			
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

	public boolean exist(Imagen imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLEXIST);
			stmt.setString(1, imagen.producto().reference);
			stmt.setInt(2, imagen.codigo());
			boolean exist=stmt.executeQuery().next();
			stmt.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+imagen+")");
		}
	}

	public List<Imagen> get(Producto producto) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLGET);
			stmt.setString(1, producto.reference);
			
			ResultSet rs = stmt.executeQuery();
			List<Imagen> imagenes = new ArrayList<Imagen>();
			while (rs.next()){
				Imagen imagen=new Imagen();
				imagen.producto(producto);
				imagen.codigo(rs.getInt("CODIGO"));
				imagen.excluirweb(false);
				imagenes.add(imagen);
			}
			
			rs.close();
			stmt.close();	
			return imagenes;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+producto+")");
		}
	}

}
