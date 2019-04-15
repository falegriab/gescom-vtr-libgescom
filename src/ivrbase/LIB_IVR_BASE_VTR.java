package ivrbase;


//import ivrbase.FunctionsGVP;
import ivrbase.KVPair;
import ivrbase.KVPairList;
import ivrbase.Parameters;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


import org.apache.log4j.Logger;
import org.json.JSONObject;

/*import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;*/

import java.sql.*;

import javax.servlet.jsp.PageContext;

public class LIB_IVR_BASE_VTR
{

	//private static  JsonParser      parser  = new JsonParser();


	//Use de conexion a mssql
	Connection miConexion = null;
	java.sql.ResultSet rs = null;
	java.sql.ResultSet rs2 = null;
	java.sql.ResultSet rs3 = null;
	java.sql.CallableStatement cs = null;
	java.sql.CallableStatement cs2 = null;
	java.sql.CallableStatement cs3 = null;

	//Use de getFlagTango()
	public String FlagTango="";

	//uso de getHorariosAtencionIVR()
	public String HorarioInhabil = "";
	public String CortarLlamada = "";
	public String RuteoFueraHorario = "";
	public String DerivaRamaIVR = "";
	public String PromptFueraHorario = "";
	public String PromptTiempoEspera = "";
	public String MarcaContingencia = "";

	//Uso de getMensajesRamaIVR()
	public String MensajesRamaIVR = "";

	//Uso de getSecuencia
	public String Secuencia = "0";

	//Uso para Validacion de Falla Masiva en Soporte T�cnico
	public String FallaMasiva = "";
	public String Prompt = "";
	public String Tecnologia = "";

	//Uso para Recuperar datos del cliente 
	public String Zona = "";
	public String Localidad = "";
	public String Nodo = "";
	public String Subnodo = "";

	//Uso para Validar si se debe entregar Sub-Meu de Futbol en Soporte Tecnico Cable 
	public String EntregaSubMenu = "";

	//Uso para Valida si entrega mensaje de Poseidon (Cambio de Grilla)
	public String EntregaMsg = "";

	//Uso para Valida si cliente se encuentra en tabla FOXCDF
	public String Salida = "";
	public String CallCenter = "";

	//Uso para Rescatar la descripcion de la sucursal para Loquendo
	public String Descripcion = "";
	//los uso para rescatar el codigo de area
	public String NumeroTelefono = "";
	//los uso para la segmentacion
	public String Segmentacion ="" ;
	public String Motivo ="";
	public String VectorCodigoServicio="1";
	public String NombreCallCenter="";

	//Proyecto Virtual Queue
	public String VirtualQueue="";
	public String VirtualQueueName="";
	public String CLIENTEVQ="";
	public String SuperaUmbral="";
	public String Umbral="";
	public String Habilidad="";
	public String UmbralSuperior="";
	public String UmbralInferior="";
	public String LimiteInferior="";
	public String LimiteSuperior="";

	//Proyecto Mensaje Clientes
	public String Masivo="";
	public String AudioMasivo="";
	public String CorteMasivo="";
	public String ClientesBD="";
	public String CorteBD="";
	public String AudioMenu="";
	public String AudioMensaje="";
	public String SubMenu="";

	public String MasivoxBase="";
	public String Audio_MasivoxBase="";
	public String Corte_MasivoxBase="";
	public String MasivoxLocalidad="";
	public String Audio_MasivoxLocalidad="";
	public String Corte_MasivoxLocalidad="";
	public String id_programacionMGral="";
	public String id_programacionMxLoc="";
	public String id_programacionMxB="";
	public String id_programacionMxBD="";
	public String Ubicacion="";

	public String Evento1="";
	public String Evento2="";
	public String Evento3="";
	public String Evento4="";
	public String Evento5="";
	public String Evento6="";
	public String Evento7="";
	public String Evento8="";
	public String Evento9="";
	public String Evento10="";
	public String Evento11="";
	public String Evento12="";
	public String TransferenciaBD="";
	public String RamaTransferencia="";

	//Proyecto EPA
	public String Valida = "";


	//Variables FunctionsGVP
	public String InstanceID = "";
	protected PageContext InstancePageContext = null;
	protected Logger log;
	private String loggerName;
	public Parameters Params = new Parameters();
	public String LogFilePath = "";
	private String loggerNameError;
	public String DebugLevel = "None";
	public String DebugFilePath = "";
	public String DebugFileMaxSize = "1000";
	protected String Timezone = "America/Buenos_Aires";

	public String ErrorFilePath = "";
	//

	public LIB_IVR_BASE_VTR(String ParametersFile)
	{
		//super(ParametersFile);
		String catalina = System.getProperty("catalina.base");
		if(catalina != null)
			if (ParametersFile.equals("")){    		
				ParametersFile = catalina + "//lib//FunctionsGVP.VTR.properties";    		    		
			}else{
				ParametersFile = catalina + "//lib//"+ParametersFile;
			}
		System.out.println(ParametersFile);
		Inicializar(ParametersFile);
		//        this.InstanceID = id;              
	}

	public LIB_IVR_BASE_VTR(String ParametersFile, String CallId)
	{
		//super(ParametersFile, CallId);
		String catalina = System.getProperty("catalina.base");
		if(catalina != null)
			if (ParametersFile.equals("")){    		
				ParametersFile = catalina + "//lib//FunctionsGVP.VTR.properties";    		    		
			}else{
				ParametersFile = catalina + "//lib//"+ParametersFile;
			}		

		Inicializar(ParametersFile);
		this.InstanceID = CallId;      
	}

	public LIB_IVR_BASE_VTR(String ParametersFile, String CallId, PageContext localPageContext)
	{
		//super(ParametersFile, CallId, localPageContext);
		Inicializar(ParametersFile);
		this.InstanceID = CallId;
		this.InstancePageContext = localPageContext;

		Debug("Se ha generando nueva instancia de la clase.", "Detail");

		Debug("    InstanceID: " + InstanceID + ".", "Detail");
	}

