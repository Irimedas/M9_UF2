import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MaximTask extends RecursiveTask<Short>{
	private static final int LLINDAR=10000000;
	private short[] arr;
	private int inici, fi;
	
	//Modificació per controlar els cops que s'exeuta i els propis fils
	private int fil;
	private static int count = 1;
	//--------------------------------
	
	//Modficació per registrar els propis fils
	public MaximTask(short[] arr, int inici, int fi, int fil) {
		this.arr = arr;
		this.inici = inici;
		this.fi = fi;
		this.fil = fil;
	}
	//--------------------------------
	
	private short getMaxSeq() {
		short max = arr[inici];
		for (int i = inici+1; i < fi; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}
	
	private short getMaxReq() {
		MaximTask task1;
		MaximTask task2;
		int mig = (inici+fi)/2+1;
		
		task1 = new MaximTask(arr, inici, mig, this.fil+1);
		task1.fork();
		
		task2 = new MaximTask(arr, mig, fi, this.fil+1);
		task2.fork();
		
		return (short) Math.max(task1.join(), task2.join());
	}
	
	 @Override
	 protected Short compute() {
		 
		 //Modificiació per mostrar la escrtutura de forma clara y els auments en el contador 
		 System.out.println("Contador->"+ this.count + " Fil->"+ this.fil + " Inici->"+this.inici+" Fi->"+this.fi +
				 "\n posicio del array->"+this.arr[this.inici]);
		 count++;
		 //--------------------------------
		 
		 if(fi - inici <= LLINDAR){
			 return getMaxSeq();
		 }else{
			 return getMaxReq();
		 }
	 }
	 
	 private static short[] createArray(int size){
		 short[] ret = new short[size];
		 for(int i=0; i<size; i++){
			 ret[i] = (short) (1000 * Math.random());
			 if(i==((short)(size*0.9))){
				 ret[i]=1005;
			 }
		 }
		 return ret;
	 } 
	 
	 public static void main(String[] args) {
		 short[] data = createArray(100000000);
		 
		 // Mira el número de processadors
		 System.out.println("Inici càlcul");
		 ForkJoinPool pool = new ForkJoinPool();
		 
		 int inici=0;
		 int fi= data.length;
		 //Modificació per a la creació registrar les execucions dels fils
		 int fil = 0;
		 MaximTask tasca = new MaximTask(data, inici, fi, fil);
		 //--------------------------------
		 
		 long time = System.currentTimeMillis();
		 // crida la tasca i espera que es completin
		 int result1 = pool.invoke(tasca);
		 // màxim
		 int result= tasca.join();
		 System.out.println("Temps utilitzat:" +(System.currentTimeMillis()-time));
		 System.out.println ("Màxim es " + result);
	 }
}
