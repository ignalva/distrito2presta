package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.IdiomaProductoDAO;
import distrito2presta.DAO.ImagenCombinacionDAO;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.ImagenCombinacion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ImagenCombinacionManager extends Manager {
	
	public boolean loadId(ImagenCombinacion imagen) throws Exception{
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.loadImagenCombinacionId(imagen);
			dao.close();
			
			Sincro.LOG.info(imagen.toString());
			
			return imagen.id!=null;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadId("+imagen+")");
		}
	}
	
	public boolean load(ImagenCombinacion imagen) throws Exception{
		try{
			ImagenCombinacionDAO dao= new ImagenCombinacionDAO(pool.getConnection());
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
	
	public void loadImagenes(Combinacion combinacion) throws Exception{
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			Map<String, Integer> map=dao.getImagenesCombinacion(combinacion);
			dao.close();
			
			combinacion.associations.imagenes.clear();
			
			Iterator<Entry<String, Integer>> iter=map.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Integer> entry=iter.next();
				ImagenCombinacion imagen=new ImagenCombinacion(combinacion,Integer.parseInt(entry.getKey()));
				imagen.id=entry.getValue();
				combinacion.associations.imagenes.add(imagen);
			}			
			
			Sincro.LOG.info(combinacion.toString());
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadImagenes("+combinacion+")");
		}
	}
	
	public void updateId(ImagenCombinacion imagen) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.updateIdImagenCombinacion(imagen);
			dao.close();
			Sincro.LOG.info(imagen.toString());
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateId("+imagen+")");
		}
	}

	public void deleteId(ImagenCombinacion imagen) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.deleteIdImagenCombinacion(imagen);
			dao.close();
			Sincro.LOG.info(imagen.toString());
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteId("+imagen+")");
		}
	}

	public boolean exist(ImagenCombinacion imagen) throws Exception {
		try{
			ImagenCombinacionDAO dao= new ImagenCombinacionDAO(pool.getConnection());
			boolean exist=dao.exist(imagen);
			dao.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+imagen+")");
		}
	}

	public List<ImagenCombinacion> get(Combinacion combinacion) throws Exception {
		try{
			ImagenCombinacionDAO dao= new ImagenCombinacionDAO(pool.getConnection());
			List<ImagenCombinacion> list=dao.get(combinacion);
			dao.close();
			return list;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+combinacion+")");
		}
	}

}
