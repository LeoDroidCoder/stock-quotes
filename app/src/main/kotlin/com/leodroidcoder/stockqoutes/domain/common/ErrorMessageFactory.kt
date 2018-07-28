package com.leodroidcoder.stockqoutes.domain.common

import android.content.Context
import com.leodroidcoder.stockqoutes.R

/**
 * Created by leonid on 9/26/17.
 */
object ErrorMessageFactory {

    /**
     * Creates error message by error code.
     *
     * @param context Context needed to retrieve String resources.
     * @param code Error code
     */
    fun create(context: Context, code: Int): String {
        var errorMessage: String? = null
        when (code) {
            ErrorCodes.ERROR_GENERIC -> errorMessage = context.getString(R.string.error_message_generic)
            ErrorCodes.ERROR_INTERNET_CONNECTION -> errorMessage = context.getString(R.string.error_message_network)
        }
        return if (errorMessage != null) errorMessage else context.getString(R.string.error_message_generic)
    }
}