package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.Categoria;
import distrito2presta.Model.Feature;
import distrito2presta.Model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductoDAO extends DAO {
	
	private static String SQLLOAD="SELECT NOMBRE, DESCRIPCION, PRECIOVENTA, TIPOIVA, KILOS, BAJA, EXCLUIRWEB, DESCRIPCIONCORTA FROM ARTICULO WHERE CODIGO=?";
	private static String SQLRELACIONADOS="SELECT I.NOMBRE FROM ARTRELACIONADOS R INNER JOIN IDIOMAARTICULO I ON I.CODIDIOMA=? AND R.CODRELACIONADO=I.CODARTICULO WHERE R.CODARTICULO=?";
	private static String SQLFEATURES="SELECT IC.NOMBRE IDCARACT, ICV.VALORTRADUC IDFEATURE, C.CODCARACT, C.VALOR FROM CARVALOR C, IDIOMACARACT IC, IDIOMACARVALID ICV WHERE C.CODCLASE=ICV.CODCLASE AND C.CODCARACT=ICV.CODCARACT AND C.VALOR=ICV.VALORCARVALID AND IC.CODIDIOMA=ICV.CODIDIOMA AND IC.CODCLASE=ICV.CODCLASE AND IC.CODCARACT=ICV.CODCARACT AND ICV.CODIDIOMA=? AND ICV.CODCLASE=? AND C.CODOBJETO=? ORDER BY IDCARACT ASC, IDFEATURE ASC";
	private static String SQLFIESTAEVENTO="SELECT IT.DESCRIPCION ID FROM IDIOMATIPO IT, IDIOMACARVALID ICV, CARVALOR CV WHERE CV.VALOR=ICV.VALORCARVALID AND ICV.CODCLASE=CV.CODCLASE AND ICV.CODOBJETO IS NULL AND ICV.CODCARACT=CV.CODCARACT AND ICV.DIMENSION IS NULL AND ICV.VALORTRADUC=IT.METATITLE AND IT.CODIDIOMA=ICV.CODIDIOMA AND CV.CODCLASE=? AND CV.CODOBJETO=? AND ICV.CODIDIOMA=? AND IT.TIPO=?";
	private static String SQLFAMILIA="SELECT IT.DESCRIPCION ID FROM ARTICULO A INNER JOIN IDIOMATIPO IT ON A.CODFAMILIA=IT.CODTIPO AND IT.CODIDIOMA=? AND IT.TIPO=? WHERE A.CODIGO=?";
	
	public ProductoDAO(Connection con) {
		super(con);
	}

	public boolean load(Producto producto) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLLOAD);
			boolean ok=false;
			
			stmt.setString(1, producto.reference);

			ResultSet rs = stmt.executeQuery();
			if (ok=rs.next()){
				producto.nombre(rs.getString("NOMBRE"));
				producto.descripcion(rs.getString("DESCRIPCION")); 			//Test Blob!!
				producto.descripcionCorta(rs.getString("DESCRIPCIONCORTA"));//Test Blob!!
				producto.price=new BigDecimal(rs.getDouble("PRECIOVENTA")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				producto.id_tax_rules_group=rs.getInt("TIPOIVA")+1; 
				producto.weight=rs.getDouble("KILOS");
				String baja=rs.getString("BAJA");
				producto.active=baja!=null && baja.equals("T")?0:1;
				String excluirweb=rs.getString("EXCLUIRWEB");
				producto.visibility=excluirweb!=null && excluirweb.equals("T")?"none":"both";
			}
			else
				Sincro.LOG.warning("No existe "+ producto +"!!");
			
			rs.close();
			stmt.close();	
			return ok;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+producto+")");
		}
	}

	public boolean setRelacionados(Producto producto) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLRELACIONADOS);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setString(2, producto.reference);
			
			producto.associations.accesorios.clear();

			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				Producto relacionado=new Producto();
				relacionado.setId(rs.getString("NOMBRE"));
				producto.associations.accesorios.add(relacionado);			
			}
			
			rs.close();
			stmt.close();
			
			return true;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".setRelacionados("+producto+")");
		}
	}

	public boolean setFeatures(Producto producto) throws Exception {
		try{
			PreparedStatement stmt = con.prepareStatement(SQLFEATURES);
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setInt(2, Feature.CODCLASE());
			stmt.setString(3, producto.reference);
			
			producto.associations.features.clear();
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				Feature feature = new Feature();
				feature.setId(rs.getString("IDCARACT"));
				feature.codcaract(rs.getInt("CODCARACT"));
				feature.valor(rs.getString("VALOR"));
				feature.id_feature_value=Integer.parseInt(rs.getString("IDFEATURE"));
				if (feature.codcaract()==Sincro.FABRICANTES)
					producto.id_manufacturer=feature.id_feature_value;
				else
					producto.associations.features.add(feature);
			}

			rs.close();
			stmt.close();
			return true;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".setFeatures("+producto+")");
		}
	}
	
	private boolean setFamilia(Producto producto) throws Exception{
		try{
			PreparedStatement stmt = con.prepareStatement(SQLFAMILIA);	
			stmt.setString(1, Sincro.LANGPRESTASHOP);
			stmt.setInt(2, Categoria.TIPO());
			stmt.setString(3, producto.reference);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				Categoria categoria = new Categoria();
				categoria.setId(rs.getString("ID"));
				producto.associations.categorias.add(categoria);
				producto.id_category_default=categoria.id;
			}
			
			rs.close();
			stmt.close();
			return true;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".setFamilia("+producto+")");
		}
	}

	private boolean setFiestaEvento(Producto producto) throws Exception{
		try{
			PreparedStatement stmt = con.prepareStatement(SQLFIESTAEVENTO);	
			stmt.setInt(1, Feature.CODCLASE());
			stmt.setString(2, producto.reference);
			stmt.setString(3, Sincro.LANGPRESTASHOP);
			stmt.setInt(4, Categoria.TIPO());
			
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				Categoria categoria = new Categoria();
				categoria.setId(rs.getString("ID"));
				producto.associations.categorias.add(categoria);
			}
			
			rs.close();
			stmt.close();
			return true;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".setFiestaEvento("+producto+")");
		}
	}

	public boolean setCategories(Producto producto) throws Exception{
		return setFamilia(producto) && setFiestaEvento(producto);
	}
}
