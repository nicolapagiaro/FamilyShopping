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
            val NOME_FIELD: String = "nome"
        }
    }

    class UpdateCategorie {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/updateCategorie.php"
            val IDS_LABEL = "ids"
            val ARRAY_CATEGORIE = "categorie"
        }
    }

    class UpdateUtente {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/updateUtenti.php"
            val URL_CHANGE_PSSW = "http://quizapp.000webhostapp.com/familyshopping/changePassword.php"
            val ID_LABEL = "id"
            val CODE_LABEL = "code"
            val IDS_LABEL = "ids"
            val ARRAY_USER = "utenti"
            val NEWPSSW_LABEL = "newPssw"
            val OLDPSSW_LABEL = "oldPssw"
        }
    }

    class UpdateCarrello {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/updateCarrello.php"
            val REMOVEITEM_URL = "http://quizapp.000webhostapp.com/familyshopping/removeCarrelloItem.php"
            val IDS_LABEL = "ids"
            val CODE_LABEL = "code"
            val ROWS_TO_UPDATE = "update"
            val ROWS_TO_ADD = "add"
            val LAST_TIMESTAMP_LABEL = "timestamp"
            val ID: String = "id"
        }
    }

    class UploadCarrelloItem {
        companion object {
            val URL = "http://quizapp.000webhostapp.com/familyshopping/saveCarrello.php"
            val MODE_LABEL = "mode"
            val ITEM_LABEL = "item"
            val MODE_ADD_LIST = 0
            val MODE_SAVE = 1
            val MODE_EDIT = 2
        }
    }
}