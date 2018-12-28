package ivrbase;

import java.util.ArrayList;
import java.util.List;

public class FunctionsString {
    
    //Utilizada en ExisteLlave(String sLlavesProductos, String sLlaveBuscada)
    public String LLaveSeleccionada = "";
	
    //Constructor Default
    public FunctionsString() {
    }
    
    public int CountToken(String sLlavesProductos, String sToken) { 
        int count = 0;
        String [] arrCadenaAux = sLlavesProductos.split(";");
		for (int i=0; i < arrCadenaAux.length; i++){
			if (arrCadenaAux[i].equals(sToken))
				count++;
		}
		/*	
	        while ((sLlavesProductos != null) && (sLlavesProductos.indexOf(sToken) != -1)) {
	            count ++;
	            sLlavesProductos = sLlavesProductos.substring(sLlavesProductos.indexOf(";", sLlavesProductos.indexOf(sToken)));
	        }
	        */
        return count;
    }
    
    public String LlavesPorProducto(String sLlavesProductos, String sProducto) {
        String sFoundKeys = "";
        
        while ((sLlavesProductos != null) && (sLlavesProductos.indexOf(sProducto) != -1)) {
            sFoundKeys = sFoundKeys + sLlavesProductos.substring(sLlavesProductos.indexOf(sProducto),
                    sLlavesProductos.indexOf(";", sLlavesProductos.indexOf(sProducto))) + ";";
            sLlavesProductos = sLlavesProductos.substring(sLlavesProductos.indexOf(";", sLlavesProductos.indexOf(sProducto)));
        }
        
        return sFoundKeys;
    }
    
    //Busca si la llave ingresada existe en la lista de productos, busca coincidencia exacta. Si existe devuelve  el token + llave en LLaveSeleccionada
    public boolean ExisteLlave(String sLlavesProductos, String sLlaveBuscada) {
        boolean bRetorno = false;
        //String sTokenProducto = "";
        if (sLlavesProductos.indexOf(";")>=0){
            String[] sLlaves = sLlavesProductos.split(";");
            String[] sLlaves_temp = sLlavesProductos.split(";"); //utilizada mas adelante por ahora se mantiene intacta, esta incluye token+llaves
            
            //Primero se dejan s�lo las llaves en un array auxiliar
            for(int i=0; i < sLlaves.length; i++){
                if (sLlaves[i].indexOf("=") >= 0)  //Si trae Token
                    sLlaves[i] = sLlaves[i].substring(sLlaves[i].indexOf("=") + 1);  //Extrae llave y la deja en la misma posicion del array
            }
            
            //Quita el posible token que traiga sLlaveBuscada
            if(sLlaveBuscada.indexOf("=")>=0)
                sLlaveBuscada = sLlaveBuscada.substring(sLlaveBuscada.indexOf("=") + 1);
            
            // Ahora consulta si la llave buscada es exactamante igual a alguna de las existentes en el array
            for(int i=0; i < sLlaves.length; i++){
                if (sLlaveBuscada.equals(sLlaves[i])){
                    bRetorno =  true;
                    LLaveSeleccionada = sLlaves_temp[i];
                }
            }
            
        } else if (sLlavesProductos.indexOf("=")>=0){
            String sLlavesProductos_aux = sLlavesProductos.substring(sLlavesProductos.indexOf("=") + 1);
            if (sLlaveBuscada.equals(sLlavesProductos)){
                bRetorno =  true;
                LLaveSeleccionada = sLlavesProductos;
            }
        }
        return bRetorno;
    }
    
	public int FindPosition(String sCadena, String sValorBuscado) {   //Formato cadena: xxx;yyy;zzz;
        int pos = -1;
        
		String [] auxCadena = sCadena.split(";");
		for ( int i=0; i<auxCadena.length; i++){
				if ( auxCadena[i].equals(sValorBuscado)){
					pos = i;
					break;
				}
		}
        return pos;
    }
	
	public String ValueByPosition(String sCadena, int iPos) {   //Formato cadena: xxx;yyy;zzz;
        String valor = "";
        
		String [] auxCadena = sCadena.split(";");
		if (iPos < auxCadena.length)
			valor = auxCadena[iPos];
					
        return valor;
    }
    
    public boolean atob(String sBool) {
        try {
            return (sBool.equalsIgnoreCase("si") || sBool.equalsIgnoreCase("true") || sBool.equalsIgnoreCase("verdadero") || sBool.equalsIgnoreCase("v"));
        } catch (Exception e) {
            return (false);
        }
    }
    
    public int atoi(String sNro) {

        try {
            int Valor = 0;
            if (sNro.indexOf('+') >= 0) //elimina signo +
                Valor = Integer.parseInt(sNro.substring(sNro.indexOf('+') + 1), 10);
            else if (sNro.indexOf('-') >= 0)
                Valor = -1 * Integer.parseInt(sNro.substring(sNro.indexOf('-') + 1), 10);
            else
                Valor = Integer.parseInt(sNro, 10);
            
            return Valor;
        } catch (Exception e) {
            return (0);
        }
        
    }
     
    public String rtrimChar(String anString, char achar) {
        int ipos = anString.length() - 1;
        
        while ((ipos >= 0) && (anString.charAt(ipos) == achar))
            ipos--;
        
        return anString.substring(0, ipos + 1);
    }
    
    public String ltrimChar(String anString, char achar) {
        int ipos = 0;
        while ((ipos < anString.length()) && (anString.charAt(ipos) == achar))
            ipos++;
        
        return anString.substring(ipos);
    }
    
    public String SplitTrxNumber(String sNumber, int ndecimales) {
        if (rtrimChar(sNumber.substring(sNumber.length() - ndecimales), '0').length() > 0)
            return sNumber.substring(0, sNumber.length() - ndecimales) + "." + rtrimChar(sNumber.substring(sNumber.length() - ndecimales), '0');
        else
            return sNumber.substring(0, sNumber.length() - ndecimales) + "." + "0";
    }
    
    public String entero(String sNumber) {
        if (sNumber.indexOf(".") >= 0)
            return sNumber.substring(0, sNumber.indexOf("."));
        else
            return sNumber;
    }
    
    public String frac(String sNumber) {
        if (sNumber.indexOf(".") >= 0)
            return rtrimChar(sNumber.substring(sNumber.indexOf(".") + 1), '0');
        else
            return "0";
    }
}
    

