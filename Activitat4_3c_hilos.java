import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Activitat4_3c_hilos extends RecursiveTask<Long> {
    long numero;
    
    public Activitat4_3c_hilos(long numero){
        this.numero=numero;
    }    
    
    @Override
    protected Long compute() {
    	
        if(numero <= 1) {
        	return (long)1;
        }
        
        long res = 0;
        
        for (int i = 0; i < numero; i++) {
        	Activitat4_3c_hilos cat1 = new Activitat4_3c_hilos(i);
            cat1.fork();
            
            Activitat4_3c_hilos cat2 = new Activitat4_3c_hilos(numero-i-1);
            cat2.fork();
            
            res = res + cat1.join() * cat2.join();
		}
        
        return res;
	 }
    
    //Funció per obtindre el temps en segons pasant-li dos numeros
  	public static long getTimeInMili(long inici, long fin){
  		long num = 0;
  		num = (fin - inici);
  		return num;
  	}
    
    public static void main(String[] args){
    	
    	for (int i = 10; i <= 22; i++) {
    		ForkJoinPool pool = new ForkJoinPool();
    		
    		long inici = System.currentTimeMillis();
    		long num = pool.invoke(new Activitat4_3c_hilos(i));
			long fin = System.currentTimeMillis();
    		
			long res_time = getTimeInMili(inici, fin);
			System.out.println("Num: " + i + ", Catalan: " + num);
			System.out.println("Temps total: " + res_time + "miliseg.\n");
		}
    	
    }
}
