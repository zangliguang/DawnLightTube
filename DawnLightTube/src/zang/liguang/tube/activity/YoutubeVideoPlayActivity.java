package zang.liguang.tube.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.fragment.RelatedVideoListFragment;
import zang.liguang.tube.fragment.RelatedVideoListFragment_;
import zang.liguang.tube.utils.DeveloperKey;
import zang.liguang.tube.view.dragpanel.DraggableListener;
import zang.liguang.tube.view.dragpanel.DraggablePanel;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
@EActivity(R.layout.activity_youtube_video_play)
public class YoutubeVideoPlayActivity extends BaseActivity
{
	
	private static final String VIDEO_POSTER_THUMBNAIL = "http://4.bp.blogspot.com/-BT6IshdVsoA/UjfnTo_TkBI/AAAAAAAAMWk/JvDCYCoFRlQ/s1600/"
			+ "xmenDOFP.wobbly.1.jpg";
	private static final String SECOND_VIDEO_POSTER_THUMBNAIL = "http://media.comicbook.com/wp-content/uploads/2013/07/x-men-days-of-future-past"
			+ "-wolverine-poster.jpg";
	private static final String VIDEO_POSTER_TITLE = "X-Men: Days of Future Past";

	@ViewById(R.id.draggable_panel)
	DraggablePanel draggablePanel;

	@Extra(value="youTubeVideoModle")
	YouTubeVideoModle youTubeVideoModle;
	private YouTubePlayer youtubePlayer;
	private YouTubePlayerSupportFragment youtubeFragment;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	
	
	@AfterViews
	void initView()
	{
		initializeYoutubeFragment();
		initializeDraggablePanel();
		hookDraggablePanelListeners();
	}
	
	
	
	
	
	
	
	/**
	 * Initialize the YouTubeSupportFrament attached as top fragment to the
	 * DraggablePanel widget and reproduce the YouTube video represented with a
	 * YouTube url.
	 */
	private void initializeYoutubeFragment()
	{
		youtubeFragment = new YouTubePlayerSupportFragment();
		youtubeFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener()
		{

			@Override
			public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
			{
				if (!wasRestored)
				{
					youtubePlayer = player;
					youtubePlayer.loadVideo(youTubeVideoModle.getVideoId());
					youtubePlayer.setShowFullscreenButton(true);
				}
			}

			@Override
			public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error)
			{
			}
		});
	}
	
	
	/**
	 * Initialize and configure the DraggablePanel widget with two fragments and
	 * some attributes.
	 */
	private void initializeDraggablePanel()
	{
		draggablePanel.setFragmentManager(getSupportFragmentManager());
		draggablePanel.setTopFragment(youtubeFragment);
		
		RelatedVideoListFragment relaytiveVideoListFragment = new RelatedVideoListFragment_().builder().youTubeVideoModle(youTubeVideoModle).build();
		draggablePanel.setBottomFragment(relaytiveVideoListFragment);
		draggablePanel.initializeView();
		//Picasso.with(this).load(SECOND_VIDEO_POSTER_THUMBNAIL).placeholder(R.drawable.xmen_placeholder).into(thumbnailImageView);
	}
	
	 /**
	   * Hook the DraggableListener to DraggablePanel to pause or resume the video when the
	   * DragglabePanel is maximized or closed.
	   */
	  private void hookDraggablePanelListeners() {
	    draggablePanel.setDraggableListener(new DraggableListener() {
	      @Override public void onMaximized() {
	        playVideo();
	      }

	      @Override public void onMinimized() {
	        //Empty
	      }

	      @Override public void onClosedToLeft() {
	        pauseVideo();
	      }

	      @Override public void onClosedToRight() {
	        pauseVideo();
	      }
	    });
	  }
	  
	  
	  /**
	   * Pause the video reproduced in the YouTubePlayer.
	   */
	  private void pauseVideo() {
	    if (youtubePlayer.isPlaying()) {
	      youtubePlayer.pause();
	    }
	  }

	  /**
	   * Resume the video reproduced in the YouTubePlayer.
	   */
	  private void playVideo() {
	    if (!youtubePlayer.isPlaying()) {
	      youtubePlayer.play();
	    }
	  }







	public YouTubePlayer getYoutubePlayer()
	{
		return youtubePlayer;
	}
	  
	  
}