	public LIB_IVR_BASE_VTR()
	{
		//super();
		Debug("Se ha generando nueva instancia de la clase.", "Detail");

		Debug("    InstanceID: " + InstanceID + ".", "Detail");
	}


	private void Inicializar(String ParametersFile) {

		InstanceID = (new Long((new Random()).nextLong()).toString());

		if( InstanceID.substring(0, 1).compareTo("-") == 0 )
			InstanceID = InstanceID.substring(1);

		InstanceID = "00000000000000000000" + InstanceID;
		InstanceID = InstanceID.substring(InstanceID.length() - 20);

		ReadParameters(ParametersFile);

		loggerName = Params.GetValue("LoggerName", "GVP");
		loggerNameError = Params.GetValue("LoggerNameError", "GVP_ERROR");
		DebugLevel = Params.GetValue("DebugLevel", "None");
		DebugFilePath = Params.GetValue("DebugFilePath");
		DebugFileMaxSize = Params.GetValue("DebugFileMaxSize", "20971520");

		ErrorFilePath = Params.GetValue("ErrorFilePath");
		Timezone = Params.GetValue("Timezone");

		System.out.println(loggerName);
		System.out.println(DebugFilePath);
		//InicializarLogger();
		// Debug("FunctionsGVP.Initialize - " + ParametersFile, "Detail");

	}


	public void ReadParameters(String ParametersFile) {
		if (! Params.ReadParametersFile(ParametersFile)){
			System.out.println("FunctionsGVP.Leyendo archivo de parámetros [" + ParametersFile +"]");
			System.out.println("FunctionsGVP.No se pudo leer archivo [" + ParametersFile + "]");
		}

	}



	public void WriteParameters (String ParametersFile)
	{
		Debug("Escribiendo archivo de par�metros.", "Detail");

		Params.WriteParametersFile(ParametersFile);
	}



	
	/***
	 * SP_GET_ANI_PREFIJO_CLIENTE
	 * @param datosEntrada(ANI)
	 * @param datosEntrada(APLICACION)
	 * @param datosEntrada(ACCION)
	 */



	public Map<String, String> ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");


		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";


