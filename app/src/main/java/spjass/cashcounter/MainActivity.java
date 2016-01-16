package spjass.cashcounter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
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


public class MainActivity extends AppCompatActivity {
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
        cashCounter = MySharedPreferences.getCashCounter(this);
        moneyArray = cashCounter.getMoneyArray();


        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, cashCounter);
        listAdapter.notifyDataSetChanged();
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
                } else if (groupPosition == 0) {
                    cashCounter.setSelectedMoney(cashCounter.getTillFloat());
                }

                ImageView image = (ImageView) findViewById(R.id.coinImageView);
                EditText countEditText = (EditText) findViewById(R.id.selectedItemEditText);
                if (cashCounter.getSelectedMoney().getType().equals("float")) {
                    countEditText.setText(MyUtil.parseCurrency(cashCounter.getSelectedMoney().getValue()));
                } else {
                    countEditText.setText(MyUtil.parseAmount(cashCounter.getSelectedMoney().getCount()));
                }
                Bitmap icon = cashCounter.getSelectedMoney().icon;
                refreshTotalText();
                MainActivity.this.prepareListData();
                image.setImageBitmap(icon);
                return false;
            }
        });
        final EditText countEditText = (EditText) findViewById(R.id.selectedItemEditText);

        countEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (countEditText.hasFocus()) {
                    //((EditText) v).performLongClick();
                    ((EditText) v).selectAll();
                }
            }
        });

        countEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    MyUtil.hideSoftKeyboard(MainActivity.this);
                    updateCountText();

                    listAdapter.notifyDataSetChanged();

                    return true;
                }
                return false;
            }
        });
        cashCounter.initIcons(getApplicationContext());
        setupUI(findViewById(R.id.main_container));
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        MySharedPreferences.saveCashCounter(cashCounter, this);
        // etc.
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<Money>>();

        listDataHeader.add("Total");
        listDataHeader.add("Coins");
        listDataHeader.add(ExpandableListAdapter.BILLS_TITLE);

        List<Money> total = new ArrayList<>();
        List<Money> coins = new ArrayList<>();
        List<Money> cash = new ArrayList<>();

        for (Money money : moneyArray) {
            if (money.getType().equals("cash")) {
                cash.add(money);
            } else if (money.getType().equals("coin")) {
                coins.add(money);
            }
        }

        total.add(cashCounter.getTillFloat());

        listDataChild.put(listDataHeader.get(0), total);
        listDataChild.put(listDataHeader.get(1), coins);
        listDataChild.put(listDataHeader.get(2), cash);

        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }

        refreshTotalText();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void refreshTotalText() {
        TextView textView = (TextView) findViewById(R.id.cashViewTotalText);
        textView.setText(MyUtil.parseCurrency(cashCounter.getSelectedMoney().getTotalValue()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            cashCounter.clear();
            ImageView image = (ImageView) findViewById(R.id.coinImageView);
            EditText countEditText = (EditText) findViewById(R.id.selectedItemEditText);
            countEditText.setText("");
            prepareListData();
            Bitmap icon = cashCounter.getSelectedMoney().icon;
            image.setImageBitmap(icon);

        }

        return super.onOptionsItemSelected(item);
    }

    public void minusClicked(View v) {
        Money money = cashCounter.getSelectedMoney();
        if (!money.getType().equals("error")) {
            //cashCounter.setMoneyArray(moneyArray);
            EditText countText = (EditText) findViewById(R.id.selectedItemEditText);
            if (!cashCounter.getSelectedMoney().getType().equals("float")) {
                money.setCount(money.getCount() - 1);
                countText.setText(MyUtil.parseAmount(money.getCount()));
            } else {
                countText.setText(MyUtil.parseCurrency(money.getValue()));
            }
            prepareListData();
        }
    }

    public void plusClicked(View v) {

        if (!cashCounter.getSelectedMoney().getType().equals("error")) {
            //cashCounter.setMoneyArray(moneyArray);
            EditText countText = (EditText) findViewById(R.id.selectedItemEditText);
            if (!cashCounter.getSelectedMoney().getType().equals("float")) {
                cashCounter.getSelectedMoney().setCount(cashCounter.getSelectedMoney().getCount() + 1);

                countText.setText(MyUtil.parseAmount(cashCounter.getSelectedMoney().getCount()));
            } else {
                countText.setText(MyUtil.parseCurrency(cashCounter.getSelectedMoney().getValue()));
            }
            prepareListData();
        }
    }

    public void updateCountText() {
        EditText countText = (EditText) findViewById(R.id.selectedItemEditText);
        String text = countText.getText().toString();
        if (!text.contains("x") && !text.contains("€") && !cashCounter.getSelectedMoney().getType().equals("float")) {
            if (!text.equals("")) {
                cashCounter.getSelectedMoney().setCount(Integer.parseInt(text));
                countText.setText(text + "x");

            } else {
                cashCounter.getSelectedMoney().setCount(0);
                countText.setText("0x");
            }
        }

        if (cashCounter.getSelectedMoney().getType().equals("float") && !text.contains("€")) {
            if (!text.equals("")) {
                cashCounter.getSelectedMoney().setValue(Integer.parseInt(text));
                countText.setText(text + "€");

            } else {
                cashCounter.getSelectedMoney().setCount(0);
                countText.setText("0€");
            }
        }
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    MyUtil.hideSoftKeyboard(MainActivity.this);
                    EditText editText = (EditText) findViewById(R.id.selectedItemEditText);
                    editText.setCursorVisible(false);
                    updateCountText();
                    refreshTotalText();

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
