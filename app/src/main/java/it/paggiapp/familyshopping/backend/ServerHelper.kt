package it.paggiapp.familyshopping.backend

import android.content.ContentValues
import android.content.Context
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.MainActivity
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.database.FamilyContract
import it.paggiapp.familyshopping.database.FamilyDatabase
import it.paggiapp.familyshopping.util.Util
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/**
 * Class to manage comunication with server
 * Created by nicola on 08/02/18.
 */
class ServerHelper(val context: Context) {
    var callback : Runnable? = null

    /**
     * Secondary contructor
     * @param callback set of instruction called after download all things
     */
    constructor(context : Context, callback: Runnable?) : this(context) {
        this.callback = callback
    }

    /**
     * Function that Utenti Table, for new user
     */
    fun updateAll() {
        val utente = Util.getUser(context)

        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateUtente.ID_LABEL, utente.id)
        requestParams.put(Comunication.UpdateUtente.CODE_LABEL, utente.codiceFamiglia)
        requestParams.put(Comunication.UpdateUtente.IDS_LABEL, Gson().toJson(
                DataStore.getDB().getUtentiId()
        ))

        val client = AsyncHttpClient()

        client.post(context, Comunication.UpdateUtente.URL, requestParams, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
                // prendo la lista degli UTENTI (membri della famiglia)
                val list = ArrayList<Utente>()
                val userInfo: JSONArray = responseString!!.getJSONArray(Comunication.UpdateUtente.ARRAY_USER)
                for (i in 0..(userInfo.length() - 1)) {
                    val u = Utente(userInfo.getJSONObject(i).getInt("id"),
                            userInfo.getJSONObject(i).getString(FamilyContract.Utenti.NOME),
                            userInfo.getJSONObject(i).getString(FamilyContract.Utenti.EMAIL),
                            userInfo.getJSONObject(i).getInt(Utente.CODFAM_LABEL),
                            "")
                    list.add(u)
                }

                DataStore.execute {
                    DataStore.getDB().salvaUtenti(list)
                }

                updateCategorie()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                callback?.run()
            }
        })
    }

    /**
     * Function that updates the Categorie Table, for new categorie
     */
    private fun updateCategorie() {
        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateCategorie.IDS_LABEL, Gson().toJson(
                DataStore.getDB().getCategorieId()
        ))

        val client = AsyncHttpClient()
        client.post(context, Comunication.UpdateCategorie.URL, requestParams, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
                // prendo la lista delle CATEGORIE
                val list = ArrayList<Categoria>()
                val catInfo: JSONArray = responseString!!.getJSONArray(Comunication.UpdateCategorie.ARRAY_CATEGORIE)
                for (i in 0..(catInfo.length() - 1)) {
                    val c = Categoria(catInfo.getJSONObject(i).getInt("id"),
                            catInfo.getJSONObject(i).getString(FamilyContract.Categorie.NOME),
                            catInfo.getJSONObject(i).getString(FamilyContract.Categorie.IMMAGINE))
                    list.add(c)
                }

                DataStore.execute {
                    DataStore.getDB().salvaCategorie(list)
                }

                updateCarrello()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                callback?.run()
            }
        })
    }

    /**
     * Function that updates the chart item
     */
    private fun updateCarrello(){
        val utente = Util.getUser(context)

        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateCarrello.CODE_LABEL, utente.codiceFamiglia)
        requestParams.put(Comunication.UpdateCarrello.IDS_LABEL, Gson().toJson(
                DataStore.getDB().getCarelloId()
        ))
        requestParams.put(Comunication.UpdateCarrello.LAST_TIMESTAMP_LABEL, DataStore.getDB().getCarrelloLastTimestamp())

        val client = AsyncHttpClient()
        client.post(context, Comunication.UpdateCarrello.URL, requestParams, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
                // insert new Carrello records in the local database
                val addList = ArrayList<ContentValues>()
                val addItem : JSONArray = responseString!!.getJSONArray(Comunication.UpdateCarrello.ROWS_TO_ADD)
                for (i in 0..(addItem.length() - 1)) {
                    val temp = addItem.getJSONObject(i)
                    val values = FamilyDatabase.carrelloToContentValues(temp)
                    addList.add(values)
                }

                // updates Carrello records in the local database
                val updateList = ArrayList<ContentValues>()
                val updateItem : JSONArray = responseString.getJSONArray(Comunication.UpdateCarrello.ROWS_TO_UPDATE)
                for (i in 0..(updateItem.length() - 1)) {
                    val temp = updateItem.getJSONObject(i)
                    val values = FamilyDatabase.carrelloToContentValuesUpdate(temp)
                    updateList.add(values)
                }

                DataStore.execute{
                    // salvo le modifiche nel db locale
                    DataStore.getDB().addItem(addList)
                    DataStore.getDB().updateItem(updateList)

                }

                // get last timestamp from the server
                val lastTimestamp = responseString.getString(Comunication.UpdateCarrello.LAST_TIMESTAMP_LABEL)
                val listItemTimestamp = DataStore.getDB().getNewCarrello(lastTimestamp)
                pushUpdateToServer(listItemTimestamp)

                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                callback?.run()
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                callback?.run()
            }
        })
    }

    /**
     * Function to sync the local database with the online database
     */
    private fun pushUpdateToServer(lista : ArrayList<Carrello>) {
        lista.forEach {
            uploadItem(it, it.inputMode!!)
        }
    }

    /**
     * Call the server and remove from the list an item
     */
    fun removeItem(carrello: Carrello) {
        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateCarrello.ID, carrello.id)

        val client = AsyncHttpClient()
        client.post(context, Comunication.UpdateCarrello.REMOVEITEM_URL, requestParams, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {}
        })
    }

    /**
     * Call the server and insert/update/edit a carrello item
     */
    fun uploadItem(carrello: Carrello, mode : Int) {
        val requestParams = RequestParams()
        requestParams.put(Comunication.UploadCarrelloItem.MODE_LABEL, mode)
        requestParams.put(Comunication.UploadCarrelloItem.ITEM_LABEL, Gson().toJson(carrello))

        val client = AsyncHttpClient()
        client.post(context, Comunication.UploadCarrelloItem.URL, requestParams, object : JsonHttpResponseHandler(){

        })
    }

    /**
     * Call the server and updates the passoword
     */
    fun changeUserPassword(idUtente : Int, oldPssw : String, newPssw : String, act : MainActivity) {
        val requestParams = RequestParams()
        requestParams.put(Comunication.UpdateUtente.ID_LABEL, idUtente)
        requestParams.put(Comunication.UpdateUtente.OLDPSSW_LABEL, oldPssw)
        requestParams.put(Comunication.UpdateUtente.NEWPSSW_LABEL, newPssw)

        val client = AsyncHttpClient()
        client.post(context, Comunication.UpdateUtente.URL_CHANGE_PSSW, requestParams, object : JsonHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {
               if(responseString!!.getInt(Comunication.Login.SUCCESS_FIELD) == 1) {
                   act.showMessage(R.string.change_pssw_done)
               }else {
                   act.showMessage(R.string.change_pssw_failed)
               }
            }
        })
    }

    /**
     * static objects
     */
    companion object {
        val ABSOLUTE_URL = "http://quizapp.000webhostapp.com/familyshopping/"
    }
}