		try { 
			
			JSONObject DataEnvioSckt = new JSONObject();
    		DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_ANI_PREFIJO_CLIENTE","GESCOM"));
    		DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_ANI_PREFIJO_CLIENTE","SP_GET_ANI_PREFIJO_CLIENTE"));
    		DataEnvioSckt.put("parameters",datosEntrada.getProperty("ANI")+"|"+datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("ACCION"));
    		DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_ANI_PREFIJO_CLIENTE","1"));
    		DataEnvioSckt.put("requestID",this.InstanceID);					

    		Debug("[ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
        	String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_ANI_PREFIJO_CLIENTE","IVRBASE"));
       // 	Debug("[ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE] - respuestaDB: "+respuestaDB,"INFO");

        	if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA_"+y, sArregloData[y]);
          //06.11.18  		Debug("[ejecutarSP_GET_ANI_PREFIJO_CLIENTE] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}



		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE] Exception "+ex.getMessage(), "DEBUG");
			//retorno.put("CODMSG", "Error");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE] FIN", "INFO");
		}
	//06.11.18	Debug("[ejecutarSP_SP_GET_ANI_PREFIJO_CLIENTE] RETORNO "+retorno, "DEBUG");
		return retorno;
	}


	
	/***
	 * SP_GET_HELPDESK
	 * @param datosEntrada(DNIS)
	 * @param datosEntrada(APLICACION)
	 */



	public Map<String, String> ejecutarSP_SP_GET_HELPDESK(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");


		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";


		try { 
			
			JSONObject DataEnvioSckt = new JSONObject();
    		DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_HELPDESK","GESCOM"));
    		DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_HELPDESK","SP_GET_HELPDESK"));
    		DataEnvioSckt.put("parameters",datosEntrada.getProperty("DNIS")+"|"+datosEntrada.getProperty("APLICACION"));
    		DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_HELPDESK","1"));
    		DataEnvioSckt.put("requestID",this.InstanceID);					

    		Debug("[ejecutarSP_SP_GET_HELPDESK] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
        	String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_HELPDESK","IVRBASE"));
      //  	Debug("[ejecutarSP_SP_GET_HELPDESK] - respuestaDB: "+respuestaDB,"INFO");

        	if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA_"+y, sArregloData[y]);
       //     		Debug("[ejecutarSP_SP_GET_HELPDESK] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}



		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_HELPDESK] Exception "+ex.getMessage(), "DEBUG");
			//retorno.put("CODMSG", "Error");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_HELPDESK] FIN", "INFO");
		}
		Debug("[ejecutarSP_SP_GET_HELPDESK] RETORNO "+retorno, "DEBUG");
		return retorno;
	}

	
	
	
	

	/***
	 * SP_GET_DATA_DNIS_APP
	 * @param datosEntrada(dnis)
	 */



	public Map<String, String> ejecutarSP_SP_GET_DATA_DNIS_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");


		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";


		try { 
			
			JSONObject DataEnvioSckt = new JSONObject();
    		DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_DATA_DNIS_APP","GESCOM"));
    		DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_DATA_DNIS_APP","SP_GET_DATA_DNIS_APP"));
    		DataEnvioSckt.put("parameters",datosEntrada.getProperty("DNIS"));
    		DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_DATA_DNIS_APP","1"));
    		DataEnvioSckt.put("requestID",this.InstanceID);					

    		Debug("[ejecutarSP_SP_GET_DATA_DNIS_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
        	String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_DATA_DNIS_APP","IVRBASE"));
    //    	Debug("[ejecutarSP_SP_GET_DATA_DNIS_APP] - respuestaDB: "+respuestaDB,"INFO");

        	if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA_"+y, sArregloData[y]);
       //     		Debug("[ejecutarSP_GET_DATA_DNIS_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}



		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_DATA_DNIS_APP] Exception "+ex.getMessage(), "DEBUG");
			//retorno.put("CODMSG", "Error");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_DATA_DNIS_APP] FIN", "INFO");
		}
		//06.11.18 Debug("[ejecutarSP_SP_GET_DATA_DNIS_APP] RETORNO "+retorno, "DEBUG");
		return retorno;
	}




	/***
	 * SP ejecutarSP_SP_GET_INTENTOS_APP
	 * 
	 * @param datosEntrada
	 *            (aplicacion,mercado,idioma)
	 */


	public Map<String, String> ejecutarSP_SP_GET_INTENTOS_APP(
			Properties datosEntrada) throws IOException {

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try {
			String servicio = "GESCOM";
			String queryIvrToDB = "SP_GET_INTENTOS_APP";
			String paramsIvrToDB = datosEntrada.getProperty("APLICACION") + "|"
					+ datosEntrada.getProperty("SEGMENTO");
			//String outParamsIvrToDB = "NumMensaje|Mensaje|cursor";
			String dataTypesIvrToDB = "string|string";

			Debug("[ejecutarSP_SP_GET_INTENTOS_APP] INICIO " + servicio + " - "
					+ queryIvrToDB, "INFO");

	
			//OPCION 2
			JSONObject DataEnvioSckt = new JSONObject();
    		DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_INTENTOS_APP","GESCOM"));
    		DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_INTENTOS_APP","SP_GET_INTENTOS_APP"));
    		DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO"));
    		DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_INTENTOS_APP","1"));
    		DataEnvioSckt.put("requestID",this.InstanceID);					

    		Debug("[ejecutarSP_SP_GET_INTENTOS_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
        	String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_INTENTOS_APP","IVRBASE"));
     //   	Debug("[ejecutarSP_SP_GET_INTENTOS_APP] - respuestaDB: "+respuestaDB,"INFO");

        	if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		retorno.put("DATA", jObjResp.get("DATA").toString());
        	}
        	
       
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_INTENTOS_APP] Exception "
					+ ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		} finally {
			Debug("[ejecutarSP_SP_GET_INTENTOS_APP] FIN", "INFO");
		}

		return retorno;
	}




	/***
	 * SP ejecutarSP_GET_PARAM_APP
	 * @param datosEntrada(aplicacion,mercado,idioma)
	 */



	public Map<String, String> ejecutarSP_GET_PARAM_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
			String servicio = "GESCOM";
			String queryIvrToDB = "SP_GET_PARAM_APP";
			String paramsIvrToDB = datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO")+"|"+datosEntrada.getProperty("TIPOSOCIO");
			//String outParamsIvrToDB = "NumMensaje|Mensaje|cursor";
			String dataTypesIvrToDB = "string|string|string";

			Debug("[ejecutarSP_GET_PARAM_APP] INICIO "+servicio+" - "+queryIvrToDB, "INFO");

		
			//OPCION 2
			    JSONObject DataEnvioSckt = new JSONObject();
			    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_PARAM_APP","GESCOM"));
			    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_PARAM_APP","SP_GET_PARAM_APP"));
			    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO"));//+"|"+datosEntrada.getProperty("TIPOSOCIO"));
			    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_PARAM_APP","1"));
			    DataEnvioSckt.put("requestID",this.InstanceID);					

			    Debug("[ejecutarSP_GET_PARAM_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
			    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_PARAM_APP","IVRBASE"));
			//    Debug("[ejecutarSP_GET_PARAM_APP] - respuestaDB: "+respuestaDB,"INFO");

			    if(!respuestaDB.equals("[]")){
	        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
	        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
	        		retorno.put("MSG", jObjResp.get("MSG").toString());
	        		retorno.put("DATA", jObjResp.get("DATA").toString());
	        		
	        		
	        	}
			

		} catch (Exception ex) {
			Debug("[ejecutarSP_GET_PARAM_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_GET_PARAM_APP] FIN", "INFO");
		}

		return retorno;
	}



	
	/***
	 * SP ejecutarSP_GET_PARAM_MARCA_APP
	 * @param datosEntrada(aplicacion,mercado,idioma)
	 */



	public Map<String, String> ejecutarSP_GET_PARAM_MARCA_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
			String servicio = "GESCOM";
			String queryIvrToDB = "SP_GET_PARAM_MARCA_APP";
			String paramsIvrToDB = datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO")+"|"+datosEntrada.getProperty("TIPOSOCIO");
			//String outParamsIvrToDB = "NumMensaje|Mensaje|cursor";
			String dataTypesIvrToDB = "string|string|string";

			Debug("[ejecutarSP_GET_PARAM_MARCA_APP] INICIO "+servicio+" - "+queryIvrToDB, "INFO");

		
			    JSONObject DataEnvioSckt = new JSONObject();
			    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_PARAM_MARCA_APP","GESCOM"));
			    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_PARAM_MARCA_APP","SP_GET_PARAM_MARCA_APP"));
			    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO"));//+"|"+datosEntrada.getProperty("TIPOSOCIO"));
			    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_PARAM_MARCA_APP","1"));
			    DataEnvioSckt.put("requestID",this.InstanceID);					

			    Debug("[ejecutarSP_GET_PARAM_MARCA_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
			    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_PARAM_MARCA_APP","IVRBASE"));
//			    Debug("[ejecutarSP_GET_PARAM_MARCA_APP] - respuestaDB: "+respuestaDB,"INFO");

			    if(!respuestaDB.equals("[]")){
	        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
	        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
	        		retorno.put("MSG", jObjResp.get("MSG").toString());
	        		retorno.put("DATA", jObjResp.get("DATA").toString());
	        		
	        	}
			

		} catch (Exception ex) {
			Debug("[ejecutarSP_GET_PARAM_MARCA_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_GET_PARAM_MARCA_APP] FIN", "INFO");
		}

		return retorno;
	}
	


	/***
	 * SP ejecutarSP_SP_GET_DESTINO_APP
	 * @param datosEntrada(aplicacion,mercado,idioma)
	 */



	public Map<String, String> ejecutarSP_SP_GET_DESTINO_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
			
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_DESTINO_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_DESTINO_APP","SP_GET_DESTINO_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO")+"|"+datosEntrada.getProperty("ORIGEN")+"|"+datosEntrada.getProperty("NIVEL_ORIGEN"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_DESTINO_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_GET_DESTINO_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_PARAM_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_SP_GET_DESTINO_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
      //      		Debug("[ejecutarSP_SP_GET_DESTINO_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_DESTINO_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_DESTINO_APP] FIN", "INFO");
		}
		return retorno;
	}




	/***
	 * SP ejecutarSP_SP_GET_AUDIO_APP
	 * @param datosEntrada(aplicacion,mercado,idioma)
	 */



	public LinkedHashMap<String, String> ejecutarSP_SP_GET_AUDIO_APP(Properties datosEntrada) throws IOException{

		LinkedHashMap<String, String> retorno = new LinkedHashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";
		int z=0;
		try { 
		
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_AUDIO_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_AUDIO_APP","SP_GET_AUDIO_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("MENU_NIVEL")+"|"+datosEntrada.getProperty("SEGMENTO"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_AUDIO_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_GET_AUDIO_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_AUDIO_APP","IVRBASE"));
		    Debug("[ejecutarSP_SP_GET_AUDIO_APP] - respuestaDB: "+respuestaDB,"INFO");

		    
		    String sOpcionValida="";
		    String sAudioOpcionValida="";
		    String sAudioOpcion="";
		    String sDescAudioOpcion="";
		    
		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\;");
        		Debug("[ejecutarSP_SP_GET_AUDIO_APP] sArregloData length: "+sArregloData.length, "INFO");
        		for(int y=0;y<sArregloData.length;y++){
        //			Debug("[ejecutarSP_SP_GET_AUDIO_APP] sArregloData: "+sArregloData[y], "INFO");
            		
            		//0|Silencio.wav|Premenu.wav|Para ayuda en//;1|Marque1.wav|IVR84600_66379_0001.wav|Para soporte en Tarjetas de credito;
            		//	if(z<sArregloData.length){//for(int z=0;z<sArregloDataReg.length;z++){
            				String[] sArregloDataReg = sArregloData[y].split("\\|");
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] sArregloDataReg: "+sArregloDataReg[z], "INFO");
            				sOpcionValida=sOpcionValida+sArregloDataReg[0]+";";
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] sOpcionValida: "+sOpcionValida, "INFO");
            				sAudioOpcionValida=sAudioOpcionValida+sArregloDataReg[1]+";";
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] sAudioOpcionValida: "+sAudioOpcionValida, "INFO");
            				sAudioOpcion=sAudioOpcion+sArregloDataReg[2]+";";
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] sAudioOpcion: "+sAudioOpcion, "INFO");
            				sDescAudioOpcion=sDescAudioOpcion+sArregloDataReg[3]+";";
           // 				Debug("[ejecutarSP_SP_GET_AUDIO_APP] sDescAudioOpcion: "+sDescAudioOpcion, "INFO");
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] valor z: "+z, "INFO");
          //  				Debug("[ejecutarSP_SP_GET_AUDIO_APP] valor y: "+y, "INFO");
            		//	}
            			//z++;
            	}
        		Debug("[ejecutarSP_SP_GET_AUDIO_APP] sOpcionValida: "+sOpcionValida, "INFO");
        		Debug("[ejecutarSP_SP_GET_AUDIO_APP] sAudioOpcionValida: "+sAudioOpcionValida, "INFO");
        		Debug("[ejecutarSP_SP_GET_AUDIO_APP] sAudioOpcion: "+sAudioOpcion, "INFO");
        		Debug("[ejecutarSP_SP_GET_AUDIO_APP] sDescAudioOpcion: "+sDescAudioOpcion, "INFO");
        		
        		retorno.put("OPCION", sOpcionValida);
        		retorno.put("OPC_AUDIO", sAudioOpcionValida);
        		retorno.put("NOM_AUDIO", sAudioOpcion);
        		retorno.put("DESC_AUDIO", sDescAudioOpcion);
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_AUDIO_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_AUDIO_APP] FIN", "INFO");
		}
		return retorno;
	}


	/***
	 * SP_GET_OPC_NIVEL_APP
	 * @param datosEntrada(categoria,mercado,idioma.fidelidad)
	 */


	public Map<String, String> ejecutarSP_SP_GET_OPC_NIVEL_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
	
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_OPC_NIVEL_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_OPC_NIVEL_APP","SP_GET_OPC_NIVEL_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("NIVEL")+"|"+datosEntrada.getProperty("OPCION")+"|"+datosEntrada.getProperty("APLICACION")+"|"+datosEntrada.getProperty("SEGMENTO"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_OPC_NIVEL_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_OPC_NIVEL_APP","IVRBASE"));
		//    Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] DATA: "+jObjResp.get("DATA").toString(), "INFO");
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
        //    		Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] FIN", "INFO");
		}
		Debug("[ejecutarSP_SP_GET_OPC_NIVEL_APP] RETORNO "+retorno, "DEBUG");
		return retorno;
	}





	

