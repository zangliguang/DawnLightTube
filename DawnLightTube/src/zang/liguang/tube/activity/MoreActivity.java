
package zang.liguang.tube.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

@EActivity(R.layout.activity_more)
public class MoreActivity extends BaseActivity {

    @ViewById(R.id.title)
    protected TextView mTitle;

    /**
     * 检查更新进度框
     */
    @ViewById(R.id.update_progress)
    public ProgressBar mProgressBar;
    /**
     * 最新版本提示
     */
    @ViewById(R.id.newest)
    public TextView mTextViewNewest;

    @AfterViews
    public void initView() {
        mTitle.setText("更多");
    }

    @Click
    void checkUpdateClicked() {
        mTextViewNewest.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        checkUpdate();
    }

    /**
     * 手动检查更新
     */
    private void checkUpdate() {}

    @Click(R.id.restart)
    public void restart(View view) {
        setCacheStr("MoreActivity", "MoreActivity");
        //WelcomeActivity_.intent(this).start();
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
