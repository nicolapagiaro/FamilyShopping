package it.paggiapp.familyshopping

import android.app.Activity
import android.app.Fragment
import android.app.ListFragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ListAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.backend.Comunication
import it.paggiapp.familyshopping.backend.DataDowload
import it.paggiapp.familyshopping.data.Categoria
import it.paggiapp.familyshopping.data.Utente
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.database.FamilyContract
import it.paggiapp.familyshopping.intro.IntroActivity
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    var currentFragment : ActiveFragment = ActiveFragment.LISTA_SPESA

    companion object {
        val REQUEST_CODE_INTRO: Int = 300
        val DELAY_SNACKBAR: Long = 1500
    }

    /**
     * onCreate method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // INIT the database and get reference of it for next uses
        DataStore.init(applicationContext)

        // if user is not logged then IntroActivity
        if (!Util.isUserLogged(applicationContext)) {
            startActivityForResult(Intent(applicationContext, IntroActivity::class.java), REQUEST_CODE_INTRO)
        } else {
            setUpMain()
        }
    }

    /**
     * onActiviy result for result by the IntroActivity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode != Activity.RESULT_OK) {
                finish()
            } else {
                setUpMain()
            }
        }
    }

    /**
     * Function thats set up all thing in the main activity
     */
    fun setUpMain() {
        // set up the bottom navigation bar
        val item1 = AHBottomNavigationItem(R.string.bn_item1, R.drawable.ic_shopping_cart_black_24dp, R.color.bn_default_color)
        val item2 = AHBottomNavigationItem(R.string.bn_item2, R.drawable.ic_folder_black_24dp, R.color.bn_default_color)
        val item3 = AHBottomNavigationItem(R.string.bn_item3, R.drawable.ic_person_black_24dp, R.color.bn_default_color)
        bottom_navigation.addItem(item1)
        bottom_navigation.addItem(item2)
        bottom_navigation.addItem(item3)

        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_navigation.currentItem = 0
        bottom_navigation.accentColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        bottom_navigation.manageFloatingActionButtonBehavior(fab_main)
        bottom_navigation.isBehaviorTranslationEnabled = true // da ottimizzare

        // listener for tab click
        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            // if the tab was already selected
            if(wasSelected) return@setOnTabSelectedListener false

            when(position) {
                0 -> {
                    // load the fragment
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, ListaFragment.newInstance())
                            .commit()
                    currentFragment = ActiveFragment.LISTA_SPESA
                }
                1 -> {
                    // load the fragment
                    currentFragment = ActiveFragment.LISTA_RICETTE
                }
                2 -> {
                    // load the fragment
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, UtenteFragment.newInstance())
                            .commit()
                    currentFragment = ActiveFragment.PROFILO_UTENTE
                }
                else -> {
                    return@setOnTabSelectedListener false
                }
            }

            true
        }

        // click listern for the main FAB
        fab_main.setOnClickListener{
            when(currentFragment) {
                ActiveFragment.LISTA_SPESA -> {
                    // start AddListaItem activity
                    val newItem = Intent(applicationContext, AddListaitem::class.java)
                    startActivity(newItem)
                }

                ActiveFragment.LISTA_RICETTE -> {
                    // start AddRicetteItem activity
                }

                ActiveFragment.PROFILO_UTENTE -> {
                    // nothing because the FAB is hided
                }
            }
        }

        // check for connection
        if (!Util.isOnline(applicationContext)) {
            Handler().postDelayed(Runnable{
                Snackbar.make(bottom_navigation, R.string.no_internet, Snackbar.LENGTH_SHORT).show()
            }, DELAY_SNACKBAR)
        }

        // display the first fragment
        val frag : ListaFragment = ListaFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, frag)
                .commit()

        // ask the server for changes
        DataDowload(applicationContext, Runnable{
            (frag.recyclerView.adapter as ListaFragment.ListaAdapter).refresh()
        }).updateAll()
    }

    /**
     * To show a message of deleting
     */
    fun showMessageItemDeleted() {
        val snack = Snackbar.make(bottom_navigation, R.string.prompt_item_deleted, Snackbar.LENGTH_SHORT)
        snack.show()
    }
}
