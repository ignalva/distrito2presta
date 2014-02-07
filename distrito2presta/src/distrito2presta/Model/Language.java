package distrito2presta.Model;

import distrito2presta.Sincro;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Language {
	
    @XmlAttribute int id=Sincro.LANGSPANISH;
    @XmlValue String valor; //No CDATA!!
    
    public Language(){
    	valor=null;
    }
    
	public Language(String valor) {
		setValor(valor);
	}
	
	public void setValor(String valor){
		this.valor=valor;
	}
}
