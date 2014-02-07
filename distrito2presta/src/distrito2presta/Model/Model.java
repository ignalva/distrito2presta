package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Model.Operacion.Estado;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

public abstract class Model implements Comparable<Model>{

	@XmlCDATA public Integer id=null;

    public abstract boolean load() throws Exception;
	public abstract Estado send() throws Exception;

	public void setId(String id) throws Exception {
		try{
			this.id=(id!=null && !id.isEmpty())?Integer.parseInt(id):null;			
		}
		catch(Exception e){
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ this +".setId("+id+")");			
		}
	}
	
	@Override
	public int compareTo(Model otro) {
		return hashCode()-otro.hashCode();
	}

	protected int HashCode(Class<? extends Model> c){
		
		if (Categoria.class.equals(c)) return 10000;
		if (ValorCaracteristica.class.equals(c)) return 2*HashCode(Categoria.class);
		if (Producto.class.equals(c)) return 2*HashCode(ValorCaracteristica.class);
		if (Imagen.class.equals(c)) return 2*HashCode(Producto.class);
		if (Stock.class.equals(c)) return 3*HashCode(Producto.class);
		if (Combinacion.class.equals(c)) return 4*HashCode(Producto.class)+HashCode(ValorCaracteristica.class);
		if (StockCombinacion.class.equals(c)) return 2*HashCode(Combinacion.class);
		if (ImagenCombinacion.class.equals(c)) return 3*HashCode(Combinacion.class);
		
		return super.hashCode();
	}
	
	public Set<Model> dependientes() throws Exception {
		return new HashSet<Model>();
	}
}
