package distrito2presta.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="prestashop")

public class PrestaCombinacion extends PrestaModel {
	
	@XmlElement Combinacion combination;

	public PrestaCombinacion(){
		combination=null;
	}
	
	public PrestaCombinacion(Combinacion combinacion){
		combination=combinacion;
	}

	@Override
	public String toString() {
		return "Presta"+ combination;
	}

	@Override
	public String Path() {
		return "combinations";
	}

	@Override
	public Model Model() {
		return combination;
	}

}
