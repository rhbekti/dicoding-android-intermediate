package com.rhbekti.storyapp.ui.main.maps

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.rhbekti.storyapp.R
import com.rhbekti.storyapp.data.Result
import com.rhbekti.storyapp.data.network.response.ListStoryItem
import com.rhbekti.storyapp.databinding.ActivityMapsBinding
import com.rhbekti.storyapp.ui.main.StoryModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        StoryModelFactory.getInstance(this)
    }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true

        setMapStyle()
        addAnyMarker()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@MapsActivity,
                    R.raw.map_style
                )
            )
            if (!success) {
                showToast(getString(R.string.load_failed))
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun addAnyMarker() {
        viewModel.getStoriesWithLocation()
        viewModel.listStoryPlace.observe(this@MapsActivity) { result ->
            setMarker(result)
        }
    }

    private fun setMarker(result: Result<List<ListStoryItem>>?) {
        if (result != null) {
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    result.data.map { story ->
                        val latLng = LatLng(story.lat, story.lon)
                        mMap.addMarker(
                            MarkerOptions().position(latLng).title(story.name)
                                .snippet(story.description)
                        )
                        boundsBuilder.include(latLng)
                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(result.error)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()
    }
}