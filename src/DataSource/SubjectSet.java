package DataSource;

public class SubjectSet 
{
	private int ppw = 0;
	private int tppw = 0;
	private String name;
	public SubjectSet(String Name, int noT, int noP) 
        {
		ppw = noP;
		tppw = noT;
		name = Name;
	}
	public String getName() 
        {
		return name;
	}
	public int getPPW() 
        {
		return ppw;
	}
	public int getTPPW() 
        {
		return tppw;
	}
	public void setName(String s) 
        {
		name=s;
	}
	public void setPPW(int i) 
        {
		ppw=i;
	}
	public void setTPPW(int i) 
        {
		tppw=i;
	}
}