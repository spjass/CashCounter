package spjass.cashcounter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spjass on 12-Jan-16.
 */
public class CashCounter {

    private List<Money> moneyArray;
    protected float total;
    Money selectedMoney;

    public CashCounter() {

        moneyArray = new ArrayList<>();

        moneyArray.add(new Money(0.01f, "coin", R.drawable.coin_1c));
        moneyArray.add(new Money(0.02f, "coin", R.drawable.coin_2c));
        moneyArray.add(new Money(0.05f, "coin", R.drawable.coin_5c));
        moneyArray.add(new Money(0.10f, "coin", R.drawable.coin_10c));
        moneyArray.add(new Money(0.20f, "coin", R.drawable.coin_20c));
        moneyArray.add(new Money(0.50f, "coin", R.drawable.coin_50c));
        moneyArray.add(new Money(1f, "coin", R.drawable.coin_1e));
        moneyArray.add(new Money(2f, "coin", R.drawable.coin_2e));
        moneyArray.add(new Money(5f, "cash", R.drawable.euro_5));
        moneyArray.add(new Money(10f, "cash", R.drawable.euro_10));
        moneyArray.add(new Money(20f, "cash", R.drawable.euro_20));
        moneyArray.add(new Money(50f, "cash", R.drawable.euro_50));
        moneyArray.add(new Money(100f, "cash", R.drawable.euro_100));
        moneyArray.add(new Money(200f, "cash", R.drawable.euro_200));
        moneyArray.add(new Money(500f, "cash", R.drawable.euro_500));

        moneyArray.get(0).setCount(50);
        Log.d("CashCounter", moneyArray.get(0).getValue() + " "  + moneyArray.get(0).getTotalValue() + "");
        moneyArray.get(5).setCount(25);
        moneyArray.get(4).setCount(25);
        selectedMoney = moneyArray.get(0);

    }


    public float getTotal() {

        total = 0;

        for (Money index : moneyArray) {
            total = total + index.getTotalValue();
        }

        return total;
    }

    public Money findMoneyByName(String name) {
        for (Money money : moneyArray) {
            if(money.name.equals(name)) {
                return money;
            }
        }

        return new Money(0.00f, "error", R.drawable.coin_1c);
    }

    public Money getSelectedMoney() {
        return selectedMoney;
    }

    public void setSelectedMoney(Money selectedMoney) {
        this.selectedMoney = selectedMoney;
    }

    public List<Money> getMoneyArray() {
        return moneyArray;
    }

    public void setMoneyArray(List<Money> moneyArray) {
        this.moneyArray = moneyArray;
    }

    public float getCoinsTotal() {
        float total = 0;

        for (Money money : moneyArray) {
            if (money.type.equals("coin")) {
                total += money.getTotalValue();
            }
        }

        Log.d("CashCounter",total + " total");


        return total;
    }

    public float getCashTotal() {
        float total = 0;

        for (Money money : moneyArray) {
            if (money.type.equals("cash")) {
                total += money.getTotalValue();
            }
        }

        return total;
    }

    public List<Money> getCoinArray() {
        List<Money> cashArray = new ArrayList<Money>();
        for (Money money:moneyArray) {
            if(money.getType().equals("coin")) {
                cashArray.add(money);
            }
        }

        return cashArray;
    }

    public List<Money> getCashArray() {
        List<Money> cashArray = new ArrayList<Money>();
        for (Money money:moneyArray) {
            if(money.getType().equals("cash")) {
                cashArray.add(money);
            }
        }

        return cashArray;
    }

    public void initIcons(Context context) {
        for(Money money : moneyArray) {
            money.initIcon(context);
        }
    }
}