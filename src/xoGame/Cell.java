package xoGame;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.IconUIResource;
import javax.swing.text.AttributeSet.ColorAttribute;

public class Cell extends JPanel
{
	private byte x;
	private byte y;
	private char content;
	private Board boardRef;
	public static CellListener theOneHowListenToAllOfus ;
	public byte getXPosition() {return x;}
	public void setX(byte x) 
	{
		if(x>=0 && x<boardRef.getBoardSize())
			this.x = x;
		else 
			throw new BoardSizeException("x", x);
	}
	public byte getYPosition() {return y;}
	public void setY(byte y) 
	{
		if(y>=0 && y<boardRef.getBoardSize())
			this.y = y;
		else 
			throw new BoardSizeException("y", y);
	}
	public void setXY(byte x, byte y)
	{
		setX(x);
		setY(y);
	}
	public char getContent() {return content;}
	public void setContent(char content) 
	{
		switch(content)
		{
			case'x': 
				this.content=content; 
				boardRef.setStatus(Status.xTurn);
				break;
			case'o': 
				this.content=content; 
				boardRef.setStatus(Status.oTurn);
				break;
			case'n': // n for empty Cell
				this.content=content; 
				break;
			default:
				this.content='e'; // e for Error
		}
	}
	public Board getBoardRef() {return boardRef;}
	public void setBoardRef(Board boardRef) 
	{
		this.boardRef = boardRef;
	}
	
	public Cell(Board bRef , byte x, byte y)
	{
		setBoardRef(bRef);
		setXY(x, y);
		setContent('n');
		theOneHowListenToAllOfus = new CellListener();
		addMouseListener(theOneHowListenToAllOfus);
		int bx=0,
			by=0;
		if(x==bRef.getBoardSize()-1)
			bx=5;
		if(y==bRef.getBoardSize()-1)
			by=5;
		MatteBorder border = new MatteBorder(5, 5, bx, by, Color.cyan);
		this.setBorder(border);
		setPreferredSize(new Dimension(60,60));
	}
	
	public void paintComponent(Graphics g)
	{
		g.setFont(new Font("sanserif", Font.PLAIN, 30));
		switch (content) 
		{
			case'x':
				g.drawString("X", 20, 50);
				break;
			case'o':
				g.drawString("O", 20, 50);
				break;
			case'e':
			default:
				g.setColor(Color.RED);
				g.drawString("e", 20, 50);
			break;
		case'n': 
			g.clearRect(0, 0, getWidth(), getHeight());
			break;
		}
	}

	
	public class CellListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent arg0) 
		{
			if(content=='n')
			{
				setContent('x');
				repaint();
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) 
		{
			if(content=='n')
			{
				Graphics gRef = getGraphics();
				//Color oldColor=gRef.getColor();
				gRef.setColor(new Color(100,255,100));
				gRef.setFont(new Font("sanserif",Font.PLAIN,30));
				//gRef.drawOval(20, 50, 20, 20);
				gRef.drawString("X", 20, 50);
				//gRef.setColor(oldColor);
				gRef.setColor(Color.BLACK);
			}
		}
		@Override
		public void mouseExited(MouseEvent arg0) 
		{
			 if(content=='n')
				 repaint();
		}
		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) 
		{
			
		}//hallo 
	}
}