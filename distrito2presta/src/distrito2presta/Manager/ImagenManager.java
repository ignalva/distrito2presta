package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.IdiomaProductoDAO;
import distrito2presta.DAO.ImagenDAO;
import distrito2presta.Model.Imagen;
import distrito2presta.Model.Producto;

import java.util.List;

public class ImagenManager extends Manager {

	public boolean loadId(Imagen imagen) throws Exception{
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.loadImagenId(imagen);
			dao.close();
			
			Sincro.LOG.info(imagen.toString());
			
			return imagen.id!=null;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadId("+imagen+")");
		}
	}
	
	public boolean load(Imagen imagen) throws Exception{
		try{
			ImagenDAO dao= new ImagenDAO(pool.getConnection());
			boolean ok=dao.load(imagen);
			dao.close();
			
			if (!loadId(imagen) && imagen.excluirweb()) return false;

			Sincro.LOG.info(imagen.toString());

			return ok;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+imagen+")");
		}
	}
	
	public void updateId(Imagen imagen) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.updateIdImagen(imagen);
			dao.close();
			Sincro.LOG.info(imagen.toString());
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateId("+imagen+")");
		}
	}

	public void deleteId(Imagen imagen) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.deleteIdImagen(imagen);
			dao.close();
			Sincro.LOG.info(imagen.toString());
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteId("+imagen+")");
		}
	}

	public boolean exist(Imagen imagen) throws Exception {
		try{
			ImagenDAO dao= new ImagenDAO(pool.getConnection());
			boolean exist=dao.exist(imagen);
			dao.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+imagen+")");
		}
	}

	public List<Imagen> get(Producto producto) throws Exception {
		try{
			ImagenDAO dao= new ImagenDAO(pool.getConnection());
			List<Imagen> list = dao.get(producto);
			dao.close();
			return list;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+producto+")");
		}
	}

}
