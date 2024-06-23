package com.example.whatflower.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.whatflower.R;

public class CharAvatarView extends View {

    private String text = "A";
    private int textColor = Color.WHITE;
    private int backgroundColor = Color.parseColor("#409EFF");
    private float textSize = 100;
    private Paint textPaint;
    private Paint backgroundPaint;


    public CharAvatarView(Context context) {
        super(context);
        init(null);
    }

    public CharAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CharAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CharAvatarView);
            textSize = a.getDimension(R.styleable.CharAvatarView_textSize, textSize);
            a.recycle();
        }

        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;


        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);


        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText(text, x, y, textPaint);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }
}