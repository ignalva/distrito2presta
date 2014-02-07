package distrito2presta.Model;

public abstract class Operacion {

	public int id;

	public enum Estado {
		CREADA, 	// Paso1: New Operacion, pasa a PENDIENTE o DESCARTADA 
		DEPENDIENTE,// Paso1: Operacion dependiente de otra
		PENDIENTE, 	// Paso2: Pendiente de enviar, empleado en Repescas
		DESCARTADA, // Paso2: No enviar y borrar
		ENVIADA, 	// Paso3: Enviada con éxito
		ERRONEA; 	// Paso3: Error de envío, no borrar de histórico	
	}

	public Estado estado;
	
	public Operacion(int id, Estado estado){
		this.id=id;
		this.estado=estado;
	}

	@Override
	public String toString() {
		if (estado==Estado.CREADA) return id+"|CREADA";
		if (estado==Estado.DEPENDIENTE) return id+"|DEPENDIENTE";
		if (estado==Estado.PENDIENTE) return id+"|PENDIENTE";
		if (estado==Estado.DESCARTADA) return id+"|DESCARTADA";
		if (estado==Estado.ENVIADA) return id+"|ENVIADA";
		if (estado==Estado.ERRONEA) return id+"|ERRONEA";
		return getClass()+" INVALIDA!!";
	}

}
