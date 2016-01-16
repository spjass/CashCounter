package spjass.cashcounter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MySharedPreferences {


    public static CashCounter getCashCounter(Activity activity) {
        CashCounter cashCounter = new CashCounter();
        SharedPreferences preferences = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        for(Money money : cashCounter.getMoneyArray()) {
            money.setCount(preferences.getInt(money.getName(),0));
        }

        cashCounter.getTillFloat().setValue(preferences.getFloat(cashCounter.getTillFloat().getName(), 0));
        cashCounter.setSelectedMoney(cashCounter.findMoneyByName(preferences.getString("selectedMoney", cashCounter.error.getName())));


        return cashCounter;
    }

    public static void saveCashCounter(CashCounter cashCounter, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for(Money money : cashCounter.getMoneyArray()) {
            editor.putInt(money.getName(), money.getCount());
        }

        editor.putString("selectedMoney", cashCounter.getSelectedMoney().getName());
        editor.putFloat(cashCounter.getTillFloat().getName(), cashCounter.getTillFloat().getValue());

        editor.commit();
    }
}
