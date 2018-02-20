package it.paggiapp.familyshopping

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Class that implements the fragment to show user information
 * Created by nicola on 08/02/18.
 */
class UtenteFragment : Fragment(), GeneralFragment{

    companion object {

        /**
         * Static method for new Instance
         */
        fun newInstance(): UtenteFragment {
            return UtenteFragment()
        }
    }

    /**
     * Creates the view displayed
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_utente, container, false)
        return view
    }

    /**
     * Function to scroll to the top
     */
    override fun scrollToTop() {}

    /**
     * Unused but has to implement due the interface
     */
    override fun refreshList() {}
}