package com.example.criminalintent.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/***
 * @author zhangjin.rolling
 * @since Thursday 2021/11/4
 */
class DatePickerFragment : DialogFragment() {

    companion object {
        private const val ARG_DATE_KEY = "date"
        const val REQUEST_DATE_KEY = "request_date"

        fun newInstance(date: Date, requestKey: String): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE_KEY, date)
            }

            return DatePickerFragment().apply {
                arguments = args
                mRequestKey = requestKey
            }
        }
    }

    private var mRequestKey: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance().apply {
            time = arguments?.getSerializable(ARG_DATE_KEY) as Date
        }
        return DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                this.parentFragmentManager.setFragmentResult(mRequestKey, Bundle().apply {
                    putSerializable(
                        REQUEST_DATE_KEY,
                        GregorianCalendar(year, month, dayOfMonth).time
                    )
                })
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

}