package it.paggiapp.familyshopping

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.paggiapp.familyshopping.data.CarrelloItem
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import it.paggiapp.familyshopping.util.inflate
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Fragment for the shopping chart
 * Created by nicola on 02/02/18.
 */
class ListaFragment : Fragment() {
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

        isOnline = Util.isOnline(context)

        val swipe : SwipeRefreshLayout = view!!.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_lista)

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

    /**
     * Function that ask the server for changes
     */
    fun loadFromServer(swipe: SwipeRefreshLayout, recyclerView: RecyclerView) {
        if(!isOnline) return


        swipe.isRefreshing = true
        // contatto il server
        // salvo le cose nel SQLite database
        // aggiorno la recyclerview
        (recyclerView.adapter as ListaAdapter).refresh()
    }


    /**
     * Adapter for the recyclerview of shopping list
     */
    class ListaAdapter() : RecyclerView.Adapter<ListaAdapter.ItemViewHolder>() {
        private var isRefreshing: Boolean = false
        var list : ArrayList<CarrelloItem> = ArrayList()

        init {
            setHasStableIds(true)
        }

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
            private var item: CarrelloItem? = null

            fun bind(item: CarrelloItem) {
                this.item = item
            }
        }
    }
}