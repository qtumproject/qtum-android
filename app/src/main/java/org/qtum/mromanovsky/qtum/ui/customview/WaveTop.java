package org.qtum.mromanovsky.qtum.ui.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.qtum.mromanovsky.qtum.R;

public class WaveTop extends View {
    int counter = 1;
    int mult;

    Path path = new Path();
    Paint paint = new Paint();
    Paint paintStroke = new Paint();
    int y;
    int x;
    int canvasHeight=0;
    int canvasWidth=0;

    public WaveTop(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        y = canvas.getHeight()/20;
        x = canvas.getWidth()/20;

        if(canvasHeight == 0) {
            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
        }

        if((counter/(canvasHeight/2)%2)==1)
            mult = (counter%(canvasHeight/2))*-3+(canvasHeight/2)*3;
        else mult = (counter%(canvasHeight/2))*3;

        path.moveTo(0, canvasHeight);
        path.lineTo(0, 10*y);
        path.cubicTo(2*x, -y+mult, 5*x, 22*y-mult, 6*x, 10*y);
        path.cubicTo(8*x, mult, 11*x, 21*y-mult, 12*x, 10*y);
        path.cubicTo(14*x, -y+mult, 18*x, 24*y-mult, 20*x, 10*y);
        path.lineTo(canvasWidth, canvasHeight);


        paint.setStrokeWidth(2);
        paint.setColor(ContextCompat.getColor(getContext(),R.color.wave_background));
        path.close();

        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(ContextCompat.getColor(getContext(),R.color.wave_stroke));

        canvas.drawPath(path, paint);
        canvas.drawPath(path, paintStroke);
        path.reset();
        counter%=canvasHeight*2;
        counter++;

        this.postInvalidateDelayed(20);
    }
}
