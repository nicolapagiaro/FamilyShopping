package it.paggiapp.familyshopping

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heinrichreimersoftware.materialintro.app.SlideFragment
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.fragment_intro_final.*

/**
 * Created by nicola on 28/01/18.
 */
class FinalFragment : SlideFragment() {

    companion object {
        /**
         * Gives a new instance of the fragment
         */
        fun newInstance() : FinalFragment {
            return FinalFragment()
        }
    }

    /**
     * onCreate function
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_intro_final, container, false)
    }

    /**
     * Function
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(!Util.isNewUser(context)) {
            // retrieve old user info
            val utente = Util.getUser(context)

            tv_loginfinal_title.text = getString(R.string.intro_title2_old)
            tv_loginfinal_subtitle.text = getString(R.string.intro_subtitle2_old)
            //et_familycode.setText(utente.codiceFamiglia)
        }

        // button SAVE click listener
        btn_save.setOnClickListener {
            //(introActivity as IntroActivity).setSavedFamilyCode(true)
        }
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Log.d("LastFrag", "attached")
    }
}