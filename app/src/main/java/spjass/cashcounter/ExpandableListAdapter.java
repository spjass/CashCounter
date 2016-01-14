package spjass.cashcounter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Money>> _listDataChild;
    private CashCounter _cashCounter;
    public static String BILLS_TITLE = "Bills";
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Money>> listChildData, CashCounter cashCounter) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._cashCounter = cashCounter;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Money money = (Money) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        TextView txtListChildSum = (TextView) convertView
                .findViewById(R.id.lblListItemSum);

        TextView txtListChildCount = (TextView) convertView
                .findViewById(R.id.lblListItemCount);

        Log.d("CashCounter", money.getCount()+" count get childview");
        Log.d("CashCounter", money.getTotalValue()+" count get childview");

        txtListChildCount.setText(money.getCount() + "x");
        txtListChildSum.setText(MyUtil.parseCurrency(money.getTotalValue()));
        txtListChild.setText(money.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition != 0) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        TextView lblListSum = (TextView) convertView.findViewById(R.id.lblListSum);

        TextView lblListAmountHeader = (TextView) convertView.findViewById(R.id.lblListCountHeader);

        TextView lblListSumHeader = (TextView) convertView.findViewById(R.id.lblListSumHeader);

        if(!headerTitle.equals("Total")) {
            if (isExpanded) {
                lblListAmountHeader.setVisibility(View.VISIBLE);
                lblListSumHeader.setVisibility(View.VISIBLE);
            } else {
                lblListAmountHeader.setVisibility(View.GONE);
                lblListSumHeader.setVisibility(View.GONE);
            }
        }

        if (headerTitle.equals("Coins")) {
            lblListSum.setText(MyUtil.parseCurrency(_cashCounter.getCoinsTotal()));
        } else if (headerTitle.equals(BILLS_TITLE)) {
            lblListSum.setText(MyUtil.parseCurrency(_cashCounter.getCashTotal()));
        } else if (headerTitle.equals("Total")) {
            lblListSum.setText(MyUtil.parseCurrency(_cashCounter.getTotal()));
        }



        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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