package DataSource;

import java.util.ArrayList;

public class ActivitySet 
{
	private int id;
	private String tag;
	private String teacher;
	private String student;
	private String subject;
	private String homeRoom;
	private ArrayList<String> room;
	private int Duration;

	public ActivitySet(int i)
        {
		setId(i);
		room=new ArrayList<String>();
	}
	public void setHomeRoom(String homeRoom)
        {
		this.homeRoom = homeRoom;
	}
	public String getHomeRoom() 
        {
		return homeRoom;
	}
	public ArrayList<String> getRooms() 
        {
		return room;
	}
	public void setId(int i)
        {
		id = i;
	}
	public int getId() 
        {
		return id;
	}
	public void setTag(String acttag) 
        {
		tag = acttag;
	}
	public String getTag() 
        {
		return tag;
	}
	public void setSubject(String subj) 
        {
		subject = subj;
	}
	public String getSubject() 
        {
		return subject;
	}
	public void setStudent(String stud)
        {
		student=stud;
	}
	public String getStudent() 
        {
		return student;
	}
	public void setDuration(int Dur) 
        {
		Duration = Dur;
	}
	public int getDuration() 
        {
		return Duration;
	}
	public void setTeacher(String tch) 
        {
		teacher=tch;
	}
	public String getTeacher() 
        {
		return teacher;
	}

}
