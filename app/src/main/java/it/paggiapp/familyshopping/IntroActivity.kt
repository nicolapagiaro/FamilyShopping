package it.paggiapp.familyshopping

import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide




class IntroActivity : IntroActivity() {

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

    }
}
