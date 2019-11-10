package net.thesimson.idealoctodoodle

import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {

    private  fun buttonListener() {

    }
    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message0.setText(R.string.title_home)
                tokyo.setTypeface(Typeface.DEFAULT_BOLD)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message0.setText(R.string.title_dashboard)
                tokyo.setTypeface(Typeface.MONOSPACE)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message0.setText(R.string.title_notifications)
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



        fun copyZoom(osmMaster:MapView, osmSlave:MapView) {
            val zoomlevel = osmMaster.zoomLevelDouble
            val calculationDistance: Float = 1000000f
            val refdist = osmMaster.projection.metersToPixels(calculationDistance)

            //target latitude
            val lat = osmSlave.mapCenter.latitude

            var targetZoom: Double = zoomlevel
            (10 until 100).forEach {
                val epsilon = Math.pow(10.0, -it.toDouble())
                while (osmSlave.projection.metersToPixels(
                        calculationDistance,
                        lat,
                        targetZoom
                    ) > refdist
                ) {
                    targetZoom *= (1 - epsilon)
                }
                while (osmSlave.projection.metersToPixels(
                        calculationDistance,
                        lat,
                        targetZoom
                    ) < refdist
                ) {
                    targetZoom *= (1 + epsilon)
                }
            }
            message0.text = worldmap0.projection.metersToPixels(1000f).toString()+ "Zoom "+worldmap0.zoomLevelDouble.toString()
            message1.text = worldmap1.projection.metersToPixels(1000f).toString()+" Zoom "+worldmap1.zoomLevelDouble.toString()
            osmSlave.controller.setZoom(targetZoom)
        }


        worldmap0.setTileSource(TileSourceFactory.MAPNIK);
        worldmap0.setMultiTouchControls(true)
        worldmap0.setBuiltInZoomControls(true)

        worldmap0.controller.setCenter(GeoPoint( 51.507222, -0.1275))

        worldmap1.setTileSource(TileSourceFactory.MAPNIK);
        worldmap1.setMultiTouchControls(true)
        worldmap1.setBuiltInZoomControls(true)

        worldmap1.controller.setCenter(GeoPoint( 51.507222, -0.1275))

        val point: GeoPoint = GeoPoint(48.8567,2.3508)
        worldmap0.controller.animateTo(point)
        worldmap0.controller.setZoom(13.0)

        worldmap1.controller.animateTo(point)
        worldmap1.controller.setZoom(13.0)


        worldmap1.addMapListener(object:MapListener{

            override fun onZoom(event: ZoomEvent?): Boolean {
                copyZoom(worldmap1,worldmap0)

                return true
            }
            override fun onScroll(scroolEvent: ScrollEvent):Boolean{
              //  val point: GeoPoint = GeoPoint(35.683333, 139.683333)
              //  worldmap0.controller.animateTo(point)

                return true
            }
        })


/*
        worldmap0.addMapListener(object:MapListener{
            override fun onZoom(zoomEvent: ZoomEvent):Boolean{
                copyZoom(worldmap0,worldmap1)
                return true
            }
            override fun onScroll(scroolEvent: ScrollEvent):Boolean{
              //  val point: GeoPoint = GeoPoint(35.683333, 139.683333)
              //  worldmap1.controller.animateTo(point)

                return true
            }
        })
*/
        /*
        worldmap1.handler.onZoom{
            val z:Double = worldmap1.controller.zoomLevelDouble
            worldmap0.controller.setZoom(z)
        }
*/
        // set up my buttons
        tokyo.setOnClickListener {
            message0.setText("Hello World")
            val point: GeoPoint = GeoPoint(35.683333, 139.683333)
            worldmap1.controller.animateTo(point)
            worldmap1.controller.setZoom(13.0)

        }
        paris.setOnClickListener{
            message0.setText("Paris Calling")
            worldmap1.latitudeSpanDouble
            val point: GeoPoint = GeoPoint(48.8567,2.3508)
            worldmap1.controller.animateTo(point)
            worldmap1.controller.setZoom(13.0)
        }
        london.setOnClickListener {
            Thread{
                this@MainActivity.runOnUiThread {
                    worldmap0.controller.setZoom(7.0)
                }
                Thread.sleep(1000)
                this@MainActivity.runOnUiThread {
                    message0.setText("London")
                    val point: GeoPoint = GeoPoint(51.507222, -0.1275)
                    worldmap0.controller.animateTo(point)
                }
                Thread.sleep(2000)
                this@MainActivity.runOnUiThread {
                    message0.setText("One")
                    worldmap0.controller.setZoom(13.0)
                    message0.setText("Two")
                }

            }.start()


        }
        mirror.setOnClickListener {
            var mapCenter = worldmap1.getMapCenter()
            var point = GeoPoint(-mapCenter.latitude, (360.0 + mapCenter.longitude) % 360.0 - 180.0)
            worldmap1.controller.animateTo(point)
            worldmap1.controller.zoomOut()
        }

     //   worldmap.controller.
    }

    override fun onResume() {
        super.onResume()
        worldmap1.onResume()
        worldmap0.onResume()
    }

    override fun onPause() {
        super.onPause()
        worldmap0.onPause()
        worldmap1.onPause()
    }
}

