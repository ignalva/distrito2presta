package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.CombinacionManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeleteCombinacion extends Combinacion {
	
	public DeleteCombinacion(Map<String, String> campos){
		super(campos);
	}

	@Override
	public boolean load() throws Exception{
		Producto actual=(Producto)Sincro.DB.get(producto);
		if (actual!=null) producto=actual;
		return actual!=null && new CombinacionManager().loadId(this) && id!=null;
	}
	
	@Override
	public Estado send() throws Exception {
		if (new WS().delete(new PrestaCombinacion(this))==Estado.ENVIADA){
			new CombinacionManager().deleteId(this);
			return Estado.ENVIADA;
		}
		else return Estado.ERRONEA;
	}
	
	@Override
	public Set<Model> dependientes() throws Exception {
		return new HashSet<Model>();
	}
	
	@Override
	public String toString() {
		return "Delete"+ super.toString();
	}
	
}
