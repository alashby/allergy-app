package com.github.www.allergyapp;

import android.util.SparseArray;

import com.github.www.allergyapp.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

public class OCRProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OCRGraphic> graphicOverlay;

    OCRProcessor(GraphicOverlay<OCRGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }

    // Detects blocks of text that include the word "ingredients"
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
        SparseArray<TextBlock> detectedItems = detections.getDetectedItems();
        for (int i = 0; i < detectedItems.size(); ++i) {
            TextBlock item = detectedItems.valueAt(i);
            if (item != null && item.getValue() != null) {
                if (item.getValue().toLowerCase().trim().contains("ingredients") ||
                        item.getValue().toLowerCase().trim().contains("contain") ||
                        item.getValue().toLowerCase().trim().contains("processes") ||
                        item.getValue().toLowerCase().trim().contains("that uses") ||
                        item.getValue().toLowerCase().trim().contains("which uses")) {
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
