package com.example.notifition.model;

import com.example.notifition.model.Distances;
import com.example.notifition.model.Durations;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route{
    public Distances distance;
    public Durations duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

}
