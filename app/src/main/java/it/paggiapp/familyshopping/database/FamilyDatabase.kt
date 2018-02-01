package it.paggiapp.familyshopping.database

import android.content.Context
import it.paggiapp.familyshopping.data.CarrelloItem
import java.util.*

/**
 * Created by nicola on 01/02/18.
 */
class FamilyDatabase(val context: Context) {
    private val helper: FamilyOpenHelper = FamilyOpenHelper(context)

    /**
     * CRUD METHOD
     */
    fun getCarello() : ArrayList<CarrelloItem> {
        val query = "select  item.id, item.nome, cat.id, cat.nome, " +
                "car.quantita, car.priorita, car.timestamp, utente.id, utente.nome, " +
                "utente.email" +
                "from ${FamilyContract.Item._TABLE_NAME} as item, " +
                "${FamilyContract.Categorie._TABLE_NAME} as cat, " +
                "${FamilyContract.CarrelloItem._TABLE_NAME} as car," +
                "${FamilyContract.Utenti._TABLE_NAME} as utente"

        val cursor = helper.readableDatabase.rawQuery(query, null)

        val retval = ArrayList<CarrelloItem>()
        while (cursor.moveToNext()) {

        }

        cursor.close()
        return retval
    }
}