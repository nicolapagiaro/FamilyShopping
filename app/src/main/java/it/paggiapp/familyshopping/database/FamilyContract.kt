package it.paggiapp.familyshopping.database

/**
 * All the specs for the SQLite database
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
     * Table CARRLLO, stores the item and quantity
     */
    object Carrello {
        val _ID = "_id"
        val _TABLE_NAME = "carrello"
        val NOME = "nome"
        val COMMENTO = "commento"
        val CATEGORIA = "categoria"
        val PRIORITA = "priorita"
        val IN_LISTA = "inLista"
        val DATA_IMMISSIONE = "dataImmissione"
        val UTENTE = "utente"
        val TIMESTAMP = "timestamp"
        val INPUT_MODE = "input_mode"
    }

    val SQL_CREATE_CARRELLO = "CREATE TABLE ${Carrello._TABLE_NAME} (" +
            "${Carrello._ID} INTEGER PRIMARY KEY, " +
            "${Carrello.NOME} TEXT, " +
            "${Carrello.COMMENTO} TEXT, " +
            "${Carrello.CATEGORIA} INTEGER, " +
            "${Carrello.PRIORITA} INTEGER, " +
            "${Carrello.IN_LISTA} INTEGER, " +
            "${Carrello.DATA_IMMISSIONE} STRING," +
            "${Carrello.UTENTE} INTEGER," +
            "${Carrello.TIMESTAMP} TEXT, " +
            "${Carrello.INPUT_MODE} INTEGER )".trimMargin()

    val SQL_DELETE_CARRELLO = "DROP TABLE IF EXISTS ${Carrello._TABLE_NAME}"


    /**
     * Table CATEGORIE, stores the category of item inserted
     */
    object Categorie {
        val _ID = "_id"
        val _TABLE_NAME = "categorie"
        val NOME = "nome"
        val IMMAGINE = "immagine"
    }

    val SQL_CREATE_CATEGORIE = "CREATE TABLE ${Categorie._TABLE_NAME} (" +
            "${Categorie._ID} INTEGER PRIMARY KEY, " +
            "${Categorie.NOME} TEXT," +
            "${Categorie.IMMAGINE} TEXT )".trimMargin()

    val SQL_DELETE_CATEGORIE = "DROP TABLE IF EXISTS ${Categorie._TABLE_NAME}"
}