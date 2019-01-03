package android.arnab.solarsystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.concurrent.TimeUnit;

public class DrawOrbit extends View {
    public static int chk = 0;
    private Paint paint = new Paint();

    private PointF pointA, pointB;
    private float radius, d;
    float centerX, centerY;
    ImageButton planets[];
    float orbits[];
    int index, val;
    int colors[] = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.MAGENTA, Color.YELLOW, Color.DKGRAY};


    public DrawOrbit(Context context) {
        super(context);
    }

    public DrawOrbit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawOrbit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);

        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        //int len=planets.length;
        final Canvas c = canvas;
        final Handler handler = new Handler();
        for (int a = 0; a < index; a++) {
            paint.setColor(getContext().getResources().getColor(R.color.brightPurple));
            c.drawCircle(centerX, centerY, orbits[a] + val, paint);

        }

    }

    public void draw(float centerX, float centerY, float orbits[], float d, int index, int val) {
        this.d = d;
        this.centerX = centerX;
        this.centerY = centerY;
        this.orbits = orbits;
        this.index = index;
        this.val = val;
        invalidate();
        requestLayout();
    }

    public static double distance(float p1x, float p1y, float p2x, float p2y) {
        return (Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2)));
    }
}
