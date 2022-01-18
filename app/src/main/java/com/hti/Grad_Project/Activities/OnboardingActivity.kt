package com.hti.Grad_Project.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hti.Grad_Project.Activities.Auth.LoginActivity
import com.hti.Grad_Project.Adapter.OnboardingViewPagerAdapter
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.Constants

import kotlinx.android.synthetic.main.activity_onboarding.*
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import com.hti.Grad_Project.Utilities.MainViewModel


class OnboardingActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button
    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("com.hti.Grad_Project.Activities", MODE_PRIVATE)

        if (!prefs.getBoolean("UserFirstTime",true)){
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        setContentView(R.layout.activity_onboarding)


        mViewPager = viewPager
        mViewPager.adapter = OnboardingViewPagerAdapter(this, this)
        mViewPager.offscreenPageLimit = 1
        btnBack = btn_previous_step
        btnNext = btn_next_step
        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    btnBack.visibility = View.INVISIBLE
                } else {
                    btnBack.visibility = View.VISIBLE
                }
                if (position == 2) {
                    btnNext.text = "FINISH"
                } else {
                    btnNext.text = getText(R.string.next)

                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        TabLayoutMediator(pageIndicator, mViewPager) { _, _ -> }.attach()

        btnNext.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                prefs.edit().putBoolean("UserFirstTime", false).commit();
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()

            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }

        btnBack.setOnClickListener {
            if (getItem() == 0) {
                finish()
            } else {
                mViewPager.setCurrentItem(getItem() - 1, true)
            }
        }
    }

    private fun getItem(): Int {
        return mViewPager.currentItem
    }

}
