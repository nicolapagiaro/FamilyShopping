package it.paggiapp.familyshopping.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import it.paggiapp.familyshopping.ModalOrderBy
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.util.Util
import org.jetbrains.anko.db.transaction
import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 * CRUD Database
 * Created by nicola on 01/02/18.
 */
class FamilyDatabase(val context: Context) {
    var currentOrder = Util.getCurrentOrder(context)
    private val helper: FamilyOpenHelper = FamilyOpenHelper(context)

    /**
     * Function that returns the shopping cart
     */
    fun getCarello(): ArrayList<Carrello> {
        var order : String = ""
        if(currentOrder == ModalOrderBy.CHOICE_PRIORITY) {
            order = "${FamilyContract.Carrello.PRIORITA} DESC"
        }
        else {
            order = "${FamilyContract.Carrello.TIMESTAMP} DESC"
        }


        val args = arrayOf(Carrello.IN_LISTA.toString())
        val cursor = helper.readableDatabase.query(
                FamilyContract.Carrello._TABLE_NAME,
                null,
                "${FamilyContract.Carrello.IN_LISTA} = ?",
                args,
                null,
                null,
                order)

        val retval = ArrayList<Carrello>()
        while (cursor.moveToNext()) {
            // get the Category from the db
            var categoria : Categoria? = null
            val args = arrayOf(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.CATEGORIA)).toString())
            val catQuery = helper.readableDatabase.query(
                    FamilyContract.Categorie._TABLE_NAME,
                    null,
                    "${FamilyContract.Carrello._ID}=?",
                    args,
                    null,
                    null,
                    null)
            while(catQuery.moveToNext()) {
                categoria = Categoria(catQuery.getInt(catQuery.getColumnIndex(FamilyContract.Categorie._ID)),
                        catQuery.getString(catQuery.getColumnIndex(FamilyContract.Categorie.NOME)))
                Log.d("Elemento", categoria.toString())
            }

            // Get the user from the db
            var utente : Utente? = null
            val args1 = arrayOf(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.UTENTE)).toString())
            val userQuery = helper.readableDatabase.query(
                    FamilyContract.Utenti._TABLE_NAME,
                    null,
                    "${FamilyContract.Utenti._ID}=?",
                    args1,
                    null,
                    null,
                    null)

            while(userQuery.moveToNext()) {
                utente = Utente(userQuery.getInt(userQuery.getColumnIndex(FamilyContract.Utenti._ID)),
                        userQuery.getString(userQuery.getColumnIndex(FamilyContract.Utenti.NOME)),
                        userQuery.getString(userQuery.getColumnIndex(FamilyContract.Utenti.EMAIL)),
                        userQuery.getInt(0))
                Log.d("Elemento", utente.toString())
            }

            catQuery.close()
            userQuery.close()


            /*Log.d("Cursor ID", cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello._ID)).toString())
            Log.d("Cursor NOME", cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.NOME)))
            Log.d("Cursor COMMENTO", cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.COMMENTO)))
            Log.d("Cursor PRIORITA", cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.PRIORITA)).toString())
            Log.d("Cursor QUANTITA", cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.QUANTITA)).toString())
            Log.d("Cursor CATEGORIA", cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.CATEGORIA)).toString())
            Log.d("Cursor DATA", cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.DATA_IMMISSIONE)))
            Log.d("Cursor ORA", cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.ORA_IMMISSIONE)))
            Log.d("Cursor TIMESTAMP", cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.TIMESTAMP)))
            Log.d("Cursor UTENTE", cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.UTENTE)).toString())*/


            val c = Carrello(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello._ID)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.NOME)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.COMMENTO)),
                    categoria,
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.QUANTITA)),
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.PRIORITA)),
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.IN_LISTA)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.DATA_IMMISSIONE)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.ORA_IMMISSIONE)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.TIMESTAMP)),
                    utente)

            Log.d("Elemento", c.toString())
            retval.add(c)
        }

        cursor.close()
        return retval
    }


    /**
     * Function that returns the item's id of shopping cart
     */
    fun getCarelloId(): ArrayList<Int> {
        val columns = arrayOf<String>(FamilyContract.Carrello._ID)

        val cursor = helper.readableDatabase.query(
                FamilyContract.Carrello._TABLE_NAME,
                columns,
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

    /**
     * Function that insert a list of Carrello items
     */
    fun addItem(lista: ArrayList<ContentValues>) {
        if(lista.size == 0) return
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                db.insert(FamilyContract.Carrello._TABLE_NAME, null, it)
            }
        }
    }

    /**
     * Function that remove a list of Carrello items
     */
    fun updateItem(lista: ArrayList<Carrello>) {
        if(lista.size == 0) return
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                // aggiorna
            }
        }
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
                    cursor.getString(col),
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
        val columns = arrayOf<String>(FamilyContract.Utenti._ID)

        val cursor = helper.readableDatabase.query(
                FamilyContract.Utenti._TABLE_NAME,
                columns,
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

    companion object {
        /**
         *
         */
        fun carrelloToContendValues(temp : JSONObject) : ContentValues {
            val values = ContentValues().apply {
                put(FamilyContract.Carrello._ID, temp.getInt("id"))
                put(FamilyContract.Carrello.NOME, temp.getString("nome"))
                put(FamilyContract.Carrello.COMMENTO, temp.getString("commento"))
                put(FamilyContract.Carrello.CATEGORIA, temp.getInt("categoria"))
                put(FamilyContract.Carrello.PRIORITA, temp.getInt("priorita"))
                put(FamilyContract.Carrello.QUANTITA, temp.getInt("quantita"))
                put(FamilyContract.Carrello.IN_LISTA, temp.getInt("inLista"))
                put(FamilyContract.Carrello.DATA_IMMISSIONE, temp.getString("dataImmissione"))
                put(FamilyContract.Carrello.ORA_IMMISSIONE, temp.getString("oraImmissione"))
                put(FamilyContract.Carrello.TIMESTAMP, temp.getString("timestamp"))
                put(FamilyContract.Carrello.UTENTE, temp.getInt("utente"))
            }
            return values
        }
    }
}