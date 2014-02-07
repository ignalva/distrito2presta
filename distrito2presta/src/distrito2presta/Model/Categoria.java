package distrito2presta.Model;

import distrito2presta.Model.Operacion.Estado;

/**
 * CATEGORIA
 * IDIOMATIPO.DESCRIPCION = ID PrestaShop
 * IDIOMATIPO.METATITLE=IDIOMACARVALID.VALORTRADUC | Cruzar con Datos Adionales
 * @author ignalva@gmail.com
 */
public class Categoria extends Model {

	public Categoria(){}
	
	@Override
	public int hashCode() {
		return id!=null?HashCode(Categoria.class)+id:HashCode(Categoria.class);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof Categoria)) return false;
	    Categoria categoria = (Categoria) other;
		return (id!=null && categoria.id!=null && id.intValue()==categoria.id.intValue());
	}
	
	@Override
	public boolean load() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO loadable!!");
	}

	@Override
	public Estado send() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO enviable!!");
	}
	
	public static int TIPO(){
		return 13;
	}	
}
