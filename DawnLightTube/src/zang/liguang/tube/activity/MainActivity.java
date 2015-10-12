package zang.liguang.tube.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zang.liguang.tube.LiGuangApplication;
import zang.liguang.tube.R;
import zang.liguang.tube.adapter.NewsFragmentPagerAdapter;
import zang.liguang.tube.bean.ChannelItem;
import zang.liguang.tube.bean.YouTubeVideoCatgoryModle;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.fragment.RelatedVideoListFragment;
import zang.liguang.tube.fragment.RelatedVideoListFragment_;
import zang.liguang.tube.fragment.VideoListFragment;
import zang.liguang.tube.fragment.VideoListFragment_;
import zang.liguang.tube.http.HttpUtil;
import zang.liguang.tube.http.Url;
import zang.liguang.tube.http.WebConstant;
import zang.liguang.tube.initview.SlidingMenuView;
import zang.liguang.tube.utils.BaseTools;
import zang.liguang.tube.utils.DeveloperKey;
import zang.liguang.tube.utils.LocalConstant;
import zang.liguang.tube.utils.LocalConstant.shareconstans;
import zang.liguang.tube.utils.LogUtils;
import zang.liguang.tube.utils.SPUtils;
import zang.liguang.tube.utils.Utils;
import zang.liguang.tube.view.LeftView;
import zang.liguang.tube.view.LeftView_;
import zang.liguang.tube.view.dragpanel.DraggableListener;
import zang.liguang.tube.view.dragpanel.DraggablePanel;
import zang.liguang.tube.wedget.ColumnHorizontalScrollView;
import zang.liguang.tube.wedget.slidingmenu.SlidingMenu;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.DeleteBuilder;

@EActivity(R.layout.main)
public class MainActivity extends BaseActivity
{
	/** 自定义HorizontalScrollView */
	@ViewById(R.id.mColumnHorizontalScrollView)
	protected ColumnHorizontalScrollView mColumnHorizontalScrollView;
	@ViewById(R.id.mRadioGroup_content)
	protected LinearLayout mRadioGroup_content;
	@ViewById(R.id.ll_more_columns)
	protected LinearLayout ll_more_columns;
	@ViewById(R.id.rl_column)
	protected RelativeLayout rl_column;
	@ViewById(R.id.button_more_columns)
	protected ImageView button_more_columns;
	@ViewById(R.id.mViewPager)
	protected ViewPager mViewPager;
	@ViewById(R.id.shade_left)
	protected ImageView shade_left;
	@ViewById(R.id.shade_right)
	protected ImageView shade_right;
	@ViewById(R.id.progressBar)
	protected ProgressBar progressBar;
	
	@ViewById(R.id.draggable_panel)
	DraggablePanel draggablePanel;
	
	protected LeftView mLeftView;

	protected SlidingMenu side_drawer;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** head 头部 的左侧菜单 按钮 */
	@ViewById(R.id.top_head)
	protected ImageView top_head;
	/** head 头部 的右侧菜单 按钮 */
	@ViewById(R.id.top_more)
	protected ImageView top_more;
	/** 用户选择的新闻分类列表 */
	protected static ArrayList<ChannelItem> userChannelLists;
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	private ArrayList<Fragment> fragments;

	private Fragment newfragment;
	private double back_pressed;

	public static boolean isChange = false;

	private NewsFragmentPagerAdapter mAdapetr;

	private List<YouTubeVideoCatgoryModle> youTubeVideoCatgoryModleList;
	
	private YouTubePlayer youtubePlayer;

	private YouTubePlayerSupportFragment youtubeFragment;

	YouTubeVideoModle youTubeVideoModle;
	/** 当前选中的栏目 */

	@Override
	public boolean isSupportSlide()
	{
		return false;
	}

	@SuppressLint("InlinedApi")
	@AfterInject
	void init()
	{
		LogUtils.zang("init:@AfterInject");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		userChannelLists = new ArrayList<ChannelItem>();
		SPUtils.get(this, shareconstans.languagekey, Utils.GetLanguage(this));
		youTubeVideoCatgoryModleList = new ArrayList<YouTubeVideoCatgoryModle>();
		setYoutubeCatgoryModelListFromDb();
		fragments = new ArrayList<Fragment>();

	}

