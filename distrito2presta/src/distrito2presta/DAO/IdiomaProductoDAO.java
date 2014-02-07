package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.DeleteCombinacion;
import distrito2presta.Model.Imagen;
import distrito2presta.Model.ImagenCombinacion;
import distrito2presta.Model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * IDIOMAARTICULO DAO con IDs PrestaShop
 * NOMBRE=PRODUCTO.ID
 * DESCRIPCION=IMAGEN.ID (codigo=id)*
 * DESCRIPCIONCORTA=IMAGENCOMBINACION.ID (codigo=id)*
 * OBSERVACIONES=COMBINACION.ID (valorCaract=id)*
 * @author ignalva@gmail.com
 */

public class IdiomaProductoDAO extends DAO {

	private static String SQLLOAD="SELECT NOMBRE, DESCRIPCION, DESCRIPCIONCORTA, OBSERVACIONES FROM IDIOMAARTICULO WHERE CODIDIOMA=? AND CODARTICULO=?";
	private static String SQLUPDATENOMBRE="UPDATE IDIOMAARTICULO SET NOMBRE=? WHERE CODIDIOMA=? AND CODARTICULO=?";
	private static String SQLUPDATEDESCRIPCION="UPDATE IDIOMAARTICULO SET DESCRIPCION=? WHERE CODIDIOMA=? AND CODARTICULO=?";
	private static String SQLUPDATEOBSERVACIONES="UPDATE IDIOMAARTICULO SET OBSERVACIONES=? WHERE CODIDIOMA=? AND CODARTICULO=?";
	private static String SQLUPDATEDESCRIPCIONCORTA="UPDATE IDIOMAARTICULO SET DESCRIPCIONCORTA=? WHERE CODIDIOMA=? AND CODARTICULO=?";
	private static String SQLINSERT="INSERT INTO IDIOMAARTICULO (CODIDIOMA,CODARTICULO,NOMBRE) VALUES(?,?,?)";
	
	private static final String SEPAPARES=";";
	private static final String SEPAVALORES="=";

	public IdiomaProductoDAO(Connection con) {
		super(con);
	}

