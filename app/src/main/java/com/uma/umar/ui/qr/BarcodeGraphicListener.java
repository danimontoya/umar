package com.uma.umar.ui.qr;

import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by danieh on 7/5/17.
 */

public interface BarcodeGraphicListener {

    void onBarcodeFound(Barcode barcode);
}
