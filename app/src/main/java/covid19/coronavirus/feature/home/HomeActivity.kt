package covid19.coronavirus.feature.home

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import covid19.coronavirus.R
import covid19.coronavirus.feature.credits.CreditsActivity
import covid19.coronavirus.feature.emergency.EmergencyActivity
import covid19.coronavirus.feature.feedback.FeedbackActivity
import covid19.coronavirus.feature.global.GlobalCasesActivity
import covid19.coronavirus.feature.search.SearchActivity
import covid19.coronavirus.feature.symptoms.SymptomsActivity
import covid19.coronavirus.firebase.analytics.AnalyticsUtil
import covid19.coronavirus.model.Country
import covid19.coronavirus.model.TotalCases
import covid19.coronavirus.util.changeBitmap
import covid19.coronavirus.util.formatNumber
import covid19.coronavirus.util.location.SmartLocation
import covid19.coronavirus.util.setTransparentStatusBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: HomeViewModel by inject()

    private var smartLocation: SmartLocation? = null

    private var mMap: GoogleMap? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var afterMarkerSelected: Marker? = null
    private var countrySelected: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTransparentStatusBar(false)
        setBottomHomeFragment()
        setDrawerLayout()
        setViewModel()
        setMap()
        setOnClick()
        requestLocationWithPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SEARCH && resultCode == Activity.RESULT_OK) {
            val country = data?.getParcelableExtra<Country>(SearchActivity.SEARCH_RESULT)
            country?.run {
                if (location == getString(R.string.your_location)) {
                    AnalyticsUtil.logEventAction(
                        this@HomeActivity,
                        AnalyticsUtil.Value.SEARCH,
                        AnalyticsUtil.Value.YOUR_LOCATION
                    )
                    requestLocationWithPermission()
                } else {
                    countrySelected = this
                    AnalyticsUtil.logEventAction(
                        this@HomeActivity,
                        AnalyticsUtil.Value.SEARCH,
                        location
                    )
                    titleCasesText.text = getString(R.string.home_bottom_country_title, location)
                    confirmedCountText.text = formatNumber(confirmed)
                    deathCountText.text = formatNumber(deaths)
                    recoveredCountText.text = formatNumber(recovered)

                    val latLngNow = LatLng(latitude, longitude)

                    val location = CameraUpdateFactory.newLatLngZoom(
                        latLngNow, ZOOM_MAP
                    )

                    mMap?.animateCamera(location)
                }
            }
        }
        if (requestCode == SmartLocation.RQ_RESOLUTION_REQUIRED && resultCode == Activity.RESULT_OK) {
            smartLocation?.activateGPS()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView)
        } else {
            moveTaskToBack(true)
        }
    }

    /** Init & set methods **/

    private fun setBottomHomeFragment() {
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet)
    }

    private fun setDrawerLayout() {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_global_cases -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_GLOBAL_CASES)
                    goToGlobalCases()
                }
                R.id.nav_symptoms -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_SYMPTOMS)
                    goToSymptoms()
                }
                R.id.nav_emergency_guide -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_EMERGENCY)
                    goToEmergencyGuide()
                }
                R.id.nav_feedback -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_FEEDBACK)
                    goToFeedback()
                }
                R.id.nav_credits -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_CREDITS)
                    goToCredits()
                }
                R.id.nav_contact -> {
                    AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_CONTACT)
                    sendEmail()
                }
            }
            drawerLayout.closeDrawer(navigationView)
            true
        }
    }

    private fun setViewModel() {
        viewModel.showTotalCasesLiveData.observe(this, observerTotalCases())
        viewModel.getCountriesLiveData.observe(this, observerGetCountries())
        viewModel.getTotalCases()
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setOnClick() {
        menuButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.MENU_OPEN)
            drawerLayout.openDrawer(navigationView)
        }
        searchText.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_OPEN_SEARCH)
            goToSearch()
        }
        globalCasesView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_GLOBAL_CASES)
            goToGlobalCases()
        }
        symptomsView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_SYMPTOMS)
            goToSymptoms()
        }
        consultButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_SYMPTOMS_BUTTON)
            goToSymptoms()
        }
        emergencyView.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_EMERGENCY)
            goToEmergencyGuide()
        }
        knowMoreButton.setOnClickListener {
            AnalyticsUtil.logEvent(this, AnalyticsUtil.Value.HOME_EMERGENCY_BUTTON)
            goToEmergencyGuide()
        }
    }

    @AfterPermissionGranted(RQ_ACCESS_FINE_LOCATION)
    private fun requestLocationWithPermission() {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startLocationUpdates()
        } else {
            EasyPermissions.requestPermissions(
                this, "Debe otorgar permisos para acceder a su ubicación",
                RQ_ACCESS_FINE_LOCATION, *perms
            )
        }
    }

    private fun startLocationUpdates() {
        smartLocation = SmartLocation(this)
        smartLocation?.addOnGetLocationSuccessful {
            val latLngNow = LatLng(it.latitude, it.longitude)

            val location = CameraUpdateFactory.newLatLngZoom(
                latLngNow, ZOOM_MAP
            )

            mMap?.animateCamera(location)

        }?.addOnGetLocationFailed {
            Log.i(SmartLocation.TAG, it.message ?: "")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.apply {

            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this@HomeActivity, R.raw.map_style
                    )
                )
                if (!success) {
                    Log.e(this@HomeActivity.javaClass.simpleName, "Style parsing failed.")
                }
            } catch (e: NotFoundException) {
                Log.e(this@HomeActivity.javaClass.simpleName, "Can't find style. Error: ", e)
            }

            uiSettings.isCompassEnabled = false
            setMinZoomPreference(1f)

            setOnMarkerClickListener {
                setSmallMarkerIcon()
                it.setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        changeBitmap(
                            this@HomeActivity,
                            R.drawable.ic_marker, 100, 100
                        )
                    )
                )

                viewModel.data[it]?.apply {
                    countrySelected = this
                    titleCasesText.text = getString(R.string.home_bottom_country_title, location)
                    confirmedCountText.text = formatNumber(confirmed)
                    deathCountText.text = formatNumber(deaths)
                    recoveredCountText.text = formatNumber(recovered)
                }

                afterMarkerSelected = it
                false
            }

            setOnMapClickListener {
                setSmallMarkerIcon()
                countrySelected = null
                viewModel.getTotalCases()
            }
        }

        viewModel.getCountryCases()
    }

    private fun setSmallMarkerIcon() {
        afterMarkerSelected?.run {
            setIcon(
                BitmapDescriptorFactory.fromBitmap(
                    changeBitmap(
                        this@HomeActivity,
                        R.drawable.ic_marker, 80, 80
                    )
                )
            )
        }
    }

    /** Go to **/

    private fun goToSearch() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivityForResult(intent, RQ_SEARCH)
    }

    private fun goToGlobalCases() {
        val intent = Intent(this, GlobalCasesActivity::class.java)
        intent.putExtra(GlobalCasesActivity.COUNTRY_SELECTED, countrySelected)
        startActivity(intent)
    }

    private fun goToSymptoms() {
        startActivity(Intent(this, SymptomsActivity::class.java))
    }

    private fun goToEmergencyGuide() {
        startActivity(Intent(this, EmergencyActivity::class.java))
    }

    private fun goToFeedback() {
        startActivity(Intent(this, FeedbackActivity::class.java))
    }

    private fun goToCredits() {
        startActivity(Intent(this, CreditsActivity::class.java))
    }

    private fun sendEmail() {
        Toast.makeText(this, getString(R.string.email_message), Toast.LENGTH_LONG).show()
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact.covid19app@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CovidApp")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello!")

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            // Empty
        }
    }

    /** Observers **/

    private fun observerTotalCases() = Observer<TotalCases> {
        titleCasesText.text = getString(R.string.home_bottom_dialog_title)
        confirmedCountText.text = formatNumber(it.confirmed)
        deathCountText.text = formatNumber(it.deaths)
        recoveredCountText.text = formatNumber(it.recovered)
    }

    private fun observerGetCountries() = Observer<MutableList<Country>> {
        val bitmap = changeBitmap(this, R.drawable.ic_marker, 80, 80)
        it.forEach { country ->

            val markerOptions = MarkerOptions()
                .position(LatLng(country.latitude, country.longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

            val marker = mMap?.addMarker(markerOptions)
            marker?.run { viewModel.data.put(marker, country) }
        }
    }

    companion object {
        private const val ZOOM_MAP = 4.8f

        private const val RQ_ACCESS_FINE_LOCATION = 1
        private const val RQ_SEARCH = 2
    }
}
