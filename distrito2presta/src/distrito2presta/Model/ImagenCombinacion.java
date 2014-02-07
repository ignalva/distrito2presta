package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.ImagenCombinacionManager;
import distrito2presta.Model.Operacion.Estado;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class ImagenCombinacion extends Imagen {
	
	protected Combinacion combinacion;
	
	public ImagenCombinacion(){
		super();
		combinacion=new Combinacion();
	}
	
	public ImagenCombinacion(Combinacion combinacion,int codigo){
		this.codigo=codigo;
		this.combinacion=combinacion;
		producto=combinacion.producto();
	}
	
	public ImagenCombinacion(Map<String, String> campos) throws Exception{
		this(new Combinacion(campos),Integer.parseInt(campos.get("CODIGO")));
	}
	
	@Override
	public int hashCode() {
		return HashCode(ImagenCombinacion.class)+combinacion.hashCode()+codigo;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof ImagenCombinacion)) return false;
	    ImagenCombinacion i = (ImagenCombinacion) other;
		return combinacion.equals(i.combinacion) && codigo==i.codigo;
	}

	@Override
	public File jpg(){
		return new File("./images/"+producto.reference+"-"+combinacion.valorCaract()+"-"+codigo+".jpg");
	}

	@Override
	public boolean load() throws Exception {
		Combinacion actual=(Combinacion)Sincro.DB.get(combinacion);
		if (actual!=null) combinacion=actual;
		producto=combinacion.producto();
		return actual!=null && new ImagenCombinacionManager().load(this);
	}

	@Override
	public Estado send() throws Exception{
		if (combinacion.id==null){
			Sincro.LOG.warning("COMBINACION ID IS NULL!!");
			return Estado.ERRONEA;
		}
		if (super.send()==Estado.ENVIADA){
			new ImagenCombinacionManager().loadImagenes(combinacion);
			return combinacion.send();
		}
		else return Estado.ERRONEA;
	}

	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = super.dependientes();
		if (combinacion.id==null) dependientes.add(combinacion);
		return dependientes;
	}

	@Override
	public String toString() {
		return "ImagenCombinacion(ID "+ id+",CODIGO "+codigo+","+ combinacion+",EXLUIRWEB "+excluirweb+")";
	}

	@Override
	public void updateId() throws Exception {
		new ImagenCombinacionManager().updateId(this);
	}

	@Override
	public void deleteId() throws Exception {
		new ImagenCombinacionManager().deleteId(this);
	}
	
	public Combinacion combinacion(){
		return combinacion;
	}

}
