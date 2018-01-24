package it.paggiapp.familyshopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heinrichreimersoftware.materialintro.app.SlideFragment

/**
 * Created by nicola on 23/01/18.
 */
class LoginFragment : SlideFragment() {

    companion object {

        /**
         * Gives a new instance of the fragment
         */
        fun newInstance() : LoginFragment {
            return LoginFragment()
        }
    }


    /**
     * onCreate function
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_intro_login, container, false)
    }

    /**
     * onActivityCreated function
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}