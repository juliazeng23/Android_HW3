package com.example.hw2solutions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.math.roundToInt
import kotlin.random.Random
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar


class hsvActivity : AppCompatActivity() {


    lateinit var seekBarHue : SeekBar
    lateinit var seekBarSaturation : SeekBar
    lateinit var seekBarValue: SeekBar
    lateinit var textViewHue: TextView
    lateinit var textViewSaturation: TextView
    lateinit var textViewValue: TextView
    lateinit var colorSquare : View
    lateinit var hexColorText : TextView
    lateinit var switchButton : Button
    lateinit var locationButton: Button

    private var hsvArr = FloatArray(3)
    private lateinit var locationClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI components
        seekBarHue = findViewById(R.id.seekBarRed)
        seekBarSaturation = findViewById(R.id.seekBarGreen)
        seekBarValue = findViewById(R.id.seekBarBlue)
        textViewHue = findViewById(R.id.textViewRed)
        textViewSaturation = findViewById(R.id.textViewGreen)
        textViewValue = findViewById(R.id.textViewBlue)
        colorSquare = findViewById(R.id.color_square)
        hexColorText = findViewById(R.id.textViewHexColor)

        switchButton = findViewById(R.id.switchButton)
        locationButton = findViewById(R.id.locationButton)

        setUpSeekbar(seekBarHue, textViewHue, resources.getString(R.string.red), 360)
        setUpSeekbar(seekBarSaturation, textViewSaturation, resources.getString(R.string.green),1000)
        setUpSeekbar(seekBarValue, textViewValue, resources.getString(R.string.blue),1000)

        // Initialize the square's color on onCreate()
        regenerateColor()

        switchButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("red",Color.red(Color.HSVToColor(hsvArr)))
            intent.putExtra("blue",Color.blue(Color.HSVToColor(hsvArr)))
            intent.putExtra("green",Color.green(Color.HSVToColor(hsvArr)))

            intent.type = "text/plain"
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        locationButton.setOnClickListener{
            locationClient = LocationServices.getFusedLocationProviderClient(this)
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationClient.lastLocation.addOnSuccessListener{location-> Log.d("Tag",location.altitude.toString())}
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }
    private fun initialSetUp(sb: SeekBar, tv: TextView, color: String) {
        // Set initial color to random number
        tv.text = resources.getString(R.string.seekbarLabel, color, 0)
    }

    private fun setUpSeekbar(sb: SeekBar, tv: TextView, color : String, max : Int) {
        // Set the max value of seekbar to max hexcode - 255
        sb.max = max
        initialSetUp(sb, tv, color)

        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                regenerateColor()

                // Set TextView based on orientation
                if(sb==seekBarHue){
                    updateSeekBarTextView(tv, color, p1,1)
                } else if (sb==seekBarSaturation) {
                    updateSeekBarTextView(tv, color, p1, 1000)
                } else if(sb==seekBarValue){
                    updateSeekBarTextView(tv, color, p1, 1000)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    // Modifies text label next to SeekBar depending on device orientation
    private fun updateSeekBarTextView(tv: TextView, color: String, progress: Int, modifier: Int) {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                textViewHue.text = "Hue"
                textViewSaturation.text = "Saturation"
                textViewValue.text = "Value"

            }
            Configuration.ORIENTATION_PORTRAIT -> {
                textViewHue.text = "Hue: "+(seekBarHue.progress).toString()
                textViewSaturation.text = "Saturation: "+((seekBarSaturation.progress+0f)/1000f).toString()
                textViewValue.text = "Value: "+((seekBarValue.progress+0f)/1000f).toString()
            }
        }
    }

    override fun onResume(){
        super.onResume()
        var hue = 0f
        var sat = 0f
        var value = 0f
        hue = intent.extras?.getFloat("Hue") ?: 0f
        sat = intent.extras?.getFloat("Saturation") ?: 0f
        value = intent.extras?.getFloat("Value") ?: 0f
        hsvArr[0]=hue
        hsvArr[1]=sat
        hsvArr[2]=value

        textViewHue.text = hsvArr[0].toString()
        textViewSaturation.text = hsvArr[1].toString()
        textViewValue.text = hsvArr[2].toString()

        seekBarHue.setProgress(hue.toInt())
        seekBarSaturation.setProgress((sat*1000).toInt())
        seekBarValue.setProgress((value*1000).toInt())
    }

    // Regenerates the color of the color square.
    private fun regenerateColor() {
        hsvArr[0] = seekBarHue.progress+0f
        hsvArr[1] = (seekBarSaturation.progress+0f)/1000f
        hsvArr[2] = (seekBarValue.progress+0f)/1000f

        colorSquare.setBackgroundColor(Color.HSVToColor(hsvArr))

        hexColorText.text = resources.getString(
            R.string.hexString,
            Integer.toHexString(Color.red(Color.HSVToColor(hsvArr))).toUpperCase(),
            Integer.toHexString(Color.blue(Color.HSVToColor(hsvArr))).toUpperCase(),
            Integer.toHexString(Color.green(Color.HSVToColor(hsvArr))).toUpperCase()
        )

    }
    private fun getColorString(latitude : Double) : String {
//        return resources.getString(
//            R.string.locationString,
//            ((latitude % 1) * 100000).roundToInt().toString().padStart(6, '0')

        return "lol"

    }

    fun test(){


    }


}
