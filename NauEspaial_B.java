import java.awt.*;
import java.util.*;

import javax.swing.*;
import java.awt.event.*;

public class NauEspaial_B extends javax.swing.JFrame {

	public NauEspaial_B() {
		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBackground(new java.awt.Color(0, 0, 0));
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
		pack();
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(NauEspaial_B.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		NauEspaial_B f = new NauEspaial_B();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setTitle("Naus Espaials");
		f.setContentPane(new PanelNau_B());
		f.setSize(480, 560);
		f.setVisible(true);
	}
}

//------------------------------------------------------------------
//------------------------------------------------------------------

class PanelNau_B extends JPanel implements Runnable, KeyListener {
	private int numNaus = 10;
	Nau_B[] array_enemics;
	static ArrayList<Dispar_B> dispars = new ArrayList<Dispar_B>();
	Nau_B nauPropia;
	
	boolean test = false;
	boolean fantasma = false;
	int fantasma_permesos = 5;
	int colisions_permeses = 4;
	
	private int maring_x = 460;
	private int maring_y = 520;
	
	private Thread tread = null;

	public PanelNau_B() {
		
		// Creo la nau propia
		// nauPropia_exemple = new Nau_B("", x, y, dsx, dsy, v, enemy)
		nauPropia = new Nau_B("NauNostra", 200, 400, 0, 0, 5, false, test, this.maring_x, this.maring_y);
		
		array_enemics = new Nau_B[numNaus];
		for (int i = 0; i < array_enemics.length; i++) {
			Random rand = new Random();
			int velocitat = (rand.nextInt(3) + 5) * 10;
			int posX = rand.nextInt(100) + 200;
			int posY = rand.nextInt(100) + 150;
			int movimentX = rand.nextInt(10) + (-5);
			int movimentY = rand.nextInt(10) + (-5);
			String nomNau = "Nau_enemiga: " + i;
			array_enemics[i] = new Nau_B(nomNau, posX, posY, movimentX, movimentY, velocitat, true, test, this.maring_x, this.maring_y);
		}

		// Creo fil per anar pintant cada 0,1 segons el joc per pantalla
		this.tread = new Thread(this);
		this.tread.start();

		// Creo listeners per a que el fil principal del programa gestioni
		// esdeveniments del teclat
		addKeyListener(this);
		setFocusable(true);

	}

	public void run() {
		System.out.println("Inici fil repintar");
		boolean colision = false;
		boolean are_enemy = true;
		
		while (!colision && are_enemy) {	
			detectarColisionsDisparsAliats(dispars, array_enemics);
			colision = detectarColisionsNaus(array_enemics, nauPropia);
			if(colisions_permeses > 0 && colision) {
				colision = false;
				this.colisions_permeses--;
				System.out.println("Vidas actuales: " + (colisions_permeses));
			}
			
			are_enemy = quedanEnemics(array_enemics);
			repaint();
		}

		nauPropia.kill();
		for (int i = 0; i < array_enemics.length; i++) {
			if(array_enemics[i] != null) {
				array_enemics[i].kill();	
			}
		}
		for (int i = 0; i < dispars.size(); i++) {
			dispars.get(i).kill();
		}
		
		dispars.clear();
		
		System.out.println("Game Over");
		this.tread.interrupt();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int i = 0; i < dispars.size(); i++) {
			if(dispars.get(i) != null) {
				int[] posicion_disparo = dispars.get(i).getPosition();
				if((posicion_disparo[0] == 0 || posicion_disparo[0] == this.maring_x)||(posicion_disparo[1] == 0 || posicion_disparo[1] == this.maring_y)) {
					dispars.get(i).kill();
					
				}else if(!dispars.get(i).isNotDead()){
					dispars.remove(i);
					i--;
					
				}else{
					dispars.get(i).pinta(g);
				}
			}
		}
		
		nauPropia.pinta(g);
		
		for (int i = 0; i < array_enemics.length; ++i) {
			if(array_enemics[i] != null) {
				array_enemics[i].pinta(g);	
			}
		}
	}

	// Metodes necesaris per gestionar esdeveniments del teclat
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		//teclas arriba, abajo, izquierda, derecha
		if (e.getKeyCode() == 38) {
			nauPropia.up();
			this.fantasma = false;
		} 
		if (e.getKeyCode() == 40) {
			nauPropia.down();
			this.fantasma = false;
		}
		if (e.getKeyCode() == 37) {
			nauPropia.left();
			this.fantasma = false;
		}
		if (e.getKeyCode() == 39) {
			nauPropia.right();
			this.fantasma = false;
		}
		
		//tecla: espacio
		if (e.getKeyCode() == 32) {
			int[] posicio = nauPropia.getPosition();
			Dispar_B dispar = new Dispar_B("dispar", posicio[0], posicio[1], 0, 0, 15, false, false, nauPropia.getOrientation(), this.maring_x, this.maring_y);
			dispars.add(dispar);
		}
		