public Map<String, String> ejecutarSP_SP_TRANSFER_APP(Properties datosEntrada){

	Map<String, String> retorno = new HashMap<String, String>();
	retorno.put("MSG", "");
	retorno.put("errorMessage", "");
	
	try { 
		
	    JSONObject DataEnvioSckt = new JSONObject();
	    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_TRANSFER_APP","GESCOM"));
	    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_TRANSFER_APP","SP_GET_TRANSFER_APP"));
	    DataEnvioSckt.put("parameters",datosEntrada.getProperty("SEGMENTO")+"|"+datosEntrada.getProperty("OPCION")+"|"+datosEntrada.getProperty("APLICACION"));
	    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_TRANSFER_APP","1"));
	    DataEnvioSckt.put("requestID",this.InstanceID);					

	    Debug("[ejecutarSP_SP_GET_TRANSFER_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
	    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_HORARIO_APP","IVRBASE"));
	//    Debug("[ejecutarSP_SP_GET_TRANSFER_APP] - respuestaDB: "+respuestaDB,"INFO");

	    if(!respuestaDB.equals("[]")){
    		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
    		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
    		retorno.put("MSG", jObjResp.get("MSG").toString());
    		Debug("[ejecutarSP_SP_GET_TRANSFER_APP] DATA: "+jObjResp.get("DATA").toString(), "INFO");
    		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
    		for(int y=0;y<sArregloData.length;y++){
        		retorno.put("DATA"+"_"+y, sArregloData[y]);
     //   		Debug("[ejecutarSP_SP_GET_TRANSFER_APP] : "+retorno.get("DATA_"+y), "INFO");
        	}
    		retorno.put("CANT-DATA", sArregloData.length+"");
    	}
		
	} catch (Exception ex) {
		Debug("[ejecutarSP_SP_GET_TRANSFER_APP] Exception "+ex.getMessage(), "DEBUG");
		retorno.put("COD-RETORNO", "-1");
		retorno.put("MSG", ex.getMessage());
	}finally{
		Debug("[ejecutarSP_SP_GET_TRANSFER_APP] FIN", "INFO");
	}
	
	
	return retorno;
}
	 



	public Map<String, String> ejecutarSP_SP_SET_LOG_NAV_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");
		try { 
	
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_SET_LOG_NAVEGA_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_SET_LOG_NAVEGA_APP","SP_SET_LOG_NAVEGA_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("ASTERISKID")+"|"+datosEntrada.getProperty("CONNECTIONID")+"|"+datosEntrada.getProperty("FECHAHORA_INICIO")+"|"+datosEntrada.getProperty("FECHAHORA_FIN")+"|"+datosEntrada.getProperty("CLAVE")+"|"+datosEntrada.getProperty("VALOR"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_SET_LOG_NAVEGA_APP","0"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_SET_LOG_NAVEGA_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			

		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] FIN", "INFO");
		}
		Debug("[ejecutarSP_SP_SET_LOG_NAV_APP] RETORNO "+retorno, "DEBUG");
		return retorno;
	}




	public Map<String, String> ejecutarSP_SP_GET_FFHHFIN_APP() throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
	
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_FFHHFIN_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_FFHHFIN_APP","SP_GET_FFHHFIN_APP"));
		    DataEnvioSckt.put("parameters","");
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_FFHHFIN_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_GET_FFHHFIN_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_FFHHFIN_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_SP_GET_FFHHFIN_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_SP_GET_FFHHFIN_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_FFHHFIN_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_FFHHFIN_APP] FIN", "INFO");
		}
		return retorno;
	}




	public Map<String, String> ejecutarSP_GET_KEY_LOG_NAVEGA_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
		
			Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] - Envio Data: "+Params.GetValue("SERVICIO_GET_KEY_LOG_NAVEGA_APP","GESCOM"),"INFO");
			Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] - Envio Data: "+Params.GetValue("QUERY_GET_KEY_LOG_NAVEGA_APP","SP_GET_KEY_LOG_NAVEGA_APP"),"INFO");
			
			//OPCION 2
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_KEY_LOG_NAVEGA_APP","VTR_ECONTACT"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_KEY_LOG_NAVEGA_APP","SP_GET_KEY_LOG_NAVEGA_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_KEY_LOG_NAVEGA_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_KEY_LOG_NAVEGA_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.get("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("KEY_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] : "+retorno.get("KEY_"+y), "INFO");
            	}
        		retorno.put("CANT-KEY", sArregloData.length+"");
        	}
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_GET_KEY_LOG_NAVEGA_APP] FIN", "INFO");
		}
		return retorno;
	}


	
	/***
	 * SP_GET_OPC_NIVEL_APP
	 * @param datosEntrada(categoria,mercado,idioma.fidelidad)
	 */


	public Map<String, String> ejecutarSP_SP_GET_HORARIO_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
	
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_HORARIO_APP","GESCOM"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_HORARIO_APP","SP_GET_HORARIO_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("SEGMENTO")+"|"+datosEntrada.getProperty("OPCION")+"|"+datosEntrada.getProperty("APLICACION"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_HORARIO_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_SP_GET_HORARIO_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_HORARIO_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_SP_GET_HORARIO_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		Debug("[ejecutarSP_SP_GET_HORARIO_APP] DATA: "+jObjResp.get("DATA").toString(), "INFO");
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_SP_GET_HORARIO_APP] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_SP_GET_HORARIO_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_SP_GET_HORARIO_APP] FIN", "INFO");
		}
		Debug("[ejecutarSP_SP_GET_HORARIO_APP] RETORNO "+retorno, "DEBUG");
		return retorno;
	}
	
	
	
	/***
	 * SP_DERIVACION_PORCENTUAL_DB
	 * @param datosEntrada(TENANT,GDIST,FECHADERIVA)
	 */


	public Map<String, String> ejecutarSP_DERIVACION_PORCENTUAL(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
	
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("DERIVACION_PORCENTUAL_DB","DERIVACION_PORCENTUAL"));
			DataEnvioSckt.put("query",Params.GetValue("DERIVACION_PORCENTUAL_SP","TRATAMIENTO.DERIVACION_PORCENTUAL_NAME_GDIST"));
			DataEnvioSckt.put("parameters",datosEntrada.getProperty("TENANT")+"|"+datosEntrada.getProperty("GDIST")+"|"+datosEntrada.getProperty("FECHADERIVA"));//iTenant+"|"+GDIST+"|"+dateFormat.format(date));
			DataEnvioSckt.put("select",Params.GetValue("SELECT_DERIVACION_PORCENTUAL","1"));
			DataEnvioSckt.put("requestID",this.InstanceID);				

		    Debug("[ejecutarSP_DERIVACION_PORCENTUAL] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_DERIVACION_PORCENTUAL","DB"));
	//	    Debug("[ejecutarSP_DERIVACION_PORCENTUAL] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		Debug("[ejecutarSP_DERIVACION_PORCENTUAL] DATA: "+jObjResp.get("DATA").toString(), "INFO");
        		String[] sArregloData = jObjResp.getString("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("DATA"+"_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_DERIVACION_PORCENTUAL] : "+retorno.get("DATA_"+y), "INFO");
            	}
        		retorno.put("CANT-DATA", sArregloData.length+"");
        	}
			
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_DERIVACION_PORCENTUAL] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_DERIVACION_PORCENTUAL] FIN", "INFO");
		}
		Debug("[ejecutarSP_DERIVACION_PORCENTUAL] RETORNO "+retorno, "DEBUG");
		return retorno;
	}

	
	
	public Map<String, String> ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP(Properties datosEntrada) throws IOException{

		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("MSG", "");
		retorno.put("errorMessage", "");

		String jsonresponse = "";
		int rc = -1;
		String data = "";
		String msg = "";
		String returncode = "0";

		try { 
		
			Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] - Envio Data: "+Params.GetValue("SERVICIO_GET_KEY_REGISTRA_CDR_EXT_APP","GESCOM"),"INFO");
			Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] - Envio Data: "+Params.GetValue("QUERY_GET_KEY_GET_REGISTRA_CDR_EXT_APP","SP_GET_KEY_GET_REGISTRA_CDR_EXT_APP"),"INFO");
			
			//OPCION 2
		    JSONObject DataEnvioSckt = new JSONObject();
		    DataEnvioSckt.put("servicio",Params.GetValue("SERVICIO_GET_KEY_REGISTRA_CDR_EXT_APP","VTR_ECONTACT"));
		    DataEnvioSckt.put("query",Params.GetValue("QUERY_GET_KEY_GET_REGISTRA_CDR_EXT_APP","SP_GET_KEY_REGISTRA_CDR_EXT_APP"));
		    DataEnvioSckt.put("parameters",datosEntrada.getProperty("APLICACION"));
		    DataEnvioSckt.put("select",Params.GetValue("SELECT_GET_KEY_REGISTRA_CDR_EXT_APP","1"));
		    DataEnvioSckt.put("requestID",this.InstanceID);					

		    Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] - Envio Data: "+DataEnvioSckt.toString(),"INFO");
		    String respuestaDB=Socket_SendRecvHA(DataEnvioSckt.getString("query"),DataEnvioSckt.toString(),Params.GetValue("TIPO_GET_KEY_REGISTRA_CDR_EXT_APP","IVRBASE"));
	//	    Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] - respuestaDB: "+respuestaDB,"INFO");

		    if(!respuestaDB.equals("[]")){
        		JSONObject jObjResp = new JSONObject(respuestaDB.replaceAll("\\[","").replaceAll("\\]",""));
        		retorno.put("COD-RETORNO", jObjResp.get("RC").toString());
        		retorno.put("MSG", jObjResp.get("MSG").toString());
        		String[] sArregloData = jObjResp.get("DATA").toString().split("\\|");
        		for(int y=0;y<sArregloData.length;y++){
            		retorno.put("KEYCDR_"+y, sArregloData[y]);
    //        		Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] : "+retorno.get("KEYCDR_"+y), "INFO");
            	}
        		retorno.put("CANT-KEY", sArregloData.length+"");
        	}
			
			
			
		} catch (Exception ex) {
			Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] Exception "+ex.getMessage(), "DEBUG");
			retorno.put("COD-RETORNO", "-1");
			retorno.put("MSG", ex.getMessage());
		}finally{
			Debug("[ejecutarSP_GET_KEY_REGISTRA_CDR_EXT_APP] FIN", "INFO");
		}
		return retorno;
	}


	
	
	
	


	public boolean Debug(String Message) {
    	return Debug(Message, "INFO");
    }
	/**
	 * Permite registrar mensajes en un archivo de log.
	 * Los parámetros para generar el archivo de log están definidos en el constructor utilizado.
	 * @param Message Mensaje a registrar en archivo de log.
	 * @param Level Nivel de log (None, Standard, Trace, Detail)
	 * @return True: Éxito<br>
	 * False: Error
	 */
	public boolean Debug(String Message, String Level) {
		logger(Message, Level);
		return true;
	}

	public void logger(String Message, String Level){    	    	

		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone(Timezone));
		Date curDate = new Date();
		//CREA UN ARCHIVO POR DIA (Y ES BASE PARA OTROS LOGS)
		String DateToStr = format.format(curDate);
		SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
		formatHora.setTimeZone(TimeZone.getTimeZone(Timezone));
		String Hora = formatHora.format(curDate);
		log = Logger.getLogger(loggerName);
		Message = DateToStr+ " "+Hora+" "+Rellena(Level, " ", 10, 1)+" ["+this.InstanceID + "] " + Message;       
		if (DebugLevel.equalsIgnoreCase("Detail") || DebugLevel.equalsIgnoreCase("Trace") || DebugLevel.equalsIgnoreCase("DEBUG")){
			log.debug(Message);
		}else{
			if (Level.equalsIgnoreCase("Standard") || Level.equalsIgnoreCase("INFO")){
				log.debug(Message);
			}
		}
	}


	//Socket
	/**
	 * <div align="justify">Funci�n para conexi�n via socket.</div><br>  
	 * @param String Server IP servidor
	 * @param int Port   Puerto TCP
	 * @param int Timeout   Tiempo de espera
	 * @param String Transaccion  Nombre de transacci�n.
	 * @param String Message2Send  Mensaje de Entrada
	 * @return String Mensaje de Salida
	 */

	public String Socket_SendRecv (String Server, int Port, int Timeout, String Transaccion, String Message2Send)throws Exception
	{
		String sReturn = "";
		Socket SocketServer = null;
		//DataOutputStream SocketServerOutputStream = null;
		PrintWriter SocketServerOutputStream = null;
		// DataInputStream SocketServerInputStream = null;
		BufferedReader SocketServerInputStream = null;

		try
		{

			//Date startTime;
			//Date stopTime;

			Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] - Se ejecutar� la transacci�n : " + Transaccion + " - " + Server + ":" + Port + " - TimeOut:" + String.valueOf(Timeout), "Detail");
			Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] -  > Data [" + Message2Send + "]", "Detail");

			//startTime = new Date();

			try
			{
				SocketServer = new Socket(Server, Port);
				SocketServer.setKeepAlive(true);
				SocketServer.setSoTimeout(Timeout);

				// SocketServerInputStream = new DataInputStream(SocketServer.getInputStream());
				SocketServerInputStream = new BufferedReader(new InputStreamReader(SocketServer.getInputStream()));
				//SocketServerOutputStream = new DataOutputStream(SocketServer.getOutputStream());
				SocketServerOutputStream = new PrintWriter( new OutputStreamWriter(SocketServer.getOutputStream()),true);
			}
			catch (IOException e)
			{
				Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] - IOException openning stream buffers: [" + e.toString() + "]", "Standard");
				throw ((Exception) e);
			}

			if (SocketServer != null && SocketServerOutputStream != null && SocketServerInputStream != null)
			{
				try
				{
					//SocketServerOutputStream.writeBytes(Message2Send);
					SocketServerOutputStream.println(Message2Send);
					sReturn = SocketServerInputStream.readLine();
	//06.11.18 		Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] - Respuesta [" + sReturn + "]", "Detail");
					//06.11.18
					System.out.println("    Respuesta [" + sReturn + "]"+ "Detail");
				}
				catch (Exception e)
				{
					Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] - Exception Writing Message/Getting Answer: [" + e.toString() + "]", "Standard");
					throw (e);
				}
			}
			//stopTime = new Date ();

			//Registrar(Message2Send, startTime, sReturn, stopTime);

			return sReturn;
		}
		catch (Exception e)
		{
			Debug("[econtact.action.FunctionsGESCOM_VTR.Socket_SendRecv] - Exception sending message: [" + e.toString() + "]", "Standard");
			throw (e);
		}
		finally {
			SocketServerOutputStream.close();
			SocketServerInputStream.close();
			SocketServer.close();
		}
	}

	public String Socket_SendRecv (String Server, String Port, String Timeout, String Transaccion, String Message2Send)
	{
		String sReturn = "";

		try
		{
			System.out.println("entro a al try de socket_sendRecv");
			Socket SocketServer = null;
			DataOutputStream SocketServerOutputStream = null;
			// DataInputStream SocketServerInputStream = null;
			BufferedReader SocketServerInputStream = null;

			System.out.println("");
			System.out.println("Se ejecutar� la transacci�n " + Transaccion + " - " + Server + ":" + Port+ "Detail");
			Debug("Se ejecutar� la transacci�n " + Transaccion + " - " + Server + ":" + Port, "Detail");
			System.out.println(" > Data [" + Message2Send + "]"+ "Detail");
			Debug(" > Data [" + Message2Send + "]", "Detail");

			try
			{
				SocketServer = new Socket(Server, Integer.parseInt(Port));
				SocketServerOutputStream = new DataOutputStream(SocketServer.getOutputStream());
				// SocketServerInputStream = new DataInputStream(SocketServer.getInputStream());
				SocketServerInputStream = new BufferedReader(new InputStreamReader(SocketServer.getInputStream()));
			}
			catch (IOException e)
			{   System.out.println("");
			System.out.println("    Couldn't get I/O for the socket connection");
			Debug("    Couldn't get I/O for the socket connection", "Trace");
			}

			if (SocketServer != null && SocketServerOutputStream != null && SocketServerInputStream != null)
			{
				try
				{
					SocketServerOutputStream.writeBytes(Message2Send);
					SocketServer.setSoTimeout(Integer.parseInt(Timeout));  // Setting 10 secs Timeout.
					sReturn = SocketServerInputStream.readLine();
					//06.11.18 Debug("    Respuesta [" + sReturn + "]", "Detail");
					System.out.println("");
					System.out.println("    Respuesta [" + sReturn + "]"+ "Detail");
				}
				catch (IOException e)
				{

					System.out.println("");
					System.out.println("    I/O failed on the socket connection");
					Debug("    I/O failed on the socket connection", "Trace");
				}
			}

			try
			{
				SocketServerOutputStream.close();
				SocketServerInputStream.close();
				SocketServer.close();
			}
			catch (Exception e)
			{
			}
		}
		catch (Exception e)
		{
			System.out.println("");
			System.out.println("    Servicio: " + Transaccion + ". Problemas al enviar mensaje: " + e.getMessage() + "."+ "Detail");
			Debug("    Servicio: " + Transaccion + ". Problemas al enviar mensaje: " + e.getMessage() + ".", "Detail");
			e.printStackTrace();
		}      
		System.out.println("");
		System.out.println("RESPUESTA:"+sReturn);
		return sReturn;
	}


	public String Secure_Socket_SendRecv (String Server, String Port, String Timeout, String Transaccion, String Message2Send)
	{
		String sReturn = "";

		try
		{
			int largo = Message2Send.length();

			Message2Send = String.format("%05d", largo + 5) + Message2Send;

			sReturn = Socket_SendRecv (Server, Port, Timeout, Transaccion, Message2Send);
			Debug("    Secure Respuesta [" + sReturn + "]", "Detail");
		}
		catch (Exception e)
		{
			Debug("    I/O failed on the secure socket connection", "Trace");
		}

		return sReturn;
	}

