package distrito2presta.Manager;

import distrito2presta.Sincro;
import distrito2presta.DAO.IdiomaProductoDAO;
import distrito2presta.DAO.ProductoDAO;
import distrito2presta.Model.PrestaProducto;
import distrito2presta.Model.Producto;
import distrito2presta.WS.WS;

public class ProductoManager extends Manager {
		
	public boolean load(Producto producto) throws Exception{
		try {
			IdiomaProductoDAO daoIdioma = new IdiomaProductoDAO(pool.getConnection());
			daoIdioma.loadProductoId(producto);
			daoIdioma.close();	

			if (producto.id!=null) get(producto);
			
			ProductoDAO daoProducto = new ProductoDAO(pool.getConnection());
			boolean ok=	daoProducto.load(producto) && 
						daoProducto.loadPrecio(producto) &&
						daoProducto.setRelacionados(producto) && 
						daoProducto.setFeatures(producto) && 
						daoProducto.setCategories(producto);
			daoProducto.close();
				
			if (ok && producto.id==null && producto.active==0){
				Sincro.LOG.info(producto+" inactivo!!");
				return false;
			}
			if (ok && producto.id==null && producto.visibility.equals("none")){
				Sincro.LOG.info(producto+" invisible!!");
				return false;
			}
					
			Sincro.LOG.info(producto.toString());
			
			return ok ;
		} 
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".load("+producto+")");
		}		
	}
	
	public void updateId(Producto producto) throws Exception{
		try {
			IdiomaProductoDAO dao = new IdiomaProductoDAO(pool.getConnection());
			dao.updateIdProducto(producto);
			dao.close();
			Sincro.LOG.info(producto.toString());
		} 
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".updateId("+producto+")");
		}
	}

	private void get(Producto to) throws Exception{
		Producto from=(Producto) new WS().get(new PrestaProducto(to));
		if (from==null) return;
		to.cache_default_attribute=from.cache_default_attribute;
		to.id_default_image=from.id_default_image;
		to.id_default_combination=from.id_default_combination;
		to.width=from.width;
		to.height=from.height;
		to.depth=from.depth;
		to.on_sale=from.on_sale;
		to.online_only=from.online_only;
		to.wholesale_price=from.wholesale_price;
		to.additional_shipping_cost=from.additional_shipping_cost;
		to.condition=from.condition;
		to.date_add=from.date_add;
		to.date_upd=from.date_upd;
		to.meta_descripcion=from.meta_descripcion;
		to.meta_claves=from.meta_claves;
		to.meta_titulo=from.meta_titulo;
		to.disponible_ahora=from.disponible_ahora;
		to.disponible_despues=from.disponible_despues;
		to.associations.categorias=from.associations.categorias;
		to.associations.imagenes=from.associations.imagenes;
		to.associations.combinaciones=from.associations.combinaciones;
		to.associations.valores=from.associations.valores;
		to.associations.etiquetas=from.associations.etiquetas;
		to.associations.stocks=from.associations.stocks;
	}

}
