package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.CombinacionManager;
import distrito2presta.Manager.ImagenManager;
import distrito2presta.Manager.ProductoManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * OJO!! Mismos ID Taxes entre PrestaShop y Distritok
 * @author ignalva@gmail.com
 */
public class Producto extends Model {

	@XmlCDATA public Integer id_manufacturer=null; 			//Fabricante
	//@XmlCDATA public Integer id_supplier=null;			//Proveedor
	@XmlCDATA public Integer id_category_default=null;		//Categoría predeterminada
	@XmlCDATA public Integer cache_default_attribute=null; 
	@XmlCDATA public Integer id_default_image=null;			//Imágen por defecto
	@XmlCDATA public Integer id_default_combination=null;	//Combinación por defecto
	@XmlCDATA public Integer id_tax_rules_group=null;		//Regla de impuestos
	@XmlCDATA String type=null;								//pack | simple | virtual
	@XmlCDATA Integer id_shop_default=null;
	@XmlCDATA public String reference=null;					//Codigo
	//@XmlCDATA String supplier_reference=null;
	//@XmlCDATA public String location=null;
	@XmlCDATA public Float width=null;						//Ancho
	@XmlCDATA public Float height=null;						//Alto
	@XmlCDATA public Float depth=null;						//Profundidad
	@XmlCDATA public Double weight=null;					//Peso en Kg
	//@XmlCDATA public Integer quantity_discount=null;
	//@XmlCDATA public String ean13=null;					//Código de barras EAN
	//@XmlCDATA public String upc=null;						//Codificación Americana
	@XmlCDATA Integer cache_is_pack=null;					//Es pack?
	//@XmlCDATA public Integer cache_has_attachments=null;	//No es posible asociar attachments!!
	//@XmlCDATA Integer is_virtual=null;					//Producto virtual (servicios, reservas y productos descargables)
	@XmlCDATA public Integer on_sale=null;					//Mostrar icono Rebajas y texto Descuento
	@XmlCDATA public Integer online_only=null;				//Disponible sólo en internet (no se vende en tiendas físicas)
	//@XmlCDATA public Double ecotax=null;
	//@XmlCDATA public Integer minimal_quantity=null;		//cantidad mínima para pedir el producto (1 para desactivar la opción)
	@XmlCDATA public Double price=null;						//Precio CON IVA
	@XmlCDATA public Double wholesale_price=null;			//Precio mayorista sin IVA
	//@XmlCDATA public String unity=null;
	//@XmlCDATA public Double unit_price_ratio=null;
	@XmlCDATA public Double additional_shipping_cost=null;	//Costo adicional de envío (por cantidad) SIN IVA
	//@XmlCDATA public Integer customizable=null;			//Con campos personalizados
	//@XmlCDATA public Integer text_fields=null;			//Número de campos de texto
	//@XmlCDATA public Integer uploadable_files=null;		//Número de campos de archivo
	@XmlCDATA public Integer active=null;					//BAJA: Activado o Desactivado
	//@XmlCDATA public String redirect_type=null;			//301 | 404 | 302
	//@XmlCDATA public Integer id_product_redirected=null;	//Producto relacionado
	@XmlCDATA Integer available_for_order=null;				//Disponible para ordenar
	//@XmlCDATA public String available_date=null;			//Fecha de disponibilidad
	@XmlCDATA public String condition=null; 				//new | used | refurbished 
	@XmlCDATA Integer show_price=null;						//Mostrar el precio
	//@XmlCDATA public Integer indexed=null;
	@XmlCDATA public String visibility=null;				//catalog | both | search | none
	@XmlCDATA Integer advanced_stock_management=null;		//NO gestión avanzada de existencias
	@XmlCDATA public String date_add=null;					//Fecha creación
	@XmlCDATA public String date_upd=null;					//Fecha actualización

	@XmlElementWrapper(name = "meta_description") @XmlElement(name = "language") public List<Language> meta_descripcion;
	@XmlElementWrapper(name = "meta_keywords") @XmlElement(name = "language") public List<Language> meta_claves;
	@XmlElementWrapper(name = "meta_title") @XmlElement(name = "language") public List<Language> meta_titulo;
	@XmlElementWrapper(name = "link_rewrite") @XmlElement(name = "language") public List<Language> enlace; 
	@XmlElementWrapper(name = "name") @XmlElement(name = "language") public List<Language> nombre;
	@XmlElementWrapper(name = "description") @XmlElement(name = "language") public List<Language> descripcion;
	@XmlElementWrapper(name = "description_short") @XmlElement(name = "language") public List<Language> descripcion_corta;
	@XmlElementWrapper(name = "available_now") @XmlElement(name = "language") public List<Language> disponible_ahora;
	@XmlElementWrapper(name = "available_later") @XmlElement(name = "language") public List<Language> disponible_despues;
	
