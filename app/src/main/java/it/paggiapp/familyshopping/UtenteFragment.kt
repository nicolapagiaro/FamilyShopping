package it.paggiapp.familyshopping

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.members_list.view.*

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

        // prendo le info dell'utente
        val utente = Util.getUser(context!!)

        // prendo i riferimenti delle view
        val username = view.findViewById<TextView>(R.id.tv_utente_name)
        val mail = view.findViewById<TextView>(R.id.tv_utente_mail)
        val familyName = view.findViewById<TextView>(R.id.tv_family_nome)
        val familyCode = view.findViewById<TextView>(R.id.tv_family_code)
        val membersContainer = view.findViewById<LinearLayout>(R.id.ll_members)

        // riempio e view
        username.text = utente.nome
        mail.text = utente.email
        familyName.text = utente.nomeFamiglia
        familyCode.text = utente.codiceFamiglia.toString()

        // mostro la lista dei membri della famiglia
        val members = DataStore.getDB().getUtenti()
        members.forEach {
            if(it.id != utente.id) {
                val v = LayoutInflater.from(context)
                        .inflate(R.layout.members_list, membersContainer, false)
                v.text1.text = it.nome
                v.text2.text = it.email
                membersContainer.addView(v)
            }
        }

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