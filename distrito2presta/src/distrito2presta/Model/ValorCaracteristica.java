package distrito2presta.Model;

import distrito2presta.Manager.ValorCaracteristicaManager;
import distrito2presta.Model.Operacion.Estado;

/**
 * ValorCaracteristica | Atributos en PrestaShop
 * OJO: PrestaShop solo ACEPTA GENERICOS no INDIVIDUALES
 * @author ignalva@gmail.com
 */

public class ValorCaracteristica extends Model {
	
	protected int codcaract;	
	protected int dimension;
	protected String valor;
	
	public ValorCaracteristica(){
		codcaract=0;
		dimension=0;
		valor="";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof ValorCaracteristica)) return false;
	    ValorCaracteristica v = (ValorCaracteristica) other;
		return codcaract==v.codcaract() && dimension==v.dimension() && valor.equals(v.valor());
	}

	@Override
	public int hashCode() {
		return HashCode(ValorCaracteristica.class)+valor.hashCode()+10*codcaract+dimension;
	}

	@Override
	public boolean load() throws Exception {
		return new ValorCaracteristicaManager().load(this);
	}

	@Override
	public Estado send() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO enviable!!");
	}

	@Override
	public String toString() {
		return "ValorCaracteristica(ID "+ id+",CODCARACT "+ codcaract+",DIMENSION "+ dimension+",VALOR "+ valor+")";
	}

	public static int CODCLASE(){
		return 0;
	}

	public int codcaract() {
		return codcaract;
	}

	public void codcaract(int codcaract) {
		this.codcaract = codcaract;
	}

	public int dimension() {
		return dimension;
	}

	public void dimension(int dimension) {
		this.dimension = dimension;
	}

	public String valor() {
		return valor;
	}

	public void valor(String valor) {
		this.valor = valor;
	}
}
