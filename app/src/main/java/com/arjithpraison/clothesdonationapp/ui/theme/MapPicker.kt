package com.arjithpraison.clothesdonationapp.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.arjithpraison.clothesdonationapp.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Marker.OnMarkerDragListener
import org.osmdroid.views.overlay.OverlayManager
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.ItemizedOverlay

@Composable
fun MapPicker(onLocationSelected: (Double, Double) -> Unit) {
    var mapView: MapView? by remember { mutableStateOf(null) }
    var marker: Marker? by remember { mutableStateOf(null) }
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }

    // Initialize OSMDroid configuration
    Configuration.getInstance().load(LocalContext.current, LocalContext.current.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE))

    AndroidView(
        factory = {
            mapView = MapView(it)
            mapView?.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(0.0, 0.0))

                // Add MapEventsOverlay to detect map clicks
                val mapEventsReceiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                        p?.let {
                            selectedLocation = it
                            marker?.position = it
                            marker?.title = "Selected Location"
                            marker?.showInfoWindow()
                        }
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint?): Boolean {
                        return false
                    }
                }
                overlays.add(MapEventsOverlay(mapEventsReceiver))

                // Add marker
                marker = Marker(this).apply {
                    setIcon(ContextCompat.getDrawable(it, R.drawable.baseline_add_location_24))
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    isDraggable = true
                    setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                        override fun onMarkerDrag(marker: Marker?) {}
                        override fun onMarkerDragEnd(marker: Marker?) {
                            marker?.position?.let { newPosition ->
                                selectedLocation = newPosition
                                onLocationSelected(newPosition.latitude, newPosition.longitude)
                            }
                        }

                        override fun onMarkerDragStart(marker: Marker?) {}
                    })
                }
                overlays.add(marker)
            }
            mapView!!
        },
        modifier = Modifier.fillMaxSize()
    )
}
