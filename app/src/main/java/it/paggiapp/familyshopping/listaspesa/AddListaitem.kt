package it.paggiapp.familyshopping.listaspesa

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_listaitem.*
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.data.Carrello.Companion.IN_LISTA
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.util.Util

/**
 * Class for inser a lista item
 */
class AddListaitem : AppCompatActivity() {
    var isNew = true
    var itemToEdit : Carrello? = null
    var itemToCreate : Carrello? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listaitem)

        // set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // when the user touches the layout of the comment, the
        // editText request the focus and show th keyboard
        item_add_comment.setOnClickListener{
            et_add_comment.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        itemToEdit = intent.getSerializableExtra("item") as Carrello?
        if(itemToEdit != null) {
            isNew = false
            // fill the view
            fillView()
        }
        else {
            // init the view
            itemToCreate = Carrello(0,
                    "",
                    "",
                    null,
                    0,
                    IN_LISTA,
                    "",
                    "",
                    Util.getUser(applicationContext))
            initView()
        }

        // categoria click listener
        item_add_category.setOnClickListener {
            // show the bottom stylesheet of the category
            // if the item is being added now
            if(!isNew) return@setOnClickListener

            // show the modal and write the callback function
            val categoryModal = CategoryModal()
            categoryModal.mDialogResult = object : CategoryModal.DialogCategoryResult {
                override fun finish(result: Categoria) {
                    // when the user click the Category
                    tv_add_category_value.text = result.nome
                    itemToCreate!!.categoria = result
                }
            }
            categoryModal.show(supportFragmentManager, CATEGORY_DIALOG_TAG)
        }

        // priorità click listener
        item_add_priority.setOnClickListener {
            // show the bottom stylesheet of the priority
            val priorityModal = PriorityModal()
            priorityModal.mDialogResult = object : PriorityModal.DialogPriorityResult {
                override fun finish(result: Int) {
                    tv_add_priority_value.text = getPrioritaText(result)
                    itemToCreate!!.priorita = result
                }
            }
            priorityModal.show(supportFragmentManager, PRIORITY_DIALOG_TAG)
        }
    }

    /**
     * onBack button pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.new_listaitem_save -> {
                Log.d("AddItem", "save btn")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * onMenu create
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_lista_item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Method to fill the view from a Carrello item
     */
    private fun fillView() {
        et_itemname.append(itemToEdit!!.nome)
        var nomeUtente = itemToEdit!!.utente?.nome
        if(nomeUtente.equals("null") || nomeUtente?.length == 0) {
            nomeUtente = getString(R.string.username_null)
        }
        tv_add_username.text = nomeUtente
        tv_add_email.text = itemToEdit!!.utente?.email
        tv_add_category_value.text = itemToEdit!!.categoria?.nome

        var comment = itemToEdit!!.commento
        if(comment.equals("null") || comment.length == 0) {
            comment = getString(R.string.comment_null)
        }
        et_add_comment.append(comment)
        tv_add_priority_value.text = getPrioritaText(itemToEdit!!.priorita)
    }

    /**
     * Method to initialize the view
     */
    private fun initView() {
        val utente = Util.getUser(applicationContext)
        tv_add_username.text = utente.nome
        tv_add_email.text = utente.email
    }

    /**
     * Function that returns the string corrisponding at the value
     */
    private fun getPrioritaText(pr : Int) : String{
        var priorita = ""
        when(pr) {
            1 -> priorita = getString(R.string.item_priorita_value_bassa)
            2 -> priorita = getString(R.string.item_priorita_value_media)
            3 -> priorita = getString(R.string.item_priorita_value_alta)
        }
        return priorita
    }

    companion object {
        val CATEGORY_DIALOG_TAG = "Modal sheet category"
        val PRIORITY_DIALOG_TAG = "Modal sheet priorita"
    }
}
