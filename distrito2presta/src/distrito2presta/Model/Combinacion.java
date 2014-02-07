package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.CombinacionManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WS;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

public class Combinacion extends Model {
	
	protected static final String SEPARADOR="-";

	@XmlCDATA public Integer id_product=null;
	@XmlCDATA public String location=null;
	@XmlCDATA public String ean13=null;				//Código de barras EAN
	@XmlCDATA public String upc=null;				//Codificación Americana
	@XmlCDATA public Integer quantity=null;			
	@XmlCDATA public String reference=null;
	@XmlCDATA public String supplier_reference=null;
	@XmlCDATA public Double wholesale_price=null;	//Precio mayorista sin IVA
	@XmlCDATA public Double price=null;				//Precio CON IVA
	@XmlCDATA public Double ecotax=null;
	@XmlCDATA public Float weight=null;
	@XmlCDATA public Double unit_price_impact=null;
	@XmlCDATA public Integer minimal_quantity=null;
	@XmlCDATA public Integer default_on=null;
	@XmlCDATA public String available_date=null;

	@XmlElement public CombinacionAssociations associations;

	protected Producto producto;
		
	public Combinacion(){
		associations=new CombinacionAssociations();
		producto=new Producto();
	}
	
	public Combinacion(Map<String, String> campos){
		this(new Producto(campos),
				Integer.parseInt(campos.get("CODCARACT")),
				campos.containsKey("VALORCARACT")?campos.get("VALORCARACT"):campos.get("VALOR"));
	}

	public Combinacion(Producto producto, int codcaract, String valorcaract) {
		associations=new CombinacionAssociations();
		minimal_quantity=1;
		this.producto=producto;
		if (valorcaract!=null){
			reference=producto.reference+SEPARADOR+valorcaract;
			String[] valoresCaract=valorcaract.split(SEPARADOR);
			for (int i=0;i<valoresCaract.length;i++){
				ValorCaracteristica valor = new ValorCaracteristica();
				valor.codcaract(codcaract);
				valor.dimension(i+1);
				valor.valor(valoresCaract[i]);
				associations.valores.add(valor);
			}
		}
	}

	@Override
	public int hashCode() {
		return HashCode(Combinacion.class)+producto.hashCode()+valorCaract().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof Combinacion)) return false;
	    Combinacion c = (Combinacion) other;
		return producto.equals(c.producto()) && associations.valores.equals(c.associations.valores); 
	}

	@Override
	public boolean load() throws Exception {
		Producto actual=(Producto)Sincro.DB.get(producto);
		if (actual!=null) producto=actual;
		return actual!=null && new CombinacionManager().load(this);
	}

	@Override
	public Estado send() throws Exception {
		try{			
			if ((id_product=producto.id)==null){
				Sincro.LOG.warning("ID_PRODUCT IS NULL!!");
				return Estado.ERRONEA;
			}

			if (price==null){
				Sincro.LOG.severe("PRICE IS NULL!!");
				return Estado.ERRONEA;
			}
			
			if (associations.valores.isEmpty()){
				Sincro.LOG.severe("VALORES IS EMPTY!!");
				return Estado.ERRONEA;
			}
			
			if (id==null){
				Combinacion response = (Combinacion) new WS().post(new PrestaCombinacion(this));
				if (response==null || (id=response.id)==null) return Estado.ERRONEA;
				new CombinacionManager().updateId(this);
				return Estado.ENVIADA;
			}
			
			return new WS().put(new PrestaCombinacion(this));			
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".send("+this+")");
		}
	}
	
	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = new HashSet<Model>();
		if (producto.id==null) dependientes.add(producto);
		if (id==null){
			dependientes.add(new StockCombinacion(this));
			dependientes.addAll(associations.imagenes);
		}
		return dependientes;
	}

	@Override
	public String toString() {
		return "Combinacion["+valorCaract()+"](ID "+id+","+ producto+",PRECIO "+price+")";
	}
	
	public Producto producto() {
		return producto;
	}

	public void producto(Producto producto) {
		this.producto = producto;
	}

	public int codcaract() {
		return associations.valores.get(0).codcaract();
	}

	public String valorCaract(){
		Iterator<ValorCaracteristica> iter = associations.valores.iterator();
		String valorCaract="";
		while (iter.hasNext())
			valorCaract+=valorCaract.isEmpty()?iter.next().valor():SEPARADOR + iter.next().valor();

		return valorCaract;
	}

	public static String SEPARADOR() {
		return SEPARADOR;
	}

}
