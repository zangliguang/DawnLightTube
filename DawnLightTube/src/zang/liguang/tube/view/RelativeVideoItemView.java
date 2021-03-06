
package zang.liguang.tube.view;

import java.util.List;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import zang.liguang.tube.bean.NewModle;
import zang.liguang.tube.utils.Options;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@EViewGroup(R.layout.item_new)
public class RelativeVideoItemView extends LinearLayout {

    @ViewById(R.id.left_image)
    protected ImageView leftImage;

    @ViewById(R.id.item_title)
    protected TextView itemTitle;

    @ViewById(R.id.item_content)
    protected TextView itemConten;

    @ViewById(R.id.article_top_layout)
    protected RelativeLayout articleLayout;

    @ViewById(R.id.layout_image)
    protected LinearLayout imageLayout;

    @ViewById(R.id.item_image_0)
    protected ImageView item_image0;

    @ViewById(R.id.item_image_1)
    protected ImageView item_image1;

    @ViewById(R.id.item_image_2)
    protected ImageView item_image2;

    @ViewById(R.id.item_abstract)
    protected TextView itemAbstract;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;

    public RelativeVideoItemView(Context context) {
        super(context);
        options = Options.getListOptions();
    }

	public void setTexts(String titleText, String contentText, String imgUrl)
	{
		articleLayout.setVisibility(View.VISIBLE);
		imageLayout.setVisibility(View.GONE);
		itemTitle.setText(titleText);

		itemConten.setText(contentText);

		if (!"".equals(imgUrl))
		{
			leftImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imgUrl, leftImage, options);
		}
		else
		{
			leftImage.setVisibility(View.GONE);
		}
	}

}
