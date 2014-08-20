package com.diacht.simpleusers.ui.fragment;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.dao.User;
import com.diacht.simpleusers.db.UsersContract;
import com.diacht.simpleusers.system.SUApplication;
import com.diacht.simpleusers.system.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map of users
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class MapFragment extends com.google.android.gms.maps.MapFragment{
    private Cursor mCursor;
    private ContentObserver mObserver;

    public static MapFragment newInstance() {
        final MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObserver = new ContentObserver(new Handler()) {
            @SuppressWarnings("deprecation")
            @Override
            public void onChange(boolean selfChange) {
                mCursor.requery();
                updateContentFromCursor();
                super.onChange(selfChange);
            }
        };
    }

    private void updateContentFromCursor() {
        if (mCursor == null) {
            return;
        }
        double myLatitude = User.NO_COORDINATES, myLongitude = User.NO_COORDINATES;
        while(mCursor.moveToNext()){
            if(Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE) != User.NO_COORDINATES &&
                    Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE) != User.NO_COORDINATES) {
                if (Utils.getIntFromCursor(mCursor, UsersContract._ID) !=
                        ((SUApplication) getActivity().getApplication()).getSettings().getId()) {
                    addPoint(Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE),
                            Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE),
                            Utils.getStringFromCursor(mCursor, User.FIELD_NAME),
                            -1, R.drawable.map_balloon, false);
                }else{
                    myLatitude =  Utils.getDoubleFromCursor(mCursor, User.FIELD_LATITUDE);
                    myLongitude = Utils.getDoubleFromCursor(mCursor, User.FIELD_LONGITUDE);
                }
            }
        }
        if(myLatitude != User.NO_COORDINATES && myLongitude != User.NO_COORDINATES){
            addPoint(myLatitude, myLongitude, getString(R.string.you_here),
                    -1, R.drawable.map_balloon_my, false);
        }
    }

    private void prepareCursor() {
        Cursor cursor = getActivity().getContentResolver().query(
                UsersContract.CONTENT_URI, null, null, null, null);
        setCursor(cursor);
    }

    private void setCursor(Cursor cursor) {
        if (cursor == null) {
            if (mCursor != null) {
                mCursor.unregisterContentObserver(mObserver);
                mCursor.close();
                mCursor = null;
            }
        } else {
            mCursor = cursor;
            mCursor.registerContentObserver(mObserver);
            updateContentFromCursor();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareCursor();
        getActivity().getActionBar().setTitle(R.string.users_map);
    }

    @Override
    public void onPause() {
        setCursor(null);
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    /**
     * Добавление текущих координат
     */
    private void setMyLocation(){
        getMap().setMyLocationEnabled(true);
        getMap().getUiSettings().setMyLocationButtonEnabled(true);
    }

    /**
     * Добавление точки по координатам
     * @param latitude
     * @param longitude
     * @param title - подпись над точкой
     * @param zoom - увеличение карты
     * @param balloon - картинка для точки на карте
     * @param isDraggable - передвигаемая ли точка
     */
    public void addPoint(double latitude, double longitude, String title, float zoom,
                          int balloon, boolean isDraggable) {
        // latitude and longitude
        LatLng point = new LatLng(latitude, longitude);
       addPoint(point, title, zoom, balloon, isDraggable);
    }

    /**
     * Добавление точки
     * @param point
     * @param title - подпись над точкой
     * @param zoom - увеличение карты
     * @param balloon - картинка для точки на карте
     * @param isDraggable - передвигаемая ли точка
     */
    public void addPoint(LatLng point, String title, float zoom,
                         int balloon, boolean isDraggable) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(point);
        if(title != null) {
            marker.title(title);
        }
        if(balloon != -1){
            marker.icon(BitmapDescriptorFactory
                    .fromResource(balloon));
        }
        marker.draggable(isDraggable);
        // adding marker
        if(getMap()!=null){
            getMap().addMarker(marker);
            CameraUpdate update = CameraUpdateFactory.newLatLng(point);
            getMap().moveCamera(update);
            //масштаб
            if(zoom != -1) {
                getMap().moveCamera(CameraUpdateFactory.zoomTo(zoom));
            }
        }
    }
}
