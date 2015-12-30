package com.xiachen.fatter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiachen on 15/12/9.
 */
public class CircleView extends View {

    Context c;
    private final int DEFAULT_TEXTSIZE = 36;
    private int screenW;
    private int screenH;
    private Paint mPathPaint;
    private TextPaint mTextPaint;
    private Path mPath;
    private float radius;
    private String text;
    private float textHeight;
    private float halfPerimeter;
    private float space;
    String[] words;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.c = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        radius = ta.getDimension(R.styleable.CircleView_radius, 0);
        text = ta.getString(R.styleable.CircleView_circlsText);
        ta.recycle();


        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPathPaint.setColor(Color.RED);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setColor(Color.CYAN);
        mTextPaint.setTextSize(DEFAULT_TEXTSIZE);

//        mTextPaint.setTextScaleX((halfPerimeter - textWidth) / text.length());
//        mTextPaint.setLetterSpacing((halfPerimeter - textWidth) / text.length());
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        halfPerimeter = (float) (2 * Math.PI * radius);
        textHeight = mTextPaint.getFontMetrics().top;
        mPath = new Path();
        initTextDraw();


    }


    private void initTextDraw() {

        words = text.split(" ");
        float textWidth = mTextPaint.measureText(text);
        space = (halfPerimeter - textWidth) / words.length;
        Utils.showLog(CircleView.class, space + "");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.addCircle(screenW / 2, screenH / 2, radius, Path.Direction.CW);
        Utils.showLog(CircleView.class, screenW + "\n" + screenH);
        drawWords(canvas);

//        canvas.drawPath(mPath, mPathPaint);
    }

    private void drawWords(Canvas canvas) {
        float wordWidth = 0;
        for (String word : words) {
            canvas.drawTextOnPath(word, mPath, wordWidth, 0, mTextPaint);
            wordWidth += mTextPaint.measureText(word) + space;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        screenW = MeasureSpec.getSize(widthMeasureSpec);
        screenH = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(screenW, screenH);
    }

}
