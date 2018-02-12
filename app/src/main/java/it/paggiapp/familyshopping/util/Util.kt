package it.paggiapp.familyshopping.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import it.paggiapp.familyshopping.listaspesa.ModalOrderBy
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Utente
import org.joda.time.Period
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class for some important functions
 * Created by nicola on 21/01/18.
 */
class Util {
    companion object {
        val NOME_MIN_CHAR: Int = 3
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

        fun getCurrentOrder(context: Context) : Int {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            return pref!!.getInt(ModalOrderBy.CHOICE_LABEL, ModalOrderBy.CHOICE_PRIORITY)
        }

        fun setOrder(context: Context, order : Int) {
            val pref : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)
            val editor : SharedPreferences.Editor? = pref?.edit()
            editor?.putInt(ModalOrderBy.CHOICE_LABEL, order)
            editor?.apply()
        }

        /**
         * Function that convert the time in a string to display
         */
        fun timeToText(dateString : String, context: Context) : String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY)
            try {
                // insert date of the item
                val dateItem = Calendar.getInstance()
                dateItem.time = simpleDateFormat.parse(dateString)

                // current date
                val current = Calendar.getInstance(Locale.ITALY)

                val period = Period(dateItem.timeInMillis, current.timeInMillis)
                if(period.days == 0 && period.hours < 12) {
                    // oggi
                    if(period.hours <= 2)
                        return context.getString(R.string.tv_item_time_few_ago)
                    else
                        return context.getString(R.string.tv_item_time_today)
                }
                else if(period.days == 1 || period.hours > 12) {
                    // ieri
                    return context.getString(R.string.tv_item_time_yesterday)
                }
                else if(period.days < 8) {
                    // n giorni fa
                    return context.getString(R.string.tv_item_time_days, period.days)
                }
                else {
                    // n settimane fa
                    val weeks : Int = period.days / 7
                    return context.resources.getQuantityString(R.plurals.tv_item_time_weeks, weeks)
                }

            } catch (ex: ParseException) {}

            return ""
        }

    }
}