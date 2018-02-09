package it.paggiapp.familyshopping

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import it.paggiapp.familyshopping.backend.DataDowload
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import it.paggiapp.familyshopping.util.inflate
import kotlinx.android.synthetic.main.lista_item.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            loadFromServer(swipe, recyclerView.adapter as ListaAdapter)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ListaAdapter()

        loadFromServer(swipe, recyclerView.adapter as ListaAdapter)

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
    fun loadFromServer(swipe: SwipeRefreshLayout, adapter: ListaAdapter) {
        if(!isOnline) return
        swipe.isRefreshing = true

        // ask the server for changes
        DataDowload(context,Runnable{
            swipe.isRefreshing = false
            adapter.refresh()
        }).updateAll()
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

            /**
             * Function that fill the layout with data
             */
            fun bind(item: Carrello) {
                this.item = item
                view.tv_categoria.text = item.categoria!!.nome
                view.tv_item_name.text = item.nome
                val commento = item.commento
                if(!commento.equals("null")){
                   view.tv_commento.text = commento
                }
                else {
                    view.tv_commento.visibility = View.GONE
                }
                val nomeUtente = item.utente!!.nome
                if(!nomeUtente.equals("null")) {
                    view.tv_info.text = nomeUtente
                }
                else {
                    view.tv_info.text = item.utente.email
                }
            }
        }
    }
}