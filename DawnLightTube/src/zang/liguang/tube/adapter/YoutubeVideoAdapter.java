
package zang.liguang.tube.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import zang.liguang.tube.bean.VideoModle;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.view.VideoItemView;
import zang.liguang.tube.view.VideoItemView_;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class YoutubeVideoAdapter extends BaseAdapter {
    public List<YouTubeVideoModle> lists = new ArrayList<YouTubeVideoModle>();

    public void appendList(List<YouTubeVideoModle> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    @RootContext
    Context context;

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VideoItemView videoItemView;

        if (convertView == null) {
            videoItemView = VideoItemView_.build(context);
        } else {
            videoItemView = (VideoItemView) convertView;
        }

        YouTubeVideoModle videoModle = lists.get(position);
        videoItemView.setYouTubeData(videoModle);

        return videoItemView;
    }
}
