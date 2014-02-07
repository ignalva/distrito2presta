package distrito2presta.WS;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import distrito2presta.Sincro;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.Model.PrestaImagen;

import javax.ws.rs.core.MediaType;

public class WSImagen extends WS {
	
	private static final boolean BUGPSCFV7498=true; // PrestaShop BUG PSCFV 7498
	
	public Estado post(PrestaImagen imagen) throws Exception {
		try{		
			Sincro.LOG.info("POST "+ imagen +" ...");			
			FormDataMultiPart form=new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("image", imagen.image.jpg()));
		
			ClientResponse response = service.path(imagen.Path()).type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);	
			
			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				imagen.Model().id=response.getEntity(PrestaImagen.class).Model().id;
				Sincro.LOG.info("Creada "+ imagen +" !!");
				return Estado.ENVIADA;
			}
			else{
				Sincro.LOG.severe(response.toString());
				Sincro.LOG.severe(response.getEntity(String.class));
				return Estado.ERRONEA;
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".post("+imagen+")");
		}
	}

	public Estado put(PrestaImagen imagen) throws Exception {
		try{
			Sincro.LOG.info("PUT "+ imagen +" ...");
			
			if (BUGPSCFV7498) return putBUGPSCFV7498(imagen);
			
			FormDataMultiPart form=new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("image", imagen.image.jpg()));
			
			ClientResponse response = service.path(imagen.Path()).path(imagen.Model().id.toString()).type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, form);

			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				return Estado.ENVIADA;
			}
			else{
				Sincro.LOG.severe(response.toString());
				Sincro.LOG.severe(response.getEntity(String.class));
				return Estado.ERRONEA;
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".put("+imagen+")");
		}
	}
	
	public Estado putBUGPSCFV7498(PrestaImagen imagen) throws Exception {
		try{			
			if (delete(imagen)==Estado.ERRONEA)
				return Estado.ERRONEA;
			else
				imagen.image.deleteId();

			if (post(imagen)==Estado.ERRONEA)
				return Estado.ERRONEA;
			else
				imagen.image.updateId();
			
			return Estado.ENVIADA;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".putBUGPSCFV7498("+imagen+")");
		}
	}
}
