package com.dooit.app.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(var email: String? = null, var name: String?,
                var photo: String? = null,
                var phone: String? = null,
                var auth_mode: String? = null,
                @ServerTimestamp
                var registered_at: Date? = null
                )