package it.paggiapp.familyshopping.backend

/**
 * Created by nicola on 29/01/18.
 */
class ComunicationInfo {
    class Login {
        companion object {
            val LOGIN_STEP = "login_step"
            val LOGIN_FIRST_STEP = 0
            val LOGIN_SECOND_STEP = 1
            val LOGIN_URL = "http://quizapp.000webhostapp.com/familyshopping/login.php"
            val MAIL_FIELD = "email"
            val PSSW_FIELD = "pssw"
            val MODE_FIELD = "mode"
            val SUCCESS_FIELD = "success"
            val USERINFO_FIELD = "user_info"
            val NEWUSER_ID_FIELD = "id"
        }
    }
}