package com.lazysecs.corgilearnsenglish.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieCompositionFactory
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.navigation.SupportFragmentNavigator
import com.lazysecs.corgilearnsenglish.ui.learn.LearnFragment
import com.lazysecs.corgilearnsenglish.ui.word_list.WordListFragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val fragmentContainerId: Int
        get() = R.id.cl_fragment_root

    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        preloadLotti()
        start()
    }

    private fun preloadLotti() {
        LottieCompositionFactory.fromRawRes(this, R.raw.celebration)
        LottieCompositionFactory.fromRawRes(this, R.raw.drinking_tea)
        LottieCompositionFactory.fromRawRes(this, R.raw.hello)
        LottieCompositionFactory.fromRawRes(this, R.raw.lick)
        LottieCompositionFactory.fromRawRes(this, R.raw.nothing_found)
        LottieCompositionFactory.fromRawRes(this, R.raw.sad)
    }

    fun hideSoftwareKeyboard(view: View?) {
        if (view != null) {
            val imm =
                applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showSoftwareKeyboard(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(
            view?.applicationWindowToken,
            InputMethodManager.SHOW_IMPLICIT,
            0
        )
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(this)
            .appComponent
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(SupportFragmentNavigator(this, fragmentContainerId))
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(
            fragmentContainerId
        )
        when (fragment) {
            is WordListFragment -> {
                if (backPressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed()
                    return
                } else {
                    Toast.makeText(
                        this,
                        R.string.press_again_to_exit,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressed = System.currentTimeMillis()
            }
            is LearnFragment -> {
                if (fragment.finishLearning) {
                    super.onBackPressed()
                    return
                } else {
                    fragment.showFinishLearningConfirmationDialog()
                }
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun start() {
        val fragment: Fragment = WordListFragment.newInstance()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.cl_fragment_root, fragment).commit()
    }
}