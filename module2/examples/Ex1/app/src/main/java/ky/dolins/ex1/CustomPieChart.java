package ky.dolins.ex1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by omar on 2017-03-12.
 * For exercise 2
 */

public class CustomPieChart extends View {

    final private static float ANIMATION_SPEED = 20.0f; // the bigger the slower
    final private static float ANIMATION_DURATION = 750.0f;
    private static int MAX_X;
    private static int MAX_Y;
    private float[] data;
    private float[] segments;
    private int[] colors;
    private RectF rect;
    private float anim_progress;
    final CustomPieChart me = this;
    private static Random rnd = new Random();
    Paint paint;
    Handler viewHandler = new Handler();
    Runnable updateView = new Runnable() {
        @Override
        public void run() {
            me.invalidate();
            if (anim_progress < 1.0)
                viewHandler.postDelayed(updateView, (int)ANIMATION_SPEED);
        }
    };


    public CustomPieChart(Context context) {
        super(context);
        setup();
    }

    public CustomPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CustomPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        data = null;
        rect = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        anim_progress = 0;
    }


    public void drawChart(float[] data) {
        this.data = data;
        segments = getPieSegments(data);
        colors = getColors(data.length);
        MAX_X = this.getWidth();
        MAX_Y = this.getHeight();
        anim_progress = 0;
        updateView.run();
    }

    public void clearChart() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        if (data == null) return;

        float segStartPoint = 0;
        int starty = MAX_Y/8;
        int length = MAX_Y*3/4;
        int startx = (MAX_X > MAX_Y) ? (MAX_X-length)/2 : 0;
        rect.set(startx, starty, startx + length, starty + length);
        canvas.drawColor(Color.TRANSPARENT);
        int i = 0;
        canvas.drawArc(rect, segStartPoint, 1*anim_progress, true, paint);
        segStartPoint += 1;


        for (float segment:segments) {
            System.out.println("Its " + segment);
            paint.setColor(colors[i]);
            canvas.drawArc(rect, segStartPoint, (segment-1)*anim_progress, true, paint);
            paint.setColor(Color.TRANSPARENT);
            canvas.drawArc(rect, segStartPoint, 1*anim_progress, true, paint);
            segStartPoint += segment*anim_progress;
            i++;
        }

        anim_progress += ANIMATION_SPEED/ANIMATION_DURATION;

    }

    private static int[] getColors(int length){
        int[] colors = new int[length];
        for (int i = 0; i < length; i++) {
            if (i == 0)
                colors[i] = Color.argb(255,30,205,70);
            else if (i == 1)
                colors[i] = Color.argb(255,50,100,255);
            else if (i == 2)
                colors[i] = Color.argb(255,255,100,50);
            else
                colors[i] = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
        return colors;
    }

    private static float[] getPieSegments(float data[]) {

        float sum = getArraySum(data);
        float[] segments = new float[data.length];
        int i = 0;

        for (float val : data) {
            segments[i] = val * 360 / sum;
            i++;
        }

        return segments;
    }

    private static float getArraySum(float[] data) {
        float total = 0;
        for (float val : data)
            if (val > 0) total += val;
            else throw new IllegalArgumentException();
        return total;
    }

}
