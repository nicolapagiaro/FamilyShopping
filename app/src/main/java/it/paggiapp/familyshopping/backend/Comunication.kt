package it.paggiapp.familyshopping.backend

/**
 * Class that stores static string for the communication with server
 * Created by nicola on 29/01/18.
 */
class Comunication {
    class Login {
        companion object {
            val LOGIN_STEP = "login_step"
            val LOGIN_FIRST_STEP = 0
            val LOGIN_SECOND_STEP = 1
            val LOGIN_SECOND_STEP2 = 2
            val LOGIN_URL = "http://quizapp.000webhostapp.com/familyshopping/login.php"
            val MAIL_FIELD = "email"
            val PSSW_FIELD = "pssw"
            val MODE_FIELD = "mode"
            val SUCCESS_FIELD = "success"
            val USERINFO_FIELD = "user_info"
            val NEWUSER_ID_FIELD = "id"
            val CODE_LABEL = "code"
        }
    }

    class UpdateUtente {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/updateUtenti.php"
            val ID_LABEL = "id"
            val CODE_LABEL = "code"
            val IDS_LABEL = "ids"
            val ARRAY_USER = "utenti"
        }
    }

    class UpdateCarrello {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/updateCarrello.php"
            val IDS_LABEL = "ids"
            val CODE_LABEL = "code"
            val ROWS_TO_UPDATE = "update"
            val ROWS_TO_ADD = "add"
            val LAST_TIMESTAMP_LABEL = "timestamp"
        }
    }
}