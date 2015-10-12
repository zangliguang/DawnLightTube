
package zang.liguang.tube.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import zang.liguang.tube.bean.PhotoDetailModle;
import zang.liguang.tube.view.PhotoDetailView;
import zang.liguang.tube.view.PhotoDetailView_;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class PhotoDetailAdapter extends BaseAdapter {

    public List<PhotoDetailModle> lists = new ArrayList<PhotoDetailModle>();

    @RootContext
    Context context;

    public void appendList(List<PhotoDetailModle> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

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
        PhotoDetailView photoItemView;
        if (convertView == null) {
            photoItemView = PhotoDetailView_.build(context);
        } else {
            photoItemView = (PhotoDetailView) convertView;
        }

        PhotoDetailModle photoDetailModle = lists.get(position);

        photoItemView.setImage(lists.size(), position, photoDetailModle.getContent(),
                photoDetailModle.getTitle(), photoDetailModle.getImgUrl());

        return photoItemView;
    }

}
