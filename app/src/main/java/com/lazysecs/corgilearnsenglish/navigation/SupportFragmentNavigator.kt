package com.lazysecs.corgilearnsenglish.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.ui.learn.LearnFragment
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class SupportFragmentNavigator(activity: FragmentActivity?, containerId: Int) :
    SupportAppNavigator(activity, containerId) {

    override fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) {
        super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction)
        if (nextFragment !is LearnFragment) {
            openFromRight(fragmentTransaction)
        }
    }

    private fun openFromRight(fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_from_right, R.anim.slide_out_to_left,
            R.anim.slide_in_from_left, R.anim.slide_out_to_right
        )
    }
}