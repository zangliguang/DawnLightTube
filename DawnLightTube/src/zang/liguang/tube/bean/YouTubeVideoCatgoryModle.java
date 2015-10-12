package zang.liguang.tube.bean;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;

import android.R.integer;

public class YouTubeVideoCatgoryModle extends BaseModle
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String videoCategoryId;
	@DatabaseField
	private String channelId;
	@DatabaseField
	private String title;
	@DatabaseField
	private boolean assignable;
	@DatabaseField
	private String hl;
	@DatabaseField
	private boolean selected;
	@DatabaseField
	private int orderid;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getHl()
	{
		return hl;
	}

	public void setHl(String hl)
	{
		this.hl = hl;
	}

	public boolean isAssignable()
	{
		return assignable;
	}

	public void setAssignable(boolean assignable)
	{
		this.assignable = assignable;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public int getOrderid()
	{
		return orderid;
	}

	public void setOrderid(int orderid)
	{
		this.orderid = orderid;
	}

	public String getVideoCategoryId()
	{
		return videoCategoryId;
	}

	public void setVideoCategoryId(String videoCategoryId)
	{
		this.videoCategoryId = videoCategoryId;
	}

}
