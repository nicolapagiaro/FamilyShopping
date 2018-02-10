package it.paggiapp.familyshopping

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Html
import android.view.*
import it.paggiapp.familyshopping.backend.DataDowload
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import it.paggiapp.familyshopping.util.inflate
import kotlinx.android.synthetic.main.lista_item.view.*
import org.joda.time.Interval
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for the shopping chart
 * Created by nicola on 02/02/18.
 */
class ListaFragment : Fragment() {
    lateinit var swipe: SwipeRefreshLayout
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

        swipe = view!!.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_lista)

        // lister for Refresh
        swipe.setOnRefreshListener {
            // update the local database asking to the server
            loadFromServer()
        }

        // the swipe to delete implementation
        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                // notify the adapter of changes
                val adapter = recyclerView.adapter as ListaAdapter
                adapter.removeAt(viewHolder!!.adapterPosition)

                // remove the item from the databases
                val removedItem= (viewHolder as ListaAdapter.ItemViewHolder).item!!
                removeItem(removedItem)

                // show message
                (activity as MainActivity).showMessageItemDeleted()
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ListaAdapter(context)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

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
    fun loadFromServer() {
        if(!isOnline) return
        swipe.isRefreshing = true

        // ask the server for changes
        DataDowload(context,Runnable{
            swipe.isRefreshing = false
            (recyclerView.adapter as ListaAdapter).refresh()
        }).updateAll()
    }

    /**
     * Function that removes the item selected from the online database
     * and the local database
     */
    fun removeItem(removedItem : Carrello) {
        DataStore.getDB().removeItem(removedItem)
        DataDowload(context).removeItem(removedItem)
    }


    /**
     * Adapter for the recyclerview of shopping list
     */
    class ListaAdapter(val context: Context) : RecyclerView.Adapter<ListaAdapter.ItemViewHolder>() {
        private var isRefreshing: Boolean = false
        var list : ArrayList<Carrello> = ArrayList()

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            refresh()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
            return ItemViewHolder(parent!!.inflate(R.layout.lista_item, false), context)
        }

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            val item = list[position]
            holder?.bind(item)
            holder?.attachOnClickListener()
        }

        /**
         * Function that ask server for changes and updates the recycler
         */
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

        /**
         * Function that removes the item from the list
         * and the recycler
         */
        fun removeAt(position: Int) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun getItemCount(): Int = list.size

        /**
         * Viewholder for the item of the list
         */
        class ItemViewHolder(v : View, val context: Context) : RecyclerView.ViewHolder(v) {
            var view: View = v
            var item: Carrello? = null

            /**
             * Function that fill the layout with data
             */
            fun bind(item: Carrello) {
                val idUtente = Util.getUser(context).id
                this.item = item
                view.tv_categoria.text = item.categoria?.nome
                view.tv_item_name.text = item.nome
                val commento = item.commento
                if(!commento.equals("null")){
                   view.tv_commento.text = commento
                }
                else {
                    view.tv_commento.visibility = View.GONE
                }
                var nomeUtente = item.utente?.nome
                if(nomeUtente.equals("null")) {
                    nomeUtente = item.utente?.email
                }
                else if(idUtente == item.utente?.id) {
                    // scritta "TU"
                    nomeUtente = context.getString(R.string.tv_item_list_author_me)
                }
                val details = context.getString(R.string.tv_item_list_details, nomeUtente, timeToText(item.dataImmissione))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.tv_info.text = Html.fromHtml(details, Html.FROM_HTML_MODE_LEGACY)
                }
                else {
                    view.tv_info.text = Html.fromHtml(details)
                }
            }

            /**
             * Function that convert the time in a string to display
             */
            private fun timeToText(dateString : String) : String {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY)
                try {
                    // insert date of the item
                    val dateItem = Calendar.getInstance()
                    dateItem.time = simpleDateFormat.parse(dateString)

                    // current date
                    val current = Calendar.getInstance(Locale.ITALY)

                    val period = Interval(dateItem.timeInMillis, current.timeInMillis).toPeriod()
                    if(period.days == 0) {
                        // oggi
                        if(period.hours <= 2)
                            return context.getString(R.string.tv_item_time_few_ago)
                        else
                            return context.getString(R.string.tv_item_time_today)
                    }
                    else if(period.days == 1) {
                        // ieri
                        return context.getString(R.string.tv_item_time_yesterday)
                    }
                    else if(period.days < 8) {
                        // n giorni fa
                        return context.getString(R.string.tv_item_time_days, period.days)
                    }
                    else {
                        // n settimane fa
                        val weeks : Int = period.days / 7
                        return context.resources.getQuantityString(R.plurals.tv_item_time_weeks, weeks)
                    }

                } catch (ex: ParseException) {}

                return ""
            }

            fun attachOnClickListener() {
                view.card_layout.setOnClickListener{
                    val detailItem = Intent(context, ShowListaItemDetails::class.java)
                    detailItem.putExtra("item", item)
                    context.startActivity(detailItem)
                }
            }
        }
    }
}