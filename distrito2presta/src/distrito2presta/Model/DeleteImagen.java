package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.ImagenManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WSImagen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeleteImagen extends Imagen {
	
	public DeleteImagen(Map<String, String> campos) throws Exception {
		super(campos);
	}

	@Override
	public boolean load() throws Exception{
		Producto actual=(Producto)Sincro.DB.get(producto);
		if (actual!=null) producto=actual;
		return actual!=null && producto.id!=null && new ImagenManager().loadId(this) && id!=null;
	}
	
	@Override
	public Estado send() throws Exception {
		if (new WSImagen().delete(new PrestaImagen(this))==Estado.ENVIADA){
			deleteId();
			jpg().delete();
			return Estado.ENVIADA;
		}
		else return Estado.ERRONEA;
	}
	
	@Override
	public Set<Model> dependientes() throws Exception {
		return new HashSet<Model>();
	}

	@Override
	public String toString(){
		return "Delete"+ super.toString();
	}

}
