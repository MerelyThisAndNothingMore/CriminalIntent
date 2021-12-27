package com.example.criminalintent.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import com.example.criminalintent.util.PictureUtils
import com.example.criminalintent.viewmodel.CrimeDetailViewModel
import java.io.File
import java.util.*

/****
 * @author : zhangjin.rolling
 * @date : 2021/10/20
 */

class CrimeFragment : Fragment() {

    companion object {

        private const val ARG_CRIME_ID = "crime_id"
        private const val DATE_PICKER_DIALOG = "date_picker_dialog"
        private const val DATE_PICKER_REQUEST_KEY = "date_picker_request"
        private const val DATE_FORMAT = "EEE, MMM, dd"

        fun newInstance(crimeId: UUID): CrimeFragment {
            return CrimeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CRIME_ID, crimeId)
                }
            }
        }
    }

    private var mCrime = Crime()
    private lateinit var mPhotoFile: File
    private lateinit var mPhotoUri: Uri

    private val crimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    private val mRegister = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let { uri ->
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = requireActivity().contentResolver.query(uri, queryFields, null, null, null)
                cursor?.use { ACursor ->
                    if (ACursor.count == 0) {
                        return@let
                    }
                    ACursor.moveToFirst()
                    val suspect = ACursor.getString(0)
                    mCrime.suspect = suspect
                    crimeDetailViewModel.saveCrime(mCrime)
                    updateUI()
                }
            }
        }
    }

    private val mPhotoRegister = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let { resultUri ->
                requireActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updateUI()
            }
        }
    }

    private var mCrimeTitleEdit: EditText? = null
    private var mCrimeData: Button? = null
    private var mCrimeSolved: CheckBox? = null
    private var mReportButton: Button? = null
    private var mPickSuspectButton: Button? = null
    private var mPictureView: ImageView? = null
    private var mCameraButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        mCrimeTitleEdit = view.findViewById(R.id.crime_title_edit)
        mCrimeData = view.findViewById(R.id.crime_date)
        mCrimeSolved = view.findViewById(R.id.crime_solved)
        mReportButton = view.findViewById(R.id.crime_report)
        mPickSuspectButton = view.findViewById(R.id.crime_suspect)
        mPictureView = view.findViewById(R.id.crime_picture)
        mCameraButton = view.findViewById(R.id.crime_camera_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                mCrime = crime
                mPhotoFile = crimeDetailViewModel.getPhotoFile(crime)
                mPhotoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.example.criminalintent.fileprovider",
                    mPhotoFile
                )
                updateUI()
            }
        }
    }

    private fun updateUI() {
        mCrimeTitleEdit?.setText(mCrime.title)
        mCrimeData?.text = mCrime.date.toString()
        mCrimeSolved?.apply {
            isChecked = mCrime.isSolved
            jumpDrawablesToCurrentState()
        }
        if (mCrime.suspect.isNotEmpty()) {
            mPickSuspectButton?.text = mCrime.suspect
        }
        if (mPhotoFile.exists()) {
            mPictureView?.setImageBitmap(PictureUtils.getScaledBitmap(mPhotoFile.path, requireActivity()))
        } else {
            mPictureView?.setImageDrawable(null)
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (mCrime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, mCrime.date).toString()
        val suspect = if (mCrime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect)
        }

        return getString(R.string.crime_report, mCrime.title, dateString, solvedString, suspect)

    }

    override fun onStart() {
        super.onStart()

        mCrimeTitleEdit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

        })

        mCrimeSolved?.setOnCheckedChangeListener { _, isChecked ->
            mCrime.isSolved = isChecked
        }

        mCrimeData?.setOnClickListener {
            DatePickerFragment.newInstance(mCrime.date, DATE_PICKER_REQUEST_KEY).apply {
                this@CrimeFragment.parentFragmentManager.setFragmentResultListener(
                    DATE_PICKER_REQUEST_KEY,
                    this@CrimeFragment) { _, result ->
                    mCrime.date = result.getSerializable(DatePickerFragment.REQUEST_DATE_KEY) as Date
                    updateUI()
                }
                show(this@CrimeFragment.parentFragmentManager, DATE_PICKER_DIALOG)
            }
        }

        mReportButton?.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.let { intent ->
                startActivity(Intent.createChooser(intent, getString(R.string.send_report)))
            }
        }

        mPickSuspectButton?.setOnClickListener {
            mRegister.launch(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI))
        }

        mCameraButton?.setOnClickListener {
            val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
            }
            requireActivity().packageManager.queryIntentActivities(photoIntent, PackageManager.MATCH_DEFAULT_ONLY)
                .forEach {
                    requireActivity().grantUriPermission(
                        it.activityInfo.packageName,
                        mPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
            mPhotoRegister.launch(photoIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(mCrime)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
}