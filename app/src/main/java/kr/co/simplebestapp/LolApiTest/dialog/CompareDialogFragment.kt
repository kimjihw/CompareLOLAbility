package kr.co.simplebestapp.LolApiTest.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import kr.co.simplebestapp.LolApiTest.CompareActivity
import kr.co.simplebestapp.LolApiTest.R
import kr.co.simplebestapp.LolApiTest.databinding.FragmentCompareDialogBinding

class CompareDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentCompareDialogBinding
    private lateinit var alertDialog: AlertDialog

    private lateinit var compareNameEt : EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCompareDialogBinding.inflate(LayoutInflater.from(requireContext()))

        alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            .setView(binding.root)
            .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(requireContext(), CompareActivity::class.java)
                val searchName : Parcelable = arguments?.getParcelable("searchName")!!
                val searchTier : Parcelable = arguments?.getParcelable("searchTier")!!
                val avg = arguments?.getStringArrayList("searchAvg")
                intent.putExtra("compareName", compareNameEt.text.toString())
                intent.putExtra("searchName", searchName)
                intent.putExtra("searchTier", searchTier)
                intent.putExtra("searchAvg", avg)
                startActivity(intent)
                dialog.dismiss()
            })

            .create()

        return alertDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        compareNameEt = binding.inputCompareNameEt
        return binding.root
    }
}