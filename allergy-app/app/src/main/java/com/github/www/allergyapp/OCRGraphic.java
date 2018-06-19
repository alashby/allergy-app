package com.github.www.allergyapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.github.www.allergyapp.camera.GraphicOverlay;
import com.google.android.gms.vision.text.TextBlock;

public class OCRGraphic extends GraphicOverlay.Graphic {
    private int id;

    private static Paint rectPaint;
    private static Paint textPaint;
    private final TextBlock text;

    OCRGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        this.text = text;

        if (rectPaint == null) {
            rectPaint = new Paint();
            rectPaint.setColor(Color.RED);
            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setStrokeWidth(5.0f);
        }

        postInvalidate();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TextBlock getTextBlock() { return text; }

    public boolean contains(float x, float y) {
        if (text == null) {
            return false;
        }
        RectF rect = new RectF(text.getBoundingBox());
        rect = translateRect(rect);
        return rect.contains(x,y);
    }

    @Override
    public void draw(Canvas canvas) {
        if (text == null) {
            return;
        }

        RectF rect = new RectF(text.getBoundingBox());
        rect = translateRect(rect);
        canvas.drawRect(rect, rectPaint);
    }

}


