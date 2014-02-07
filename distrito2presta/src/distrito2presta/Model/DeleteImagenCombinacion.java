package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.ImagenCombinacionManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WSImagen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeleteImagenCombinacion extends ImagenCombinacion {

	public DeleteImagenCombinacion(Map<String, String> campos) throws Exception {
		super(campos);
	}

	@Override
	public boolean load() throws Exception{
		Combinacion actual=(Combinacion)Sincro.DB.get(combinacion);
		if (actual!=null) combinacion=actual;
		producto=combinacion.producto();
		return actual!=null && producto.id!=null && new ImagenCombinacionManager().loadId(this) && id!=null;
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