	@XmlElement public ProductAssociations associations;
		
	public Producto(){
		nombre= new ArrayList<Language>();
		descripcion= new ArrayList<Language>();
		descripcion_corta= new ArrayList<Language>();
		enlace= new ArrayList<Language>();
		associations=new ProductAssociations();
		reference="";
	}
	
	public Producto(Map<String, String> campos){
		this();
		type="simple";
		id_shop_default=Sincro.PRESTASHOP;
		advanced_stock_management=0;
		online_only=0;
		cache_is_pack=0;
		show_price=1;
		available_for_order=1;
		reference=campos.containsKey("CODARTICULO")?campos.get("CODARTICULO"):campos.get("CODIGO");
		if (reference==null) reference="";
	}

	@Override
	public boolean load() throws Exception {
		return new ProductoManager().load(this);
	}
	
	@Override
	public Estado send() throws Exception {
		try{
			if (nombre.isEmpty()){
				Sincro.LOG.severe("NAME IS EMPTY!!");
				return Estado.ERRONEA;
			}		
			
			if (price==null){
				Sincro.LOG.severe("PRICE IS NULL!!");
				return Estado.ERRONEA;
			}
			
			if (id==null){
				Producto response = (Producto) new WS().post(new PrestaProducto(this));
				if (response==null || (id=response.id)==null) return Estado.ERRONEA;
				associations.stocks=response.associations.stocks;
				new ProductoManager().updateId(this);
				return Estado.ENVIADA;					
			}

			return new WS().put(new PrestaProducto(this));
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +" sending "+this+"!!");
		}
	}

	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = new HashSet<Model>();
		if (id==null){
			dependientes.add(new Stock(this));
			dependientes.addAll(new ImagenManager().get(this));
			dependientes.addAll(new CombinacionManager().get(this));
		}
		return dependientes;
	}
	
	public Integer StockId(Integer id_product_attribute) throws Exception{
		Integer idStock=associations.StockId(id_product_attribute);
		if (idStock!=null) return idStock;
		if (id!=null) return ((Producto)new WS().get(new PrestaProducto(this))).associations.StockId(id_product_attribute);
		return null;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof Producto)) return false;
	    Producto p = (Producto) other;
		return reference.equals(p.reference);
	}

	@Override
	public int hashCode() {
		return HashCode(Producto.class)+reference.hashCode();
	}

	@Override
	public String toString() {
		String msg="Producto(ID "+id+",CODIGO "+reference+",PRICE "+price+",VISIBILITY "+visibility+",ACTIVE "+active;
		if (!nombre.isEmpty()) msg+=",NAME "+nombre.get(0).valor;
		return msg+")";
	}
	
	public void nombre(String valor) {
		if (nombre.isEmpty())
			nombre.add(new Language(valor));
		else
			nombre.get(0).setValor(valor);
		
		String friendlyURL=valor.toLowerCase().replaceAll("[^a-z0-9-]","");

		if (enlace.isEmpty())
			enlace.add(new Language(friendlyURL));
		else
			enlace.get(0).setValor(friendlyURL);
	}

	public void descripcion(String valor) throws Exception {
		if (descripcion.isEmpty())
			descripcion.add(new Language(valor));
		else
			descripcion.get(0).setValor(valor);
	}

	public void descripcionCorta(String valor) throws Exception {
		if (descripcion_corta.isEmpty())
			descripcion_corta.add(new Language(valor));
		else
			descripcion_corta.get(0).setValor(valor);
	}

	public static boolean UPDATEFIELDS(Map<String, String> nuevos) {
		return 	nuevos.containsKey("CODIGO") || 
				nuevos.containsKey("NOMBRE") ||
				nuevos.containsKey("DESCRIPCION") ||
				nuevos.containsKey("PRECIOVENTA") ||
				nuevos.containsKey("TIPOIVA") ||
				nuevos.containsKey("KILOS") ||
				nuevos.containsKey("BAJA") ||
				nuevos.containsKey("EXCLUIRWEB") ||
				nuevos.containsKey("DESCRIPCIONCORTA");
	}

}
