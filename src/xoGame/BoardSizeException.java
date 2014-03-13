package xoGame;
public class BoardSizeException extends IllegalArgumentException 
{
	private int argValue;
	private String argName;
	public BoardSizeException(String argName , int argValue)
	{
		super();
		this.argName=argName;
		this.argValue=argValue;
	}
	//add by me!!
	public int getArgValue(){return argValue;}
	public String getArgName(){return argName;}
}
