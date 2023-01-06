package kr.co.simplebestapp.LolApiTest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.simplebestapp.LolApiTest.fragments.GoldFragment
import kr.co.simplebestapp.LolApiTest.fragments.KDAFragment
import kr.co.simplebestapp.LolApiTest.fragments.MinionFragment
import kr.co.simplebestapp.LolApiTest.fragments.WardFragment

class ViewPagerAdapter(fa : FragmentActivity, var count : Int) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int  = count

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> KDAFragment()
            1 -> MinionFragment()
            2 -> GoldFragment()
            else -> WardFragment()
        }
    }
}