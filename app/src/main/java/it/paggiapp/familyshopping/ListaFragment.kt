package it.paggiapp.familyshopping

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.backend.Comunication
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.database.FamilyDatabase
import it.paggiapp.familyshopping.util.Util
import it.paggiapp.familyshopping.util.inflate
import kotlinx.android.synthetic.main.lista_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Fragment for the shopping chart
 * Created by nicola on 02/02/18.
 */
class ListaFragment : Fragment() {
    lateinit var recyclerView : RecyclerView
    var isOnline = false

    companion object {

        /**
         * Static method for new Instance
         */
        fun newInstance(): ListaFragment {
            return ListaFragment()
        }
    }

    /**
     * Creates the view displayed
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_lista, container, false)
        setHasOptionsMenu(true)
        isOnline = Util.isOnline(context)

        val swipe : SwipeRefreshLayout = view!!.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_lista)

        // lister for Refresh
        swipe.setOnRefreshListener {
            // update the local database asking to the server
            loadFromServer(swipe, recyclerView)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ListaAdapter()
        loadFromServer(swipe, recyclerView)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Listener per il menu
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item == null) return false
        val id = item.itemId
        if(id == R.id.orderby_lista) {
            // filtra
            val orderBy = ModalOrderBy()
            orderBy.recyclerView = recyclerView
            orderBy.show(activity.supportFragmentManager, "BottomSheet Fragment")
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Function that ask the server for changes
     */
    fun loadFromServer(swipe: SwipeRefreshLayout, recyclerView: RecyclerView) {
        if(!isOnline) return
        swipe.isRefreshing = true

        // contatto il server
        // salvo le cose nel SQLite database
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

                    // aggiorno la recyclerview
                    (recyclerView.adapter as ListaAdapter).refresh()
                }

                swipe.isRefreshing = false
            }
        })
    }


    /**
     * Adapter for the recyclerview of shopping list
     */
    class ListaAdapter : RecyclerView.Adapter<ListaAdapter.ItemViewHolder>() {
        private var isRefreshing: Boolean = false
        var list : ArrayList<Carrello> = ArrayList()

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            refresh()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
            return ItemViewHolder(parent!!.inflate(R.layout.lista_item, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            val item = list[position]
            holder?.bind(item)
        }

        fun refresh() {
            if (isRefreshing) return
            isRefreshing = true
            DataStore.execute {
                val list = DataStore.getDB().getCarello()
                Handler(Looper.getMainLooper()).post {
                    this@ListaAdapter.list = list
                    notifyDataSetChanged()
                    isRefreshing = false
                }
            }
        }

        override fun getItemCount(): Int = list.size

        /**
         * Viewholder for the item of the list
         */
        class ItemViewHolder(v : View) : RecyclerView.ViewHolder(v) {
            private var view: View = v
            private var item: Carrello? = null

            fun bind(item: Carrello) {
                this.item = item
                view.tv_item_name.text = item.nome
            }
        }
    }
}