	public void setYoutubeCatgoryModelListFromDb()
	{
		try
		{
			youTubeVideoCatgoryModleList = LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().queryBuilder()
					.orderBy("orderid", true).where().eq("selected", true).and().eq("hl", Utils.GetLanguage(MainActivity.this)).query();
			LogUtils.zang(youTubeVideoCatgoryModleList.size() + "");
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Background
	protected void getYouTubeCatgoryListFromWeb()
	{
		String videoCatgoryResult = "";
		try
		{
			String result2 = HttpUtil.getByHttpClient(this,
					"https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&hl=zh&regionCode=US&key=AIzaSyCvAB4P7jOIUBuPEXyaHtshu8h_URDt6Uw");
			System.out.println(result2);
			videoCatgoryResult = HttpUtil.getByHttpClient(this, Url.youtubeGetVideoCategorieslist, new BasicNameValuePair(WebConstant.YoutubeParams.part,
					WebConstant.YoutubeParams.snippet), new BasicNameValuePair(WebConstant.YoutubeParams.key, WebConstant.YoutubeParams.keyvalue),
					new BasicNameValuePair(WebConstant.YoutubeParams.hl, SPUtils.get(this, shareconstans.languagekey, Utils.GetLanguage(this)).toString()),
					new BasicNameValuePair(WebConstant.YoutubeParams.regionCode, "us"));

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(videoCatgoryResult))
		{

			JSONObject videoCatgoryResultJsonObject;
			JSONArray videoCatgoryResultJsonArray;
			try
			{
				JSONObject videocatogoryJO;
				Gson gson = new Gson();
				videoCatgoryResultJsonObject = new JSONObject(videoCatgoryResult);
				videoCatgoryResultJsonArray = videoCatgoryResultJsonObject.getJSONArray(WebConstant.YoutubeParams.videocatgoryResultname);
				for (int i = 0; i < videoCatgoryResultJsonArray.length(); i++)
				{
					videocatogoryJO = ((JSONObject) videoCatgoryResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet);
					YouTubeVideoCatgoryModle ss = new YouTubeVideoCatgoryModle();
					ss.setChannelId(videocatogoryJO.getString(LocalConstant.channelId));
					ss.setAssignable(videocatogoryJO.getBoolean(LocalConstant.assignable));
					ss.setTitle(videocatogoryJO.getString(LocalConstant.title));
					ss.setHl(Utils.GetLanguage(this).toString());
					ss.setOrderid(i);
					ss.setVideoCategoryId(((JSONObject) videoCatgoryResultJsonArray.get(i)).get(WebConstant.YoutubeParams.id).toString());
					ss.setSelected(i < 10);
					LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().createOrUpdate(ss);
				}
				DeleteBuilder<YouTubeVideoCatgoryModle, Integer>	 deleteBuilder = LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().deleteBuilder();
				deleteBuilder.where().not().eq("hl", Utils.GetLanguage(this));
				deleteBuilder.delete();
				setChangelView();
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@AfterViews
	void initView()
	{
		try
		{
			
			LogUtils.zang("initView:@AfterViews");
			initSlidingMenu();
			initViewPager();
			if (youTubeVideoCatgoryModleList.size() == 0)
			{
				getYouTubeCatgoryListFromWeb();
			}else{
				setChangelView();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private void initViewPager()
	{
		mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	protected void initSlidingMenu()
	{
		mLeftView = LeftView_.build(this);
		side_drawer = SlidingMenuView.instance().initSlidingMenuView(this, mLeftView);
	}

	@Click(R.id.top_head)
	protected void onMenu(View view)
	{
		if (side_drawer.isMenuShowing())
		{
			side_drawer.showContent();
		}
		else
		{
			side_drawer.showMenu();
		}
	}

	// @Click(R.id.top_more)
	// protected void onAcount(View view) {
	// if (side_drawer.isSecondaryMenuShowing()) {
	// side_drawer.showContent();
	// } else {
	// side_drawer.showSecondaryMenu();
	// }
	// }

	@Click(R.id.button_more_columns)
	protected void onMoreColumns(View view)
	{
		openActivityForResult(ChannelActivity_.class, CHANNELREQUEST);
	}

	/**
	 * 当栏目项发生变化时候调用
	 */
	@UiThread
	public void setChangelView()
	{
		initColumnData();

	}

	/** 获取Column栏目 数据 */
	private void initColumnData()
	{
		setYoutubeCatgoryModelListFromDb();
		initTabColumn();
		initFragment();
	}

	/**
	 * 初始化Column栏目项
	 */
	private void initTabColumn()
	{
		mRadioGroup_content.removeAllViews();
		int count = userChannelLists.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for (int i = 0; i < youTubeVideoCatgoryModleList.size(); i++)
		{
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(youTubeVideoCatgoryModleList.get(i).getTitle());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if (columnSelectIndex == i)
			{
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++)
					{
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else
						{
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
						}
					}
				}
			});
			mRadioGroup_content.addView(columnTextView, i, params);
		}
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment()
	{
		fragments.clear();
		int count = youTubeVideoCatgoryModleList.size();
		for (int i = 0; i < count; i++)
		{
			// Bundle data = new Bundle();
			// data.putString("text", nameString);
			// data.putInt("id", userChannelList.get(i).getId());
			// initFragment(nameString);
			// map.put(nameString, nameString);
			// newfragment.setArguments(data);
			// fragments.add(initFragment(nameString));
			// fragments.add(nameString);
			fragments.add(initFragment(youTubeVideoCatgoryModleList.get(i)));
		}
		// fragments.add(initFragment("asd"));
		mAdapetr.appendList(fragments);
		progressBar.setVisibility(View.GONE);
		mAdapetr.notifyDataSetChanged();
	}

	public Fragment initFragment(YouTubeVideoCatgoryModle youTubeVideoCatgoryModle)
	{
		VideoListFragment myFragment = VideoListFragment_.builder().youTubeVideoCatgoryModle(youTubeVideoCatgoryModle).build();
		return VideoListFragment_.builder().youTubeVideoCatgoryModle(youTubeVideoCatgoryModle).build();
	}

	/**
	 * ViewPager切换监听方法
	 */
	public OnPageChangeListener pageListener = new OnPageChangeListener()
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int position)
		{
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	/**
	 * 选择的Column里面的Tab
	 */
	private void selectTab(int tab_postion)
	{
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++)
		{
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++)
		{
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion)
			{
				ischeck = true;
			}
			else
			{
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{
			if (isChange)
			{
				setChangelView();
				// initColumnData();
				// initTabColumn();
				isChange = false;
				 MainActivity_.intent(this).start();
			    	finish();
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 点击两次返回退出系统
	 * 
	 * @param view
	 */
	@Override
	public void onBackPressed()
	{
		if(null!=draggablePanel&&draggablePanel.isMaximized()){
			draggablePanel.minimize();
			return;
		}
		if (side_drawer.isMenuShowing())
		{
			side_drawer.showContent();
		}
		else
		{
			System.out.println(isShow());
			if (isShow())
			{
				dismissProgressDialog();
			}
			else
			{
				if (back_pressed + 3000 > System.currentTimeMillis())
				{
					finish();
					super.onBackPressed();
				}
				else
					showCustomToast(getString(R.string.press_again_exit));
				// Toast.makeText(this, getString(R.string.press_again_exit),
				// 1).show();

				back_pressed = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

	}

	@Override
	public void onPause()
	{
		super.onPause();

	}
	
	
	
	/**
	 * Initialize the YouTubeSupportFrament attached as top fragment to the
	 * DraggablePanel widget and reproduce the YouTube video represented with a
	 * YouTube url.
	 */
	public void initializeYoutubeFragment(final YouTubeVideoModle youTubeVideoModle2)
	{
		youTubeVideoModle=youTubeVideoModle2;
		youtubeFragment = new YouTubePlayerSupportFragment();
		youtubeFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener()
		{

			@Override
			public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
			{
				if (!wasRestored)
				{
					youtubePlayer = player;
					youtubePlayer.loadVideo(youTubeVideoModle2.getVideoId());
					youtubePlayer.setShowFullscreenButton(true);
				}
			}

			@Override
			public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason)
			{
				      Toast.makeText(MainActivity.this, errorReason.toString(), Toast.LENGTH_LONG).show();
			}
		});
	}
	
	
	/**
	 * Initialize and configure the DraggablePanel widget with two fragments and
	 * some attributes.
	 */
	public void initializeDraggablePanel()
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
	  public void hookDraggablePanelListeners() {
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

	public YouTubeVideoModle getYouTubeVideoModle()
	{
		return youTubeVideoModle;
	}

	public void setYouTubeVideoModle(YouTubeVideoModle youTubeVideoModle)
	{
		this.youTubeVideoModle = youTubeVideoModle;
	}

	public DraggablePanel getDraggablePanel()
	{
		return draggablePanel;
	}
	  
}
