package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Manager.ImagenManager;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.WS.WSImagen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

public class Imagen extends Model {

	@XmlCDATA Integer id_product=null;
	@XmlCDATA Integer position=null;
	@XmlCDATA Integer cover=null;
	@XmlCDATA byte[] content=null;
	
    protected int codigo;
    protected boolean excluirweb;
    protected Producto producto;

    public Imagen(){
    	codigo=0;
    	producto=new Producto();
    }
    
    public Imagen(Map<String, String> campos) throws Exception{
    	try{
        	codigo=Integer.parseInt(campos.get("CODIGO"));
        	producto=new Producto(campos);
    	}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass());
		}
    }

	@Override
	public int hashCode() {
		return HashCode(Imagen.class)+producto.hashCode()+codigo;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
	    if (!(other instanceof Imagen)) return false;
	    Imagen i = (Imagen) other;
		return producto.equals(i.producto) && codigo==i.codigo;
	}

	@Override
	public boolean load() throws Exception {
		Producto actual=(Producto)Sincro.DB.get(producto);
		if (actual!=null) producto=actual;
		return actual!=null && new ImagenManager().load(this);
	}

	@Override
	public String toString() {
		return "Imagen(ID "+ id+",CODIGO "+codigo+","+producto+",EXCLUIRWEB "+excluirweb+")";
	}

	public File jpg(){
		return new File("./images/"+producto.reference+"-"+codigo+".jpg");
	}
	
	public void save(Blob foto) throws Exception {
		try{
			File jpg=jpg();
			Sincro.LOG.info("Saving "+jpg.getName()+" de "+ foto.length()/1000 + " Kb ...");
			jpg.setWritable(true);
			FileOutputStream out = new FileOutputStream(jpg);
			InputStream in = foto.getBinaryStream();
			byte[] buffer=new byte[2048];
			int len=0;
			while ((len=in.read(buffer))>=0) out.write(buffer, 0, len);
			out.flush();
			out.close();
			foto.free();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".save");
		}
	}

	public void updateId() throws Exception {
		new ImagenManager().updateId(this);
	}

	public void deleteId() throws Exception {
		new ImagenManager().deleteId(this);
	}

	@Override
	public Estado send() throws Exception{
		try{			
			if (producto.id==null){
				Sincro.LOG.warning("PRODUCT ID IS NULL!!");
				return Estado.ERRONEA;
			}

			if (id==null){
				if (new WSImagen().post(new PrestaImagen(this))==Estado.ERRONEA) return Estado.ERRONEA;
				if (id!=null) updateId();
				return id==null?Estado.ERRONEA:Estado.ENVIADA;
			}

			// id!=null
			if (excluirweb){ //DELETE				
				if (new WSImagen().delete(new PrestaImagen(this))==Estado.ENVIADA){
					deleteId();
					jpg().delete();
					return Estado.ENVIADA;					
				}
				else return Estado.ERRONEA;
			}
			else return new WSImagen().put(new PrestaImagen(this));	
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +" sending "+ this +"!!");
		}
	}

	@Override
	public Set<Model> dependientes() throws Exception {
		Set<Model> dependientes = new HashSet<Model>();
		if (producto.id==null) dependientes.add(producto);
		return dependientes;
	}
	
	public int codigo() {
		return codigo;
	}

	public void codigo(int codigo) {
		this.codigo = codigo;
	}

	public boolean excluirweb() {
		return excluirweb;
	}

	public void excluirweb(boolean excluirweb) {
		this.excluirweb = excluirweb;
	}

	public Producto producto() {
		return producto;
	}
	
	public void producto(Producto producto){
		this.producto=producto;
	}
}
