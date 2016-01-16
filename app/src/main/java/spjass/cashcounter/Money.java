package spjass.cashcounter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DecimalFormat;


public class Money {
    private String name;
    int count;
    float value;
    float totalValue;
    String type;
    int drawable;
    Bitmap icon;

    public Money(float value, String type, int imageName) {
        this.value = value;
        this.type = type;
        this.drawable = imageName;

    }

    public String getName() {
        if(!type.equals("float")) {
            DecimalFormat df = new DecimalFormat("0.00€");
            return df.format(value);
        } else {
            return "Till float";
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void initIcon(Context context) {
        this.icon = BitmapFactory.decodeResource(context.getResources(),
                drawable);
        icon = MyUtil.eraseBG(icon, -16777216);
    }



    public void setCount(int count) {
        this.count = count;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getTotalValue() {
        return count * value;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
