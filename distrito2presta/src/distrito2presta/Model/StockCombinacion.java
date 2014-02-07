package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.StockCombinacionManager;
import distrito2presta.Model.Operacion.Estado;

import java.util.Map;
import java.util.Set;

public class StockCombinacion extends Stock {
	
	private Combinacion combinacion;
	
	public StockCombinacion(Map<String, String> campos){
		super(campos);
		campos.put("VALORCARACT",campos.get("VALOR"));
		combinacion=new Combinacion(campos);
	}

	public StockCombinacion(Combinacion combinacion) {
		super(combinacion.producto());
		this.combinacion=combinacion;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof StockCombinacion)) return false;
	    StockCombinacion stock = (StockCombinacion) other;
		return combinacion.equals(stock.combinacion());
	}

	@Override
	public int hashCode() {
		return HashCode(StockCombinacion.class)+combinacion.hashCode();
	}

	@Override
	public boolean load() throws Exception {
		Combinacion actual=(Combinacion)Sincro.DB.get(combinacion);
		if (actual!=null) combinacion=actual; else return false;
		producto=combinacion.producto();
		new StockCombinacionManager().load(this);
		return true;
	}
	
	@Override
	public Estado send() throws Exception{ 
		try{
			if ((id_product_attribute=combinacion.id)==null){
				Sincro.LOG.warning("ID_PRODUCT_ATTRIBUTE IS NULL!!");
				return Estado.ERRONEA;
			}
			return super.send();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +" sending "+ this);
		}
	}
	
	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = super.dependientes();
		if (combinacion.id==null) dependientes.add(combinacion);
		return dependientes;		
	}
	
	@Override
	public String toString() {
		return "StockCombinacion(ID "+id+",ID_PRODUCT_ATTRIBUTE "+id_product_attribute+","+combinacion+",QUANTITY "+quantity+")";
	}
	
	public Combinacion combinacion() {
		return combinacion;
	}

}
