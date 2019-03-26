package net.thesimson.idealoctodoodle

import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private  fun buttonListener() {

    }
    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                tokyo.setTypeface(Typeface.DEFAULT_BOLD)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                tokyo.setTypeface(Typeface.MONOSPACE)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                tokyo.setTypeface(Typeface.SANS_SERIF)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ctx = getApplicationContext()

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main)

     //todo   navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        worldmap.setTileSource(TileSourceFactory.MAPNIK);
        worldmap.setMultiTouchControls(true)
        worldmap.setBuiltInZoomControls(true)

        worldmap.controller.setCenter(GeoPoint( 51.507222, -0.1275))
        // set up my buttons
        tokyo.setOnClickListener {
            message.setText("Hello World")
            val point: GeoPoint = GeoPoint(35.683333, 139.683333)
            worldmap.controller.animateTo(point)
            worldmap.controller.setZoom(13.0)

        }
        paris.setOnClickListener{
            message.setText("Paris Calling")
            worldmap.latitudeSpanDouble
            val point: GeoPoint = GeoPoint(48.8567,2.3508)
            worldmap.controller.animateTo(point)
            worldmap.controller.setZoom(13.0)
        }
        london.setOnClickListener {
            Thread{
                this@MainActivity.runOnUiThread {
                    worldmap.controller.setZoom(7.0)
                }
                Thread.sleep(1000)
                this@MainActivity.runOnUiThread {
                    message.setText("London")
                    val point: GeoPoint = GeoPoint(51.507222, -0.1275)
                    worldmap.controller.animateTo(point)
                }
                Thread.sleep(2000)
                this@MainActivity.runOnUiThread {
                    message.setText("One")
                    worldmap.controller.setZoom(13.0)
                    message.setText("Two")
                }

            }.start()


        }
        mirror.setOnClickListener {
            var mapCenter = worldmap.getMapCenter()
            var point = GeoPoint(-mapCenter.latitude, (360.0 + mapCenter.longitude) % 360.0 - 180.0)
            worldmap.controller.animateTo(point)
            worldmap.controller.zoomOut()
        }

     //   worldmap.controller.
    }

    override fun onResume() {
        super.onResume()
        worldmap.onResume()
    }

    override fun onPause() {
        super.onPause()
        worldmap.onPause()
    }
}

