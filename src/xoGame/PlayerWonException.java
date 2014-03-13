package xoGame;
public class PlayerWonException extends Exception 
{
	private char player;
	byte xPos , yPos;
	int dir;
	public PlayerWonException(char p, byte x, byte y, int dir)
	{
		super();
		player=p;
		xPos=x;
		yPos=y;
		this.dir=dir;
	}
	public char getPlayer(){return player;}
	public byte getxPos() {return xPos;}
	public byte getyPos() {return yPos;}
	public int getDIR() {return dir;}
}