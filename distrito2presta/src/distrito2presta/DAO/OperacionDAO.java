package distrito2presta.DAO;

import distrito2presta.Sincro;
import distrito2presta.Model.OperacionDistrito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OperacionDAO extends DAO {

	private static String SQLGETHISTORICO = "SELECT O.ID, O.OPERACION, O.TABLA, K.CAMPO, K.VALOR, NULL NEW_VALUE FROM DK$OPERATIONLOG O INNER JOIN DK$KEYLOG K ON O.ID=K.OPER_ID WHERE O.OPERACION<>? UNION SELECT OP.ID, OP.OPERACION, OP.TABLA, C.CAMPO, C.OLD_VALUE, C.NEW_VALUE VALOR FROM DK$OPERATIONLOG OP INNER JOIN DK$COLUMNLOG C ON OP.ID=C.OPER_ID WHERE OP.OPERACION<>? ORDER BY 1 ASC";
	private static final int MAXIDS=1500;
	
	public OperacionDAO(Connection con) {
		super(con);
	}
	
	public List<OperacionDistrito> getHistorico() throws Exception{
		try{
			List<OperacionDistrito> lista = new ArrayList<OperacionDistrito>();
			PreparedStatement stmt = con.prepareStatement(SQLGETHISTORICO);
			
			stmt.setInt(1, OperacionDistrito.DELETE);
			stmt.setInt(2, OperacionDistrito.ADD);
			ResultSet rs = stmt.executeQuery();
					
			OperacionDistrito operacion=null;

			while(rs.next()){
				int id = rs.getInt("ID");
				
				if (operacion==null)
					operacion=new OperacionDistrito(id,rs.getInt("OPERACION"),rs.getString("TABLA"));					

				
				if (id!=operacion.id){
					lista.add(operacion);
					operacion=new OperacionDistrito(id,rs.getInt("OPERACION"),rs.getString("TABLA"));					
				}
					
				operacion.load(rs.getString("CAMPO"), rs.getString("VALOR"), rs.getString("NEW_VALUE"));
			}
			
			if (operacion!=null) lista.add(operacion);
			
			rs.close();
			stmt.close();
			return lista;
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".getHistorico");
		}
	}

	public void deleteHistorico(Stack<Integer> ids) throws Exception {
		try{
			Sincro.LOG.info("Deleting historico de "+ ids.size() +" operaciones ...");
			Statement stmt = con.createStatement();
			
			while(!ids.isEmpty()){
				int i=1;
				String SQL="DELETE FROM DK$OPERATIONLOG WHERE ID IN (";
				while(!ids.isEmpty() && i<MAXIDS){
					i++;
					SQL+=ids.pop();
					if (!ids.isEmpty() && i<MAXIDS) SQL+=",";
				}
				SQL+=")";
				stmt.executeUpdate(SQL);
			}

			stmt.close();
		}
		catch (Exception e) {
			Sincro.LOG.severe(e.toString());
			throw new Exception("FATAL ERROR "+ getClass() +".deleteHistorico");
		}
	}
}
