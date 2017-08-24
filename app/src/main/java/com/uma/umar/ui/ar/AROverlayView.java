package com.uma.umar.ui.ar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.opengl.Matrix;
import android.view.View;

import com.uma.umar.R;
import com.uma.umar.model.ARPoint;
import com.uma.umar.ui.ar.listener.ArrowsListener;
import com.uma.umar.utils.LocationHelper;
import com.uma.umar.utils.UMALog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ntdat on 1/13/17.
 */

public class AROverlayView extends View {

    private static final String TAG = "AROverlayView";
    private static final int MARGIN = 300;

    Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<ARPoint> arPoints;
    private ArrowsListener mListener;

    public AROverlayView(Context context) {
        super(context);
        this.context = context;
        arPoints = new ArrayList<>();
    }

    public AROverlayView(Context context, ArrayList<ARPoint> arPoints, ArrowsListener listener) {
        super(context);
        this.context = context;
        this.arPoints = arPoints;
        this.mListener = listener;
    }

    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        this.invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(50);

        boolean showArrows = true;
        boolean gotIn = false;
        for (int i = 0; i < arPoints.size(); i++) {
            float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInECEF = LocationHelper.WSG84toECEF(arPoints.get(i).getLocation());
            float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                UMALog.d(TAG, "daniehhhh got in here!");
                gotIn = true;
                final float x = (0.5f + cameraCoordinateVector[0] / cameraCoordinateVector[3]) * canvas.getWidth();
                final float y = (0.5f - cameraCoordinateVector[1] / cameraCoordinateVector[3]) * canvas.getHeight();

                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.marker_256);
                canvas.drawBitmap(bitmap, x, y, paint);
                String poiName = arPoints.get(i).getName();
                float textWidth = paint.measureText(poiName);
                canvas.drawText(poiName, x - (25 * poiName.length() / 2), y - 80, paint);

                boolean condRight = x > -textWidth / 2;
                boolean condLeft = x < canvas.getWidth() + textWidth / 2;
                UMALog.d(TAG, poiName + " Should be visible XXX ?? " + condRight + " && " + condLeft + " ? " + (condRight && condLeft));

                boolean condd = condRight && condLeft;
                //mListener.shouldBeVisibleAnyArrow(!condd, !condd);
                showArrows = showArrows && condd;
            }
        }
        if (gotIn) {
            mListener.shouldBeVisibleAnyArrow(!showArrows, !showArrows);
        } else {
            mListener.shouldBeVisibleAnyArrow(true, true);
        }
    }
}
