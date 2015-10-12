
package zang.liguang.tube.activity;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.adapter.PhotoDetailAdapter;
import zang.liguang.tube.bean.PhotoDetailModle;
import zang.liguang.tube.http.HttpUtil;
import zang.liguang.tube.utils.SplitNetStringUtils;
import zang.liguang.tube.utils.StringUtils;
import zang.liguang.tube.wedget.flipview.FlipView;
import zang.liguang.tube.wedget.flipview.FlipView.OnFlipListener;
import zang.liguang.tube.wedget.flipview.FlipView.OnOverFlipListener;
import zang.liguang.tube.wedget.flipview.OverFlipMode;

@EActivity(R.layout.activity_photo)
public class PhotoDetailActivity extends BaseActivity implements OnFlipListener,
        OnOverFlipListener {
    @ViewById(R.id.flip_view)
    protected FlipView mFlipView;

    @Bean
    protected PhotoDetailAdapter photoDetailAdapter;

    private String imgUrl;

    @AfterInject
    public void init() {
        try {
            if (getIntent().getExtras().getString("photoUrl") != null) {
                imgUrl = getIntent().getExtras().getString("photoUrl");
                showProgressDialog();
                loadData(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterViews
    public void initView() {
        // imageAdapter.appendList(imgList);
        try {
            mFlipView.setOnFlipListener(this);
            mFlipView.setAdapter(photoDetailAdapter);
            mFlipView.peakNext(false);
            mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
            mFlipView.setOnOverFlipListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadData(String url) {
        if (hasNetWork()) {
            loadPhotoList(url);
        } else {
            dismissProgressDialog();
            showShortToast(getString(R.string.not_network));
            String result = getCacheStr(imgUrl);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
        }
    }

    @Background
    void loadPhotoList(String url) {
        String result;
        try {
            result = HttpUtil.getByHttpClient(this, url);
            getResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void getResult(String result) {
        setCacheStr(imgUrl, result);
        dismissProgressDialog();
        try {
            List<PhotoDetailModle> list = SplitNetStringUtils.getPhotoDetailModles(result);
            photoDetailAdapter.appendList(list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious,
            float overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

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
