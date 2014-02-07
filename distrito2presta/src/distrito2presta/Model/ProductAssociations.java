package distrito2presta.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ProductAssociations {

	@XmlElementWrapper(name = "categories")	@XmlElement(name = "category") public Set<Categoria> categorias;
	@XmlElementWrapper(name = "images")	@XmlElement(name = "image") public List<Imagen> imagenes;
	@XmlElementWrapper(name = "combinations") @XmlElement(name = "combinations") public List<Combinacion> combinaciones;
	@XmlElementWrapper(name = "product_option_values") @XmlElement(name = "product_options_values") public List<ValorCaracteristica> valores;
	@XmlElementWrapper(name = "product_features") @XmlElement(name = "product_feature") public List<Feature> features;
	@XmlElementWrapper(name = "tags") @XmlElement(name = "tag") public List<Etiqueta> etiquetas;
	@XmlElementWrapper(name = "stock_availables") @XmlElement(name = "stock_available") public List<Stock> stocks;
	@XmlElementWrapper(name = "accessories") @XmlElement(name = "product") public List<Producto> accesorios;
	//@XmlElementWrapper(name = "product_bundle")	@XmlElement(name = "products") public List<Producto> packs;

	public ProductAssociations(){
		categorias=new HashSet<Categoria>();
		imagenes=new ArrayList<Imagen>();
		combinaciones=new ArrayList<Combinacion>();
		valores=new ArrayList<ValorCaracteristica>();
		features=new ArrayList<Feature>();
		etiquetas=new ArrayList<Etiqueta>();
		stocks=new ArrayList<Stock>();
		accesorios=new ArrayList<Producto>();
		//packs=new ArrayList<Producto>();
	}
	
	public Integer StockId(Integer id_product_attribute){
		Iterator<Stock> iter = stocks.iterator();
		while (iter.hasNext()){
			Stock stock = iter.next();
			if (id_product_attribute!=null && id_product_attribute.intValue()==stock.id_product_attribute) return stock.id;
		}
		return null;
	}
	
}
