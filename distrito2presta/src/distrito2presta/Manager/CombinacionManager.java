package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.CombinacionDAO;
import distrito2presta.DAO.IdiomaProductoDAO;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.DeleteCombinacion;
import distrito2presta.Model.Model;
import distrito2presta.Model.Producto;
import distrito2presta.Model.StockCombinacion;
import distrito2presta.Model.ValorCaracteristica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CombinacionManager extends Manager {
	
	public boolean loadId(Combinacion combinacion) throws Exception{
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.loadCombinacionId(combinacion);
			dao.close();
			
			Sincro.LOG.info(combinacion.toString());
			
			return combinacion.id!=null;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".loadId("+combinacion+")");
		}
	}

	public boolean load(Combinacion combinacion) throws Exception {
		try{	
			loadId(combinacion);
			
			CombinacionDAO dao = new CombinacionDAO(pool.getConnection());
			dao.load(combinacion); 
			dao.close();
			
			if (combinacion.price==null) 
				if (combinacion.producto().price!=null)
					combinacion.price=combinacion.producto().price;
				else{
					Sincro.LOG.warning(combinacion+" sin precio!!");
					return false;
				}
						
			ListIterator<ValorCaracteristica> iter = combinacion.associations.valores.listIterator();
			while (iter.hasNext()){
				ValorCaracteristica valor=(ValorCaracteristica) Sincro.DB.get(iter.next());
				if (valor!=null) iter.set(valor); else return false;
			}
						
			new ImagenCombinacionManager().loadImagenes(combinacion);
			
			Sincro.LOG.info(combinacion.toString());

			return true;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+combinacion+")");
		}
	}

	public boolean exist(Combinacion combinacion) throws Exception {
		try{
			CombinacionDAO dao = new CombinacionDAO(pool.getConnection());
			boolean exist=dao.exist(combinacion);
			dao.close();
			return exist;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".exist("+combinacion+")");
		}
	}

	public void updateId(Combinacion combinacion) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.updateIdCombinacion(combinacion);
			dao.close();			
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateId("+combinacion+")");
		}		
	}

	public void deleteId(DeleteCombinacion combinacion) throws Exception {
		try{
			IdiomaProductoDAO dao= new IdiomaProductoDAO(pool.getConnection());
			dao.deleteIdCombinacion(combinacion);
			dao.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteId("+combinacion+")");
		}				
	}

	public List<Model> get(Producto producto) throws Exception {
		try{
			List<Model> list = new ArrayList<Model>();
			
			CombinacionDAO dao = new CombinacionDAO(pool.getConnection());
			Iterator<Combinacion> iter=dao.get(producto).iterator();
			dao.close();

			while (iter.hasNext()){
				Combinacion combinacion=iter.next();
				list.add(combinacion);
				list.add(new StockCombinacion(combinacion));
				list.addAll(new ImagenCombinacionManager().get(combinacion));
			}
			
			return list;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+producto+")");
		}				
	}

}
