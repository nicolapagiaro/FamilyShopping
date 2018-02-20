package it.paggiapp.familyshopping.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.heinrichreimersoftware.materialintro.app.SlideFragment
import com.loopj.android.http.*
import kotlinx.android.synthetic.main.fragment_intro_login.*
import cz.msebera.android.httpclient.Header
import it.paggiapp.familyshopping.util.EmailValidator
import it.paggiapp.familyshopping.util.Util
import org.json.JSONObject
import android.support.design.widget.Snackbar
import it.paggiapp.familyshopping.R
import it.paggiapp.familyshopping.backend.Comunication.*
import it.paggiapp.familyshopping.data.Utente

/**
 * Fragment per il login dell'utente
 * Created by nicola on 23/01/18.
 */
class LoginFragment : SlideFragment() {
    var email : String = ""
    var password : String = ""
    var nome : String = ""
    var isLogged : Boolean = false

    companion object {
        /**
         * Gives a new instance of the fragment
         */
        fun newInstance() : LoginFragment {
            return LoginFragment()
        }
    }


    /**
     * onCreate function
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intro_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // listener for the login button
        btn_login.setOnClickListener {


            // check for internet connection
            if(!Util.isOnline(context!!)) {
                Snackbar.make(introActivity.contentView, R.string.no_internet, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // check if the login is valid
            if(isLoginValid()) {
                btn_login.text = getString(R.string.action_login_fetching)
                btn_login.isClickable = false

                val requestParams = RequestParams()
                requestParams.put(Login.LOGIN_STEP, Login.LOGIN_FIRST_STEP)
                requestParams.put(Login.MAIL_FIELD, email)
                requestParams.put(Login.NOME_FIELD, nome)
                requestParams.put(Login.PSSW_FIELD, password)

                val client = AsyncHttpClient()

                client.post(context!!, Login.LOGIN_URL, requestParams, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: JSONObject?) {

                        if(responseString?.getInt(Login.SUCCESS_FIELD) == 0) {
                            // LOGIN OK
                            if(responseString.getInt(Login.MODE_FIELD) == 0) {
                                // new user
                                btn_login.text = getString(R.string.action_login_done)
                                Util.setNewUser(context!!, true)


                                // memorizzo tutti i dati dell'utente nelle sharedPref
                                val id = responseString.getInt(Login.NEWUSER_ID_FIELD)
                                val user = Utente(id, nome, email, 0)
                                Util.saveUser(context!!, user)
                            }
                            else {
                                // old user
                                btn_login.text = getString(R.string.action_login_done)

                                // memorizzo tutti i dati dell'utente nelle sharedPref
                                val userArray = responseString.getJSONObject(Login.USERINFO_FIELD)
                                val user = Utente(userArray.getInt("id"),
                                        userArray.getString("nome"),
                                        userArray.getString("email"),
                                        userArray.getInt("codiceFamiglia"))
                                Util.setNewUser(context!!, false)
                                Util.saveUser(context!!, user)
                                if(et_nome.text.length != 0)
                                    et_nome.setText("")
                                et_nome.append(user.nome)
                            }

                            // l'utente si è loggato
                            isLogged = true
                        }
                        else {
                            // LOGIN ERROR
                            showLoginGeneralError()
                        }
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, errorResponse: String?, throwable: Throwable?) {
                        showLoginGeneralError()
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                        showLoginGeneralError()
                    }
                })
            }
        }
    }

    /**
     * Function to check if the input by the user is valid
     */
    private fun isLoginValid(): Boolean {
        email = et_email.text.toString().trim()
        nome = et_nome.text.toString().trim()
        password = et_password.text.toString().trim()

        if(!EmailValidator.validateEmail(email)) {
            wrapper_email.isErrorEnabled  = true
            wrapper_email.error = getString(R.string.error_invalid_email)
            return false
        }
        else {
            wrapper_email.error = null
            wrapper_email.isErrorEnabled = false
        }

        if(nome.length > 0 && nome.length <= Util.NOME_MIN_CHAR) {
            wrapper_nome.isErrorEnabled  = true
            wrapper_nome.error = getString(R.string.error_invalid_name)
            return false
        }
        else {
            wrapper_nome.error = null
            wrapper_nome.isErrorEnabled = false
        }

        if(password.length <= Util.PSSW_MIN_CHAR) {
            wrapper_password.isErrorEnabled = true
            wrapper_password.error = getString(R.string.error_invalid_password)
            return false
        }
        else {
            wrapper_password.error = null
            wrapper_password.isErrorEnabled = false
        }

        return true
    }

    /**
     * Function that show to user the general error
     */
    private fun showLoginGeneralError() {
        btn_login.isClickable = true
        btn_login.text = getString(R.string.action_login)
        Snackbar.make(introActivity.contentView, R.string.error_login, Snackbar.LENGTH_LONG).show()
    }

    /**
     * If the user is logged than he can go Forward
     */
    override fun canGoForward(): Boolean {
        return isLogged
    }
}