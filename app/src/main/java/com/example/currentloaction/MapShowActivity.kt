package com.example.currentloaction

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.currentloaction.databinding.ActivityMapShowBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapShowActivity : AppCompatActivity(), OnMapReadyCallback {
    var currentmarker: Marker?=null
    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapShowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val latitude= intent.getDoubleExtra("latitude", 0.0)
        Log.d("location",latitude.toString())


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        val latitude= intent.getDoubleExtra("latitude",0.0)
        val longitude= intent.getDoubleExtra("longitude",0.0)
        val sydney = LatLng(latitude, longitude)
        drawMarker(sydney)


        mMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if (currentmarker!=null)
                    currentmarker?.remove()
                val newLatLong=LatLng(p0.position.latitude,p0.position.longitude)
                drawMarker(newLatLong)

            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun drawMarker(latLng: LatLng){
        val markerOption=  MarkerOptions().position(latLng).title("Marker in Sydney").draggable(true).snippet(getLocation(latLng.latitude,latLng.longitude))
        binding.address.text=markerOption.snippet
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20f))
        currentmarker=mMap.addMarker(markerOption)
        currentmarker?.showInfoWindow()
    }
    private fun getLocation(lat:Double,long:Double):String{
        val geoCoder= Geocoder(this, Locale.getDefault())
        val address=geoCoder.getFromLocation(lat,long,1)
        return address[0].getAddressLine(0).toString()
    }


}