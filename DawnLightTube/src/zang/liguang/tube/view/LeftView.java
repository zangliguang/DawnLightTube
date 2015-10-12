
package zang.liguang.tube.view;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

import zang.liguang.tube.R;
import zang.liguang.tube.activity.*;
import zang.liguang.tube.initview.SlidingMenuView;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;



@EViewGroup(R.layout.activity_left)
public class LeftView extends LinearLayout {

    private final BaseActivity context;

    public LeftView(Context context) {
        super(context);
        this.context = (BaseActivity) context;
    }

    @Click(R.id.news)
    public void enterPics(View view) {
        context.openActivity(NewYorkTimeActivity_.class);
        isShow();
    }

    @Click(R.id.video)
    public void enterVideo(View view) {
       // context.openActivity(VideoActivity_.class);
        isShow();
    }

    @Click(R.id.ties)
    public void enterMessage(View view) {
      //  context.openActivity(MessageActivity_.class);
        isShow();
    }

    @Click(R.id.tianqi)
    public void enterTianQi(View view) {
       // context.openActivity(WeatherActivity_.class);
        isShow();
    }

    @Click(R.id.more)
    public void enterMore(View view) {
       // context.openActivity(MoreActivity_.class);
        isShow();
    }

    public void isShow() {
        if (SlidingMenuView.instance().slidingMenu.isMenuShowing()) {
            SlidingMenuView.instance().slidingMenu.showContent();
        }
    }

}
