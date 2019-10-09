package com.dooit.app.util

import java.text.NumberFormat
import java.util.*

class AppUtils {

    companion object {

        fun currency(str: Int?): String {
            val format = NumberFormat.getNumberInstance(Locale.GERMANY)
            return format.format(str)
        }

    }

}

