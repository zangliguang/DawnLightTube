package zang.liguang.tube.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import zang.liguang.tube.LiGuangApplication;
import zang.liguang.tube.R;
import zang.liguang.tube.adapter.DragAdapter;
import zang.liguang.tube.adapter.OtherAdapter;
import zang.liguang.tube.bean.YouTubeVideoCatgoryModle;
import zang.liguang.tube.bean.ChannelManage;
import zang.liguang.tube.http.WebConstant;
import zang.liguang.tube.utils.LocalConstant;
import zang.liguang.tube.utils.Utils;
import zang.liguang.tube.wedget.DragGrid;
import zang.liguang.tube.wedget.OtherGridView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 频道管理
 */
@EActivity(R.layout.channel)
public class ChannelActivity extends BaseActivity implements OnItemClickListener
{
	public static String TAG = "ChannelActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	List<YouTubeVideoCatgoryModle> otherChannelList = new ArrayList<YouTubeVideoCatgoryModle>();
	/** 用户栏目列表 */
	List<YouTubeVideoCatgoryModle> userChannelList = new ArrayList<YouTubeVideoCatgoryModle>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;

	@ViewById(R.id.local_chanel_edit)
	EditText localChanelEditText;
	@ViewById(R.id.add_local_chanel_btn)
	Button addLocalChanelBtn;

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.channel);
	// try {
	// initView();
	// initData();
	// } catch (Exception e) {ss
	// e.printStackTrace();
	// }
	// }

	
	
	@Click(R.id.add_local_chanel_btn)
    public void AddLocalChanel(View view) {
		if(!TextUtils.isEmpty(localChanelEditText.getText().toString())){
			YouTubeVideoCatgoryModle channel=new YouTubeVideoCatgoryModle();
			channel.setChannelId(LocalConstant.local);
			channel.setAssignable(false);
			channel.setTitle(localChanelEditText.getText().toString());
			channel.setHl(Utils.GetLanguage(this).toString());
			channel.setOrderid(userAdapter.getCount());
			channel.setSelected(true);
			channel.setVideoCategoryId(localChanelEditText.getText().toString());
			
			try
			{
				LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().createOrUpdate(channel);
				userAdapter.addItem(channel);
				userAdapter.notifyDataSetChanged();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			Toast.makeText(this, "请填写自定义频道名称", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	/** 初始化布局 */
	@AfterViews
	void initView()
	{
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		initData();
	}

	/** 初始化数据 */
	@Background
	void initData()
	{
		try
		{
			userChannelList = LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().queryBuilder().orderBy("orderid", true).where()
					.eq("selected", true).query();
			otherChannelList = LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().queryBuilder().orderBy("orderid", true).where()
					.eq("selected", false).query();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setData();
	}

	@UiThread
	void setData()
	{
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);
		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
	{
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove)
		{
			return;
		}
		switch (parent.getId())
		{
		case R.id.userGridView:
			// position为 0，1 的不可以进行任何操作
			final ImageView moveImageView = getView(view);
			if (moveImageView != null)
			{
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final YouTubeVideoCatgoryModle channel = ((DragAdapter) parent.getAdapter()).getItem(position);// 获取点击的频道内容
				if(channel.getChannelId().equals(LocalConstant.local)){
					try
					{
						LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().delete(channel);
						userAdapter.setRemove(position);
						userAdapter.remove();
						
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					otherAdapter.setVisible(false);
					// 添加到最后一个
					channel.setSelected(false);
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								int[] endLocation = new int[2];
								// 获取终点的坐标
								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
								moveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
								userAdapter.setRemove(position);
								LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().update(channel);
								// ChannelManage.getManage(LiGuangApplication.getApp().getSQLHelper())
								// .updateChannel(
								// channel, "0");
							}
							catch (Exception localException)
							{
							}
						}
					}, 50L);

				}
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView2 = getView(view);
			if (moveImageView2 != null)
			{
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final YouTubeVideoCatgoryModle channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				channel.setSelected(true);
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							moveAnim(moveImageView2, startLocation, endLocation, channel, otherGridView);
							otherAdapter.setRemove(position);
							LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().update(channel);
						}
						catch (Exception localException)
						{
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void moveAnim(View moveView, int[] startLocation, int[] endLocation, final YouTubeVideoCatgoryModle moveChannel, final GridView clickGridView)
	{
		// 将当前栏目增加到改变过的listview中 若栏目已经存在删除点，不存在添加进去

		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid)
				{
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}
				else
				{
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation)
	{
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup()
	{
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view)
	{
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/** 退出时候保存选择后数据库的设置 */
	void saveChannel()
	{
		ChannelManage.getManage(LiGuangApplication.getApp().getSQLHelper()).deleteAllChannel();
		// ChannelManage.getManage(LiGuangApplication.getApp().getSQLHelper()).saveUserChannel(
		// userAdapter.getChannnelLst());
		// ChannelManage.getManage(LiGuangApplication.getApp().getSQLHelper()).saveOtherChannel(
		// otherAdapter.getChannnelLst());
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

	@Override
	public void finish()
	{
		super.finish();

		if (userAdapter.isListChanged())
		{
			YouTubeVideoCatgoryModle ycm;
			for (int i = 0; i < userAdapter.getChannnelLst().size(); i++)
			{
				ycm = userAdapter.getChannnelLst().get(i);
				ycm.setOrderid(i);
				try
				{
					LiGuangApplication.getApp().getDatabaseHelper().getYouTubeVideoCatgoryModleDao().update(ycm);
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			MainActivity_.isChange = true;
			// saveChannel();
			Log.d(TAG, "数据发生改变");
		}
	}
}
