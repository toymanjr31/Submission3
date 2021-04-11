package com.dicoding.androidprogramming.submission3.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.androidprogramming.submission3.activity.FollowerFragment
import com.dicoding.androidprogramming.submission3.activity.FollowingFragment
import com.dicoding.androidprogramming.submission3.R

class SectionsPagerAdapter (private val mContext: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }



}