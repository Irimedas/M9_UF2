import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Activitat4_3a_hilos extends RecursiveTask<Long> {
    long numero;
    long elevacio;
    
    public Activitat4_3a_hilos(long numero, long elevacio){
        this.numero = numero;
        this.elevacio = elevacio;
    }    
    
    @Override
    protected Long compute() {
    	
        if(elevacio == 0) {
        	return (long)1;
        }
        
        Activitat4_3a_hilos xval1 = new Activitat4_3a_hilos(numero, elevacio - 1);
        xval1.fork();
        
        return  numero * xval1.join();
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
			
			ForkJoinPool pool = new ForkJoinPool();
			long inici = System.currentTimeMillis();
    		long num = pool.invoke(new Activitat4_3a_hilos((long)43, i));
    		long fin = System.currentTimeMillis();
			
    		System.out.println("Elevación: " + i + ", 2 valor: " + num);
    		long res_time = getTimeInMili(inici, fin);
			System.out.println("Temps total: " + res_time + "miliseg.\n");
		}
	}
}
