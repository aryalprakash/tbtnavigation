package com.tbtnavigation.MapBoxDirections

import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod


class MapboxManager(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "MapBoxDirections"
    }

    @ReactMethod
    fun startNavigation(routeString: String) {
        val intent = Intent(reactApplicationContext, MapboxActivity::class.java)
        intent.putExtra("routeString", routeString)
        // intent.putExtra("destination", destination)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactApplicationContext.startActivity(intent)
    }

}