package distrito2presta.Model;

import distrito2presta.Model.Operacion.Estado;

public class Etiqueta extends Model {
	
	public Etiqueta(){}
	
	@Override
	public boolean load() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO loadable!!");
	}

	@Override
	public Estado send() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO enviable!!");
	}

}
