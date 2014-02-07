package distrito2presta.Model;

import distrito2presta.Sincro;
import distrito2presta.Model.Operacion.Estado;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class DataBase {
	
	private SortedMap<Model, OperacionPresta> map;

	public DataBase(){
		map = new TreeMap<Model, OperacionPresta>();
	}

	public Model get(Model entidad) throws Exception{
		if (!map.containsKey(entidad))
			if (entidad.load()) 
				map.put(entidad, new OperacionPresta(0,Estado.DEPENDIENTE,entidad));
			else
				map.put(entidad, new OperacionPresta(0,Estado.DESCARTADA,entidad));	
		
		OperacionPresta op = map.get(entidad);
		return op.estado!=Estado.DESCARTADA?op.entidad:null;
	}
	
	public boolean load(OperacionPresta nueva) throws Exception {
		if (!map.containsKey(nueva.entidad)){
			nueva.estado=nueva.entidad.load()?Estado.PENDIENTE:Estado.DESCARTADA;
			map.put(nueva.entidad, nueva);
		}
		else{
			OperacionPresta actual = map.get(nueva.entidad);
			nueva.entidad=actual.entidad;
			if (nueva.estado==Estado.CREADA && actual.estado==Estado.DEPENDIENTE) actual.estado=Estado.PENDIENTE;
			nueva.estado=actual.estado;
		}		
	
		return nueva.estado!=Estado.DESCARTADA;
	}
	
	public void send() throws Exception{ 
		Iterator<OperacionPresta> iter = map.values().iterator();
		while (iter.hasNext()){
			OperacionPresta op = iter.next();
			if (op.estado==Estado.PENDIENTE) op.estado=op.entidad.send();
		}
	}

	public Set<Integer> getErroneas(){
		Iterator<OperacionPresta> iter=map.values().iterator();
		Set<Integer> erroneas = new HashSet<Integer>();
		while(iter.hasNext()){
			OperacionPresta op = iter.next();
			if (op.estado==Estado.ERRONEA){
				erroneas.add(op.id);
				Sincro.LOG.warning(op +" ERRONEA!!");
			}
		}
		return erroneas;
	}

}
