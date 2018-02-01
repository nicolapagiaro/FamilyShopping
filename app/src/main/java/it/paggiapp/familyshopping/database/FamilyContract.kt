package it.paggiapp.familyshopping.database

/**
 * Created by nicola on 01/02/18.
 */
object FamilyContract {

    /**
     * Table UTENTI, stores the info of family members
     */
    object Utenti {
        val _ID = "_id"
        val _TABLE_NAME = "utenti"
        val NOME = "nome"
        val EMAIL = "email"
    }

    val SQL_CREATE_UTENTI = "CREATE TABLE ${Utenti._TABLE_NAME} (" +
            "${Utenti._ID} INTEGER PRIMARY KEY, " +
            "${Utenti.NOME} TEXT, " +
            "${Utenti.EMAIL} TEXT)".trimMargin()

    val SQL_DELETE_UTENTI = "DROP TABLE IF EXISTS ${Utenti._TABLE_NAME}"

    /**
     * Table ITEM, stores the item that the family insert
     */
    object Item {
        val _ID = "_id"
        val _TABLE_NAME = "articoli_spesa"
        val NOME = "nome"
        val CATEGORIA = "categoria"
    }

    val SQL_CREATE_ITEM = "CREATE TABLE ${Item._TABLE_NAME} (" +
            "${Item._ID} INTEGER PRIMARY KEY, " +
            "${Item.NOME} TEXT, " +
            "${Item.CATEGORIA} INTEGER)".trimMargin()

    val SQL_DELETE_ITEM = "DROP TABLE IF EXISTS ${Item._TABLE_NAME}"


    /**
     * Table CARRLLO_ITEM, stores the item and quantity
     */
    object CarrelloItem {
        val _ID = "_id"
        val _TABLE_NAME = "carrello_item"
        val QUANTITA = "quantita"
        val PRIORITA = "priorita"
        val DATA_IMMISSIONE = "dataImmissione"
        val ORA_IMMISSIONE = "oraImmissione"
        val TIMESTAMP = "timestamp"
    }

    val SQL_CREATE_CARRELLOITEM = "CREATE TABLE ${CarrelloItem._TABLE_NAME} (" +
            "${CarrelloItem._ID} INTEGER PRIMARY KEY, " +
            "${CarrelloItem.QUANTITA} INTEGER, " +
            "${CarrelloItem.PRIORITA} INTEGER, )" +
            "${CarrelloItem.DATA_IMMISSIONE} date," +
            "${CarrelloItem.ORA_IMMISSIONE} time," +
            "${CarrelloItem.TIMESTAMP} timestamp)".trimMargin()

    val SQL_DELETE_CARRELLOITEM = "DROP TABLE IF EXISTS ${CarrelloItem._TABLE_NAME}"


    /**
     * Table CATEGORIE, stores the category of item inserted
     */
    object Categorie {
        val _ID = "_id"
        val _TABLE_NAME = "categorie"
        val NOME = "nome"
    }

    val SQL_CREATE_CATEGORIE = "CREATE TABLE ${Categorie._TABLE_NAME} (" +
            "${Categorie._ID} INTEGER PRIMARY KEY, " +
            "${Categorie.NOME} TEXT)".trimMargin()

    val SQL_DELETE_CATEGORIE = "DROP TABLE IF EXISTS ${Categorie._TABLE_NAME}"
}