package distrito2presta.Model;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * Feature o producto.product_feature_values | Datos Adicionales en DistritoK
 * IDIOMACARACT.NOMBRE = ID
 * IDIOMACARVALID.VALORTRADUC = id_feature_value
 * OJO!! FABRICANTES.VALORTRADUC = 0
 * @author ignalva@gmail.com
 */
public class Feature extends ValorCaracteristica {

	@XmlCDATA public Integer custom=null;
	@XmlCDATA public int id_feature_value;
		
	@Override
	public boolean load() throws Exception {
		throw new Exception("FATAL ERROR "+ this +" NO loadable!!");
	}

	public static int CODCLASE(){
		return 2;
	}
	
	@Override
	public String toString() {
		return "Feature(ID "+id+",CODCARACT "+ codcaract+",ID_FEATURE_VALUE "+ id_feature_value+",VALOR "+ valor+",CUSTOM "+ custom+")";
	}
}
