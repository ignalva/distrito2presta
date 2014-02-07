package distrito2presta.Model;

import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeleteProducto extends Producto {

	public DeleteProducto(Map<String, String> campos) throws Exception{
		super(campos);
		setId(campos.get("NOMBRE"));	
	}

	@Override
	public int hashCode() {
		return id!=null?super.hashCode()+id:super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof DeleteProducto)) return false;
	    DeleteProducto producto = (DeleteProducto) other;
		return super.equals(producto) && id!=null && producto.id!=null && id.intValue()==producto.id.intValue();
	}

	@Override
	public boolean load() throws Exception{
		return id!=null;
	}

	@Override
	public Estado send() throws Exception {
		return new WS().delete(new PrestaProducto(this));
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
