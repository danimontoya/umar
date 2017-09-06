package com.uma.umar.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uma.umar.R;
import com.uma.umar.UmARApplication;

import static com.uma.umar.UmARApplication.getContext;

/**
 * Created by ntdat on 1/16/17.
 */

public class ARPoint implements Parcelable, Target {

    private String placeId;
    private String name;
    private String url;
    private Location location;
    private double distanceInMeters = 0;
    private Bitmap bitmap;
    private int bitmapXPosition;
    private int bitmapYPosition;

    public ARPoint(String placeId, String name, String url, double lat, double lon, double altitude) {
        this.placeId = placeId;
        this.name = name;
        this.url = url;
        location = new Location("ARPoint");
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setAltitude(altitude);
        UmARApplication.getInstance().getPicasso().with(getContext()).load(url).into(this);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    protected ARPoint(Parcel in) {
        placeId = in.readString();
        name = in.readString();
        url = in.readString();
        location = (Location) in.readValue(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeId);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ARPoint> CREATOR = new Parcelable.Creator<ARPoint>() {
        @Override
        public ARPoint createFromParcel(Parcel in) {
            return new ARPoint(in);
        }

        @Override
        public ARPoint[] newArray(int size) {
            return new ARPoint[size];
        }
    };

    public void setDistanceInMeters(double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public String getPlaceId() {
        return placeId;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        this.bitmap = bitmap;
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {

    }

    @Override
    public void onPrepareLoad(Drawable drawable) {

    }

    public Bitmap getBitmap() {
        UmARApplication.getInstance().getPicasso().with(getContext()).load(url).into(this);
        return bitmap != null ? bitmap : BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.marker_256);
    }

    public void setBitmapXYPosition(int bitmapXPosition, int bitmapYPosition) {
        this.bitmapXPosition = bitmapXPosition;
        this.bitmapYPosition = bitmapYPosition;
    }

    public boolean isTouched(View view, MotionEvent motionEvent) {

        if (bitmap == null)
            return false;

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Check if the x and y position of the touch is inside the bitmap
                if (x > bitmapXPosition && x < bitmapXPosition + bitmap.getWidth() && y > bitmapYPosition && y < bitmapYPosition + bitmap.getWidth()) {
                    //Bitmap touched
                    Toast.makeText(UmARApplication.getContext(), getName(), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return false;
    }
}
