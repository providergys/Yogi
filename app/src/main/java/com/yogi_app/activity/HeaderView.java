package com.yogi_app.activity;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;
import com.yogi_app.R;


@Parent
@SingleTop
@Layout(R.layout.list_group)
public class HeaderView {
    private static String TAG = "HeaderView";
    @View(R.id.lbl_exp)
    TextView headerText;
    private Context mContext;
    private String mHeaderText;
    public HeaderView(Context context,String headerText) {
        this.mContext = context;
        this.mHeaderText = headerText;
    }
    @Resolve
    private void onResolve(){
        Log.d(TAG, "onResolve");
        headerText.setText(mHeaderText);
    }
    @Expand
    private void onExpand(){
        Log.d(TAG, "onExpand");
    }
    @Collapse
    private void onCollapse(){
        Log.d(TAG, "onCollapse");
    }
}