//PARA EL IVR GESCOM
	public String Socket_SendRecvHA(String Transaccion, String Message2Send, String Tipo)
	{
		String sReturn = "";
		String ServerPRI = this.Params.GetValue("SocketServerHostPRI_" + Tipo, "172.17.233.99");
		String ServerBKP = this.Params.GetValue("SocketServerHostBKP_" + Tipo, "172.17.233.99");
		int PortPRI = Integer.parseInt(this.Params.GetValue("SocketServerPortPRI_" + Tipo, "50091").trim());
		int PortBKP = Integer.parseInt(this.Params.GetValue("SocketServerPortBKP_" + Tipo, "50091").trim());
		int TimeoutPRI = Integer.parseInt(this.Params.GetValue("SocketServerTimeoutPRI_" + Tipo, "3000").trim());
		int TimeoutBKP = Integer.parseInt(this.Params.GetValue("SocketServerTimeoutBKP_" + Tipo, "3000").trim());

		Debug("[econtact.FunctionsGESCOM_VTR.Socket_SendRecvHA] Conectando con SocketServer Primario.", "Detail");

		Date startTime = new Date();
		try
		{
			sReturn = Socket_SendRecv(ServerPRI, PortPRI, TimeoutPRI, Transaccion, Message2Send);
		}
		catch (Exception e)
		{
			Debug("[econtact.FunctionsGESCOM_VTR.Socket_SendRecvHA] Conexion SocketServer Primario fallida, intentando con SocketServer Backup.", "Standard");
			try
			{
				sReturn = Socket_SendRecv(ServerBKP, PortBKP, TimeoutBKP, Transaccion, Message2Send);
			}
			catch (Exception e1)
			{
				Debug("[econtact.FunctionsGESCOM_VTR.Socket_SendRecvHA] Intentos superados.", "Standard");
			}
		}
		Date stopTime = new Date();

		//07.02.19 Registrar(Message2Send, startTime, sReturn, stopTime);

		return sReturn;
	}


	/** Metodo para rellenar cadenas
	 * Orden = 1:derecha   2:izquierda
	 * @params valor String
	 * @params caracter String
	 * @params largo Int
	 * @params orden Int
	 * @return valor
	 */
	public String Rellena(String valor, String caracter, int largo, int orden)
	{
		int largoV = valor.length();

		if(orden == 1)
		{
			for(int i = largoV; i<largo; i++)
			{
				valor = valor + caracter;
			}
		}
		else
		{
			for(int i = largoV; i<largo; i++)
			{
				valor = caracter + valor;
			}
		}

		return valor;
	}


