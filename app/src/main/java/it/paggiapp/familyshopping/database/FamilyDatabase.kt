package it.paggiapp.familyshopping.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import it.paggiapp.familyshopping.listaspesa.ModalOrderBy
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
        var order = ""
        if (currentOrder == ModalOrderBy.CHOICE_PRIORITY) {
            order = "${FamilyContract.Carrello.PRIORITA} DESC"
        } else {
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

        return getCarrelloFull(cursor)
    }

    /**
     * Function to take the elemments no in th list, but already inserted
     */
    fun getItemsNotInCarrello() : ArrayList<Carrello> {
        val args = arrayOf(Carrello.NO_IN_LISTA.toString())
        val cursor = helper.readableDatabase.query(
                FamilyContract.Carrello._TABLE_NAME,
                null,
                "${FamilyContract.Carrello.IN_LISTA} = ?",
                args,
                null,
                null,
                null)

        return getCarrelloFull(cursor)
    }

    /**
     * Function that remove the item from the CARRELLO
     * Set the flag 'inLista' at 0
     */
    fun removeItem(removedItem : Carrello) {
        val db = helper.writableDatabase
        db.transaction {
            removedItem.inLista = Carrello.NO_IN_LISTA
            val args = arrayOf<String>(removedItem.id.toString())
            db.update(FamilyContract.Carrello._TABLE_NAME,
                    fromValues(removedItem),
                    "${FamilyContract.Carrello._ID} = ?",
                    args)
        }
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
    fun addItem(lista: ArrayList<ContentValues>) : Long {
        if (lista.size == 0) return -1
        val db = helper.writableDatabase
        var id : Long = -1
        db.transaction {
            lista.forEach {
                id = db.insert(FamilyContract.Carrello._TABLE_NAME, null, it)
            }
        }

        return id
    }

    /**
     * Function that remove a list of Carrello items
     */
    fun updateItem(lista: ArrayList<ContentValues>): Boolean {
        if (lista.size == 0) return false
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                val args = arrayOf<String>(it.getAsString(FamilyContract.Carrello._ID))
                db.update(FamilyContract.Carrello._TABLE_NAME,
                        it,
                        "${FamilyContract.Carrello._ID} = ?",
                        args)
            }
        }
        return true
    }


    /**
     * Function that saves a list of user in the database
     */
    fun salvaUtenti(lista: ArrayList<Utente>) {
        if (lista.size == 0) return
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                val values = fromValues(it)
                db.insert(FamilyContract.Utenti._TABLE_NAME, null, values)
            }
        }
    }

    /**
     * Function that saves a list of categorie in the database
     */
    fun salvaCategorie(lista: ArrayList<Categoria>) {
        if (lista.size == 0) return
        val db = helper.writableDatabase
        db.transaction {
            lista.forEach {
                val values = fromValues(it)
                db.insert(FamilyContract.Categorie._TABLE_NAME, null, values)
            }
        }
    }

    /**
     * Function that returns a list of Carrello item not updated
     * in the database online
     */
    fun getNewCarrello(timestamp : String) : ArrayList<Carrello> {
        val args = arrayOf(timestamp)
        val cursor = helper.readableDatabase.query(
                FamilyContract.Carrello._TABLE_NAME,
                null,
                "${FamilyContract.Carrello.TIMESTAMP} > ?",
                args,
                null,
                null,
                "${FamilyContract.Carrello.TIMESTAMP} DESC")

        return getCarrelloFull(cursor)
    }

    /**
     * Function that makes the select carrello query
     */
    private fun getCarrelloFull(cursor : Cursor) : ArrayList<Carrello> {
        val retval = ArrayList<Carrello>()
        while (cursor.moveToNext()) {
            // get the Category from the db
            var categoria: Categoria? = null
            val args1 = arrayOf(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.CATEGORIA)).toString())
            val catQuery = helper.readableDatabase.query(
                    FamilyContract.Categorie._TABLE_NAME,
                    null,
                    "${FamilyContract.Carrello._ID}=?",
                    args1,
                    null,
                    null,
                    null)
            while (catQuery.moveToNext()) {
                categoria = Categoria(catQuery.getInt(catQuery.getColumnIndex(FamilyContract.Categorie._ID)),
                        catQuery.getString(catQuery.getColumnIndex(FamilyContract.Categorie.NOME)))
            }

            // Get the user from the db
            var utente: Utente? = null
            val args2 = arrayOf(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.UTENTE)).toString())
            val userQuery = helper.readableDatabase.query(
                    FamilyContract.Utenti._TABLE_NAME,
                    null,
                    "${FamilyContract.Utenti._ID}=?",
                    args2,
                    null,
                    null,
                    null)

            while (userQuery.moveToNext()) {
                utente = Utente(userQuery.getInt(userQuery.getColumnIndex(FamilyContract.Utenti._ID)),
                        userQuery.getString(userQuery.getColumnIndex(FamilyContract.Utenti.NOME)),
                        userQuery.getString(userQuery.getColumnIndex(FamilyContract.Utenti.EMAIL)),
                        userQuery.getInt(0))
            }

            catQuery.close()
            userQuery.close()

            val c = Carrello(cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello._ID)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.NOME)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.COMMENTO)),
                    categoria,
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.PRIORITA)),
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.IN_LISTA)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.DATA_IMMISSIONE)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Carrello.TIMESTAMP)),
                    utente,
                    cursor.getInt(cursor.getColumnIndex(FamilyContract.Carrello.INPUT_MODE)))
            retval.add(c)
        }

        cursor.close()
        return retval
    }

    /**
     * Function that gives the content values from a User
     */
    fun fromValues(utente: Utente): ContentValues {
        val values = ContentValues().apply {
            put(FamilyContract.Utenti._ID, utente.id)
            put(FamilyContract.Utenti.NOME, utente.nome)
            put(FamilyContract.Utenti.EMAIL, utente.email)
        }
        return values
    }

    /**
     * Function that gives the content values from a Categoria
     */
    fun fromValues(categoria: Categoria): ContentValues {
        val values = ContentValues().apply {
            put(FamilyContract.Categorie._ID, categoria.id)
            put(FamilyContract.Categorie.NOME, categoria.nome)
        }
        return values
    }

    fun fromValues(carrello: Carrello) : ContentValues {
        val values = ContentValues().apply {
            put(FamilyContract.Carrello._ID, carrello.id)
            put(FamilyContract.Carrello.NOME, carrello.nome)
            put(FamilyContract.Carrello.COMMENTO, carrello.commento)
            put(FamilyContract.Carrello.CATEGORIA,carrello.categoria!!.id)
            put(FamilyContract.Carrello.PRIORITA, carrello.priorita)
            put(FamilyContract.Carrello.IN_LISTA, carrello.inLista)
            put(FamilyContract.Carrello.DATA_IMMISSIONE, carrello.dataImmissione)
            put(FamilyContract.Carrello.TIMESTAMP, carrello.timestamp)
            put(FamilyContract.Carrello.UTENTE, carrello.utente!!.id)
            put(FamilyContract.Carrello.INPUT_MODE, carrello.inputMode)
        }
        return values
    }

    /**
     * Function that returns the list of Utenti
     */
    fun getUtenti(): ArrayList<Utente> {
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
    fun getUtentiId(): ArrayList<Int> {
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

    /**
     * Function that returns the last timestamp of th etable
     * to sync with the online database
     */
    fun getCarrelloLastTimestamp(): String {
        val columns = arrayOf<String>(FamilyContract.Carrello.TIMESTAMP)
        val cursor = helper.readableDatabase.query(
                FamilyContract.Carrello._TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                "${FamilyContract.Carrello.TIMESTAMP} DESC")

        var retval = ""
        if (cursor.moveToNext()) {
            retval = cursor.getString(0)
        }

        cursor.close()
        return retval
    }

    /**
     * Function that return a list of IDs of categorie table
     */
    fun getCategorieId(): ArrayList<Int> {
        val columns = arrayOf<String>(FamilyContract.Categorie._ID)
        val cursor = helper.readableDatabase.query(
                FamilyContract.Categorie._TABLE_NAME,
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
     * Function that return a list of Categorie objcets
     */
    fun getCategorie() : ArrayList<Categoria> {
        val cursor = helper.readableDatabase.query(
                FamilyContract.Categorie._TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null)

        val retval = ArrayList<Categoria>()
        while (cursor.moveToNext()) {
            val c = Categoria(cursor.getInt(cursor.getColumnIndex(FamilyContract.Categorie._ID)),
                    cursor.getString(cursor.getColumnIndex(FamilyContract.Categorie.NOME)))
            retval.add(c)
        }
        cursor.close()
        return retval
    }

    companion object {

        /**
         * Gives an instance of a complete Carrello rows as ContentValues
         */
        fun carrelloToContentValues(temp: JSONObject): ContentValues {
            val values = ContentValues().apply {
                put(FamilyContract.Carrello._ID, temp.getInt("id"))
                put(FamilyContract.Carrello.NOME, temp.getString(FamilyContract.Carrello.NOME))
                put(FamilyContract.Carrello.COMMENTO, temp.getString(FamilyContract.Carrello.COMMENTO))
                put(FamilyContract.Carrello.CATEGORIA, temp.getInt(FamilyContract.Carrello.CATEGORIA))
                put(FamilyContract.Carrello.PRIORITA, temp.getInt(FamilyContract.Carrello.PRIORITA))
                put(FamilyContract.Carrello.IN_LISTA, temp.getInt(FamilyContract.Carrello.IN_LISTA))
                put(FamilyContract.Carrello.DATA_IMMISSIONE, temp.getString(FamilyContract.Carrello.DATA_IMMISSIONE))
                put(FamilyContract.Carrello.TIMESTAMP, temp.getString(FamilyContract.Carrello.TIMESTAMP))
                put(FamilyContract.Carrello.UTENTE, temp.getInt(FamilyContract.Carrello.UTENTE))
            }
            return values
        }

        /**
         * Gives an instance of a updating Carrello rows as ContentValues
         */
        fun carrelloToContentValuesUpdate(temp: JSONObject): ContentValues {
            val values = ContentValues().apply {
                put(FamilyContract.Carrello._ID, temp.getInt("id"))
                put(FamilyContract.Carrello.COMMENTO, temp.getString(FamilyContract.Carrello.COMMENTO))
                put(FamilyContract.Carrello.PRIORITA, temp.getInt(FamilyContract.Carrello.PRIORITA))
                put(FamilyContract.Carrello.IN_LISTA, temp.getInt(FamilyContract.Carrello.IN_LISTA))
                put(FamilyContract.Carrello.TIMESTAMP, temp.getString(FamilyContract.Carrello.TIMESTAMP))
            }
            return values
        }
    }
}