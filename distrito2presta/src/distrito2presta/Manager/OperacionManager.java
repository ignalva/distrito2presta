package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.OperacionDAO;
import distrito2presta.Model.Combinacion;
import distrito2presta.Model.DeleteCombinacion;
import distrito2presta.Model.DeleteImagen;
import distrito2presta.Model.DeleteImagenCombinacion;
import distrito2presta.Model.DeleteProducto;
import distrito2presta.Model.Feature;
import distrito2presta.Model.Imagen;
import distrito2presta.Model.ImagenCombinacion;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.Model.OperacionDistrito;
import distrito2presta.Model.OperacionPresta;
import distrito2presta.Model.Producto;
import distrito2presta.Model.Stock;
import distrito2presta.Model.StockCombinacion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class OperacionManager extends Manager {

	public List<OperacionDistrito> getHistorico() throws Exception{
		try{
			OperacionDAO dao= new OperacionDAO(pool.getConnection());		
			List<OperacionDistrito> historico = dao.getHistorico();
			dao.close();
			Sincro.LOG.info("Historico con "+ historico.size() +" operaciones");
			return historico;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".getHistorico()");
		}			
	}
	
	public Set<OperacionPresta> getOperacionesPresta(OperacionDistrito op) throws Exception{
		try{
			Set<OperacionPresta> operaciones= new HashSet<OperacionPresta>();
			
			// Combinacion
			if (op.tabla.equals("VENTA"))
				if(op.campos.get("CODCARACT")!=null && op.campos.get("VALORCARACT")!=null && op.campos.get("CODTARIFA")==null){
					Combinacion combinacion=new Combinacion(op.campos);
					boolean exist=new CombinacionManager().exist(combinacion);
					if (op.tipo==OperacionDistrito.ADD && exist)
						operaciones.add(new OperacionPresta(op.id,Estado.CREADA,combinacion));
					if (op.tipo==OperacionDistrito.UPDATE && exist && op.nuevos.containsKey("PRECIO"))
						operaciones.add(new OperacionPresta(op.id,Estado.CREADA,combinacion));
					if (op.tipo==OperacionDistrito.DELETE)
						if (exist)
							operaciones.add(new OperacionPresta(op.id,Estado.CREADA,combinacion));
						else
							operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteCombinacion(op.campos)));
				}
			
			// StockCombinacion
			if (op.tabla.equals("CAREXIST")){
				StockCombinacion stock=new StockCombinacion(op.campos);
				boolean exist=new CombinacionManager().exist(stock.combinacion());
				if (op.tipo==OperacionDistrito.ADD && exist){
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,stock.combinacion()));
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,stock));					
				}
				if (op.tipo==OperacionDistrito.UPDATE && op.nuevos.containsKey("STOCK1") && exist)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,stock));
				if (op.tipo==OperacionDistrito.DELETE)
					if (exist)
						operaciones.add(new OperacionPresta(op.id,Estado.CREADA,stock));
					else
						operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteCombinacion(op.campos)));	
			}
			
			// ImagenCombinacion
			if (op.tabla.equals("CARVALIDFOTO")){
				ImagenCombinacion imagen=new ImagenCombinacion(op.campos);
				boolean exist=new ImagenCombinacionManager().exist(imagen);
				if (op.tipo==OperacionDistrito.ADD && exist)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,imagen));
				if (op.tipo==OperacionDistrito.UPDATE && exist && (op.nuevos.containsKey("FOTO") || op.nuevos.containsKey("EXCLUIRWEB")))
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,imagen));
				if (op.tipo==OperacionDistrito.DELETE && !exist){
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteImagenCombinacion(op.campos)));
					if (!new CombinacionManager().exist(imagen.combinacion()))
						operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteCombinacion(op.campos)));
				}
			}
			
			// Producto
			if (op.tabla.equals("ARTICULO") && op.tipo!=OperacionDistrito.DELETE){
				if (op.tipo==OperacionDistrito.ADD)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Producto(op.campos)));
				if (op.tipo==OperacionDistrito.UPDATE && Producto.UPDATEFIELDS(op.nuevos))
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Producto(op.campos)));
			}
			
			if (op.tabla.equals("ARTRELACIONADOS"))
				operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Producto(op.campos)));
			
			if (op.tabla.equals("CARVALOR") && Integer.parseInt(op.campos.get("CODCLASE"))==Feature.CODCLASE()){
				op.campos.put("CODARTICULO", op.campos.get("CODOBJETO"));
				operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Producto(op.campos)));
			}

			// DeleteProducto
			if (op.tabla.equals("IDIOMAARTICULO") && op.tipo==OperacionDistrito.DELETE)
				if (Sincro.LANGPRESTASHOP.equals(op.campos.get("CODIDIOMA")))
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteProducto(op.campos)));
			
			// StockProducto
			if (op.tabla.equals("EXISTENC")){
				if (op.tipo==OperacionDistrito.ADD || op.tipo==OperacionDistrito.DELETE)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Stock(op.campos)));
				if (op.tipo==OperacionDistrito.UPDATE && op.nuevos.containsKey("STOCK1"))
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new Stock(op.campos)));
			}
				
			// Imagen
			if (op.tabla.equals("FOTOGRAF")){
				Imagen imagen=new Imagen(op.campos);
				boolean exist=new ImagenManager().exist(imagen);
				if (op.tipo==OperacionDistrito.ADD && exist)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,imagen));
				if (op.tipo==OperacionDistrito.UPDATE && exist && (op.nuevos.containsKey("FOTO") || op.nuevos.containsKey("EXCLUIRWEB")))
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,imagen));
				if (op.tipo==OperacionDistrito.DELETE && !exist)
					operaciones.add(new OperacionPresta(op.id,Estado.CREADA,new DeleteImagen(op.campos)));
			}
			
			return operaciones;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".getOperacionesPresta("+ op +")");
		}
	}
	
	public void deleteHistorico(List<OperacionDistrito> historico) throws Exception{ 
		try{
			Stack<Integer> papelera= new Stack<Integer>();
			Set<Integer> erroneas = Sincro.DB.getErroneas();
			Iterator<OperacionDistrito> iter=historico.iterator();
			while(iter.hasNext()){
				OperacionDistrito operacion = iter.next();
				if (!erroneas.contains(operacion.id))
					papelera.add(operacion.id);
			}
			
			if (!papelera.isEmpty()){
				OperacionDAO dao= new OperacionDAO(pool.getConnection());
				dao.deleteHistorico(papelera);
				dao.close();
			}			
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteHistorico()");
		}
	}

}
