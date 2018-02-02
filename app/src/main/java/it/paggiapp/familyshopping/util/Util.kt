package it.paggiapp.familyshopping.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Utente

/**
 * Class for some important functions
 * Created by nicola on 21/01/18.
 */
class Util {
    companion object {
        val PSSW_MIN_CHAR = 5

        /**
         * Function to check if there is connection
         */
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        /**
         * Function used to know if the user is logged or no
         */
        fun isUserLogged(context: Context) : Boolean{
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            return pref!!.getBoolean(context.getString(R.string.user_logged), false)
        }

        /**
         * Function used to set the user loggedIn Flag
         */
        fun loginUser(context: Context) {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            val editor : SharedPreferences.Editor? = pref?.edit()
            editor?.putBoolean(context.getString(R.string.user_logged), true)
            editor?.apply()
        }

        /**
         * Function that saves the user in SharedPreferences
         */
        fun saveUser(context: Context, utente: Utente) {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            val editor : SharedPreferences.Editor? = pref?.edit()
            editor?.putInt(Utente.ID_LABEL, utente.id)
            editor?.putString(Utente.NOME_LABEL, utente.nome)
            editor?.putString(Utente.EMAIL_LABEL, utente.email)
            editor?.putInt(Utente.CODFAM_LABEL, utente.codiceFamiglia)
            editor?.apply()
        }

        /**
         * Function that return Utente object from SharedPreferences
         */
        fun getUser(context: Context) : Utente {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            return Utente(pref!!.getInt(Utente.ID_LABEL, 0),
                    pref.getString(Utente.NOME_LABEL, null),
                    pref.getString(Utente.EMAIL_LABEL, null),
                    pref.getInt(Utente.CODFAM_LABEL, 0))
        }

        fun setNewUser(context: Context, new : Boolean) {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            val editor : SharedPreferences.Editor? = pref?.edit()
            editor?.putBoolean(Utente.NEW_USER, new)
            editor?.apply()
        }

        fun isNewUser(context: Context): Boolean {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            return pref!!.getBoolean(Utente.NEW_USER, false)
        }
    }
}