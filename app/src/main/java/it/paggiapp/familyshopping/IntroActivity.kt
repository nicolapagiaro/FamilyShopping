package it.paggiapp.familyshopping

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import it.paggiapp.familyshopping.util.Util

class IntroActivity : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * If the user il already logged in then go to main activity
         */
        if(Util.isUserLogged(applicationContext)) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        /**
         * Hide the button back to avoid SKIP function
         */
        isButtonBackVisible = false


        /**
         * First slide: presentation
         */
        addSlide(SimpleSlide.Builder()
                .title(R.string.intro_title1)
                .description(R.string.intro_descr1)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image)
                .scrollable(false)
                .build())


        /**
         * Second slide: register/login user
         */
        addSlide(FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(LoginFragment.newInstance())
                .build())

        /**
         * Third slide:
         */
        addSlide(SimpleSlide.Builder()
                .title(R.string.intro_title1)
                .description(R.string.intro_descr1)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .image(R.drawable.intro_image)
                .scrollable(false)
                .build())

        /**
         * Set the navigation policy for the slide
         */
        setNavigationPolicy(object: NavigationPolicy {
            override fun canGoForward(p0: Int): Boolean {
                if(p0 == 1) {
                    return false
                }
                return true
            }

            override fun canGoBackward(p0: Int): Boolean {
                return true
            }

        })

    }
}
