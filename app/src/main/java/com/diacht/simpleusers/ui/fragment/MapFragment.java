package com.diacht.simpleusers.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map fragment.
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment
        implements GoogleMap.OnMapLongClickListener, View.OnClickListener{
    public static final double FIELD_LATITUDE = 47.812004;
    public static final double FIELD_LONGITUDE = 35.107017;
    public static final String FIELD_ADDRESS = "empire state building";

    public static MapFragment newInstance() {
        final MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMyLocation();
        addPoint(FIELD_LATITUDE, FIELD_LONGITUDE, "address", 14.0f, -1, false);
        //getMap().setOnMapLongClickListener(this);
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

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }
}
