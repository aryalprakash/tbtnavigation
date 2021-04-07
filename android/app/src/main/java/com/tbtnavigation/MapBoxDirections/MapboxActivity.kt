package com.tbtnavigation.MapBoxDirections
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.ui.NavigationViewOptions
// import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
// import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.navigation.ui.OnNavigationReadyCallback
import com.mapbox.navigation.ui.listeners.NavigationListener
import com.mapbox.navigation.ui.map.NavigationMapboxMap
import com.mapbox.navigation.ui.camera.NavigationCamera
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import java.lang.ref.WeakReference;
import android.util.Log;


import com.tbtnavigation.R
import kotlinx.android.synthetic.main.activity_embedded_navigation.*

class MapboxActivity : AppCompatActivity(), OnNavigationReadyCallback, NavigationListener {
    private lateinit var navigationMapboxMap: NavigationMapboxMap
    // private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var routeString: String

    val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private val destination: Point = Point.fromLngLat(139.784915, 35.680960)
    private val origin: Point = Point.fromLngLat(139.7745686, 35.677573)
    
    // var request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
    //     .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
    //     .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
    //     .build()
    
    // locationEngine.requestLocationUpdates(request, callback, mainLooper)
    // locationEngine.getLastLocation(callback)
    
    private val route by lazy { getRoutes() }

    private val mapboxNavigation: MapboxNavigation by lazy {
        val mapboxNavigationOptions = MapboxNavigation.
            defaultNavigationOptionsBuilder(this, getString(R.string.mapbox_access_token))
            .build()         
        MapboxNavigation(mapboxNavigationOptions);
    }

    // private void initLocationEngine() {
    //     locationEngine = LocationEngineProvider.getBestLocationEngine(this);
         
    //     LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
    //     .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
    //     .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
         
    //     locationEngine.requestLocationUpdates(request, callback, getMainLooper());
    //     locationEngine.getLastLocation(callback);
    // }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_embedded_navigation)
        navigationView.onCreate(savedInstanceState)
        navigationView.initialize(
            this,
            // getInitialCameraPosition()
        )
        // navigationView.initialize(this)
    }

    private fun getInitialCameraPosition(): CameraPosition {
        val originCoordinate = route.routeOptions()?.coordinates()?.get(0)
        return CameraPosition.Builder()
            .target(LatLng(originCoordinate!!.latitude(), originCoordinate.longitude()))
            .zoom(15.0)
            .build()
    }

    // override fun onMapReady(mapboxMap: MapboxMap) {
    //     mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
    //         val cameraPosition = CameraPosition.Builder()
    //             .target(LatLng(destination.latitude(), destination.longitude()))
    //             .zoom(16.5)
    //             .build()
    //         mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            

    //         // Ideally we should use Mapbox.getAccessToken(), but to show GuidanceView we need a
    //         // specific access token for route request.
    //         mapboxNavigation.requestRoutes(
    //             RouteOptions.builder().applyDefaultParams()
    //                 .accessToken(getString(R.string.mapbox_access_token))
    //                 .coordinates(origin, null, destination)
    //                 .alternatives(true)
    //                 .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
    //                 .bannerInstructions(true)
    //                 .build(),
    //             routesReqCallback
    //         )
    //     }
    // }

    // private val routesReqCallback = object : RoutesRequestCallback {
    //     override fun onRoutesReady(routes: List<DirectionsRoute>) {
    //         navigationMapboxMap?.drawRoute(routes[0])
    //     }

    //     override fun onRoutesRequestFailure(throwable: Throwable, routeOptions: RouteOptions) {
    //     }

    //     override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
    //     }
    // }

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
                 val locationEngine = LocationEngineProvider.getBestLocationEngine(this)
                this.navigationMapboxMap = navigationView.retrieveNavigationMapboxMap()!!
                // navigationView.retrieveMapboxNavigation()?.let { this.mapboxNavigation = it }
                this.getInitialCameraPosition();
                val optionsBuilder = NavigationViewOptions.builder(this)
                optionsBuilder.navigationListener(this)
                optionsBuilder.directionsRoute(route)
                optionsBuilder.locationEngine(locationEngine)
                optionsBuilder.shouldSimulateRoute(false)
                // Timer().schedule(2000) {
                    // navigationView?.startCamera(route)
                    navigationView?.startNavigation(optionsBuilder.build())
                    navigationView?.startLayoutAnimation()
                    
                // }
            }

            // val navigationLauncherOptions = NavigationLauncherOptions.builder() //1
            // .directionsRoute(route) //2
            // .shouldSimulateRoute(false) //3
            // .build()

            // NavigationLauncher.startNavigation(this, navigationLauncherOptions) //4
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
        // mapboxNavigation.requestRoutes(
        //     RouteOptions.builder().applyDefaultParams()
        //         .accessToken(getString(R.string.mapbox_access_token))
        //         .coordinates(origin, null, destination)
        //         .alternatives(true)
        //         .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
        //         .bannerInstructions(true)
        //         .build(),
        //     routesReqCallback
        // )
        
        return DirectionsRoute.fromJson(routeString)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
