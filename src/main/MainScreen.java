package main;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
public class MainScreen{
	public static void main(String[] args){
		
		JFrame window = new JFrame("Array Sorting Algorithms");				// jframe widow
		window.setContentPane(new Main());						// content of window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);								// un-expandable
		window.pack();
		window.setVisible(true);				
	}
}