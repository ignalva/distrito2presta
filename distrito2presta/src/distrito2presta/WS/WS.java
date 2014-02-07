package distrito2presta.WS;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import distrito2presta.Sincro;
import distrito2presta.Model.Model;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.Model.PrestaModel;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

//DEBUG: prestashop\config\define.inc.php\_PS_MODE_DEV_

public class WS {

	protected WebResource service;

	public WS() {
		Client client = Client.create(new DefaultClientConfig());
	    client.addFilter(new HTTPBasicAuthFilter(Sincro.PRESTAACCOUNT, ""));
	    service = client.resource(getBaseURI());
	}
	
	protected static URI getBaseURI() {
	    return UriBuilder.fromUri("http://"+ Sincro.PRESTAACCOUNT + "@"+ Sincro.PRESTAHOST +"/api").build();
	}

	public Model get(PrestaModel m) throws Exception{
		try{
			Sincro.LOG.info("GET "+ m +" ...");
			ClientResponse response = service.path(m.Path()).path(m.Model().id.toString()).type(MediaType.TEXT_XML).get(ClientResponse.class);
			
			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				return response.getEntity(m.getClass()).Model();
			}
			else{
				Sincro.LOG.severe(response.toString());
				Sincro.LOG.severe(response.getEntity(String.class));
			    Sincro.LOG.severe(marshal(m).toString());
			    return null;
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".get("+m+")");
		}
	}
	
	public Model post(PrestaModel m) throws Exception{
		try{
			Sincro.LOG.info("POST "+ m +" ...");
			ClientResponse response = service.path(m.Path()).type(MediaType.TEXT_XML).post(ClientResponse.class, m);	

			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				return response.getEntity(m.getClass()).Model();
			}
			else{
				Sincro.LOG.severe(response.toString());
				Sincro.LOG.severe(response.getEntity(String.class));
			    Sincro.LOG.severe(marshal(m).toString());
				return null;
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".post("+m+")");
		}
	}
	
	public Estado put(PrestaModel m) throws Exception {
		try{
			Sincro.LOG.info("PUT "+ m +" ...");
			ClientResponse response = service.path(m.Path()).path(m.Model().id.toString()).type(MediaType.TEXT_XML).put(ClientResponse.class, m);
			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				return Estado.ENVIADA;
			}
			else{
				Sincro.LOG.severe(response.toString());
				Sincro.LOG.severe(response.getEntity(String.class));
			    Sincro.LOG.severe(marshal(m).toString());
				return Estado.ERRONEA;
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".put("+m+")");
		}
	}

	public Estado delete(PrestaModel m) throws Exception {
		try{
			Sincro.LOG.info("DELETE "+ m +" ...");
			ClientResponse response=service.path(m.Path()).path(m.Model().id.toString()).type(MediaType.TEXT_XML).delete(ClientResponse.class);
			if (sucess(response.getStatus())){
				Sincro.LOG.info(response.toString());
				return Estado.ENVIADA;
			}
			else{
				if (response.getStatus()==404){
					Sincro.LOG.warning(response.toString());
					return Estado.ENVIADA;
				}
				else{
					Sincro.LOG.severe(response.toString());
					Sincro.LOG.severe(response.getEntity(String.class));
					return Estado.ERRONEA;			
				}
			}
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".delete("+m+")");
		}
	}
	
	protected boolean sucess(int status){
		return status>=200 && status<300;
	}
	
	protected OutputStream marshal(PrestaModel m) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(m.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    ByteArrayOutputStream out=new ByteArrayOutputStream();
	    marshaller.marshal(m, out);
	    return out;
	}
} 

