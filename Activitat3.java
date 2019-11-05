import java.io.ObjectInputStream.GetField;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Activitat3 {

	//Funció de Fibonacci en forma recursiva
	public static long calculaFibonacci(long numero) {
        double calcul = java.lang.Math.cos(54879854);
        if (numero==0) { return 0; }
        else if (numero==1) { return 1; }
        else { 
            return (calculaFibonacci(numero-2) + calculaFibonacci(numero-1)); 
        }
    }
	
	//Funció per obtindre el temps en segons pasant-li dos numeros
	public static long getTimeInMili(long inici, long fin){
		long num = 0;
		num = (fin - inici) / 1000;
		return num;
	}
	
	//Codi principal del arxiu
	public static void main(String[] args){
		Scanner teclado = new Scanner(System.in);
		
		//Array de numeros per a fer proves
		int[] array = {45,46,47,48,49,50,51,52}; 
		
		for (int i = 0; i < array.length; i++) {
			System.out.println("Numero utilizado para el calculo de Fibonacci: " + array[i]);
			
			int num = array[i];
			long inici = System.currentTimeMillis();
			
			//Funcion uqe aplica el calculo de Fibonacci de un numero de forma recursiva
			calculaFibonacci(num);
			
			long fin = System.currentTimeMillis();
			
			long res_time = getTimeInMili(inici, fin);
			System.out.println("Temps total: " + res_time + "seg.\n");
		}
		teclado.close();
	}
}