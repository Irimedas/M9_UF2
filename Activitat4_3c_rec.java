
public class Activitat4_3c_rec {
	
	public static long calculaCatalan(long numero) 
	{ 
	    if (numero <= 1) {
	    	return (long)1; 
	    }
	    
	    long res = 0; 
	    for (long i=0; i<numero; i++){
	    	res += calculaCatalan(i) * calculaCatalan(numero-i-1);
	    } 
	  
	    return res; 
	} 
	
	//Funció per obtindre el temps en segons pasant-li dos numeros
  	public static long getTimeInMili(long inici, long fin){
  		long num = 0;
  		num = (fin - inici);
  		return num;
  	}

	public static void main(String[] args) {
		for (long i = 10; i <= 22; i++) {
			
			long inici = System.currentTimeMillis();
			System.out.println("Num: " + i + ", Catalan: " + calculaCatalan(i));
			long fin = System.currentTimeMillis();
			
			long res_time = getTimeInMili(inici, fin);
			System.out.println("Temps total: " + res_time + "miliseg.\n");
		}
	}

}
