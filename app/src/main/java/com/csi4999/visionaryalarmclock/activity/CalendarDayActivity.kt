package com.csi4999.visionaryalarmclock.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.adapter.ReminderListAdapter
import com.csi4999.visionaryalarmclock.database.MySharedPreferences
import com.csi4999.visionaryalarmclock.database.ReminderDatabase
import com.csi4999.visionaryalarmclock.databinding.ActivityCalendarDayBinding
import com.csi4999.visionaryalarmclock.databinding.AlarmSettingDialogBinding
import com.csi4999.visionaryalarmclock.model.AddNewReminderTable
import com.csi4999.visionaryalarmclock.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class CalendarDayActivity : AppCompatActivity() {

    lateinit var activityCalendarDayBinding: ActivityCalendarDayBinding
    private var postPoneValue: String = FIFTEEN_MINUTES
    private lateinit var remindeLisAdapter: ReminderListAdapter
    lateinit var reminderDatabase: ReminderDatabase
    lateinit var mySharedPreferences: MySharedPreferences
    private val reminderTableList: ArrayList<AddNewReminderTable> = ArrayList<AddNewReminderTable>()
    var tag = CalendarDayActivity::class.simpleName
    lateinit var calDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCalendarDayBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_calendar_day)
        initAll()
    }

    private fun initAll() {


        calDate = intent.getStringExtra(CALENDAR_DATE).toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(calDate)

        reminderDatabase = ReminderDatabase.invoke(this)
        mySharedPreferences = MySharedPreferences(this);
        remindeLisAdapter = ReminderListAdapter(
            this, reminderTableList,
            object : ReminderListAdapter.OnClickListner {
                override fun onStarClick(position: Int) {
                    var addNewReminderTable = reminderTableList.get(position)
                    updateReminder(addNewReminderTable)
                }

                override fun onDotsClick(position: Int) {
                    var addNewReminderTable = reminderTableList.get(position)
                    openBottomSheetDialog(addNewReminderTable)
                }

                override fun onMobileClick(position: Int) {
                    var phone = reminderTableList.get(position).title
                    openDailer(phone!!.substring(5, phone!!.length))
                }

                override fun onItemClick(position: Int) {
                    onEditButtonClicked(reminderTableList.get(position))
                }


            }, mySharedPreferences
        )
        val layoutManager = LinearLayoutManager(this)

        activityCalendarDayBinding.rvCalendarDayReminderList.layoutManager = layoutManager
        activityCalendarDayBinding.rvCalendarDayReminderList.itemAnimator = DefaultItemAnimator()
        activityCalendarDayBinding.rvCalendarDayReminderList.adapter = remindeLisAdapter

        getReminderList(calDate)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getReminderList(calDate: String) {
        lifecycleScope.launch {
            var reminderTable: List<AddNewReminderTable>? = null
            withContext(Dispatchers.IO) {
                reminderTable = reminderDatabase.addNewReminderDao().fetchDateWiseReminder(calDate)
            }
            reminderTableList.clear()
            for (i in reminderTable!!) {
                if (calDate.equals(i.date)) {
                    reminderTableList.add(i)
                }
            }
            remindeLisAdapter.notifyDataSetChanged()

        }
    }

    private fun updateReminder(addNewReminderTable: AddNewReminderTable) {

        lifecycleScope.launch {
            var isFav: Boolean
            if (addNewReminderTable.isFav) {
                isFav = false
            } else {
                isFav = true
            }
            addNewReminderTable.isFav = isFav
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
                if (long > 0) {
                    getReminderList(calDate)
                } else {
                    toast("Reminder Added Failed")
                }
            }
        }

    }

    private fun openBottomSheetDialog(addNewReminderTable: AddNewReminderTable) {
        val bottomSheetDialog =
            BottomSheetDialog(this@CalendarDayActivity)
        val bottomSheetAllReminderDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(bottomSheetDialog.getContext()),
                R.layout.alarm_setting_dialog,
                null,
                false
            ) as AlarmSettingDialogBinding
        bottomSheetDialog.setContentView(bottomSheetAllReminderDialogBinding.root)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        BottomSheetBehavior.from(bottomSheetAllReminderDialogBinding.root.parent as View).peekHeight =
            1000

        bottomSheetAllReminderDialogBinding.llEdit.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            onEditButtonClicked(addNewReminderTable)
        }

        bottomSheetAllReminderDialogBinding.llDelete.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            onDeleteButtonClicked(addNewReminderTable)
        }






        bottomSheetAllReminderDialogBinding.llPostPone.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            onPostPoneButtonClicked(addNewReminderTable)
        }

        bottomSheetAllReminderDialogBinding.llDone.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            addReminderToDone(addNewReminderTable)
        }
        bottomSheetDialog.show()
    }

    private fun onEditButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val intent = Intent(this, AddNewReminder::class.java)
        intent.putExtra(ADD_REMINDER_TABLE, addNewReminderTable)
        startActivity(intent)
    }


    private fun onDeleteButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this@CalendarDayActivity)
        materialDialogBuilder.setTitle("Delete the task")
        materialDialogBuilder.setMessage("Are you sure you want to delete the task ?")
        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Delete") { dialog, which ->
            lifecycleScope.launch {
                var long: Int = 0
                withContext(Dispatchers.IO) {
                    long = reminderDatabase.addNewReminderDao().deleteData(addNewReminderTable)
                }
                if (long > 0) {
                    onCancelAlarm(addNewReminderTable.alarmId)
                    getReminderList(calDate)
                } else {
                    toast("Delete Fail")
                }
            }
        }
        materialDialogBuilder.show()

    }

    private fun addReminderToDone(addNewReminderTable: AddNewReminderTable) {

        lifecycleScope.launch {
            addNewReminderTable.isDone = true
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
            }
            if (long > 0) {
                toast("Marked as Done")
                getReminderList(calDate)
            } else {
                toast("Reminder Added Failed")
            }

        }

    }

    private fun onPostPoneButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this@CalendarDayActivity)
        materialDialogBuilder.setTitle("PostPone")
        val singleItems = arrayOf(
            FIFTEEN_MINUTES,
            THIRTY_MINUTES,
            ONE_HOUR,
            TOMORROW_AT_10,
            TOMORROW_AT_14,
            TOMORROW_AT_18
        );
        var checkedItem = 0
        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Postpone") { dialog, which ->
            updatePostponeReminder(addNewReminderTable, postPoneValue)
        }
        materialDialogBuilder.setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
            // Respond to item chosen
            postPoneValue = singleItems[which]
        }
        materialDialogBuilder.show()
    }

    private fun updatePostponeReminder(
        addNewReminderTable: AddNewReminderTable,
        postPoneValue: String
    ) {
        var dateTime =
            dateTimeFormat.parse(addNewReminderTable.date + " " + addNewReminderTable.time)
        var postponeMillies: Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateTime.time
        calendar.add(Calendar.DATE, 1)
        val tomorrowDate = dateFormat.format(calendar.timeInMillis)
        if (postPoneValue.equals(FIFTEEN_MINUTES)) {
            postponeMillies = dateTime.time + INTERVAL_FIFTEEN_MINUTES
            Log.d(tag, "updatePostponeReminder: FIFTEEN_MINUTES" + postponeMillies)
        } else if (postPoneValue.equals(THIRTY_MINUTES)) {
            postponeMillies = dateTime.time + INTERVAL_THIRTY_MINUTES
            Log.d(tag, "updatePostponeReminder:THIRTY_MINUTES " + postponeMillies)
        } else if (postPoneValue.equals(ONE_HOUR)) {
            postponeMillies = dateTime.time + INTERVAL_ONE_HOUR
            Log.d(tag, "updatePostponeReminder: " + postponeMillies)
        } else if (postPoneValue.equals(TOMORROW_AT_10)) {
            dateTime = dateTime24HourFormat.parse(tomorrowDate + " " + "10:00")
            postponeMillies = dateTime.time
            Log.d(tag, "updatePostponeReminder: " + dateFormat.format(calendar.timeInMillis))
        } else if (postPoneValue.equals(TOMORROW_AT_14)) {
            dateTime = dateTime24HourFormat.parse(tomorrowDate + " " + "14:00")
            postponeMillies = dateTime.time
        } else {
            dateTime = dateTime24HourFormat.parse(tomorrowDate + " " + "18:00")
            postponeMillies = dateTime.time
        }
        val date = dateFormat.format(postponeMillies)
        val time = simpleDateFormat.format(postponeMillies)
        lifecycleScope.launch {
            val reminderTitle = addNewReminderTable.title
            val reminderAlarmId = addNewReminderTable.alarmId
            val reminderReportAs = addNewReminderTable.reportAs
            addNewReminderTable.date = date
            addNewReminderTable.time = time
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
            }
            if (long > 0) {
                if (System.currentTimeMillis() < postponeMillies) {
                    onSchedulAlarm(
                        postponeMillies.toLong(),
                        reminderTitle!!,
                        addNewReminderTable.repeat!!,
                        reminderAlarmId.toInt(),
                        reminderReportAs!!
                    )
                    toast("Postponed by " + postPoneValue)
                } else {
                    toast("Postponed by .")
                }
                Log.d(tag, "saveReminder: " + postponeMillies)
                getReminderList(calDate)
            } else {
                toast("Reminder Updated Failed")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)

        return super.onCreateOptionsMenu(menu)
    }

}