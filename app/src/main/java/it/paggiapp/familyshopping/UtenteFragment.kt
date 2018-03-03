package it.paggiapp.familyshopping

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.members_list.view.*
import kotlinx.android.synthetic.main.password_change_dialog.view.*

/**
 * Class that implements the fragment to show user information
 * Created by nicola on 08/02/18.
 */
class UtenteFragment : Fragment(), GenericFragment {
    lateinit var scrollView: ScrollView

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
        scrollView = view.findViewById(R.id.utente_scroll)
        val username = view.findViewById<TextView>(R.id.tv_utente_name)
        val mail = view.findViewById<TextView>(R.id.tv_utente_mail)
        val pssw = view.findViewById<LinearLayout>(R.id.change_password)
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
            if (it.id != utente.id) {
                val v = LayoutInflater.from(context)
                        .inflate(R.layout.members_list, membersContainer, false)
                v.text1.text = it.nome
                v.text2.text = it.email
                membersContainer.addView(v)
            }
        }

        // click listener per il cambio della password
        pssw.setOnClickListener {
            val v = LayoutInflater.from(context)
                    .inflate(R.layout.password_change_dialog, null, false)
            val pssw1 = v.et_change_pssw1
            val pssw2 = v.et_change_pssw2

            val dialog = AlertDialog.Builder(activity)
                    .setTitle(R.string.dialogo_change_pssw_title)
                    .setView(v)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .create()

            // listener for the dialog
            dialog.setOnShowListener {
                val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val buttonCanc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // button "OK" listener
                button.setOnClickListener {
                    if (validatePassword(pssw1.text.toString(), pssw2.text.toString())) {
                        // change pssw

                        // close dialog
                        dialog.dismiss()

                    } else
                        Toast.makeText(context, R.string.change_pssw_error, Toast.LENGTH_SHORT).show()
                }

                // button "Cancella" listener
                buttonCanc.setOnClickListener {
                    // close dialog
                    dialog.dismiss()
                }
            }

            dialog.show()

            // force to show the keyboard
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        return view
    }

    /**
     * Function that validates the input
     */
    private fun validatePassword(psswText1: String, psswText2: String): Boolean {
        if (psswText1.length <= Util.PSSW_MIN_CHAR) {
            return false
        }

        if (psswText2.length <= Util.PSSW_MIN_CHAR) {
            return false
        }

        return true
    }

    /**
     * Function to scroll to the top
     */
    override fun scrollToTop() {
        scrollView.smoothScrollTo(0, 0)
    }

    /**
     * Unused but has to implement due the interface
     */
    override fun refreshList() {}
}