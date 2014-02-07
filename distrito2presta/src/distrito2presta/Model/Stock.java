package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.StockManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

public class Stock extends Model {
	
	@XmlCDATA public Integer id_product_attribute=null;
	@XmlCDATA public Integer id_product=null;
	@XmlCDATA public Integer id_shop=null;
	@XmlCDATA public Integer id_shop_group=null;
	@XmlCDATA public Integer quantity=null;
	@XmlCDATA public Integer depends_on_stock=null;
	@XmlCDATA public Integer out_of_stock=null;	//Cuando no haya existencias 0|1|2 (preferencias PrestaShop)

	protected Producto producto;
	
	public Stock(){
		producto=new Producto();
	}
	
	public Stock(Map<String, String> campos){
		this(new Producto(campos));
	}

	public Stock(Producto producto) {
		id_shop=Sincro.PRESTASHOP;
		out_of_stock=2;
		depends_on_stock=0;
		this.producto=producto;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof Stock)) return false;
	    Stock stock = (Stock) other;
		return producto.equals(stock.producto());
	}

	@Override
	public int hashCode() {
		return HashCode(Stock.class)+producto.hashCode();
	}

	@Override
	public boolean load() throws Exception {
		Producto actual=(Producto)Sincro.DB.get(producto);
		if (actual!=null) producto=actual; else return false;
		id_product_attribute=0;
		new StockManager().load(this);
		return true;
	}
	
	@Override
	public Estado send() throws Exception{ 
		try{
			if ((id_product=producto.id)==null){
				Sincro.LOG.warning("ID_PRODUCT IS NULL!!");
				return Estado.ERRONEA;
			}
			if ((id=producto.StockId(id_product_attribute))==null){
				Sincro.LOG.warning("ID IS NULL!!");
				return Estado.ERRONEA;
			}	
			return new WS().put(new PrestaStock(this));
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +" sending "+ this);
		}
	}

	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = new HashSet<Model>();
		if (producto.id==null) dependientes.add(producto);
		return dependientes;
	}
	
	@Override
	public String toString() {
		return "Stock(ID "+id+",ID_PRODUCT_ATTRIBUTE "+id_product_attribute+","+producto+",QUANTITY "+quantity+")";
	}

	public Producto producto() {
		return producto;
	}
}