		//tecla: A
		if(!this.test) {
			if (e.getKeyCode() == 65) {
				if(this.fantasma_permesos > 0) {
					nauPropia.boom();
					this.fantasma = true;
					System.out.println("Modo fantasma restantes: " + (this.fantasma_permesos - 1));
					this.fantasma_permesos--;
				}
			}	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	//Programar para detectar las colisiones entre las naves
	public boolean detectarColisionsNaus(Nau_B[] array_enemics, Nau_B nau_jugador){
		
		boolean choque = false;
		
		if(!this.fantasma){
			int[] posicio_propia = nau_jugador.getPosition();
			
			for (int i = 0; i < array_enemics.length; i++) {
				if(array_enemics[i] != null) {
					int[] posicio_enemiga = array_enemics[i].getPosition();
					
					//Deteccio de colsions en base al centre del punt
					if((((posicio_propia[0] + 10) >= posicio_enemiga[0]) && ((posicio_propia[0] - 10) <= posicio_enemiga[0]))&&
						(((posicio_propia[1] + 10) >= posicio_enemiga[1]) && ((posicio_propia[1] - 10) <= posicio_enemiga[1]))) {
						choque = true;
						array_enemics[i].kill();
						array_enemics[i] = null;
					}	
				}
			}	
		}
		
		return choque;
	}
	
	//Programar para detectar las colisiones entre los disparos aliados i las naves (No detecta las coilisiones entre disparos i enemigos)
	public void detectarColisionsDisparsAliats(ArrayList<Dispar_B> dispars, Nau_B[] array_enemics) {
		
		for (int i = 0; i < array_enemics.length; i++) {
			for (int j = 0; j < dispars.size(); j++) {
				if(array_enemics[i] != null) {
					int[] posicio_enemic = array_enemics[i].getPosition();
					
					//Esta aplicat per relentitzar la execució de la funció(Si no, genera un NullPointExeption)
//                    Math.cos(999999999);
					
                    if(dispars.get(j) != null) {
						int[] posicio_dispar = dispars.get(j).getPosition();
						
						//Deteccio de colsions en base al centre del punt
						if((((posicio_enemic[0] + 10) >= posicio_dispar[0]) && ((posicio_enemic[0] - 10) <= posicio_dispar[0]))&&
							(((posicio_enemic[1] + 10) >= posicio_dispar[1]) && ((posicio_enemic[1] - 10) <= posicio_dispar[1]))) {
							
							array_enemics[i].kill();
							array_enemics[i] = null;
							
							dispars.get(j).kill();
							System.out.println("Impacto");
						}
					}		
				}
			}
		}
	}
	
	public boolean quedanEnemics(Nau_B[] array_enemics){
		
		boolean comprovacio = false;
		
		for (int i = 0; i < array_enemics.length; i++) {
			if(array_enemics[i] != null) {
				comprovacio = true;
			}
		}
		return comprovacio;
	}
}

//------------------------------------------------------------------
//------------------------------------------------------------------

class Nau_B extends Thread {
	private String nomNau;
	private int x, y;
	private int dsx, dsy, v; // v-> velocitat_nau
	private int tx = 10;
	private int ty = 10;
	private boolean enemy = false;
	private boolean test = false;
	private boolean alive = true;
	
	private int maring_x = 0;
	private int maring_y = 0;
	
	//Si la nau que dispara es la nostra necesita orientacio del cap per disparar
	//0->up, 1->down, 2-> left, 3->right
	private int orientation = 0;
	
	
	private String[] nau_sprites = {"/images/nau_up.png","/images/nau_down.png","/images/nau_left.png","/images/nau_right.png","/images/enemy.png"};
	private String enemy_sprites = "/images/enemy.png";

	private String[] test_sprites = {"/images/enemy_test.png","/images/nau_test.png"};
	
	private Image image;
	private Image[] nau_moves_sprites = new Image[this.nau_sprites.length];

	private Image nau_test;
	private Thread tread;
	
	public Nau_B(String nomNau, int x, int y, int dsx, int dsy, int v, boolean enemy, boolean test, int margin_x_canvas, int margin_y_canvas) {
		this.nomNau = nomNau;
		this.x = x;
		this.y = y;
		this.dsy = dsy;
		this.dsx = dsx;
		this.v = v;
		this.enemy = enemy;
		this.test = test;
		this.maring_x = margin_x_canvas;
		this.maring_y = margin_y_canvas;
		
		String img = new String();


		//Comprovem si estem en test i si la nau es aliada o enemiga
		if(this.test){
			if(!this.enemy){
				img = test_sprites[1];
			
			}else{
				img = test_sprites[0];
			}

		}else{
			if(!this.enemy){
			for (int i = 0; i < nau_sprites.length; i++) {
				//System.out.println(nau_sprites[i]);
				this.nau_moves_sprites[i] = new ImageIcon(Nau_B.class.getResource(nau_sprites[i] + "")).getImage();
			}
			img = nau_sprites[0];
			
		} else {
			img = enemy_sprites;
		}
		}
		
		image = new ImageIcon(Nau_B.class.getResource(img)).getImage();
		this.tread = new Thread(this);
		tread.start();
	}

