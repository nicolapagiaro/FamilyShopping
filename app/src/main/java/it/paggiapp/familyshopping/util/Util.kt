package it.paggiapp.familyshopping.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import it.paggiapp.familyshopping.R

/**
 * Class for some important functions
 * Created by nicola on 21/01/18.
 */
class Util {
    companion object {

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
    }
}