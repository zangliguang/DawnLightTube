
package zang.liguang.tube.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zang.liguang.tube.R;
import zang.liguang.tube.activity.MainActivity;
import zang.liguang.tube.activity.YoutubeVideoPlayActivity;
import zang.liguang.tube.adapter.CardsAnimationAdapter;
import zang.liguang.tube.adapter.RelativeVideoAdapter;
import zang.liguang.tube.bean.NewModle;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.http.HttpUtil;
import zang.liguang.tube.http.Url;
import zang.liguang.tube.http.WebConstant;
import zang.liguang.tube.initview.InitView;
import zang.liguang.tube.utils.StringUtils;
import zang.liguang.tube.wedget.swiptlistview.SwipeListView;
import zang.liguang.tube.wedget.viewimage.Animations.SliderLayout;
import zang.liguang.tube.wedget.viewimage.SliderTypes.BaseSliderView;
import zang.liguang.tube.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

@EFragment(R.layout.activity_main)
public class RelatedVideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnSliderClickListener {
    protected SliderLayout mDemoSlider;
    @ViewById(R.id.swipe_container)
    protected SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.listview)
    protected SwipeListView mListView;
    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NewModle> newHashMap;
    View headView;

    @Bean
    protected RelativeVideoAdapter relativeVideoAdapter;
    protected List<NewModle> listsModles;
    private int index = 0;
    private boolean isRefresh = false;
    
    @FragmentArg
    YouTubeVideoModle youTubeVideoModle;
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @AfterInject
    protected void init() {
        listsModles = new ArrayList<NewModle>();
        url_maps = new HashMap<String, String>();

        newHashMap = new HashMap<String, NewModle>();
    }

    @AfterViews
    protected void initView() {

        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mListView, getActivity());
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.relative_video_head_item, null);
        ((TextView) headView.findViewById(R.id.video_title)).setText(youTubeVideoModle.getTitle());
        final TextView VideoDescriptionView = ((TextView) headView.findViewById(R.id.video_description));
        VideoDescriptionView.setText(youTubeVideoModle.getDescription());
        ImageView showDescriptionImageView=(ImageView) headView.findViewById(R.id.show_description);
        if(TextUtils.isEmpty(youTubeVideoModle.getDescription())){
        	showDescriptionImageView.setVisibility(View.INVISIBLE);
        }
        showDescriptionImageView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (VideoDescriptionView.getVisibility() == View.VISIBLE)
				{
					v.setBackgroundResource(R.drawable.ic_action_next_normal);
					VideoDescriptionView.setVisibility(View.GONE);
				}
				else
				{
					v.setBackgroundResource(R.drawable.ic_action_previous_normal);
					VideoDescriptionView.setVisibility(View.VISIBLE);
				}

			}
		});
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        mListView.addHeaderView(headView);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(relativeVideoAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        loadData(Url.youtubeGetCategoriyVideoslist);
        mListView.setShowFooterProgressBar(false);
        mListView.setDropDownStyle(false);
        mListView.getFooterButton().setVisibility(View.GONE);
    }

    void loadData(String url) {
        if (getMyActivity().hasNetWork()) {
            loadNewList(url);
        } else {
//            mListView.onBottomComplete();
//            mProgressBar.setVisibility(View.GONE);
//            getMyActivity().showShortToast(getString(R.string.not_network));
//            String result = getMyActivity().getCacheStr("DianYingFragment" + currentPagte);
//            if (!StringUtils.isEmpty(result)) {
//                getResult(result);
//            }
        }
    }


    @Override
    public void onRefresh() {
    	
    }

    @ItemClick(R.id.listview)
    protected void onItemClick(int position) {
    	String videoid = ((YouTubeVideoModle) relativeVideoAdapter.getItem(position)).getVideoId();
    	((MainActivity) getActivity()).getYoutubePlayer().loadVideo(videoid);
    	 ((TextView) headView.findViewById(R.id.video_description)).setText(((YouTubeVideoModle) relativeVideoAdapter.getItem(position)).getDescription());
    	 ((TextView) headView.findViewById(R.id.video_title)).setText(((YouTubeVideoModle) relativeVideoAdapter.getItem(position)).getTitle());
    }

    public void enterDetailActivity(NewModle newModle) {
    	
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("newModle", newModle);
//        Class<?> class1;
//        if (newModle.getImagesModle() != null && newModle.getImagesModle().getImgList().size() > 1) {
//            class1 = YoutubeVideoPlayActivity_.class;
//        } else {
//            class1 = YoutubeVideoPlayActivity_.class;
//        }
//        ((BaseActivity) getActivity()).openActivity(class1,
//                bundle, 0);
    }

    @Background
    void loadNewList(String url) {
        String result;
        try {
            result = HttpUtil.getByHttpClient(getActivity(), url,
            		new BasicNameValuePair(WebConstant.YoutubeParams.part,WebConstant.YoutubeParams.snippet),
            		new BasicNameValuePair(WebConstant.YoutubeParams.order,WebConstant.YoutubeParams.date),
            		new BasicNameValuePair(WebConstant.YoutubeParams.type,WebConstant.YoutubeParams.video),
            		new BasicNameValuePair(WebConstant.YoutubeParams.regionCode,WebConstant.YoutubeParams.US),
            		new BasicNameValuePair(WebConstant.YoutubeParams.relatedToVideoId,youTubeVideoModle.getVideoId()),
            		new BasicNameValuePair(WebConstant.YoutubeParams.maxResults,"50"),
            		new BasicNameValuePair(WebConstant.YoutubeParams.key,WebConstant.YoutubeParams.keyvalue)
            		
            		
            		);
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void getResult(String result) {
       // getMyActivity().setCacheStr("DianYingFragment" + currentPagte, result);
        if (isRefresh) {
            isRefresh = false;
            relativeVideoAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);

        List<YouTubeVideoModle> YouTubeVideoModleList=getVideoListFromJson(result);
        relativeVideoAdapter.clear();
        relativeVideoAdapter.appendList(YouTubeVideoModleList);
        mListView.onBottomComplete();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        NewModle newModle = newHashMap.get(slider.getUrl());
        enterDetailActivity(newModle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    
    
	public List<YouTubeVideoModle> getVideoListFromJson(String VideoResult)
	{
		 List<YouTubeVideoModle> YouTubeVideoModleList=new ArrayList<YouTubeVideoModle>();
		if(!TextUtils.isEmpty(VideoResult)){
			try
			{  
			    YouTubeVideoModle youTubeVideoModle;
			    String publishtime="";
			    SimpleDateFormat sdf=sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
 			    JSONObject videoResultJsonesultJson = new JSONObject(VideoResult);
				JSONArray videoResultJsonArray=videoResultJsonesultJson.getJSONArray(WebConstant.YoutubeParams.videocatgoryResultname);
			    for(int i=0;i<videoResultJsonArray.length();i++){
			    	youTubeVideoModle=new YouTubeVideoModle();
			    	youTubeVideoModle.setVideoId(((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.id).getString(WebConstant.YoutubeParams.videoId));
			    	youTubeVideoModle.setChannelId(((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet).getString(WebConstant.YoutubeParams.channelId));
			    	youTubeVideoModle.setTitle(((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet).getString(WebConstant.YoutubeParams.title));
			    	youTubeVideoModle.setDescription(((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet).getString(WebConstant.YoutubeParams.description));
			    	youTubeVideoModle.setChannelTitle(((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet).getString(WebConstant.YoutubeParams.channelTitle));
			    	publishtime=((JSONObject) videoResultJsonArray.get(i)).getJSONObject(WebConstant.YoutubeParams.snippet).getString(WebConstant.YoutubeParams.publishedAt);
			    	youTubeVideoModle.setPublishedAt(sdf.parse(publishtime.replace("Z", " UTC")));
			    	YouTubeVideoModleList.add(youTubeVideoModle);
			    }
			  
			
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   	
        }
		return YouTubeVideoModleList;
	}

	public YouTubeVideoModle getYouTubeVideoModle()
	{
		return youTubeVideoModle;
	}

	public void setYouTubeVideoModle(YouTubeVideoModle youTubeVideoModle)
	{
		this.youTubeVideoModle = youTubeVideoModle;
	}

	public View getHeadView()
	{
		return headView;
	}
	
	
}
