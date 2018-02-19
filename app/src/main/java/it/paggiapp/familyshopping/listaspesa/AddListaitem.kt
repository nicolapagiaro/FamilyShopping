package it.paggiapp.familyshopping.listaspesa

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_listaitem.*
import android.view.*
import android.view.inputmethod.InputMethodManager
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.backend.Comunication
import it.paggiapp.familyshopping.backend.ServerHelper
import it.paggiapp.familyshopping.data.Carrello
import it.paggiapp.familyshopping.data.Carrello.Companion.IN_LISTA
import it.paggiapp.familyshopping.data.Carrello.Companion.PRIORITA_MEDIA
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.suggestion_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for inser a lista item
 */
class AddListaitem : AppCompatActivity() {
    lateinit var suggestionItems: ArrayList<Carrello>
    private var uploadMode = Comunication.UploadCarrelloItem.MODE_ADD_LIST
    private var isSuggestionVisible = false
    private var isNew = true
    private var item: Carrello? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listaitem)

        // set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // when the user touches the layout of the comment, the
        // editText request the focus and show th keyboard
        item_add_comment.setOnClickListener {
            et_add_comment.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        item = intent.getSerializableExtra("item") as Carrello?
        if (item != null) {
            isNew = false
            // fill the view
            fillView(item!!)
            item!!.inputMode = Comunication.UploadCarrelloItem.MODE_EDIT
            uploadMode = Comunication.UploadCarrelloItem.MODE_EDIT
        } else {
            // init the view
            item = Carrello(null,
                    "",
                    "",
                    null,
                    PRIORITA_MEDIA,
                    IN_LISTA,
                    "",
                    "",
                    Util.getUser(applicationContext),
                    Comunication.UploadCarrelloItem.MODE_ADD_LIST)
            initView()
        }

        // categoria click listener
        item_add_category.setOnClickListener {
            // show the bottom stylesheet of the category
            // if the item is being added now
            if (!isNew) return@setOnClickListener

            // show the modal and write the callback function
            val categoryModal = CategoryModal()
            categoryModal.mDialogResult = object : CategoryModal.DialogCategoryResult {
                override fun finish(result: Categoria) {
                    // when the user click the Category
                    tv_add_category_value.text = result.nome
                    item!!.categoria = result
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
                    item?.priorita = result
                }
            }
            priorityModal.show(supportFragmentManager, PRIORITY_DIALOG_TAG)
        }

        // fill the list of recents item
        DataStore.execute {
            suggestionItems = DataStore.getDB().getItemsNotInCarrello()
            for (i in (0 until suggestionItems.size)) {
                val v: View = LayoutInflater.from(this)
                        .inflate(R.layout.suggestion_list_item, null, false)
                v.first_line_item.text = suggestionItems[i].nome
                v.second_line_item.text = suggestionItems[i].categoria?.nome

                v.setOnClickListener {
                    // when the user click the suggestion
                    fillView(suggestionItems[i])
                    item = suggestionItems[i]
                    item!!.inLista = IN_LISTA
                    item!!.utente = Util.getUser(applicationContext)
                    item!!.inputMode = Comunication.UploadCarrelloItem.MODE_SAVE
                    uploadMode = Comunication.UploadCarrelloItem.MODE_SAVE
                    hideSuggestion()
                }
                sugg_list.addView(v)
            }
        }

        // listeners for the EditText in the toolbar
        et_itemname.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isNew) {
                showSuggestion()
            }
        }

        et_itemname.setOnClickListener {
            if (isNew)
                showSuggestion()
        }

    }

    /**
     * onBackPressed
     */
    override fun onBackPressed() {
        if (isSuggestionVisible) {
            // if the suggestion in visible when user hit the back
            // key, hide it
            hideSuggestion()
        } else {
            super.onBackPressed()
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
                if(isSuggestionVisible) {
                    hideSuggestion()
                }
                else if(validateInput()) {
                    saveItem()
                }
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        invalidateOptionsMenu()
        if(isSuggestionVisible)
            menu?.findItem(R.id.new_listaitem_save)?.setTitle(R.string.close_suggestion)
        else
            menu?.findItem(R.id.new_listaitem_save)?.setTitle(R.string.save_item)

        return super.onPrepareOptionsMenu(menu)
    }

    /**
     * Method to fill the view from a Carrello item
     */
    private fun fillView(obj: Carrello) {
        et_itemname.setText("")
        et_itemname.append(obj.nome)

        if (isNew) {
            initView()
        } else {
            var nomeUtente = obj.utente?.nome
            if (nomeUtente.equals("null") || nomeUtente!!.isEmpty()) {
                nomeUtente = getString(R.string.username_null)
            }
            tv_add_username.text = nomeUtente
            tv_add_email.text = obj.utente?.email
        }

        tv_add_category_value.text = obj.categoria?.nome
        et_add_comment.setText("")
        var comment = obj.commento
        if (comment.equals("null") || comment.isEmpty()) {
            comment = getString(R.string.item_add_comment)
            et_add_comment.hint = comment
        } else {
            et_add_comment.append(comment)
        }
        tv_add_priority_value.text = getPrioritaText(obj.priorita)
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
    private fun getPrioritaText(pr: Int): String {
        var priorita = ""
        when (pr) {
            1 -> priorita = getString(R.string.item_priorita_value_bassa)
            2 -> priorita = getString(R.string.item_priorita_value_media)
            3 -> priorita = getString(R.string.item_priorita_value_alta)
        }
        return priorita
    }

    /**
     * Function that show the suggestion frame
     */
    private fun showSuggestion() {
        if (isSuggestionVisible || suggestionItems.isEmpty())
            return

        invalidateOptionsMenu()
        item_edit_container.visibility = View.INVISIBLE
        item_sugg_container.visibility = View.VISIBLE
        isSuggestionVisible = true
    }

    /**
     * Function that hide the suggestion frame
     */
    private fun hideSuggestion() {
        if (!isSuggestionVisible)
            return

        invalidateOptionsMenu()
        item_edit_container.visibility = View.VISIBLE
        item_sugg_container.visibility = View.INVISIBLE
        isSuggestionVisible = false
    }

    /**
     * Method for save/update the item in the databases
     */
    private fun saveItem() {
        val serverHelper = ServerHelper(applicationContext)

        // list of an element
        val list = ArrayList<ContentValues>()
        list.add(DataStore.getDB().fromValues(item!!))

        when (uploadMode) {
            Comunication.UploadCarrelloItem.MODE_ADD_LIST -> {
                DataStore.execute {
                    // save in the local db
                    val idInserted = DataStore.getDB().addItem(list)
                    item!!.id = idInserted.toInt()

                    Handler(Looper.getMainLooper()).post {
                        // save in the server
                        serverHelper.uploadItem(item!!, uploadMode)
                    }
                }
            }
            Comunication.UploadCarrelloItem.MODE_SAVE -> {
                DataStore.execute {
                    // save in the local db
                    DataStore.getDB().updateItem(list)
                }

                Handler(Looper.getMainLooper()).post {
                    serverHelper.uploadItem(item!!, uploadMode)
                }
            }
            Comunication.UploadCarrelloItem.MODE_EDIT -> {
                DataStore.execute {
                    // save in the local db
                    DataStore.getDB().updateItem(list)
                }

                Handler(Looper.getMainLooper()).post {
                    // save in the server
                    serverHelper.uploadItem(item!!, uploadMode)
                }
            }
        }

        // finish activity
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * Function that validates the input
     */
    private fun validateInput(): Boolean {
        val articolo = et_itemname.text.toString()

        // add the timestamp
        item!!.timestamp = Util.getCurrentTimestamp(Locale.US)

        // if the item is new, add the insert date
        if (isNew) {
            item!!.dataImmissione = Util.getCurrentTimestamp(Locale.ITALY)
        }

        if (articolo.isEmpty()) {
            // show error
            Snackbar.make(add_item_coordinator, R.string.error_no_item_name, Snackbar.LENGTH_SHORT).show()
            return false
        } else {
            item!!.nome = articolo
        }

        if (item!!.categoria == null) {
            // show error
            Snackbar.make(add_item_coordinator, R.string.error_no_item_category, Snackbar.LENGTH_SHORT).show()
            return false
        }

        item!!.commento = et_add_comment.text.toString()

        return true
    }


    companion object {
        val CATEGORY_DIALOG_TAG = "Modal sheet category"
        val PRIORITY_DIALOG_TAG = "Modal sheet priorita"
        val ADDLISTITEM_CODE = 666
    }
}
