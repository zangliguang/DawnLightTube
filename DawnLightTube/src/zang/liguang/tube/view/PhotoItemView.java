
package zang.liguang.tube.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.utils.Options;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EViewGroup(R.layout.item_photo)
public class PhotoItemView extends RelativeLayout {

    @ViewById(R.id.photo_img)
    protected ImageView photoImg;

    @ViewById(R.id.photo_title)
    protected TextView photoTitle;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;

    public PhotoItemView(Context context) {
        super(context);
        options = Options.getListOptions();
    }

    public void setData(String title, String picUrl) {

        picUrl = picUrl.replace("auto", "854x480x75x0x0x3");

        photoTitle.setText(title);
        imageLoader.displayImage(picUrl, photoImg, options);
    }

}
