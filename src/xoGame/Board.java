package xoGame;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Board extends JPanel
{
	private byte boardSize;
	private byte streakToWin;
	private Cell board[][];
	
	Status status;
	public void setStatus( Status s)
	{
		switch(s)
		{
		case xTurn:
			//this.repaint();// TODO ?? it's done at the end of computer choice
			status=s;
			// empty for now
			break;
		case oTurn:
			
			break;
		case xWon:
			break;
		case oWon:
			break;
		}
	}
	
	public byte getBoardSize() {return boardSize;}
	public void setBoardSize(byte size) 
	{
		if(size>3 && size<11)
			this.boardSize = size;
		else
			throw new BoardSizeException("size", size);
	}
	public byte getStreakToWin() {return streakToWin;}
	public void setStreakToWin(byte streakToWin) 
	{
		if(streakToWin>boardSize)
			throw new BoardSizeException("streakToWin", streakToWin);
		this.streakToWin = streakToWin;
	}
	
	public Board(byte size , byte stw)
	{
		setBoardSize(size);
		setStreakToWin(stw);
		board = new Cell[size][size]; 
		setLayout(new GridLayout(size, size));
		status=Status.xTurn; // Human is X
							// game starts @ Human Turn 
		for (byte i = 0; i < boardSize; i++) 
		{
			for(byte j=0; j<boardSize; j++)
			{
				Cell anewCell =new Cell(this,i, j);
				board[i][j]= anewCell;
				this.add(anewCell);
			}
		}
	}
	
	public void setCell(byte x, byte y, char val)
	{
		board[x][y].setContent(val);
	}
	
	
	public void deciceWheretoPlay()
	{
		WeighedBoard weighedBoard = new WeighedBoard();
		
		ArrayList<Cell>  bestChoices=weighedBoard.getBestChoices();
		Random rg = new Random();
		bestChoices.get(rg.nextInt(bestChoices.size())).setContent('o');
	}
	
	
	private class WeighedBoard
	{
		private long xWeighs[][]={};
		private long oWeighs[][]={};
		private long xMax;
		private long oMax;
		private void calculateXMax()
		{
			xMax=0;
			for(int i=0;i<xWeighs.length;i++)
			{
				for(int j=0; j<xWeighs[i].length;j++)
					if(xWeighs[i][j]>xMax)
						xMax=xWeighs[i][j];
			}
		}
		private void calculateOMax()
		{
			oMax=0;
			for(int i=0;i<oWeighs.length;i++)
			{
				for(int j=0; j<oWeighs[i].length;j++)
					if(oWeighs[i][j]>oMax)
						oMax=oWeighs[i][j];
			}
		}
		
		private ArrayList<Cell> xMaxList;
		private ArrayList<Cell> oMaxList;
		private ArrayList<Cell> bestChoices;
		
		private final byte RIGHT = 0 ;
		private final byte DOWN = 1 ;
		private final byte POSITIVE_DIAGONAL=2;
		private final byte NEGATIVE_DIAGONAL=3;
		
		public ArrayList<Cell> getBestChoices() 
		{
			calculateXMax();calculateOMax();
			
			if(oMax>=xMax)
			{
				getMaxList('o');
				getMaxList2('x');
			}
			else /*oMax < xMax*/
			{
				getMaxList('x');
				getMaxList2('o');
			}
			return bestChoices;
		}
		
		public WeighedBoard()
		{
			xWeighs=new long[boardSize][boardSize];
			oWeighs=new long[boardSize][boardSize];
			
			loopOver();
		}
		
		private void loopOver()
		{
			for (int i = 0; i < boardSize; i++) 
			{
				for (int j = 0; j < boardSize; j++) 
				{
					if(j<(boardSize-streakToWin))
						countBooth(i,j,RIGHT);
					if(i<boardSize-streakToWin)
						countBooth(i,j,DOWN);
					if((i<boardSize-streakToWin )&&(j<boardSize-streakToWin ))
						countBooth(i,j,POSITIVE_DIAGONAL);
					if((i<boardSize-streakToWin )&&(j>boardSize-streakToWin ))
						countBooth(i,j,NEGATIVE_DIAGONAL);
					if((i>boardSize-streakToWin )&&(j>boardSize-streakToWin ))
						break;
				}
			}
		}
		
		private void countBooth(int i , int j , int dir)
		{
			try
			{
				acumulate(i,j,dir,'x',countSpecific(i,j,dir,'x'));
				acumulate(i,j,dir,'o',countSpecific(i,j,dir,'o'));
			}
			catch(PlayerWonException e)
			{
				
			}
		}
		
		private long countSpecific(int i, int j, int dir, char player) 
								   throws PlayerWonException
		{
			char me = 'x', him='o' ;
			if(player=='o')
				{me = 'o'; him='x' ;}
			int meCount=0 , himCount=0;
			int xStep=0 , yStep=0;
			switch(dir)
			{
				case RIGHT:
					xStep=1;
					break;
				case DOWN:
					yStep=1;
					break;
				case POSITIVE_DIAGONAL:
					xStep = yStep = 1;
					break;
				case NEGATIVE_DIAGONAL:
					xStep=-1; yStep =1;
			}
			for(int k=0 ; k< streakToWin ; k++ , i+=yStep , j+=xStep)
			{
				if(board[i][j].getContent()==me)
					meCount++;
				else if(board[i][j].getContent()==him)
					himCount++;
			}
			if(himCount==streakToWin)
				throw new PlayerWonException(player, (byte)i,(byte)j, dir);
			
			if(himCount!=0)
				return 0;
			return 20^meCount;
		}
		
		private void acumulate(int i , int j , int dir, char player, long value)
		{
			int xStep=0 , yStep=0;
			switch(dir)
			{
				case RIGHT:
					xStep=1;
					break;
				case DOWN:
					yStep=1;
					break;
				case POSITIVE_DIAGONAL:
					xStep = yStep = 1;
					break;
				case NEGATIVE_DIAGONAL:
					xStep=-1; yStep =1;
			}
			long [][] arrRef;
			if(player=='x')
				arrRef=xWeighs;
			else
				arrRef=oWeighs;
			for(int k=0; k<streakToWin; k++, i+=yStep , j+=xStep)
			{
				if(board[i][j].getContent()=='n')
					arrRef[i][j]+=value; 
			}
		}
		
		public void getMaxList (char player)
		{
			xMaxList.clear();
			oMaxList.clear();
			long arrRef[][] = xWeighs;
			long compareValue=xMax;
			ArrayList<Cell> listRef=xMaxList;
			if (player=='o') 
			{
				arrRef=oWeighs;
				compareValue=oMax;
				listRef=oMaxList;
			}
			for(int i =0; i<arrRef.length;i++)
				for(int j=0;j<arrRef[i].length;j++)
					if(arrRef[i][j]==compareValue)
						listRef.add(board[i][j]);
		}
		
		public void getMaxList2(char player)
		{
			long arrRef[][] = xWeighs;
			long compareValue=xMax;
			ArrayList<Cell> listRef=oMaxList;
			
			if (player=='o') 
			{
				arrRef=oWeighs;
				compareValue=oMax;
				listRef=xMaxList;
			}
			
			long tempMax=0;
			for(int i=0;i<listRef.size();i++)
			{
				if(arrRef[listRef.get(i).getXPosition()][listRef.get(i).getYPosition()]>tempMax)
				{
					bestChoices.clear();
					bestChoices.add(board[listRef.get(i).getXPosition()][listRef.get(i).getYPosition()]);
					tempMax=arrRef[listRef.get(i).getXPosition()][listRef.get(i).getYPosition()];
				}
				else if (arrRef[listRef.get(i).getXPosition()][listRef.get(i).getYPosition()]==tempMax)
					bestChoices.add(board[listRef.get(i).getXPosition()][listRef.get(i).getYPosition()]);
			}
		}
	}
}



enum Status
{
	xTurn,oTurn,xWon,oWon;
} 
