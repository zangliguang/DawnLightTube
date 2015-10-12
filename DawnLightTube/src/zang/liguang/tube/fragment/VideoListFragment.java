
package zang.liguang.tube.fragment;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.SetValue;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import zang.liguang.tube.R;
import zang.liguang.tube.activity.*;
import zang.liguang.tube.adapter.CardsAnimationAdapter;
import zang.liguang.tube.adapter.VideoAdapter;
import zang.liguang.tube.adapter.YoutubeVideoAdapter;
import zang.liguang.tube.bean.NewModle;
import zang.liguang.tube.bean.YouTubeVideoCatgoryModle;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.http.HttpUtil;
import zang.liguang.tube.http.Url;
import zang.liguang.tube.http.WebConstant;
import zang.liguang.tube.http.json.NewListJson;
import zang.liguang.tube.initview.InitView;
import zang.liguang.tube.utils.LocalConstant;
import zang.liguang.tube.utils.LogUtils;
import zang.liguang.tube.utils.StringUtils;
import zang.liguang.tube.wedget.swiptlistview.SwipeListView;
import zang.liguang.tube.wedget.viewimage.Animations.SliderLayout;
import zang.liguang.tube.wedget.viewimage.SliderTypes.BaseSliderView;
import zang.liguang.tube.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EFragment(R.layout.activity_main)
public class VideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
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

    @Bean
    protected YoutubeVideoAdapter youtubeVideoAdapter;
    protected List<YouTubeVideoModle> listsModles;
    private int index = 0;
    private String pageToken ="";
    private boolean isRefresh = false;
    
    
    
    @FragmentArg("youTubeVideoCatgoryModle")
    YouTubeVideoCatgoryModle youTubeVideoCatgoryModle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @AfterInject
    protected void init() {
        listsModles = new ArrayList<YouTubeVideoModle>();
        url_maps = new HashMap<String, String>();

        newHashMap = new HashMap<String, NewModle>();
        LogUtils.zang(youTubeVideoCatgoryModle.getTitle());
    }

    @AfterViews
    protected void initView() {

        swipeLayout.setOnRefreshListener(this);
        InitView.instance().initSwipeRefreshLayout(swipeLayout);
        InitView.instance().initListView(mListView, getActivity());
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
        mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
        // mListView.addHeaderView(headView);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(youtubeVideoAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
       // loadData(getCommonUrl(index + "", Url.NBAId));
        loadData(Url.youtubeGetCategoriyVideoslist, youTubeVideoCatgoryModle);

        mListView.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(TextUtils.isEmpty(pageToken)){
            		mListView.onBottomComplete();
            	}else{
            		currentPagte++;
                    index = index + 50;
                    loadData(Url.youtubeGetCategoriyVideoslist, youTubeVideoCatgoryModle);
            	}
                
            }
        });
    }

	private void loadData(String url, YouTubeVideoCatgoryModle youTubeVideoCatgoryModle) {
        if (getMyActivity().hasNetWork()) {
            loadVideoList(url,youTubeVideoCatgoryModle);
        } else {
            mListView.onBottomComplete();
            mProgressBar.setVisibility(View.GONE);
            getMyActivity().showShortToast(getString(R.string.not_network));
            String result = getMyActivity().getCacheStr("VideoListFragment" + currentPagte);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPagte = 1;
                isRefresh = true;
                pageToken="";
                loadData(Url.youtubeGetCategoriyVideoslist, youTubeVideoCatgoryModle);
                url_maps.clear();
                mDemoSlider.removeAllSliders();
            }
        }, 2000);
    }

    @ItemClick(R.id.listview)
    protected void onItemClick(int position) {
//    	((MainActivity_) getActivity()).setYouTubeVideoModle(listsModles.get(position));
    	YouTubeVideoModle youTubeVideoModle = listsModles.get(position);
    	//((MainActivity) getActivity()).getYoutubePlayer().loadVideo(((YouTubeVideoModle) youtubeVideoAdapter.getItem(position)).getVideoId());
    	Toast.makeText(getActivity(), "点击播放", Toast.LENGTH_SHORT).show();
    	if(null!=((MainActivity) getActivity()).getYoutubePlayer()){
    		((MainActivity) getActivity()).getYoutubePlayer().loadVideo(((YouTubeVideoModle) youtubeVideoAdapter.getItem(position)).getVideoId());
    		((RelatedVideoListFragment) ((MainActivity) getActivity()).getDraggablePanel().getBottomFragment()).setYouTubeVideoModle((YouTubeVideoModle) youtubeVideoAdapter.getItem(position));
    		((RelatedVideoListFragment) ((MainActivity) getActivity()).getDraggablePanel().getBottomFragment()).loadData(Url.youtubeGetCategoriyVideoslist);
    		((TextView) ((RelatedVideoListFragment) ((MainActivity) getActivity()).getDraggablePanel().getBottomFragment()).getHeadView().findViewById(R.id.video_title)).setText(((YouTubeVideoModle) youtubeVideoAdapter.getItem(position)).getTitle());
    		((TextView) ((RelatedVideoListFragment) ((MainActivity) getActivity()).getDraggablePanel().getBottomFragment()).getHeadView().findViewById(R.id.video_description)).setText(((YouTubeVideoModle) youtubeVideoAdapter.getItem(position)).getDescription());
    		((MainActivity) getActivity()).getDraggablePanel().maximize();
    	}else{
    		((MainActivity) getActivity()).initializeYoutubeFragment( listsModles.get(position));
        	((MainActivity) getActivity()).initializeDraggablePanel();
        	((MainActivity) getActivity()).hookDraggablePanelListeners();
    	}
    	
//    	 Bundle bundle = new Bundle();
//         bundle.putSerializable("youTubeVideoModle", youTubeVideoModle);
//         Intent intent=new Intent(getActivity(),YoutubeVideoPlayActivity_.class);
//         intent.putExtra("youTubeVideoModle",youTubeVideoModle);
//         startActivity(intent);
        //enterDetailActivity(youTubeVideoModle);
    }

    public void enterDetailActivity(NewModle newModle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("newModle", newModle);
        Class<?> class1;
        if (newModle.getImagesModle() != null && newModle.getImagesModle().getImgList().size() > 1) {
            class1 = ImageDetailActivity_.class;
        } else {
            class1 = DetailsActivity_.class;
        }
        ((BaseActivity) getActivity()).openActivity(class1,
                bundle, 0);
    }

    @Background
    void loadVideoList(String url, YouTubeVideoCatgoryModle youTubeVideoCatgoryModle2) {
        String result;
        try {
           
        	
        	if(youTubeVideoCatgoryModle2.getChannelId().equals(LocalConstant.local)){
        		result = HttpUtil.getByHttpClient(getActivity(), url,
                		new BasicNameValuePair(WebConstant.YoutubeParams.part,WebConstant.YoutubeParams.snippet),
                		new BasicNameValuePair(WebConstant.YoutubeParams.order,WebConstant.YoutubeParams.date),
                		new BasicNameValuePair(WebConstant.YoutubeParams.type,WebConstant.YoutubeParams.video),
                		new BasicNameValuePair(WebConstant.YoutubeParams.regionCode,WebConstant.YoutubeParams.US),
                		new BasicNameValuePair(WebConstant.YoutubeParams.querycontent,youTubeVideoCatgoryModle2.getVideoCategoryId().replace(" ", "%20")),
                		new BasicNameValuePair(WebConstant.YoutubeParams.maxResults,"50"),
                		new BasicNameValuePair(WebConstant.YoutubeParams.pageToken,pageToken),
                		new BasicNameValuePair(WebConstant.YoutubeParams.key,WebConstant.YoutubeParams.keyvalue)
                		);
        	}else{
        		result = HttpUtil.getByHttpClient(getActivity(), url,
                		new BasicNameValuePair(WebConstant.YoutubeParams.part,WebConstant.YoutubeParams.snippet),
                		new BasicNameValuePair(WebConstant.YoutubeParams.order,WebConstant.YoutubeParams.date),
                		new BasicNameValuePair(WebConstant.YoutubeParams.type,WebConstant.YoutubeParams.video),
                		new BasicNameValuePair(WebConstant.YoutubeParams.regionCode,WebConstant.YoutubeParams.US),
                		new BasicNameValuePair(WebConstant.YoutubeParams.videoCategoryId,youTubeVideoCatgoryModle2.getVideoCategoryId()),
                		new BasicNameValuePair(WebConstant.YoutubeParams.maxResults,"50"),
                		new BasicNameValuePair(WebConstant.YoutubeParams.pageToken,pageToken),
                		new BasicNameValuePair(WebConstant.YoutubeParams.key,WebConstant.YoutubeParams.keyvalue)
                		);
        	}
        	
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void getResult(String VideoResult) {
        //getMyActivity().setCacheStr("VideoListFragment" + currentPagte, VideoResult);
        if (isRefresh) {
            isRefresh = false;
            youtubeVideoAdapter.clear();
            listsModles.clear();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeLayout.setRefreshing(false);

        List<YouTubeVideoModle> YouTubeVideoModleList=getVideoListFromJson(VideoResult);
        youtubeVideoAdapter.appendList(YouTubeVideoModleList);
        listsModles.addAll(YouTubeVideoModleList);
        mListView.onBottomComplete();
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
 			    if(videoResultJsonesultJson.has(WebConstant.YoutubeParams.nextPageToken)){
 			    	pageToken=videoResultJsonesultJson.getString(WebConstant.YoutubeParams.nextPageToken);
 			    }else{
 			    	pageToken="";
 			    }
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
}
