
package zang.liguang.tube.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import zang.liguang.tube.bean.VideoModle;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.view.NewItemView;
import zang.liguang.tube.view.NewItemView_;
import zang.liguang.tube.view.RelativeVideoItemView;
import zang.liguang.tube.view.RelativeVideoItemView_;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class RelativeVideoAdapter extends BaseAdapter {
    public List<YouTubeVideoModle> lists = new ArrayList<YouTubeVideoModle>();

    private String currentItem;

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

    public void currentItem(String item) {
        this.currentItem = item;
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

    	RelativeVideoItemView newItemView;

        if (convertView == null) {
            newItemView = RelativeVideoItemView_.build(context);
        } else {
            newItemView = (RelativeVideoItemView) convertView;
        }

        YouTubeVideoModle newModle = lists.get(position);
         newItemView.setTexts(newModle.getTitle(), newModle.getDescription(),
        		 "https://i.ytimg.com/vi/"+newModle.getVideoId()+"/default.jpg");
        return newItemView;
    }
}
