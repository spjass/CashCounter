package spjass.cashcounter;

/**
 * Created by Spjass on 12-Jan-16.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CashView extends LinearLayout {

    public CashView(Context context) {
        super(context);
        init();
    }

    public CashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cash_view, this);

    }
}
