package net.thesimson.idealoctodoodle

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private  fun buttonListener() {

    }
//    -> {
//        message.setText("Hello World")
//        tokyo.setTypeface(Typeface.DEFAULT)
//        return@OnNavigationItemSelectedListener true
//
//    }
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
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        tokyo.setOnClickListener {
            message.setText("Hello World")
            tokyo.setTypeface(Typeface.DEFAULT)
        }
    }
}
