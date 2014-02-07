package distrito2presta.Model;

import java.util.HashMap;
import java.util.Map;

public class OperacionDistrito extends Operacion {
	
	public static final int ADD=0;
	public static final int UPDATE=1;
	public static final int DELETE=2;
	
	public int tipo;
	
	public String tabla;
	public Map<String, String> campos;
	public Map<String, String> nuevos;

	public OperacionDistrito(int id, int tipo, String tabla) {
		super(id,Estado.DESCARTADA);
		this.tabla=tabla;
		this.tipo=tipo;
		campos=new HashMap<String, String>();
		nuevos=new HashMap<String, String>();
	}

	public void load(String campo, String valor, String newvalor) {
		campos.put(campo, valor);
		if (newvalor!=null) nuevos.put(campo, newvalor);
	}

	@Override
	public String toString(){
		if (tipo==ADD) return "ADD|"+ super.toString() +"|"+tabla;
		if (tipo==UPDATE) return "UPDATE|"+ super.toString() +"|"+tabla;
		if (tipo==DELETE) return "DELETE|"+ super.toString() +"|"+tabla;
		return getClass()+" INVALIDA!!";
	}
}
