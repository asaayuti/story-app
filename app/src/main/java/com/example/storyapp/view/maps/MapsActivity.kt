package com.example.storyapp.view.maps

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.data.api.response.StoryItem
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.view.WelcomeActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var listStory: List<StoryItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setMapStyle()
        addManyMarker()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "setMapStyle: Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "setMapStyle: Can't find style. Error: " , exception)
        }
    }

    private fun addManyMarker() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isNullOrEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getStoriesWithLocation(user.token).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            Result.Loading -> {}
                            is Result.Success -> {
                                listStory = result.data.listStory
                                listStory?.forEach { story ->
                                    val latLng = LatLng(story?.lat ?: 0.0, story?.lon ?: 0.0)
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(story?.name)
                                            .snippet(story?.description)
                                    )
                                }
                            }

                            is Result.Error -> {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }

}