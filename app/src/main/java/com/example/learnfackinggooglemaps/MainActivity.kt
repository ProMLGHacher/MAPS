package com.example.learnfackinggooglemaps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.*
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.MarkerOptions




class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnMarkerClickListener {

    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2
            )
        } else {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

    }



    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))

        googleMap.uiSettings.isCompassEnabled = true
        googleMap.isBuildingsEnabled = true
        googleMap.setOnPoiClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap.isMyLocationEnabled = true

        addCircle()

        var marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Hello world")
                .snippet("Additional text")
        )

        addPolygon()



    }

    //при нажатии на объект
    override fun onPoiClick(pointOfInterest: PointOfInterest) {
        Toast.makeText(
            this, pointOfInterest.name,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // рисует круг
    fun addCircle() {
        val seattle = LatLng(47.6062095, -122.3320708)
        val circleOptions = CircleOptions()
        circleOptions.center(seattle)
        circleOptions.radius(8500.0)
        circleOptions.fillColor(  Color.parseColor("#7FFF0000"))
        circleOptions.strokeColor(Color.TRANSPARENT)
        circleOptions.strokeWidth(0f)
        googleMap.addCircle(circleOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(seattle))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.remove()
        return true
    }

//    private fun createNewMarkers(markers: List<LatLng>)  {
//        markers.forEach {
////            if(it.coor == "") return@forEach
////            val lat = it.coor.split(", ")
//            val latLng = LatLng(lat[0].toDouble(), lat[1].toDouble())
//            val marker = googleMap.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_orangemarker))
//                    .title(it.type)
//            ) ?: return
//            marker.tag = it.id
//        }
//    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, R.drawable.marker_background)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun moveCamera(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10F))
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

//    fun addPolyLine(markers: List<PlaceObject>) {
//
//        var plo = PolylineOptions()
//        googleMap.clear()
//        createNewMarkers(listMutant)
//
//        plo.color(Color.BLACK)
//        plo.geodesic(true)
//        plo.startCap(RoundCap())
//        plo.width(8f)
//        plo.jointType(JointType.BEVEL)
//        googleMap.addPolyline(plo)
//    }

        fun addPolygon() {

            var plo = PolygonOptions()
//            googleMap.clear()
//            createNewMarkers(listMutant)

            val issaquah = LatLng(47.5301011, -122.0326191)
            val seattle = LatLng(47.6062095, -122.3320708)
            val bellevue = LatLng(47.6101497, -122.2015159)
            val sammamish = LatLng(47.6162683, -122.0355736)
            val redmond = LatLng(47.6739881, -122.121512)

            plo.add(issaquah, sammamish, redmond, seattle)
            plo.fillColor(Color.parseColor("#7FFF0000"))
            plo.strokeColor(Color.parseColor("#00000000"))
            googleMap.addPolygon(plo)

            moveCamera(redmond)

    }



}