package it.paggiapp.familyshopping.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import it.paggiapp.familyshopping.data.CarrelloItem
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.database.*
import it.paggiapp.familyshopping.database.FamilyContract.Utenti.EMAIL
import it.paggiapp.familyshopping.database.FamilyContract.Utenti.NOME
import it.paggiapp.familyshopping.database.FamilyContract.Utenti._ID
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.transaction
import kotlin.collections.ArrayList

/**
 * CRUD Database
 * Created by nicola on 01/02/18.
 */
class FamilyDatabase(val context: Context) {
    private val helper: FamilyOpenHelper = FamilyOpenHelper(context)

    /**
     * Function that returns the shopping cart
     */
    fun getCarello(): ArrayList<CarrelloItem> {
        val cursor = helper.readableDatabase.query(
                FamilyContract.CarrelloItem._TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "${FamilyContract.CarrelloItem.TIMESTAMP} DESC")

        val retval = ArrayList<CarrelloItem>()
        while (cursor.moveToNext()) {
            // faccio robe
        }

        cursor.close()
        return retval
    }

    /**
     * Function that saves a list of user in the database
     */
    fun salvaUtenti(lista: ArrayList<Utente>) {
        if(lista.size == 0) return
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                val values = fromValues(it)
                db.insert(FamilyContract.Utenti._TABLE_NAME, null, values)
            }
        }
    }

    /**
     * Function that gives the content values from a User
     */
    fun fromValues(utente: Utente) : ContentValues {
        val values = ContentValues().apply {
            put(FamilyContract.Utenti._ID, utente.id)
            put(FamilyContract.Utenti.NOME, utente.nome)
            put(FamilyContract.Utenti.EMAIL, utente.email)
        }
        return values
    }

    /**
     * Function that returns the list of Utenti
     */
    fun getUtenti() : ArrayList<Utente> {
        val cursor = helper.readableDatabase.query(
                FamilyContract.Utenti._TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null)

        val retval = ArrayList<Utente>()
        while (cursor.moveToNext()) {
            var col = 0
            val u = Utente(cursor.getInt(col++),
                    cursor.getString(col++),
                    cursor.getString(col++),
            0)
            retval.add(u)
        }

        cursor.close()
        return retval
    }


    /**
     * Function that returns all the id of Utenti
     */
    fun getUtentiId() : ArrayList<Int> {
        val cursor = helper.readableDatabase.query(
                FamilyContract.Utenti._TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null)

        val retval = ArrayList<Int>()
        while (cursor.moveToNext()) {
            retval.add(cursor.getInt(0))
        }

        cursor.close()
        return retval
    }
}