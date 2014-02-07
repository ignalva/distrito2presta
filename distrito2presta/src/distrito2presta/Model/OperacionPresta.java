package distrito2presta.Model;

public class OperacionPresta extends Operacion {
	
	public Model entidad;
	
	public OperacionPresta(int id, Estado estado, Model entidad){
		super(id,estado);
		this.entidad=entidad;
	}
	
	@Override
	public String toString(){
		return super.toString()+"|"+entidad;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof OperacionPresta)) return false;
	    OperacionPresta op = (OperacionPresta) other;
	    return entidad.equals(op.entidad);
	}

	@Override
	public int hashCode() {
		return entidad.hashCode();
	}
}