	private Map<String, Integer> string2map(String campo) throws Exception{
		try{
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (campo!=null && !campo.isEmpty()){
				String[] pares=campo.split(SEPAPARES);
				for(String par:pares){
					String[] valores=par.split(SEPAVALORES);
					if (valores.length==2) map.put(valores[0], Integer.parseInt(valores[1]));
				}
			}
			return map;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".string2map");
		}		
	}
	
	private String map2string(Map<String, Integer> map) throws Exception{
		try{
			String out="";
			Iterator<Entry<String, Integer>> iter=map.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Integer> entry=iter.next();
				if (!out.isEmpty()) out+=SEPAPARES;
				out+=entry.getKey()+SEPAVALORES+entry.getValue();
			}
			return out;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".map2string");
		}		
	}
	
	private Map<String, Integer> getMap(Producto producto, String campo) throws Exception{
		try{
			Map<String, Integer> map;
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, producto.reference);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) map=string2map(rs.getString(campo));
			else map=new HashMap<String, Integer>();
			rs.close();
			stmt.close();
			return map;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".getMap");
		}		
	}
	
	public void loadProductoId(Producto producto) throws Exception{
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, producto.reference);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) producto.setId(rs.getString("NOMBRE"));
			else Sincro.LOG.info(producto+" SIN ID!!");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadProductoId("+producto+")");
		}
	}
	

	public void loadImagenId(Imagen imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, imagen.producto().reference);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) imagen.id=string2map(rs.getString("DESCRIPCION")).get(Integer.toString(imagen.codigo()));
			else Sincro.LOG.info(imagen+" SIN ID!!");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadImagenId("+imagen+")");
		}		
	}

	public void updateIdImagen(Imagen imagen) throws Exception {
		try{
			Map<String, Integer> map=getMap(imagen.producto(),"DESCRIPCION");
			map.put(Integer.toString(imagen.codigo()), imagen.id);
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEDESCRIPCION);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, imagen.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateIdImagen("+imagen+")");
		}		
	}

	public void deleteIdImagen(Imagen imagen) throws Exception {
		try{
			Map<String, Integer> map=getMap(imagen.producto(),"DESCRIPCION");
			map.remove(Integer.toString(imagen.codigo()));
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEDESCRIPCION);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, imagen.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteIdImagen("+imagen+")");
		}		
	}

	public void updateIdProducto(Producto producto) throws Exception {
		try{
			PreparedStatement stmtINS = con.prepareStatement(SQLINSERT);
			PreparedStatement stmtUPD = con.prepareStatement(SQLUPDATENOMBRE);
		
			stmtUPD.setString(1, producto.id.toString());
			stmtUPD.setString(2, Sincro.LANGPRESTASHOP);
			stmtUPD.setString(3, producto.reference);
			
			if (stmtUPD.executeUpdate()<= 0){
				stmtINS.setString(1, Sincro.LANGPRESTASHOP);
				stmtINS.setString(2, producto.reference);
				stmtINS.setString(3, producto.id.toString());
				stmtINS.executeUpdate();
				stmtINS.close();	
			}
			stmtUPD.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateIdProducto("+producto+")");
		}
	}

	public void loadImagenCombinacionId(ImagenCombinacion imagen) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, imagen.producto().reference);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) imagen.id=string2map(rs.getString("DESCRIPCIONCORTA")).get(Integer.toString(imagen.codigo()));
			else Sincro.LOG.info(imagen+" SIN ID!!");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadImagenCombinacionId("+imagen+")");
		}
	}

	public void updateIdImagenCombinacion(ImagenCombinacion imagen) throws Exception {
		try{
			Map<String, Integer> map=getMap(imagen.producto(),"DESCRIPCIONCORTA");
			map.put(Integer.toString(imagen.codigo()), imagen.id);
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEDESCRIPCIONCORTA);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, imagen.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateIdImagenCombinacion("+imagen+")");
		}
	}

	public void deleteIdImagenCombinacion(ImagenCombinacion imagen) throws Exception {
		try{
			Map<String, Integer> map=getMap(imagen.producto(),"DESCRIPCIONCORTA");
			map.remove(Integer.toString(imagen.codigo()));
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEDESCRIPCIONCORTA);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, imagen.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteIdImagenCombinacion("+imagen+")");
		}
	}

	public void loadCombinacionId(Combinacion combinacion) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, combinacion.producto().reference);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) combinacion.id=string2map(rs.getString("OBSERVACIONES")).get(combinacion.valorCaract());
			else Sincro.LOG.info(combinacion+" SIN ID!!");
			rs.close();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadCombinacionId("+combinacion+")");
		}
	}

	public void updateIdCombinacion(Combinacion combinacion) throws Exception {
		try{
			Map<String, Integer> map=getMap(combinacion.producto(),"OBSERVACIONES");
			map.put(combinacion.valorCaract(), combinacion.id);
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEOBSERVACIONES);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, combinacion.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateIdCombinacion("+combinacion+")");
		}
	}

	public void deleteIdCombinacion(DeleteCombinacion combinacion) throws Exception {
		try{
			Map<String, Integer> map=getMap(combinacion.producto(),"OBSERVACIONES");
			map.remove(combinacion.valorCaract());
			
			PreparedStatement stmt = con.prepareStatement(SQLUPDATEOBSERVACIONES);
			stmt.setString(1, map2string(map));
			stmt.setString(2, Sincro.LANGPRESTASHOP);
			stmt.setString(3, combinacion.producto().reference);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteIdCombinacion("+combinacion+")");
		}
	}

	public Map<String, Integer> getImagenesCombinacion(Combinacion combinacion) throws Exception{
		return getMap(combinacion.producto(),"DESCRIPCIONCORTA");
	}
}
