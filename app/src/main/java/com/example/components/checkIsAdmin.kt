package com.example.components

import android.content.Intent
import com.example.automain.admin.utils.MenuActivity
import com.example.automain.user.utils.UserMenuActivity

fun checkIsAdmin(callback: (Int) -> Unit) {
    val email = fetchCurrentUserEmail()

    getDocumentByEmailAndDB("users", email) { document ->
        if (document != null) {
            if (document.get("isAdmin") == true) {
                callback(1) // admin
            } else {
                callback(0) // not admin
            }
        } else {
            callback(0) // no document found or error case
        }
    }
}

/*checkIsAdmin { result ->
    if (result == 1) {
        println("User is an admin.")
    } else {
        println("User is not an admin.")
    }
}*/


