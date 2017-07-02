package com.uma.umar.ui.ar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.opengl.Matrix;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uma.umar.R;
import com.uma.umar.helper.LocationHelper;
import com.uma.umar.model.ARPoint;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ntdat on 1/13/17.
 */

public class AROverlayView extends View {

    Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<ARPoint> arPoints;


    public AROverlayView(Context context) {
        super(context);
        this.context = context;
        arPoints = new ArrayList<>();
    }

    public AROverlayView(Context context, ArrayList<ARPoint> arPoints) {
        super(context);
        this.context = context;
        this.arPoints = arPoints;
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

        final int radius = 30;
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(60);

        for (int i = 0; i < arPoints.size(); i++) {
            float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInECEF = LocationHelper.WSG84toECEF(arPoints.get(i).getLocation());
            float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                final float x = (0.5f + cameraCoordinateVector[0] / cameraCoordinateVector[3]) * canvas.getWidth();
                final float y = (0.5f - cameraCoordinateVector[1] / cameraCoordinateVector[3]) * canvas.getHeight();

                //canvas.drawCircle(x, y, radius, paint);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.marker);
                canvas.drawBitmap(bitmap, x, y, paint);
                // FIXME get images before and cache them
//                Picasso.with(getContext())
//                        .load(arPoints.get(i).getUrl())
//                        .into(new Target() {
//                            @Override
//                            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
//                                canvas.drawBitmap(bitmap, x, y, paint);
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Drawable drawable) {
//
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable drawable) {
//
//                            }
//                        });

                canvas.drawText(arPoints.get(i).getName(), x - (30 * arPoints.get(i).getName().length() / 2), y - 80, paint);
            }
        }
    }
}
