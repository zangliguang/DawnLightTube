//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package zang.liguang.tube;


public final class LiGuangApplication_
    extends LiGuangApplication
{

    private static LiGuangApplication INSTANCE_;

    public static LiGuangApplication getInstance() {
        return INSTANCE_;
    }

    /**
     * Visible for testing purposes
     * 
     */
    public static void setForTesting(LiGuangApplication application) {
        INSTANCE_ = application;
    }

    @Override
    public void onCreate() {
        INSTANCE_ = this;
        init_();
        super.onCreate();
    }

    private void init_() {
    }

}
