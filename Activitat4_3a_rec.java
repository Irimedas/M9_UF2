
public class Activitat4_3a_rec {
	
	public static long calculaXValor(long numero, long elevacio) 
	{ 
	    if (elevacio == 0) {
	    	return (long)1; 
	    }
	    
	    return numero * calculaXValor(numero, elevacio - 1); 
	} 
	
	//Funció per obtindre el temps en segons pasant-li dos numeros
	public static long getTimeInMili(long inici, long fin){
		long num = 0;
		num = (fin - inici);
		return num;
	}

	public static void main(String[] args) {
		for (long i = 1; i <= 62; i++) {
			
			//double calcul = java.lang.Math.cos(461168999);
			
			long inici = System.currentTimeMillis();
			System.out.println("Elevación: " + i + ", 2 valor: " + calculaXValor(2, i));
			long fin = System.currentTimeMillis();
			
			long res_time = getTimeInMili(inici, fin);
			System.out.println("Temps total: " + res_time + "miliseg.\n");
		}
	}

}
