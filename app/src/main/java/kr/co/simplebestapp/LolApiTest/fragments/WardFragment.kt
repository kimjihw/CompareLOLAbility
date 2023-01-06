package kr.co.simplebestapp.LolApiTest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.simplebestapp.LolApiTest.R
import kr.co.simplebestapp.LolApiTest.databinding.FragmentWardBinding


class WardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding by lazy{FragmentWardBinding.inflate(inflater, container, false)}
        return binding.root
    }


}