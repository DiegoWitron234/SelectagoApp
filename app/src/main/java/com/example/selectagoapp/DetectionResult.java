package com.example.selectagoapp;

import android.graphics.RectF;

public class DetectionResult {
    private float score;
    private RectF boundingBoxes;

    public DetectionResult(float score, RectF boundingBox) {
        this.score = score;
        this.boundingBoxes = boundingBox;
    }

    public RectF getBoundingBox() {
        return boundingBoxes;
    }

    public float getConfidence() {
        return score;
    }
}
