package com.csi4999.visionaryalarmclock.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.database.ReminderDatabase
import com.csi4999.visionaryalarmclock.databinding.ActivityAddNewReminderBinding
//import com.csi4999.visionaryalarmclock.databinding.DialogMobileNoBinding
import com.csi4999.visionaryalarmclock.model.AddNewReminderTable
import com.csi4999.visionaryalarmclock.util.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class AddNewReminder : AppCompatActivity(), View.OnClickListener {

    lateinit var selectedDate: Date
    lateinit var selectedTime: Date
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var simpleTimeFormat24: SimpleDateFormat
    lateinit var simpleTimeFormat12: SimpleDateFormat
    lateinit var selectedRepeat: String
    lateinit var selectedReportAs: String
    lateinit var binder: ActivityAddNewReminderBinding
    lateinit var reminderDatabase: ReminderDatabase
    lateinit var marker: String
    lateinit var addNewReminderTable: AddNewReminderTable

    lateinit var trafficSwitch: SwitchMaterial
    lateinit var startLayout: LinearLayout
    lateinit var destLayout: LinearLayout

    var tag = AddNewReminder::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = DataBindingUtil.setContentView(this, R.layout.activity_add_new_reminder)
        initAllControls()

        trafficSwitch=findViewById(R.id.switchTrafficMode)
        startLayout=findViewById(R.id.startPointLayout)
        destLayout=findViewById(R.id.destPointLayout)

        Log.d("TAG", "This is the status before checking"+trafficSwitch.isChecked)
        trafficSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (trafficSwitch.isChecked){
                Log.d("TAG", "This is the status after checking"+trafficSwitch.isChecked)
                startLayout.visibility = View.VISIBLE
                destLayout.visibility = View.VISIBLE
            }else{
                startLayout.visibility = View.GONE
                destLayout.visibility = View.GONE
            }
        }



        //var trafficSwitch = findViewById(R.id.switchTrafficMode) as SwitchMaterial
        //var trafficSwitch = findViewById<Switch>(R.id.switchTrafficMode)
        //var startLayout = findViewById<LinearLayout>(R.id.startPointLayout)
        //var destLayout = findViewById<LinearLayout>(R.id.destPointLayout)




    }



    private fun initAllControls() {
        reminderDatabase = ReminderDatabase.invoke(this@AddNewReminder)
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        simpleTimeFormat24 = SimpleDateFormat("HH:mm")
        simpleTimeFormat12 = SimpleDateFormat("hh:mm a")
        supportActionBar?.hide()
        binder.llAddRemiderDate.setOnClickListener(this)
        binder.llAddRemiderTime.setOnClickListener(this)
        binder.llAddRemiderRepeat.setOnClickListener(this)
        binder.llAddRemiderMarker.setOnClickListener(this)
        binder.llAddRemiderReportAs.setOnClickListener(this)
        binder.llTrafficAlert.setOnClickListener(this)
        binder.llWeatherAlert.setOnClickListener(this)
        binder.btnSaveReminder.setOnClickListener(this)
        binder.ivBack.setOnClickListener(this)

        /*trafficSwitch=findViewById(R.id.switchTrafficMode)
        startLayout=findViewById(R.id.startPointLayout)
        destLayout=findViewById(R.id.destPointLayout)
        Log.d("TAG", "This is the status before checking"+trafficSwitch.isChecked)
        if (trafficSwitch.isChecked){
            Log.d("TAG", "This is the status after checking"+trafficSwitch.isChecked)
            startLayout.visibility = View.VISIBLE
            destLayout.visibility = View.VISIBLE
        }else{
            startLayout.visibility = View.GONE
            destLayout.visibility = View.GONE
        }*/
        /*
       * set default values
       * */
        if (intent.hasExtra(ADD_REMINDER_TABLE)) {
            addNewReminderTable =
                intent.getParcelableExtra(ADD_REMINDER_TABLE)!!
            binder.etRemiderTitle.text =
                Editable.Factory.getInstance().newEditable(addNewReminderTable.title)
            selectedDate = simpleDateFormat.parse(addNewReminderTable.date)
            binder.tvAddNewRemiderDate.setText("${simpleDateFormat.format(selectedDate)}")
            selectedTime = simpleTimeFormat12.parse(addNewReminderTable.time!!)
            binder.tvAddNewRemiderTime.setText("${simpleTimeFormat12.format(selectedTime)}")
            selectedRepeat = addNewReminderTable.repeat!!
            binder.tvAddNewRemiderRepeat.text = selectedRepeat
            selectedReportAs = addNewReminderTable.reportAs!!
            binder.tvReportAs.text = selectedReportAs
            marker = addNewReminderTable.marker!!
            binder.tvSelectMarker.text = marker
        } else {
            selectedDate = Date()
            binder.tvAddNewRemiderDate.setText("${simpleDateFormat.format(selectedDate)}")
            selectedTime = Date()
            binder.tvAddNewRemiderTime.setText("${simpleTimeFormat12.format(selectedTime)}")
            selectedRepeat = ONCE
            binder.tvAddNewRemiderRepeat.text = selectedRepeat
            selectedReportAs = NOTIFICATION
            binder.tvReportAs.text = selectedReportAs
            marker = "None"
            binder.tvSelectMarker.text = marker
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            binder.llAddRemiderDate.id -> onDateSelectionButtonClicked()
            binder.llAddRemiderTime.id -> onTimeSelectionButtonClicked()
            binder.llAddRemiderRepeat.id -> {
                onRepeatButtonClicked()
            }
            binder.llAddRemiderMarker.id -> {
                markerButtonClicked()
            }
            binder.llAddRemiderReportAs.id -> {
                reportAs()
            }
            binder.btnSaveReminder.id -> {
                onSaveButtonClicked()
            }
            binder.ivBack.id -> {
                onBackPressed()
            }
        }
    }

    private fun openMobileDialog() {
        var mobileNo: String = ""
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this)

        /*val dialogMobileNoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(materialDialogBuilder.getContext()),
            R.layout.dialog_mobile_no,
            null,
            false
        ) as DialogMobileNoBinding*/
        /*materialDialogBuilder.setView(dialogMobileNoBinding.root)*/

        materialDialogBuilder.setTitle("Add Mobile No.")

        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Okay") { dialog, which ->
            // Respond to positive button press

            if (!mobileNo.isEmpty() && !isValidMobile(mobileNo)) {
                toast("enter valid mobile number")
            } else {
                binder.etRemiderTitle.text =
                    Editable.Factory.getInstance().newEditable("Call: " + mobileNo)
            }
        }

        /*dialogMobileNoBinding.etMobileNo.addTextChangedListener {
            mobileNo = it.toString()
        }*/

        materialDialogBuilder.show()
    }

    /**
     * Showing google speech input dialog
     */
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Say Something"
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            toast("Sorry! Your device doesn\\'t support speech input")
        }
    }

    /**
     * Receiving speech input
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binder.etRemiderTitle.text =
                        Editable.Factory.getInstance().newEditable(result!![0])
                }
            }
        }
    }

    private fun onSaveButtonClicked() {
        val remiderTitle = binder.etRemiderTitle.text.toString()
        val reminderDate = binder.tvAddNewRemiderDate.text.toString()
        val reminderTime = binder.tvAddNewRemiderTime.text.toString()
        val reminderRepeat = binder.tvAddNewRemiderRepeat.text.toString()
        val reminderReportAs = binder.tvReportAs.text.toString()
        val reminderMarker = binder.tvSelectMarker.text.toString()

        /*
        * convert selected date time to millies
        * */
        val dateTime = reminderDate.plus(" ").plus(reminderTime)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val date = simpleDateFormat.parse(dateTime)
        val millies = date.time
        val calendar = Calendar.getInstance()

        if (remiderTitle?.trim()!!.isEmpty()) {
            toast("Please add reminder Title")
        } else if (reminderDate?.trim()!!.isEmpty()) {
            toast("Please select reminder date")
        } else if (reminderTime?.trim()!!.isEmpty()) {
            toast("Please select remider time")
        } else if (reminderRepeat?.trim()!!.isEmpty()) {
            toast("Please Select Repeat")
        } else if (reminderReportAs?.trim()!!.isEmpty()) {
            toast("Please select reported as");
        } else if (reminderMarker?.trim()!!.isEmpty()) {
            toast("Please select reported as");
        } else if (millies <= calendar.timeInMillis) {
            println("YOOO $millies");
            println("yoOOO $calendar.timeInMillis")
            toast("Please select future time");
        } else {
            if (intent.hasExtra(ADD_REMINDER_TABLE)) {
                updateReminder(
                    remiderTitle,
                    reminderDate,
                    reminderTime,
                    reminderRepeat,
                    reminderMarker,
                    reminderReportAs,
                    millies
                )
            } else {
                saveReminder(
                    remiderTitle,
                    reminderDate,
                    reminderTime,
                    reminderRepeat,
                    reminderMarker,
                    reminderReportAs,
                    millies
                )
            }

        }
    }


    private fun updateReminder(
        remiderTitle: String,
        reminderDate: String,
        reminderTime: String,
        reminderRepeat: String,
        reminderMarker: String,
        reminderReportAs: String,
        millies: Long
    ) {
        lifecycleScope.launch {

            addNewReminderTable.title = remiderTitle
            addNewReminderTable.date = reminderDate
            addNewReminderTable.time = reminderTime
            addNewReminderTable.repeat = reminderRepeat
            addNewReminderTable.marker = reminderMarker
            /* * if type is notification and it is changed to alarm , cancel alarm is important
             * otherwise not work correctly
            * */
            var alarmId: Int = 0
            val random = Random()
            if (addNewReminderTable.reportAs.equals(NOTIFICATION) && reminderReportAs.equals(ALARM)) {
                onCancelAlarm(addNewReminderTable.alarmId.toInt())
                alarmId = random.nextInt(10000000)
            } else if (addNewReminderTable.reportAs.equals(ALARM) && reminderReportAs.equals(
                    NOTIFICATION
                )
            ) {
                alarmId = random.nextInt(10000000)
                onCancelAlarm(addNewReminderTable.alarmId.toInt())
            } else {
                alarmId = addNewReminderTable.alarmId
            }
            addNewReminderTable.reportAs = reminderReportAs
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
            }
            if (long > 0) {

                onSchedulAlarm(millies, remiderTitle, reminderRepeat, alarmId, reminderReportAs)
                Log.d(tag, "saveReminder: " + millies)

                toast("Reminder Updated Successfully")
                Activity.RESULT_OK
                finish()
            } else {
                toast("Reminder Updated Failed")
            }
        }
    }

    private fun saveReminder(
        remiderTitle: String,
        selectedDate: String,
        selectedTime: String,
        selectedRepeat: String,
        reminderMarker: String,
        selectedReportAs: String,
        millies: Long
    ) {
        lifecycleScope.launch {
            val alarmId = System.currentTimeMillis().toInt()
            val addNewReminder = AddNewReminderTable(
                0,
                remiderTitle,
                selectedDate,
                selectedTime,
                selectedRepeat,
                reminderMarker,
                selectedReportAs,
                "Not Specified",
                false,
                alarmId,
                false
            )
            var long: Long = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().insertAll(addNewReminder)
            }
            if (long > 0) {
                onSchedulAlarm(millies, remiderTitle, selectedRepeat, alarmId, selectedReportAs)
                Log.d(tag, "saveReminder: " + millies)
                Log.d(tag, "saveReminder: alarmid " + alarmId)

                toast("Reminder Added Successfully")
                Activity.RESULT_OK
                finish()
            } else {
                toast("Reminder Added Failed")
            }

        }

    }

    private fun onDateSelectionButtonClicked() {
        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Select a date");
        materialDateBuilder.setSelection(selectedDate.time)
        val materialDatePicker: MaterialDatePicker<*> = materialDateBuilder.build()
        materialDatePicker.show(supportFragmentManager, "date_picker")
        materialDatePicker.addOnPositiveButtonClickListener {
            selectedDate = Date(it.toString().toLong())
            binder.tvAddNewRemiderDate.setText("${simpleDateFormat.format(selectedDate)}")


        }
    }

    private fun onTimeSelectionButtonClicked() {

        val currentTime = simpleTimeFormat24?.format(selectedTime)
        val hour = currentTime.toString().substring(0, currentTime.toString().indexOf(":"))
        val minute = currentTime.toString()
            .substring(currentTime.toString().indexOf(":") + 1, currentTime?.length!!)

        val materialTimeBuilder = MaterialTimePicker.Builder()
        materialTimeBuilder.setTitleText("Select a time")
        materialTimeBuilder.setHour(hour.toInt())
        materialTimeBuilder.setMinute(minute.toInt())
        val materialTimePicker = materialTimeBuilder.build()
        materialTimePicker.show(supportFragmentManager, "time_picker")
        materialTimePicker.addOnPositiveButtonClickListener {
            selectedTime =
                simpleTimeFormat24.parse("${materialTimePicker.hour}:${materialTimePicker.minute}")
            binder.tvAddNewRemiderTime.setText("${simpleTimeFormat12?.format(selectedTime)}")

        }

    }

    private fun onRepeatButtonClicked() {
        val materialDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(this)
        materialDialogBuilder.setTitle("Repeat")
        val singleItems = arrayOf(ONCE, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY);
        var checkedItem = 0
        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Okay") { dialog, which ->
            // Respond to positive button press
        }

        /*
        * default checked item
        * */
        if (selectedRepeat == ONCE) {
            checkedItem = 0
        } else if (selectedRepeat == HOURLY) {
            checkedItem = 1
        } else if (selectedRepeat == DAILY) {
            checkedItem = 2
        } else if (selectedRepeat == WEEKLY) {
            checkedItem = 3
        } else if (selectedRepeat == MONTHLY) {
            checkedItem = 4
        } else {
            checkedItem = 5
        }

        // Single-choice items (initialized with checked item)
        materialDialogBuilder.setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->

            // Respond to item chosen
            selectedRepeat = singleItems[which];
            binder.tvAddNewRemiderRepeat.setText("$selectedRepeat")
        }
        materialDialogBuilder.show()
    }

    private fun markerButtonClicked() {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this)



        materialDialogBuilder.setTitle("Pick a Marker")

        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Okay") { dialog, which ->
            // Respond to positive button press
            binder.tvSelectMarker.text = marker
        }





        materialDialogBuilder.show()
    }

    private fun reportAs() {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this)
        materialDialogBuilder.setTitle("Repeat")
        val singleItems = arrayOf(NOTIFICATION, ALARM);
        var checkedItem = 0

        /*
       * default checked item
       * */
        if (selectedReportAs.equals(NOTIFICATION)) {
            checkedItem = 0
        } else {
            checkedItem = 1
        }

        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Okay") { dialog, which ->
            // Respond to positive button press
        }
        // Single-choice items (initialized with checked item)
        materialDialogBuilder.setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
            // Respond to item chosen
            selectedReportAs = singleItems[which];
            binder.tvReportAs.setText("$selectedReportAs")
        }
        materialDialogBuilder.show()
    }

}