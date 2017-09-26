package com.uma.umar.ui.qr;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uma.umar.R;
import com.uma.umar.ui.qr.camera.CameraSourcePreview;
import com.uma.umar.ui.qr.camera.GraphicOverlay;

/**
 * Created by danieh on 7/5/17.
 */

public class BarcodeViewHolder {

    public CameraSourcePreview mPreview;
    public GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    public ImageView mScanFrameImageView, mFlashlightButton;
    public TextView mBarcodePlaceTextView, mBarcodePlaceDescTextView;
    public ImageView mPlaceDetailsButton;

    public BarcodeViewHolder(View view) {
        mPreview = (CameraSourcePreview) view.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) view.findViewById(R.id.graphicOverlay);

        mScanFrameImageView = (ImageView) view.findViewById(R.id.scan_frame_imageview);
        mFlashlightButton = (ImageView) view.findViewById(R.id.flashlight_imageview);

        mBarcodePlaceTextView = (TextView) view.findViewById(R.id.barcode_place);
        mBarcodePlaceDescTextView = (TextView) view.findViewById(R.id.barcode_place_description);

        mPlaceDetailsButton = (ImageView) view.findViewById(R.id.barcode_places_button);
    }

}
