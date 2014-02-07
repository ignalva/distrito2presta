package distrito2presta.Model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class CombinacionAssociations {

	@XmlElementWrapper(name = "product_option_values") @XmlElement(name = "product_option_value") public List<ValorCaracteristica> valores;
	@XmlElementWrapper(name = "images")	@XmlElement(name = "image") public List<ImagenCombinacion> imagenes;

	public CombinacionAssociations(){
		imagenes=new ArrayList<ImagenCombinacion>();
		valores=new ArrayList<ValorCaracteristica>();
	}	
	
}
