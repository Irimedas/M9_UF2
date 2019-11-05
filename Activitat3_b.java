import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Activitat3_b extends RecursiveTask<Long> {
    long numero;
    public Activitat3_b(long numero){
        this.numero=numero;
    }    
    @Override
    protected Long compute() {
        double calcul = java.lang.Math.cos(54879854);
        if(numero <= 1) return numero;
        Activitat3_b fib1 = new Activitat3_b(numero-1);
        fib1.fork();
	Activitat3_b fib2 = new Activitat3_b(numero-2);
        fib2.fork();
	 return fib1.compute()+ fib2.join();
	 }
    
    //Funció per obtindre el temps en segons pasant-li dos numeros
  	public static long getTimeInMili(long inici, long fin){
  		long num = 0;
  		num = (fin - inici) / 1000;
  		return num;
  	}
    
    public static void main(String[] args){
    	
    	int[] array = {45,46,47,48,49,50,51,52}; 
    	
    	for (int i = 0; i < array.length; i++) {
    		
    		int num = array[i];
    		
    		ForkJoinPool pool = new ForkJoinPool();
    		System.out.println("Numero sobre el cual s'aplica el calcul de Fibonaccci amb fils: " + num);
    		
    		long inici = System.currentTimeMillis();
            pool.invoke(new Activitat3_b(num));    
    		
            long fin = System.currentTimeMillis();
			long res_time = getTimeInMili(inici, fin);
			System.out.println("Temps total: " + res_time + "seg.\n");
    	}
    }
}

