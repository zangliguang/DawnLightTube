package zang.liguang.tube.bean;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;

public class YouTubeRegionModle extends BaseModle
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId=true)
	private int id;
	/**
	 * region id
	 */
	@DatabaseField
	private String gl;
	/**
	 * name
	 */
	@DatabaseField
	private String name;
	/**
	 * language id
	 */
	@DatabaseField
	private String hl;

	public String getGl()
	{
		return gl;
	}

	public void setGl(String gl)
	{
		this.gl = gl;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHl()
	{
		return hl;
	}

	public void setHl(String hl)
	{
		this.hl = hl;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
