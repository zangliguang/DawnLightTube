
package zang.liguang.tube.bean;

import java.util.List;

import android.R.integer;

import com.j256.ormlite.field.DatabaseField;

public class YouTubeLanguageModle extends BaseModle {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * od
     */
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String hl;
    /**
     * name
     */
    @DatabaseField
    private String name;
    /**
     * namehl
     */
    @DatabaseField
    private String namehl;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getNamehl()
	{
		return namehl;
	}
	public void setNamehl(String namehl)
	{
		this.namehl = namehl;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getHl()
	{
		return hl;
	}
	public void setHl(String hl)
	{
		this.hl = hl;
	}
   
    
    
}
