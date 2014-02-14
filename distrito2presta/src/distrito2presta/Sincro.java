package distrito2presta;

import distrito2presta.Manager.Manager;
import distrito2presta.Manager.OperacionManager;
import distrito2presta.Model.DataBase;
import distrito2presta.Model.Model;
import distrito2presta.Model.Operacion.Estado;
import distrito2presta.Model.OperacionDistrito;
import distrito2presta.Model.OperacionPresta;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * DistritoK SQLTPV version 11/09/2013
 * PrestaShop 1.5.5.0
 * Export ... Runnable JAR File / Package required libraries into generated JAR
 * @author ignalva@gmail.com
 * @version 1.0
 */

public class Sincro {
		
	public final static Logger LOG = Logger.getLogger(Sincro.class.getName());
	public static DataBase DB = new DataBase();
	
	// Configuración
	public static int PRESTASHOP;
	public static String PRESTAACCOUNT;
	public static String PRESTAHOST;
	public static String DISTRITODB;
	public static String DISTRITOHOST;
	public static int DISTRITOPORT;	
	public static String USER;
	public static String PASS;
	public static String CHARSET;
	public static int LANGSPANISH;
	public static String LANGPRESTASHOP;
	public static int FABRICANTES;
	public static int TARIFAWEB;
	
	public static void main(String[] args) {
		try{
			config(args[0]);
			
			OperacionManager mng = new OperacionManager();
			
			//PASO1-GetHistorico
			List<OperacionDistrito> historico=mng.getHistorico();
			
			//PASO2-GetOperacionesPresta
			Set<OperacionPresta> operaciones = new HashSet<OperacionPresta>();
			Iterator<OperacionDistrito> iterHist= historico.iterator();
			while (iterHist.hasNext()) operaciones.addAll(mng.getOperacionesPresta(iterHist.next()));

			//PASO3-LOAD
			Iterator<OperacionPresta> iterOp = operaciones.iterator();
			while (iterOp.hasNext()){
				OperacionPresta op = iterOp.next();
				if (DB.load(op)){
					Iterator<Model> iterDep=op.entidad.dependientes().iterator();
					while (iterDep.hasNext())
						DB.load(new OperacionPresta(op.id,Estado.CREADA,iterDep.next()));
				}
			}
			
			//PASO4-SEND
			DB.send();
			
			//PASO5-DELETE
			mng.deleteHistorico(historico);
		}
		catch(Exception e){
			LOG.severe(e.toString());
		}
		finally{
			Manager.close();			
			LOG.info("END!!");
		}
	}

	private static void config(String configproperties) throws Exception {
		try{
			Properties p = new Properties();
			p.load(new FileInputStream(configproperties));
				
			PRESTASHOP=Integer.parseInt(p.getProperty("PRESTASHOP"));
			PRESTAACCOUNT=p.getProperty("PRESTAACCOUNT");
			PRESTAHOST=p.getProperty("PRESTAHOST");
			DISTRITODB=p.getProperty("DISTRITODB");
			DISTRITOHOST=p.getProperty("DISTRITOHOST");
			DISTRITOPORT=Integer.parseInt(p.getProperty("DISTRITOPORT"));
			USER=p.getProperty("USER");
			PASS=p.getProperty("PASS");
			CHARSET=p.getProperty("CHARSET");
			LANGSPANISH=Integer.parseInt(p.getProperty("LANGSPANISH"));	
			LANGPRESTASHOP=p.getProperty("LANGPRESTASHOP");
			FABRICANTES=Integer.parseInt(p.getProperty("FABRICANTES"));
			TARIFAWEB=Integer.parseInt(p.getProperty("TARIFAWEB"));
			
			FileHandler f=new FileHandler("./logs/"+Sincro.class.getName()+".%g.log",true);
			f.setFormatter(new SimpleFormatter());
			
			if (p.getProperty("DEBUG").equals("1")){
				LOG.setLevel(Level.ALL);
				f.setLevel(Level.ALL);
			}
			else{
				LOG.setLevel(Level.WARNING);					
				f.setLevel(Level.WARNING);
			}			
			LOG.addHandler(f); 
			
		}
		catch(Exception e){
			LOG.severe(e.toString());
			throw new Exception("FATAL ERROR config");
		}			
	}
}
