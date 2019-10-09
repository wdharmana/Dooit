package com.dooit.app.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Transaction(
    var id: String? = null, var remark: String? = null,
    var photo: String? = null,
    var debit: Int = 0,
    var credit: Int = 0,
    var auth_mode: String? = null,
    @ServerTimestamp
    var created_at: Date? = null
)