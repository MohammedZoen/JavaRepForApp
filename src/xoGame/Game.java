package xoGame;

import javax.swing.JFrame;

public class Game 
{
	Board board;
	 public static void main(String a[])
	{
		JFrame app = new JFrame();
		Board b = new Board((byte)8, (byte)5); 
		app.add(b);
		app.pack();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(false);
	}
}
