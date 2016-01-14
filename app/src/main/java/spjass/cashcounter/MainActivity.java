package spjass.cashcounter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Money>> listDataChild;
    CashCounter cashCounter;
    List<Money> moneyArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        cashCounter = new CashCounter();
        moneyArray = cashCounter.getMoneyArray();

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, cashCounter);
        listAdapter.notifyDataSetChanged();
        Log.d("CashCounter", cashCounter.getMoneyArray().get(0).getCount() + "");
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if (groupPosition == 1) {
                    cashCounter.setSelectedMoney(cashCounter.getCoinArray().get(childPosition));
                } else if (groupPosition == 2) {
                    cashCounter.setSelectedMoney(cashCounter.getCashArray().get(childPosition));
                }

                ImageView image = (ImageView) findViewById(R.id.coinImageView);

                Bitmap icon = cashCounter.getSelectedMoney().icon;

                image.setImageBitmap(icon);
                return false;
            }
        });
        cashCounter.initIcons(getApplicationContext());
        setupUI(findViewById(R.id.main_container));
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Money>>();

        listDataHeader.add("Total");
        listDataHeader.add("Coins");
        listDataHeader.add(ExpandableListAdapter.BILLS_TITLE);

        List<Money> coins = new ArrayList<Money>();
        List<Money> cash = new ArrayList<Money>();

        for (Money money : moneyArray) {
            if (money.getType().equals("cash")) {
                cash.add(money);
            } else {
                coins.add(money);
            }

            Log.d("CashCounter", "prepareListData() " + money.getCount());
        }

        listDataChild.put(listDataHeader.get(1), coins);
        listDataChild.put(listDataHeader.get(2), cash);

        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void minusClicked(View v) {
        cashCounter.getSelectedMoney().setCount(cashCounter.getSelectedMoney().getCount() - 1);
        //cashCounter.setMoneyArray(moneyArray);

        prepareListData();
    }

    public void plusClicked(View v) {
        cashCounter.getSelectedMoney().setCount(cashCounter.getSelectedMoney().getCount() + 1);
        //cashCounter.setMoneyArray(moneyArray);

        prepareListData();
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    MyUtil.hideSoftKeyboard(MainActivity.this);
                    EditText editText = (EditText) findViewById(R.id.selectedItemEditText);
                    editText.setCursorVisible(false);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
