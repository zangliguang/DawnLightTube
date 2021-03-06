
package zang.liguang.tube.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.bean.WeatherModle;
import zang.liguang.tube.initview.SlidingMenuView;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.item_weather)
public class WeatherItemView extends LinearLayout {

    @ViewById(R.id.week)
    protected TextView mWeek;
    @ViewById(R.id.weahter_image)
    protected ImageView mWeatherImage;
    @ViewById(R.id.temperature)
    protected TextView mTemperature;
    @ViewById(R.id.weather)
    protected TextView mWeather;
    @ViewById(R.id.wind)
    protected TextView mWind;

    public WeatherItemView(Context context) {
        super(context);
    }

    public void setData(WeatherModle weatherModle) {
        try {
            mWeek.setText("星" + weatherModle.getDate().split("日星")[1]);
            mTemperature.setText(weatherModle.getTemperature().replace("低温", "~").split("高温")[1]);
            mWeather.setText(weatherModle.getWeather());
            mWind.setText(weatherModle.getWind());
            SlidingMenuView.instance().setWeatherImage(mWeatherImage, weatherModle.getWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
