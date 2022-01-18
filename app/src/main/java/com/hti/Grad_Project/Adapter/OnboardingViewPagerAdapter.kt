package com.hti.Grad_Project.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hti.Grad_Project.R
import com.hti.Grad_Project.fragments.OnboardingFragment



class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context):
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                "Ask any question you want",
                "With our app you can upload any pdf or book and ask any questions you want and using Ai you will got the answer from you pdf",
                R.drawable.vector_ask_question
            )
            1 -> OnboardingFragment.newInstance(
                "Choose Ai model you want",
                "You can choose how the app will answer your questions,\nif you want an faster response you can choose DistlBert,\n also if you want deeper answer you could use Bert",
                R.drawable.vector_distlbert_or_bert
            )
            else -> OnboardingFragment.newInstance(
                "Ask your question using Text Recognition",
                "If you want to take a photo to any text and ask the question you want on this text",
                R.drawable.vector_ocr
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}