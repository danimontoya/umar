/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uma.umar.ui.qr;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.uma.umar.ui.qr.camera.GraphicOverlay;

/**
 * Generic tracker which is used for tracking or reading a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
class BarcodeGraphicTracker extends Tracker<Barcode> {

    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;
    private BarcodeGraphicListener mListener;
    private View mScannerFrame;

    BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic, View scannerFrame, BarcodeGraphicListener listener) {
        mOverlay = overlay;
        mGraphic = graphic;
        mListener = listener;
        mScannerFrame = scannerFrame;
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, Barcode item) {
        mGraphic.setId(id);
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);

        // Callback to send the barcode / QR code to the activity.. when detected
        if (isBarcodeContainedWithinFrame(item))
            mListener.onBarcodeFound(item);
    }

    private boolean isBarcodeContainedWithinFrame(Barcode item) {
        Rect scannerFrame = new Rect();
        mScannerFrame.getDrawingRect(scannerFrame);

        RectF rectF = new RectF(item.getBoundingBox());
        rectF.left = mGraphic.translateX(rectF.left);
        rectF.top = mGraphic.translateY(rectF.top) + mOverlay.getTop();
        rectF.right = mGraphic.translateX(rectF.right);
        rectF.bottom = mGraphic.translateY(rectF.bottom) + mOverlay.getTop();

        Rect barcodeRect = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);

        return scannerFrame.contains(barcodeRect);
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }
}
