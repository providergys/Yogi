package com.yogi_app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.model.FaqRequest;
import com.yogi_app.model.FaqResponse;

import java.util.HashMap;
import java.util.List;


public class ExpandlListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<FaqResponse> listHeader;
  //  private List<FaqResponse> listChild;
    private HashMap<FaqResponse, List<FaqResponse>> listChild;
//    private List<FaqResponse> listChild;
    ExpandableListView expandableListView;
    public ExpandlListAdapter(Context context,List<FaqResponse> listHeader , HashMap<FaqResponse, List<FaqResponse>> listChild) {
        this._context = context;
        this.listHeader = listHeader ;
        this.listChild = listChild ;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        FaqResponse faqResponse = listHeader.get(groupPosition);
        final FaqResponse expandedListText = (FaqResponse) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate( R.layout.list_item, null);

            Typeface mFont = Typeface.createFromAsset(_context.getAssets(), "robotlight.ttf");
            TextView lblListHeader=(TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(mFont);
            lblListHeader.setText(expandedListText.getAnswer());
           // Toast.makeText(_context,""+faqResponse.getAnswer(),Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  this.listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int i1) {
       return this.listChild.get(this.listHeader.get(groupPosition)).get(i1);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition)).size();
    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FaqResponse listTitle = (FaqResponse) getGroup(groupPosition);
    //    FaqResponse faqResponse = listHeader.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            Typeface mFont = Typeface.createFromAsset(_context.getAssets(), "robotlight.ttf");
            TextView lbl_exp=(TextView) convertView.findViewById(R.id.lbl_exp);
            lbl_exp.setTypeface(mFont);
           lbl_exp.setText(listTitle.getAnswer());
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}