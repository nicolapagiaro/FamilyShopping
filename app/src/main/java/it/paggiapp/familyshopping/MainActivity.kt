package it.paggiapp.familyshopping

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import it.paggiapp.familyshopping.backend.ServerHelper
import it.paggiapp.familyshopping.database.DataStore
import it.paggiapp.familyshopping.intro.IntroActivity
import it.paggiapp.familyshopping.listaspesa.ListaFragment
import it.paggiapp.familyshopping.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import it.paggiapp.familyshopping.listaspesa.AddListaitem
import it.paggiapp.familyshopping.listaspesa.ShowListaItemDetails

class MainActivity : AppCompatActivity() {
    var currentFragment : ActiveFragment = ActiveFragment.LISTA_SPESA
    private lateinit var frag: GenericFragment

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
        else if(requestCode == AddListaitem.ADDLISTITEM_CODE){
            if(resultCode == Activity.RESULT_OK) {
                frag.refreshList()
            }
        }
        else if(requestCode == ShowListaItemDetails.SHOWITEM_CODE){
            if(resultCode == Activity.RESULT_OK) {
                frag.refreshList()
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
        bottom_navigation.isBehaviorTranslationEnabled = false // da ottimizzare

        // listener for tab click
        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            // if the tab was already selected
            if(wasSelected) {
                frag.scrollToTop()
                return@setOnTabSelectedListener false
            }

            when(position) {
                0 -> {
                    frag = ListaFragment.newInstance()

                    // load the fragment
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, frag as ListaFragment)
                            .commit()
                    currentFragment = ActiveFragment.LISTA_SPESA
                    fab_main.show()

                }
                1 -> {
                    //frag = RicetteFragment.newInstance()

                    // load the fragment
                    currentFragment = ActiveFragment.LISTA_RICETTE
                    fab_main.show()
                }
                2 -> {
                    frag = UtenteFragment.newInstance()

                    // load the fragment
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, frag as UtenteFragment)
                            .commit()
                    currentFragment = ActiveFragment.PROFILO_UTENTE
                    fab_main.hide()
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
                    startActivityForResult(newItem, AddListaitem.ADDLISTITEM_CODE)
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
                showMessage(R.string.no_internet)
            }, DELAY_SNACKBAR)
        }

        // display the first fragment
        frag = ListaFragment.newInstance()
        val tempF = frag as ListaFragment
        val initRefresh = Bundle()
        initRefresh.putBoolean("refresh", true)
        tempF.arguments = initRefresh
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, tempF)
                .commit()

        // ask the server for changes
        ServerHelper(applicationContext, Runnable{
            tempF.swipe.isRefreshing = false
            (tempF.recyclerView.adapter as ListaFragment.ListaAdapter).refresh()
        }).updateAll(supportActionBar)
    }

    /**
     * To show a message
     */
    fun showMessage(@StringRes message : Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Snackbar.make(bottom_navigation, message, Snackbar.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * To show a message
     */
    fun showMessage(message : String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Snackbar.make(bottom_navigation, message, Snackbar.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
