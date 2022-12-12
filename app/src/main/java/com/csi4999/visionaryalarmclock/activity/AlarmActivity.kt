package com.csi4999.visionaryalarmclock.activity

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.database.MySharedPreferences
import com.csi4999.visionaryalarmclock.database.ReminderDatabase
import com.csi4999.visionaryalarmclock.databinding.ActivityAlarmBinding
import com.csi4999.visionaryalarmclock.util.*
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*


class AlarmActivity : AppCompatActivity() {


    var reminderAlarmId: Int = 0
    lateinit var r: Ringtone
    lateinit var activityAlarmBinding: ActivityAlarmBinding
    var tag = AlarmActivity::class.simpleName
    var ringtonUri: Uri? = null
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var reminderTitle:String
    var reminderMillies:Long = 0
    lateinit var reminderReportAs:String
    lateinit var reminderDatabase:ReminderDatabase

    lateinit var trafficSwitch: SwitchMaterial
    lateinit var weatherSwitch: SwitchMaterial
    lateinit var alarmMapLayout:LinearLayout
    lateinit var alarmWeather: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isDeviceLocked(this)){
            val wind = this.getWindow();

            wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            wind.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
        activityAlarmBinding = DataBindingUtil.setContentView(this,R.layout.activity_alarm)
        initAll()

       /* trafficSwitch=findViewById(R.id.switchTrafficMode)
        weatherSwitch=findViewById(R.id.switchWeatherMode)
        alarmMapLayout=findViewById(R.id.LLAlarmMap)
        alarmWeather=findViewById(R.id.RLAlarmWeather)

        trafficSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (trafficSwitch.isChecked){
                Log.d("TAG", "This is the status after checking"+trafficSwitch.isChecked)
                alarmMapLayout.visibility = View.VISIBLE
            }else{
                alarmMapLayout.visibility = View.GONE
            }
        }

        weatherSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (weatherSwitch.isChecked){
                Log.d("TAG", "This is the status after checking"+weatherSwitch.isChecked)
                alarmWeather.visibility = View.VISIBLE
            }else{
                alarmWeather.visibility = View.GONE
            }
        }*/

    }

    override fun onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onDestroy()
    }
    private fun initAll() {

        supportActionBar?.hide()

            mySharedPreferences = MySharedPreferences(this)
            reminderDatabase = ReminderDatabase.invoke(this)
            reminderTitle = mySharedPreferences.getReminderTitle()!!
            val calendar = Calendar.getInstance()
            reminderMillies = calendar.timeInMillis
            reminderAlarmId = mySharedPreferences.getAlarmId()!!.toInt()
            reminderReportAs = mySharedPreferences.getReportAs()!!
            activityAlarmBinding.tvTitle.text = reminderTitle
            activityAlarmBinding.tvTime.text = simpleDateFormat.format(Date(reminderMillies))

            try {
                ringtonUri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                r = RingtoneManager.getRingtone(
                    this,
                    ringtonUri
                )
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            activityAlarmBinding.tvDismiss.setOnClickListener { v ->
                activityAlarmBinding.tvDismiss.visibility = View.INVISIBLE
                activityAlarmBinding.tvHoldTheButton.visibility = View.VISIBLE

            }

            activityAlarmBinding.tvHoldTheButton.setOnLongClickListener { v ->
                r.stop()
                finish()
                return@setOnLongClickListener true
            }

            activityAlarmBinding.tvSnooze.setOnClickListener { view ->
                onScheduleAlarmFor10Minutes(
                    reminderMillies,
                    reminderTitle,
                    reminderAlarmId,
                    reminderReportAs
                )
                r.stop()
                toast("Alarm is set for next 10 minutes")
                finish()
            }


    }




}