package distrito2presta.DAO;

import distrito2presta.Sincro;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.PooledConnection;

import org.firebirdsql.ds.FBConnectionPoolDataSource;

public class ConnectionPool extends FBConnectionPoolDataSource {
	
	private PooledConnection pc=null;	
	
	public ConnectionPool() {
		super();
		try{
			setEncoding(Sincro.CHARSET); 
			setDatabaseName(Sincro.DISTRITODB);
			setPortNumber(Sincro.DISTRITOPORT);
			setServerName(Sincro.DISTRITOHOST);
			setUser(Sincro.USER);
			setPassword(Sincro.PASS);
	
			pc=getPooledConnection();
			
			Sincro.LOG.info("Execution Default Charset = "+ Charset.defaultCharset().name()); 
			Sincro.LOG.info("ConnectionPool[chatset("+ getCharSet() +"), encoding("+ getEncoding() +")]");
		}
		catch(SQLException e){
			Sincro.LOG.severe(e.toString());
		}					
	}
	
	public Connection getConnection(){
		try{
			if (pc!=null) return pc.getConnection();
			else return null;
		}
		catch(SQLException e){
			Sincro.LOG.severe(e.toString());
			return null;
		}			
	}
	
	public void close(){
		try {
			if (pc!=null) pc.close();
		} 
		catch(SQLException e){
			Sincro.LOG.severe(e.toString());
		}			
	}
}
