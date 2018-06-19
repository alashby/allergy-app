package com.github.www.allergyapp;

import android.util.SparseArray;

import com.github.www.allergyapp.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

public class OCRProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OCRGraphic> graphicOverlay;

    OCRProcessor(GraphicOverlay<OCRGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }

    // Detects blocks of text that include the word "ingredients"
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                if (item.getValue().toLowerCase().contains("ingredients")) {
                    OCRGraphic graphic = new OCRGraphic(graphicOverlay, item);
                    graphicOverlay.add(graphic);
                }
            }
        }
    }

    @Override
    public void release() {
        graphicOverlay.clear();
    }
}