/*	public boolean Registrar(String Message2send, Date time2Send, String Response, Date responseTime)
	{
		String RegMessage = "";

		//TimeZone tz = TimeZone.getTimeZone(this.Params.GetValue("timeZone", "Chile/Continental"));
		TimeZone tz = TimeZone.getDefault();

		SimpleDateFormat DateFormatter = new SimpleDateFormat("dd-MMM-yyyy;HH:mm:ss.SSS");
		DateFormatter.setTimeZone(tz);
		String DateString = DateFormatter.format(time2Send);

		long Duracion = responseTime.getTime() - time2Send.getTime();

		String IdAPP = lookUp(this.InstancePageContext, "Log_App");
		IdAPP = (IdAPP.equals("")) || (IdAPP.equals("0")) ? lookUp(this.InstancePageContext, "APPID") : IdAPP;

		String sMercado = lookUp(this.InstancePageContext, "Var_Tipo_Temp");
		String sSegmento = lookUp(this.InstancePageContext, "Var_Tipo_Pcs");
		String sMovil = lookUp(this.InstancePageContext, "Var_Pcs");
		String sOrigen = lookUp(this.InstancePageContext, "Var_Tipo_Call");

		RegMessage = RegMessage + DateString + ";" + this.InstanceID + ";" + IdAPP + ";" + sMovil + ";" + sMercado + ";" + sSegmento + ";" + sOrigen + ";";

		RegMessage = RegMessage + Message2send + ";" + Response + ";" + Long.toString(Duracion) + ";" + "\n";
		try
		{
			String RegMQFilePath = this.Params.GetValue("RegMQFilePath", "RegMQ");

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setTimeZone(tz);

			RegMQFilePath = RegMQFilePath + "_" + df.format(new Date()) + ".csv";

			File fRegMQFile = new File(RegMQFilePath);

			fRegMQFile.createNewFile();
			if (fRegMQFile.canWrite())
			{
				FileOutputStream osDebugFile = new FileOutputStream(fRegMQFile, true);

				osDebugFile.write(RegMessage.getBytes());
				osDebugFile.close();
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
*/
   
protected String lookUp(PageContext localPageContext, String ssn)
{
	String ssnVal = "";
	if (localPageContext == null) {
		return "";
	}
	if ((ssn == null) || (ssn.equals(""))) {
		return "";
	}
	ssnVal = (String)localPageContext.getSession().getAttribute(ssn);
	if (ssnVal == null) {
		ssnVal = "";
	}
	return ssnVal;
}

	
}	
	