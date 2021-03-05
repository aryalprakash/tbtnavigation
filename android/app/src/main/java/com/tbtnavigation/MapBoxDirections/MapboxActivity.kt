package com.tbtnavigation.MapBoxDirections
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.ui.NavigationViewOptions
import com.mapbox.navigation.ui.OnNavigationReadyCallback
import com.mapbox.navigation.ui.listeners.NavigationListener
import com.mapbox.navigation.ui.map.NavigationMapboxMap
import com.mapbox.geojson.Point

import android.util.Log;


import com.tbtnavigation.R
import kotlinx.android.synthetic.main.activity_embedded_navigation.*

class MapboxActivity : AppCompatActivity(), OnNavigationReadyCallback, NavigationListener {
    private lateinit var navigationMapboxMap: NavigationMapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var routeString: String
    
    private val route by lazy { getRoutes() }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_embedded_navigation)
        navigationView.onCreate(savedInstanceState)
        navigationView.initialize(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        navigationView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        navigationView.onStart()
    }

    override fun onResume() {
        super.onResume()
        navigationView.onResume()
    }

    override fun onStop() {
        super.onStop()
        navigationView.onStop()
    }

    override fun onPause() {
        super.onPause()
        navigationView.onPause()
    }

    override fun onDestroy() {
        navigationView.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigationView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        navigationView.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNavigationReady(isRunning: Boolean) {
        if (!isRunning && !::navigationMapboxMap.isInitialized) {
            if (navigationView.retrieveNavigationMapboxMap() != null) {
                this.navigationMapboxMap = navigationView.retrieveNavigationMapboxMap()!!
                navigationView.retrieveMapboxNavigation()?.let { this.mapboxNavigation = it }
                val optionsBuilder = NavigationViewOptions.builder(this)
                optionsBuilder.navigationListener(this)
                optionsBuilder.directionsRoute(route)
                // optionsBuilder.shouldSimulateRoute(true)
                navigationView.startNavigation(optionsBuilder.build())
            }
        }
    }

    override fun onNavigationRunning() {
        // Empty because not needed in this example
    }

    override fun onNavigationFinished() {
        finish()
    }

    override fun onCancelNavigation() {
        navigationView.stopNavigation()
        finish()
    }

    private fun getRoutes(): DirectionsRoute {
        routeString = intent.getStringExtra("routeString")
        
        return DirectionsRoute.fromJson(routeString)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
