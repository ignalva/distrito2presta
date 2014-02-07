package distrito2presta.Manager;

import distrito2presta.DAO.ConnectionPool;

public abstract class Manager {
	
	protected static ConnectionPool pool=null;
	
	public Manager(){
		if (pool == null) pool=new ConnectionPool();	
	}

	public static void close(){
		if (pool!=null) {
			pool.close();
			pool=null;
		}		
	}	
	
}

