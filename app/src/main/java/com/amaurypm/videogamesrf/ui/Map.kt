package com.amaurypm.videogamesrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.databinding.ActivityMainBinding
import com.amaurypm.videogamesrf.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    //Para google maps
    private lateinit var map: GoogleMap

    var latitude = 0.0
    var longitud = 0.0
    var nom = ""
    var par = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        latitude = intent.getDoubleExtra("Latitud", 0.0)

        longitud = intent.getDoubleExtra("Longitud", 0.0)

        nom = intent.getStringExtra("Nombre").toString()

        par = intent.getStringExtra("Parent").toString()

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker(){
        val coordinates = LatLng(latitude,longitud)
        val marker = MarkerOptions()
            .position(coordinates)
            .title(nom)
            .snippet(par)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }
}