package com.csi4999.visionaryalarmclock.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.activity.AddNewReminder
import com.csi4999.visionaryalarmclock.adapter.ReminderListAdapter
import com.csi4999.visionaryalarmclock.database.MySharedPreferences
import com.csi4999.visionaryalarmclock.database.ReminderDatabase
import com.csi4999.visionaryalarmclock.databinding.AlarmSettingDialogBinding
import com.csi4999.visionaryalarmclock.databinding.FragmentAllReminderBinding
import com.csi4999.visionaryalarmclock.model.AddNewReminderTable
import com.csi4999.visionaryalarmclock.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class AllReminderFragment : Fragment() {

    private var postPoneValue: String = FIFTEEN_MINUTES
    private lateinit var fragmentAllReminderBinding: FragmentAllReminderBinding
    private lateinit var remindeLisAdapter1: ReminderListAdapter
    private lateinit var remindeLisAdapter2: ReminderListAdapter
    private lateinit var remindeLisAdapter3: ReminderListAdapter
    private lateinit var remindeLisAdapter4: ReminderListAdapter
    lateinit var reminderDatabase: ReminderDatabase
    private val overDueReminderList: ArrayList<AddNewReminderTable> =
        ArrayList()
    private val todayReminderList: ArrayList<AddNewReminderTable> = ArrayList()
    private val tomorrowReminderList: ArrayList<AddNewReminderTable> =
        ArrayList()
    private val upcomingReminderList: ArrayList<AddNewReminderTable> =
        ArrayList()
    lateinit var mySharedPreferences: MySharedPreferences;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllReminderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_all_reminder, container, false)
        initAll()
        return fragmentAllReminderBinding.root
    }

    private fun initAll() {


        reminderDatabase = ReminderDatabase.invoke(requireContext())
        mySharedPreferences = MySharedPreferences(requireContext())
        remindeLisAdapter1 = ReminderListAdapter(
            requireContext(), overDueReminderList,
            object : ReminderListAdapter.OnClickListner {
                override fun onStarClick(position: Int) {
                    val addNewReminderTable = overDueReminderList.get(position)
                    updateReminder(addNewReminderTable)
                }

                override fun onDotsClick(position: Int) {
                    val addNewReminderTable = overDueReminderList.get(position)
                    openBottomSheetDialog(addNewReminderTable)
                }

                override fun onMobileClick(position: Int) {
                    val phone = overDueReminderList[position].title
                    activity?.openDailer(phone!!.substring(5, phone.length))
                }

                override fun onItemClick(position: Int) {
                    onEditButtonClicked(overDueReminderList.get(position))
                }


            }, mySharedPreferences
        )

        fragmentAllReminderBinding.rvOverDueReminderList.itemAnimator = DefaultItemAnimator()
        fragmentAllReminderBinding.rvOverDueReminderList.adapter = remindeLisAdapter1

        remindeLisAdapter2 = ReminderListAdapter(
            requireContext(), todayReminderList,
            object : ReminderListAdapter.OnClickListner {
                override fun onStarClick(position: Int) {
                    val addNewReminderTable = todayReminderList.get(position)
                    updateReminder(addNewReminderTable)
                }

                override fun onDotsClick(position: Int) {
                    val addNewReminderTable = todayReminderList.get(position)
                    openBottomSheetDialog(addNewReminderTable)
                }

                override fun onMobileClick(position: Int) {
                    val phone = todayReminderList.get(position).title
                    activity?.openDailer(phone!!.substring(5, phone.length))
                }

                override fun onItemClick(position: Int) {
                    onEditButtonClicked(todayReminderList.get(position))
                }

            }, mySharedPreferences
        )
        fragmentAllReminderBinding.rvTodayReminderList.itemAnimator = DefaultItemAnimator()
        fragmentAllReminderBinding.rvTodayReminderList.adapter = remindeLisAdapter2

        remindeLisAdapter3 = ReminderListAdapter(
            requireContext(), tomorrowReminderList,
            object : ReminderListAdapter.OnClickListner {
                override fun onStarClick(position: Int) {
                    var addNewReminderTable = tomorrowReminderList.get(position)
                    updateReminder(addNewReminderTable)
                }

                override fun onDotsClick(position: Int) {
                    var addNewReminderTable = tomorrowReminderList.get(position)
                    openBottomSheetDialog(addNewReminderTable)
                }

                override fun onMobileClick(position: Int) {
                    var phone = tomorrowReminderList.get(position).title
                    activity?.openDailer(phone!!.substring(5, phone!!.length))
                }

                override fun onItemClick(position: Int) {
                    onEditButtonClicked(tomorrowReminderList.get(position))
                }

            }, mySharedPreferences
        )

        fragmentAllReminderBinding.rvTomorrowReminderList.itemAnimator = DefaultItemAnimator()
        fragmentAllReminderBinding.rvTomorrowReminderList.adapter = remindeLisAdapter3

        remindeLisAdapter4 = ReminderListAdapter(
            requireContext(), upcomingReminderList,
            object : ReminderListAdapter.OnClickListner {
                override fun onStarClick(position: Int) {
                    var addNewReminderTable = upcomingReminderList.get(position)
                    updateReminder(addNewReminderTable)
                }

                override fun onDotsClick(position: Int) {
                    var addNewReminderTable = upcomingReminderList.get(position)
                    openBottomSheetDialog(addNewReminderTable)
                }

                override fun onMobileClick(position: Int) {
                    val phone = upcomingReminderList.get(position).title
                    activity?.openDailer(phone!!.substring(5, phone!!.length))
                }

                override fun onItemClick(position: Int) {
                    onEditButtonClicked(upcomingReminderList.get(position))
                }

            }, mySharedPreferences
        )

        fragmentAllReminderBinding.rvUpcomingReminderList.itemAnimator = DefaultItemAnimator()
        fragmentAllReminderBinding.rvUpcomingReminderList.adapter = remindeLisAdapter4

        getReminderList()
    }

    private fun openBottomSheetDialog(addNewReminderTable: AddNewReminderTable) {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext())
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

        bottomSheetAllReminderDialogBinding.llPostPone.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            onPostPoneButtonClicked(addNewReminderTable)
        }

        bottomSheetAllReminderDialogBinding.llDelete.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            onDeleteButtonClicked(addNewReminderTable)
        }





        bottomSheetAllReminderDialogBinding.llDone.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            addReminderToDone(addNewReminderTable)
        }
        bottomSheetDialog.show()
    }

    private fun onEditButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val intent = Intent(context, AddNewReminder::class.java)
        intent.putExtra(ADD_REMINDER_TABLE, addNewReminderTable)
        startActivity(intent)
    }

    private fun onDeleteButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(requireContext())
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
                    activity?.onCancelAlarm(addNewReminderTable.alarmId)
                    getReminderList()
                } else {
                    activity?.toast("Delete Fail")
                }


            }
        }
        materialDialogBuilder.show()

    }

    private fun updateReminder(addNewReminderTable: AddNewReminderTable) {

        lifecycleScope.launch {
            var isFav: Boolean = !addNewReminderTable.isFav
            addNewReminderTable.isFav = isFav
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
            }
            if (long > 0) {
                getReminderList()
            } else {
                activity?.toast("Reminder Added Failed")
            }
        }

    }

    private fun addReminderToDone(addNewReminderTable: AddNewReminderTable) {

        lifecycleScope.launch {
            addNewReminderTable.isDone = true
            var long: Int = 0
            withContext(Dispatchers.IO) {
                long = reminderDatabase.addNewReminderDao().updateAll(addNewReminderTable)
            }
            if (long > 0) {
                getReminderList()
            } else {
                activity?.toast("Reminder Added Failed")
            }
        }

    }

    override fun onResume() {
        getReminderList()
        super.onResume()
    }

    @SuppressWarnings("kotlin:S3776")
    //getReminderList() method have cognitive complexity
    private fun getReminderList() {
        lifecycleScope.launch {
            var reminderTable: List<AddNewReminderTable>? = null
            withContext(Dispatchers.Default) {
                reminderTable = reminderDatabase.addNewReminderDao().fetchDoneReminder(false)
            }
            overDueReminderList.clear()
            todayReminderList.clear()
            tomorrowReminderList.clear()
            upcomingReminderList.clear()
            val today = Date()
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 1)
            val tomorrow = cal.time
            for (i in reminderTable!!) {
                if (System.currentTimeMillis() > dateTimeFormat.parse(i.date + " " + i.time).time) {
                    overDueReminderList.add(i)
                }
            }
            for (i in reminderTable!!) {
                if (dateFormat.format(today).equals(i.date)
                    && System.currentTimeMillis() < dateTimeFormat.parse(i.date + " " + i.time).time
                ) {
                    todayReminderList.add(i)
                }
            }
            for (i in reminderTable!!) {
                if (dateFormat.format(tomorrow).equals(i.date)) {
                    tomorrowReminderList.add(i)
                }
            }
            for (i in reminderTable!!) {
                if (System.currentTimeMillis() < dateTimeFormat.parse(i.date + " " + i.time).time
                    && !dateFormat.format(tomorrow).equals(i.date)
                    && !dateFormat.format(today).equals(i.date)
                ) {
                    upcomingReminderList.add(i)
                }
            }
            if (overDueReminderList.isEmpty()) {
                fragmentAllReminderBinding.rvOverDueReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvOverDue.visibility = View.GONE
            } else {
                fragmentAllReminderBinding.rvOverDueReminderList.visibility = View.VISIBLE
                fragmentAllReminderBinding.tvOverDue.visibility = View.VISIBLE
            }
            if (todayReminderList.isEmpty()) {
                fragmentAllReminderBinding.rvTodayReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvToday.visibility = View.GONE
            } else {
                fragmentAllReminderBinding.rvTodayReminderList.visibility = View.VISIBLE
                fragmentAllReminderBinding.tvToday.visibility = View.VISIBLE
            }
            if (tomorrowReminderList.isEmpty()) {
                fragmentAllReminderBinding.rvTomorrowReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvTomorrow.visibility = View.GONE
            } else {
                fragmentAllReminderBinding.rvTomorrowReminderList.visibility = View.VISIBLE
                fragmentAllReminderBinding.tvTomorrow.visibility = View.VISIBLE
            }
            if (upcomingReminderList.isEmpty()) {
                fragmentAllReminderBinding.rvUpcomingReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvUpcoming.visibility = View.GONE
            } else {
                fragmentAllReminderBinding.rvUpcomingReminderList.visibility = View.VISIBLE
                fragmentAllReminderBinding.tvUpcoming.visibility = View.VISIBLE
            }
            if (overDueReminderList.isEmpty() && todayReminderList.isEmpty() && tomorrowReminderList.isEmpty() && upcomingReminderList.isEmpty()) {
                fragmentAllReminderBinding.rvOverDueReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvOverDue.visibility = View.GONE
                fragmentAllReminderBinding.rvTodayReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvToday.visibility = View.GONE
                fragmentAllReminderBinding.rvTomorrowReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvTomorrow.visibility = View.GONE
                fragmentAllReminderBinding.rvUpcomingReminderList.visibility = View.GONE
                fragmentAllReminderBinding.tvUpcoming.visibility = View.GONE

                fragmentAllReminderBinding.llEmptyLayout.visibility = View.VISIBLE
            } else {
                fragmentAllReminderBinding.llEmptyLayout.visibility = View.GONE

            }
            remindeLisAdapter1.notifyDataSetChanged()
            remindeLisAdapter2.notifyDataSetChanged()
            remindeLisAdapter3.notifyDataSetChanged()
            remindeLisAdapter4.notifyDataSetChanged()

        }
    }

    /*
    * on tab swipe
    * refresh page
    * */
    override fun setMenuVisibility(menuVisible: Boolean) {
//        if (menuVisible && activity != null) {
//           childFragmentManager
//                .beginTransaction()
//                .detach(this)
//                .attach(this)
//                .commit()
//        }
        super.setMenuVisibility(menuVisible)
    }

    private fun onPostPoneButtonClicked(addNewReminderTable: AddNewReminderTable) {
        val materialDialogBuilder =
            MaterialAlertDialogBuilder(requireContext())
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
            Log.d(tag, "updatePostponeReminder:FIFTEEN_MINUTES " + postponeMillies)
        } else if (postPoneValue.equals(THIRTY_MINUTES)) {
            postponeMillies = dateTime.time + INTERVAL_THIRTY_MINUTES
            Log.d(tag, "updatePostponeReminder: THIRTY_MINUTES" + postponeMillies)
        } else if (postPoneValue.equals(ONE_HOUR)) {
            postponeMillies = dateTime.time + INTERVAL_ONE_HOUR
            Log.d(tag, "updatePostponeReminder: ONE_HOUR" + postponeMillies)
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
                    activity?.onSchedulAlarm(
                        postponeMillies.toLong(),
                        reminderTitle!!,
                        addNewReminderTable.repeat!!,
                        reminderAlarmId.toInt(),
                        reminderReportAs!!
                    )
                    activity?.toast("Postponed by " + postPoneValue)
                } else {
                    activity?.toast("Postponed by .")
                }
                Log.d(tag, "saveReminder: " + postponeMillies)
                getReminderList()
            } else {
                activity?.toast("Reminder Updated Failed")
            }

        }
    }


}