package com.google.android.gms.samples.vision.ocrreader.model;

import com.google.android.gms.samples.vision.ocrreader.OcrDetectorProcessor;

import java.io.Serializable;

public class OcrModel implements Serializable {

    OcrDetectorProcessor ocrDetectorProcessor;

    public OcrDetectorProcessor getOcrDetectorProcessor() {
        return ocrDetectorProcessor;
    }

    public void setOcrDetectorProcessor(OcrDetectorProcessor ocrDetectorProcessor) {
        this.ocrDetectorProcessor = ocrDetectorProcessor;
    }
}
