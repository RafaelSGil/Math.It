package pt.isec.amov.mathit.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetUtils {
    companion object {
        private const val LIMIT = 4096

        @RequiresApi(Build.VERSION_CODES.M)
        fun verifyNetworkStateV3(context: Context): Boolean {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            with(connMgr.activeNetwork) {
                with(connMgr.getNetworkCapabilities(this)) {
                    if (this?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                        this?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                    )
                        return true
                }
            }
            return false
        }
    }
}