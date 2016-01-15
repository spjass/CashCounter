package spjass.cashcounter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juhorautio on 15.1.16.
 */
public class MySharedPreferences {


    public static CashCounter getCashCounter(Activity activity) {
        CashCounter cashCounter = new CashCounter();
        SharedPreferences preferences = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        for(Money money : cashCounter.getMoneyArray()) {
            money.setCount(preferences.getInt(money.getName(),0));
        }

        return cashCounter;
    }

    public static void saveCashCounter(CashCounter cashCounter, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for(Money money : cashCounter.getMoneyArray()) {
            editor.putInt(money.name, money.getCount());
        }

        editor.commit();

        Log.d("CashCounter", preferences.getInt("0.10€",-1) + " COIN");
    }
}
