package distrito2presta.DAO;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO {
	
	protected Connection con = null;
	
	public DAO(Connection con){
		this.con=con;
	}
	
	public void close() throws SQLException{
		con.close();
	}

}
