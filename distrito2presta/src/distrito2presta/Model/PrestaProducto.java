package distrito2presta.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="prestashop")

public class PrestaProducto extends PrestaModel {

	@XmlElement Producto product;
	
	public PrestaProducto(){
		product=null;
	}

	public PrestaProducto(Producto producto) {
		this.product=producto;
	}

	@Override
	public String Path() {
		return "products";
	}

	@Override
	public String toString() {
		return "Presta"+ product;
	}

	@Override
	public Model Model() {
		return product;
	}

}
