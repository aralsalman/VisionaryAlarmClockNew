package com.csi4999.visionaryalarmclock.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.activity.AddNewReminder
import com.csi4999.visionaryalarmclock.activity.CalendarDayActivity
import com.csi4999.visionaryalarmclock.database.ReminderDatabase
import com.csi4999.visionaryalarmclock.databinding.FragmentCalendarBinding
import com.csi4999.visionaryalarmclock.model.AddNewReminderTable
import com.csi4999.visionaryalarmclock.util.CALENDAR_DATE
import com.csi4999.visionaryalarmclock.util.dateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() {

    private lateinit var fragmentCalendarBinding: FragmentCalendarBinding
    lateinit var reminderDatabase: ReminderDatabase
    var eventDay = ArrayList<EventDay>()
    var calendarList = ArrayList<Calendar>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCalendarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )
        initAll()
        return fragmentCalendarBinding.root
    }

    private fun initAll() {
        reminderDatabase = ReminderDatabase.invoke(requireContext())

        getReminderList()

        fragmentCalendarBinding.fabAddReminder.setOnClickListener {
            val intent = Intent(context, AddNewReminder::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        fragmentCalendarBinding.calendarView.setOnDayClickListener(OnDayClickListener { eventDay ->
            var calendarDay = eventDay.calendar
            for (i in calendarList) {
                if (i.equals(calendarDay)) {
                    var calDate = dateFormat.format(calendarDay.time)
                    val intent = Intent(context, CalendarDayActivity::class.java)
                    intent.putExtra(CALENDAR_DATE, calDate)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

        })

    }

    private fun getReminderList() {
        lifecycleScope.launch {
            var reminderTable: List<AddNewReminderTable>? = null
            withContext(Dispatchers.IO) {
                reminderTable = reminderDatabase.addNewReminderDao().fetchAllData()
            }
            for (i in reminderTable!!) {
                var calendar = Calendar.getInstance()
                var date = dateFormat.parse(i.date)
                calendar.time = date
                calendarList.add(calendar)
                eventDay.add(
                    EventDay(
                        calendar, R.drawable.ic_baseline_alarm_24, Color.parseColor(
                            "#FD6262"
                        )
                    )
                )
            }
            fragmentCalendarBinding.calendarView.setEvents(eventDay)


        }
    }

    override fun onResume() {
        getReminderList()
        super.onResume()
    }
}