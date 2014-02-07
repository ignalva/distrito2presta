package distrito2presta.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="prestashop")

public class PrestaStock extends PrestaModel {
	
	@XmlElement Stock stock_available;
	
	public PrestaStock(){
		stock_available=null;
	}

	public PrestaStock(Stock s){
		stock_available=s;
	}
	
	@Override
	public String toString() {
		return "Presta"+ stock_available;
	}

	@Override
	public String Path() {
		return "stock_availables";
	}

	@Override
	public Model Model() {
		return stock_available;
	}

}
