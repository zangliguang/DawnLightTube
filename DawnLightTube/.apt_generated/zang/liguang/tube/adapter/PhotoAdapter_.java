//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package zang.liguang.tube.adapter;

import android.content.Context;

public final class PhotoAdapter_
    extends PhotoAdapter
{

    private Context context_;

    private PhotoAdapter_(Context context) {
        context_ = context;
        init_();
    }

    public static PhotoAdapter_ getInstance_(Context context) {
        return new PhotoAdapter_(context);
    }

    private void init_() {
        context = context_;
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

}