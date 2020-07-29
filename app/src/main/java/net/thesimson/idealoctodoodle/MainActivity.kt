package net.thesimson.idealoctodoodle

import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.UiThread
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quicklocations.*
import kotlinx.android.synthetic.main.quicklocations.view.*
import kotlinx.android.synthetic.main.quicklocations.view.london
import kotlinx.android.synthetic.main.quicklocations.view.mirror
import kotlinx.android.synthetic.main.quicklocations.view.paris
import kotlinx.android.synthetic.main.worldmaps.*
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {
    private var wm1:IGeoPoint? = null
    private var wm0:IGeoPoint? = null
    private var zoomlevel0:Double = 0.0
    private var zoomlevel1:Double = 0.0
    private  var zoomlock = -1

    // Lulea airport 	65°32′37″N 022°07′19″E   //3500 m
    // singapore airport 01°21′33″N 103°59′22″E  // 4000 m
    val LLA = GeoPoint( 65.543611, 22.121944 )
    val SIN = GeoPoint (1.359167, 103.989444)
    val point: GeoPoint = GeoPoint(48.8567,2.3508)

    private  fun buttonListener() {

    }
    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message0.setText(R.string.title_home)
//                tokyo.setTypeface(Typeface.DEFAULT_BOLD)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message0.setText(R.string.title_dashboard)
//                tokyo.setTypeface(Typeface.MONOSPACE)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message0.setText(R.string.title_notifications)
//                tokyo.setTypeface(Typeface.SANS_SERIF)
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
            val calculationDistance: Float = 10000.00f
            val refdist = osmMaster.projection.metersToPixels(calculationDistance)

            //target latitude
            val lat = osmSlave.mapCenter.latitude

            val y1 = osmSlave.projection.metersToPixels(calculationDistance, lat,osmSlave.zoomLevelDouble )
            val y2 = osmSlave.projection.metersToPixels(calculationDistance, lat,osmMaster.zoomLevelDouble )

            val targetZoom = if (y1 == y2 ) {
                osmMaster.zoomLevelDouble
            }else {
                ((y2 - refdist) * (osmSlave.zoomLevelDouble) + (refdist - y1) * (osmMaster.zoomLevelDouble)) / (y2 - y1)
            }

            message0.text =
            //            worldmap0.projection.metersToPixels(1000f).toString()+
            //"Zoom "+worldmap0.zoomLevelDouble.toString()+
                worldmap0.mapCenter.toString()
            message1.text =
            // worldmap1.projection.metersToPixels(1000f).toString()+
            //" Zoom "+worldmap1.zoomLevelDouble.toString()+
                worldmap1.mapCenter.toString()
            osmSlave.controller.setZoom(targetZoom)
        }

        worldmap0.setTileSource(TileSourceFactory.MAPNIK);
        worldmap0.setMultiTouchControls(true)
        worldmap0.setBuiltInZoomControls(false)


        worldmap1.setTileSource(TileSourceFactory.MAPNIK);
        worldmap1.setMultiTouchControls(true)
        worldmap1.setBuiltInZoomControls(false)

        worldmap0.controller.setCenter(LLA)
        worldmap1.controller.setCenter(SIN)

        zoomlock = 0
        worldmap0.controller.setZoom(13.0)

        worldmap1.addMapListener(object:MapListener{
            @UiThread
            override fun onZoom(event: ZoomEvent?): Boolean {
                if (zoomlock == 0) {
                    zoomlock = 1
                    copyZoom(worldmap1,worldmap0)
                    zoomlock = 0
                }
                return true
            }
            @UiThread
            override fun onScroll(scroolEvent: ScrollEvent):Boolean{
                if (zoomlock == 0) {
                    zoomlock = 1
                    copyZoom(worldmap1,worldmap0)
                    zoomlock = 0
                }
                return true
            }
        })

     worldmap0.addMapListener(object:MapListener{
         override fun onZoom(zoomEvent: ZoomEvent):Boolean{
             if (zoomlock == 0) {
                 zoomlock = 2
                 copyZoom(worldmap0,worldmap1)
                 zoomlock = 0
             }
             return true
         }
         override fun onScroll(scroolEvent: ScrollEvent):Boolean{
             if (zoomlock == 0) {
                 zoomlock = 2
                 copyZoom(worldmap0,worldmap1)
                 zoomlock = 0
             }
             return true
         }
     })

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
//         Thread{
//             this@MainActivity.runOnUiThread {
//                 worldmap0.controller.setZoom(7.0)
//             }
//             Thread.sleep(1000)
//             this@MainActivity.runOnUiThread {
//                 message0.setText("London")
//                 val point: GeoPoint = GeoPoint(51.507222, -0.1275)
//                 worldmap0.controller.animateTo(point)
//             }
//             Thread.sleep(2000)
//             this@MainActivity.runOnUiThread {
//                 message0.setText("One")
//                 worldmap0.controller.setZoom(13.0)
//                 message0.setText("Two")
//             }
//
//         }.start()
     //Goto    55.995309, 13.441772

         worldmap0.controller.animateTo(GeoPoint(55.995309, 13.441772))

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
     if(wm0 != null) {
         worldmap0.controller.setCenter(wm0)
         worldmap0.controller.setZoom(zoomlevel0)
     }
     if(wm1 != null) {
         worldmap1.controller.setCenter(wm1)
         worldmap1.controller.setZoom(zoomlevel1)
     }
 }

 override fun onPause() {
     super.onPause()
     worldmap0.onPause()
     worldmap1.onPause()

     wm0 = worldmap0.mapCenter
     zoomlevel0 = worldmap0.zoomLevelDouble
     wm1 = worldmap1.mapCenter
     zoomlevel1 =worldmap1.zoomLevelDouble
 }
}

