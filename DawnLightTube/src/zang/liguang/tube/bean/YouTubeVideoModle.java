package zang.liguang.tube.bean;

import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;

import android.R.integer;

public class YouTubeVideoModle extends BaseModle
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	private String videoId;
	@DatabaseField
	private String channelId;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String channelTitle;
	@DatabaseField
	private Date publishedAt;
	@DatabaseField
	private String  defaulturl;
	@DatabaseField
	private String  mediumurl;
	@DatabaseField
	private String  highurl;
	public String getVideoId()
	{
		return videoId;
	}
	public void setVideoId(String videoId)
	{
		this.videoId = videoId;
	}
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
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getChannelTitle()
	{
		return channelTitle;
	}
	public void setChannelTitle(String channelTitle)
	{
		this.channelTitle = channelTitle;
	}
	public Date getPublishedAt()
	{
		return publishedAt;
	}
	public void setPublishedAt(Date publishedAt)
	{
		this.publishedAt = publishedAt;
	}
	public String getDefaulturl()
	{
		return defaulturl;
	}
	public void setDefaulturl(String defaulturl)
	{
		this.defaulturl = defaulturl;
	}
	public String getMediumurl()
	{
		return mediumurl;
	}
	public void setMediumurl(String mediumurl)
	{
		this.mediumurl = mediumurl;
	}
	public String getHighurl()
	{
		return highurl;
	}
	public void setHighurl(String highurl)
	{
		this.highurl = highurl;
	}
	
	
}
