package distrito2presta.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="prestashop")

public class PrestaImagen extends PrestaModel {

	@XmlElement public Imagen image;
	
	public PrestaImagen(){
		image=null;
	}
	
	public PrestaImagen(Imagen image){
		this.image=image;
	}
	
	@Override
	public String Path() {
		return "images/products/"+ image.producto().id;
	}
	
	@Override
	public String toString() {
		return "Presta"+ image;
	}

	@Override
	public Model Model() {
		return image;
	}
}
