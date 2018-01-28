package it.paggiapp.familyshopping

import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import it.paggiapp.familyshopping.util.Util

class IntroActivity : IntroActivity() {
    private var savedFamilyCode : Boolean = false
    private var isNewUser : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
         * Third slide: welcome/welcome back
         */
        addSlide(FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(FinalFragment.newInstance())
                .build())

        /**
         * Set the navigation policy for the slide
         */
        setNavigationPolicy(object: NavigationPolicy {
            override fun canGoForward(p0: Int): Boolean {
                if(p0 == 1) {
                    return Util.isUserLogged(applicationContext)
                }
                else if(p0 == 2) {
                    return savedFamilyCode
                }

                return true
            }

            override fun canGoBackward(p0: Int): Boolean {
                return p0 != 0
            }

        })

    }

    fun setSavedFamilyCode(state : Boolean) {
        savedFamilyCode = state
    }

    fun setIsNewUser(state : Boolean) {
        isNewUser = state
    }

    fun getIsNewuser() : Boolean {return isNewUser}
}
