package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main extends JPanel implements Runnable, MouseListener{

	//dimensions
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	public static final int menheight = 500;

	//game thread
	private Thread thread;
	private boolean running; 
	private int FPS = 70;
	private long targetTime = 1000/FPS; 

	//image
	private BufferedImage image;
	private Graphics2D g;

	// threads
	private Sorter sorter;
	public boolean sorting = false;
	public boolean radixing = false;
	//arr
	public static int arrSize= 1000;
	JTextArea sizeBox;
	public int disloc = 0;
	static int arr[] = new int[arrSize];
	volatile int playspeed = 2;
	Button[] buttons = new Button[]{
			new Button("Sequential",10, menheight+10, 30, 80),
			new Button("Random", 10+100, menheight+10, 30, 80),
			new Button("Bubble Sort", 10+100*2, menheight+10, 30, 80),
			new Button("Shuffle", 10+100*3, menheight+10, 30, 80),
			new Button("Insetion Sort", 10+100*4, menheight+10, 30, 80),
			new Button("Selection Sort", 10+100*5, menheight+10, 30, 90),
			new Button("Quick Sort", 10+100*6, menheight+10, 30, 90),
			new Button("QMerge Sort", 10+100*7, menheight+10, 30, 90),
			new Button("Radix Sort", 10+100*8, menheight+10, 30, 90),
			new Button("Stop", 10+100*9, menheight+10, 30, 60),
			new Button("Slower", 10, menheight+90, 30, 80),
			new Button("Faster", 100*2, menheight+90, 30, 80),
			new Button("Reverse", 10+100*3, menheight+90, 30, 80),
			new Button("Almost Sorted", 10+100*4, menheight+90, 30, 90)};
	public class Sorter implements Runnable {
		public Thread t;
		private int task = -1;
		public int getTask() {
			return task;
		}

		public void setTask(int task) {
			this.task = task;
		}

		private void insertionSort(int[] arr) throws InterruptedException{
			for(int x=1; x<arr.length; x++){
				int y=x;
				while(y>0&&arr[y-1]>arr[x]) y--;
				for(int z=x; z>y; z--){
					arr[z] ^= arr[z-1];
					arr[z-1] ^= arr[z];
					arr[z] ^= arr[z-1];
					Thread.sleep(playspeed);
				}
			}
		}

		private void BubbleSort(int[] arr) throws InterruptedException{
			int mark1 = 0, mark2 = 1, sorted = 0;
			while(sorted!=arr.length-1){
				if(mark1 == arr.length-1-sorted){
					sorted++;
					mark1=0; mark2=1;
				}
				if(arr[mark1]
						>arr[mark2]){
					arr[mark1] ^= arr[mark2];
					arr[mark2] ^= arr[mark1];
					arr[mark1] ^= arr[mark2];
				}
				mark1++;
				mark2++;
				Thread.sleep(playspeed);
			}	
		}	

		private void quickSort(int start, int end, int[] arr) throws InterruptedException{
			if(end-start<2) return;
			int pivot = start;
			int left = start;
			int right = end-1;


			while(true){
				Thread.sleep(playspeed);

				while(left<pivot && arr[left]<=arr[pivot])
					left++;
				while(right>pivot && arr[right]>=arr[pivot])
					right--;

				if(left==pivot&&right==pivot)
					break;

				if(left==pivot)
					pivot = right;
				else if(right==pivot)
					pivot = left;

				int temp = arr[right];
				arr[right] = arr[left];
				arr[left] = temp;
			}
			quickSort(start,pivot+1, arr);
			quickSort(pivot+1, end, arr);
		}

		private void mergeSort(int start, int end) throws InterruptedException{
			if(end-start<=0)	return;
			int mid = (start+end)/2;
			mergeSort(start,mid);
			mergeSort(mid+1,end);
			quickSort(start,end+1, arr);
		}

		public int getDig(int num, int dig){
			return (int) ((num%Math.pow(10,dig))/Math.pow(10,dig-1));
		}

		private void radixSort(int start, int end) throws InterruptedException{
			radixing = true;
			int max =0;
			for(int x=0; x<arr.length; x++)
				if(arr[x]>max) max = arr[x];
			max = (int) Math.log10(max) +1;
			
			int ind = 0;
			for(int m=1; m<max; m++){
				int temp[] = Arrays.copyOf(arr, arr.length);
				for(int val=0; val<10; val++){
					for(int x=0; x<arr.length; x++){
						if(getDig(arr[x],m)==val)
							temp[ind++] = arr[x];
					}
					drawArr(temp);
					Thread.sleep(playspeed*30);
				}
				arr = Arrays.copyOf(temp, arr.length);
				ind = 0;
			}
			radixing = false;
		}

		private void selectionSort(int[] arr) throws InterruptedException{
			for(int x=0; x<arr.length; x++){
				int smal = 99999;
				int sind = 0;
				for(int y=x; y<arr.length; y++){
					if(arr[y]<smal&&arr[y]!=-1){
						smal = arr[y];
						sind = y;
					}
				}
				int temp = arr[x];
				arr[x] = arr[sind];
				arr[sind] = temp;
				Thread.sleep(playspeed);
			}
		}

		public void seq(int[] x){
			for(int y=0; y<x.length; y++)
				x[y] = y+1;
		}

		public void randpop(int[] x){
			for(int y=0; y<x.length; y++)
				x[y] = 1 + (int)(Math.random() * ((x.length - 1) + 1));
		}

		public boolean issorted(int[] x){
			for(int y=1; y<x.length; y++)
				if(x[y]<x[y-1])
					return false;
			return true;
		}

		public void shuffle(int[] x) throws InterruptedException{
			for(int y=0; y<x.length; y++){
				int ind = (int)(Math.random() * ((x.length-1) + 1));
				int temp = x[y];
				x[y] = x[ind];
				x[ind] = temp;
				Thread.sleep(playspeed);
			}
		}

		public void kSortPop(int[] x) throws InterruptedException{
			seq(x);
			int ksort = x.length/20;
			for(int y=0; y<x.length; y++){
				int ind = rand(y-(y<ksort?y:ksort),y+(y>=x.length-ksort?x.length-y-1:ksort));
				int temp = x[y];
				x[y] = x[ind];
				x[ind] = temp;
				Thread.sleep(playspeed);
			}
		}
		public void rev(int[] x){
			for(int y=0; y<x.length; y++)
				x[y] = x.length-y;
		}
		public Sorter(){
		}
		public void run() {
			try{
				sorting = true;
				if(task==0){
					seq(arr);
				}
				else if(task==1){
					randpop(arr);
				}
				else if(task==2){
					BubbleSort(arr);
				}
				else if(task==3){
					shuffle(arr);
				}
				else if(task==4){
					insertionSort(arr);
				}
				else if(task==5){
					selectionSort(arr);
				}
				else if(task==6){
					quickSort(0, arr.length, arr);
				}
				else if(task == 7){
					mergeSort(0, arr.length-1);
				}
				else if(task == 8){
					radixSort(0, arr.length-1);
				}
				else if(task == 12){
					rev(arr);
				}
				else if(task == 12){
					rev(arr);
				}
				else if(task == 13){
					kSortPop(arr);
				}
				else{
					sorting = false;
				}
			}catch(InterruptedException e){

			}
			sorting = false;
			t = null;
		}
		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this);
				t.setDaemon(true);
				t.start ();
			}
		}

	}
	public Main(){			// Game constructor
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	private void init(){							// initalizes game states
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		sorter = new Sorter();
		sorter.seq(arr);
		initBox();
	}
	public void initBox(){
		sizeBox = new JTextArea(String.valueOf(arrSize));
		add(sizeBox);
		sizeBox.setBounds(WIDTH-220, menheight+80, 70,20);
		sizeBox.setEditable(true);
		sizeBox.setDoubleBuffered(true);
	}
	public void addNotify(){				// declares parent status and adds listeners
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addMouseListener(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public void run(){								// runs game
		init();

		long start, elapsed, wait;					//Vars to keep track of game's run times
		while(running){
			start = System.nanoTime();

			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed/1000000;
			if(wait <0) wait = 0;
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void update() {	
		if(sizeBox.getY()<100)
			sizeBox.setBounds(WIDTH-220, menheight+80, 70,20);
		try{
			String sizeIn = sizeBox.getText();
			int in = Integer.parseInt(sizeIn);
			if(in<=WIDTH&&in>0&&in!=arrSize){
				arrSize = in;
				if(sorter.t!=null){
					sorter.t.interrupt();
					sorter.t = null;
				}
				arr = new int[arrSize];
				sorter.setTask(0);
				sorter.start();
				disloc = (WIDTH - ((WIDTH/arrSize)*arrSize))/2;
				g.setColor(Color.black);
				g.fillRect(0,0,WIDTH,menheight);
			}
		}catch(Exception e){
			sizeBox.setText("100");
		}
	}

	private void draw() {
		if(!radixing)
		drawArr(arr);
		// menu
		g.setColor(Color.white);
		g.fillRect(0,menheight,WIDTH,HEIGHT-menheight);
		g.setColor(Color.black);
		g.drawString(String.valueOf(playspeed), 140, menheight+90);
		g.drawString("Array Length:", sizeBox.getX()-90, sizeBox.getY()+10);
		g.drawString("Click to edit (max "+WIDTH+")", sizeBox.getX()+sizeBox.getWidth(), sizeBox.getY()+10);
		for(int x=0; x<buttons.length; x++)
			buttons[x].draw(g);
		sizeBox.repaint();

	}
	private void drawArr(int[] arr){
		int numCol = arr.length;
		for (int i = 0; i < numCol; ++i)
		{
			float hue = (float)(arr[i])/numCol;
			g.setColor(Color.getHSBColor(hue, (float)1, (float)1));
			g.fillRect(WIDTH/numCol *i + disloc,0,WIDTH/numCol, menheight);
			g.setColor(Color.black);
			//g.drawString(String.valueOf((int)(hue*10)), WIDTH/numCol*i, menheight+10);
		}
		g.setColor(Color.black);
	}

	private void drawToScreen() {						// scales and draws game with formating
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH,  HEIGHT, null);
		g2.dispose();
	}

	
	public void mousePressed(MouseEvent e) {
		for(int x=0; x<buttons.length; x++){
			if(buttons[x].containsPoint(e.getX(), e.getY())){
				if(x == 9){
					if(sorter.t!=null)
						sorter.t.interrupt();
				}
				else if(x==10){
					playspeed++;
				}
				else if(x==11 && playspeed>0){
					playspeed--;
				}
				else{
					sorter.setTask(x);
					sorter.start();
				}
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	public static int rand(int max, int min) {
		return (int)(Math.random() * ((max - min) -1) + min)+1;
	}
}
