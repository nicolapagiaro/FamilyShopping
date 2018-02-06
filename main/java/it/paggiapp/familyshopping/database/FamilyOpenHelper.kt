package it.paggiapp.familyshopping.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by nicola on 01/02/18.
 */
class FamilyOpenHelper(context: Context) : SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION) {

    // static object
    companion object {
        val DATABASE_NAME = "family.db"
        val DATABASE_VERSION  = 1
    }

    /**
     * onCreate database
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(FamilyContract.SQL_CREATE_UTENTI)
        db.execSQL(FamilyContract.SQL_CREATE_CARRELLO)
        db.execSQL(FamilyContract.SQL_CREATE_CATEGORIE)
    }

    /**
     * onUpgrade database
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(FamilyContract.SQL_DELETE_UTENTI)
        db.execSQL(FamilyContract.SQL_DELETE_CARRELLO)
        db.execSQL(FamilyContract.SQL_DELETE_CATEGORIE)
        onCreate(db)
    }
}