	public int velocitat() {
		return v;
	}

	public void moure() {
		x = x + dsx;
		y = y + dsy;
		// si arriva als marges ...
		if (x >= this.maring_x - tx || x <= tx)
			dsx = -dsx;
		if (y >= this.maring_y - ty || y <= ty)
			dsy = -dsy;
		
		if(!this.enemy) {
			this.dsx = 0;
			this.dsy = 0;
		}
	}

	public void pinta(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.image, x, y, null);
	}

	public void run() {
		while (this.alive) {
			try {
				Thread.sleep(this.v);
			} catch (Exception e) {
			}
			moure();
		}
		tread.interrupt();
	}

	//Apartats de moviment
	public void up() {
		if((this.y - this.v) > 0){
			this.dsy = 0 - this.v;
			this.orientation = 0;
		}
		
		if(!test){
			this.image = this.nau_moves_sprites[0];
		}
	}

	public void down() {
		if((this.y + this.v) < this.maring_y) {
			this.dsy = 0 + this.v;
			this.orientation = 1;
		}
		if(!test){
			this.image = this.nau_moves_sprites[1];
		}
	}
	
	public void left() {
		if((this.x - this.v) > 0) {
			this.dsx = 0 - this.v;
			this.orientation = 2;
		}
		if(!test){
			this.image = this.nau_moves_sprites[2];
		}
	}

	public void right() {
		if((this.x + this.v) < this.maring_x) {
			this.dsx = 0 + this.v;
			this.orientation = 3;
		}
		if(!test){
			this.image = this.nau_moves_sprites[3];
		}
	}
	
	//Obtenim la orientació actual de la nau
	public int getOrientation(){
		return this.orientation;
	}

	public void kill(){
		this.alive = false;
	}

	//Retorna la posicio de la nau 
	//0->X
	//1->Y
	public int[] getPosition(){
		int[] position = {
			this.x,
			this.y
		};
		return position;
	}
	
	//Apartats extres de la nau
	public void boom() {
		this.image = this.nau_moves_sprites[4];
	}
}

//------------------------------------------------------------------
//------------------------------------------------------------------

class Dispar_B extends Thread {
	private String nomDispar;
	private int x, y;
	private int dsx, dsy, v; // v-> velocitat_dispar
	private int tx = 10;
	private int ty = 10;
	private boolean enemy_shoot = false;
	private boolean test = false;
	private boolean alive = true;
	
	private int maring_x = 0;
	private int maring_y = 0;
	private int orientation_shoot = 0;
	
	private String[] shoot_sprite = {"/images/shoot.png","/images/shoot.png"};
	private Image image;
	private Thread tread;
	
	public Dispar_B(String nomDispar, int x, int y, int dsx, int dsy, int v, boolean enemy, boolean test, int orientacio, int margin_x_canvas, int margin_y_canvas) {
		this.nomDispar = nomDispar;
		this.x = x + 5;
		this.y = y + 5;
		this.dsy = dsy;
		this.dsx = dsx;
		this.v = v;
		this.enemy_shoot = enemy;
		this.test = test;
		this.orientation_shoot = orientacio;
		this.maring_x = margin_x_canvas;
		this.maring_y = margin_y_canvas;
		
		String img = new String();

		//Comprovem si estem en test i si la nau es aliada o enemiga
		if(this.test){
			img = shoot_sprite[0];
		}else{
			img = shoot_sprite[1];
		}
		
		image = new ImageIcon(Dispar_B.class.getResource(img)).getImage();
		this.tread = new Thread(this);
		tread.start();
	}

	public int velocitat() {
		return v;
	}

	public void moure() {
		switch(this.orientation_shoot) {
			case 0:
				if((this.y - 1) >= 0){
					this.y = y - 1;
				}else {
					kill();
				}
				break;
				
			case 1:
				if((this.y + 1) <= maring_y){
					this.y = y + 1;
				}else {
					kill();
				}
				break;
				
			case 2:
				if((this.x - 1) >= 0){
					this.x = x - 1;
				}else {
					kill();
				}
				break;
				
			case 3:
				if((this.x + 1) <= maring_x) {
					this.x = x + 1;	
				}else {
					kill();
				}
				break;
		}
	}

	public void pinta(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.image, x, y, null);
	}

	public void run() {
		while (this.alive) {
			try {
				Thread.sleep(this.v);
			} catch (Exception e) {
			}
			moure();
		}
		tread.interrupt();
	}

	public void kill(){
		this.alive = false;
	}

	//Retorna la posicio de la nau 
	//0->X
	//1->Y
	public int[] getPosition(){
		int[] position = {
			this.x,
			this.y
		};
		return position;
	}
	
	public boolean isNotDead() {
		return this.alive;
	}
}