package com.alhamoly.redditnews.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.Intent.*
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alhamoly.redditnews.R
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow


object Utils {

    fun getVerticalLayoutManager(mContext: Context): LinearLayoutManager =
        object : LinearLayoutManager(mContext, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

    fun getHorizontalLayoutManager(mContext: Context): LinearLayoutManager =
        object : LinearLayoutManager(mContext, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

    fun getGridLayoutManager(
        mContext: Context,
        spanCount: Int,
        orientation: Int
    ): GridLayoutManager =
        object : GridLayoutManager(mContext, spanCount, orientation, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }

    @Suppress("DEPRECATION")
    fun TextView.fromHtml(html: String?) {
        this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    fun isConnected(value: Boolean): Boolean = value

    fun Boolean.toInt() = if (this) 1 else 0

    fun Int.toBoolean() = this == 1

    fun Int.format2(): String = String.format("%.2f", this)
    fun Double.format0(): String = String.format(Locale.ENGLISH, "%.0f", this)
    fun Double.format2(): String = String.format(Locale.ENGLISH, "%.2f", this)
    fun Double.format3(): String = String.format(Locale.ENGLISH, "%.3f", this)

    fun Double.numberFormat2(): String {
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val formatter: NumberFormat = DecimalFormat("#,###", symbols)
        return formatter.format(this).decimalFormat()
    }

    fun Int.numberFormat(): String {
        Log.e(TAG, "numberFormat: o $this")
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val formatter: NumberFormat = DecimalFormat("#,###", symbols)
        val f = formatter.format(this)
        Log.e(TAG, "numberFormat: f $f")
        return f
    }

    fun Long.numberFormat(): String {
        Log.e(TAG, "numberFormat: o $this")
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val formatter: NumberFormat = DecimalFormat("#,###", symbols)
        val f = formatter.format(this)
        Log.e(TAG, "numberFormat: f $f")
        return f
    }


    fun Any.decimalFormat(): String {

        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val df = DecimalFormat("#,###.##", symbols)
        df.roundingMode = RoundingMode.CEILING

        return df.format(this)
    }

    fun Double.decimalFormat3(): String {

        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        val df = DecimalFormat("#,###.###", symbols)
        df.roundingMode = RoundingMode.CEILING

        return df.format(this)
    }


    fun Double.customFormat(): String {

        return if (this.toInt() > 0) this.format(0)
        else {

            var i = 1.0

            while ((this * 10.0.pow(i)).toInt() == 0 && i < 10) {
                i++
            }

            this.format(i.toInt())
        }

    }

    fun Double.format(i: Int): String = String.format(Locale.ENGLISH, "%.${i}f", this)

    fun EditText.showKeyboard(context: Context) {
        this.requestFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun EditText.hideSoftKeyboard(context: Context) {
        this.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }


    fun emptyList(size: Int = 5): ArrayList<String> {

        val arrayList = ArrayList<String>()

        repeat(size) { arrayList.add("item ${it + 1}") }

        return arrayList
    }

    fun TextView.discount(txt: String) {
        this.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = txt
        }
    }

    fun generateRandomString(len: Int = 10): String {
        val alphanumerics = CharArray(26) { (it + 97).toChar() }.toSet()
            .union(CharArray(9) { (it + 48).toChar() }.toSet())
        return (0 until len).map {
            alphanumerics.toList().random()
        }.joinToString("")
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun toastInternet(context: Context) {
        Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_SHORT)
            .show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun call(mContext: Context, phone: String?) {

        if (phone.isNullOrEmpty())
            return

        val i = Intent(ACTION_DIAL)
        i.data = Uri.parse("tel:$phone")
        mContext.startActivity(i)

    }

    fun refreshCurrentFragment(view: View, current_dest: Int, arguments: Bundle?) {
        view.findNavController().navigate(
            current_dest,
            arguments,
            NavOptions.Builder()
                .setPopUpTo(current_dest, true)
                .build()
        )
    }

    fun showSnackbar(mView: View, txt: String) {

        val snackbar = Snackbar.make(mView, txt, Snackbar.LENGTH_SHORT)

        val snackbarView = snackbar.view

//        snackbarView.setBackgroundColor(ContextCompat.getColor(mView.context, R.color.yellow_2))

        val snackbarTextView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView

        snackbarTextView.setTextColor(ContextCompat.getColor(mView.context, R.color.white))

        snackbarTextView.text = txt

        snackbar.show()

    }


    @SuppressLint("NewApi")
    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        activity.recreate()
    }

    @SuppressLint("NewApi")
    fun loadLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    fun String.startWeb(context: Context) {
        try {

            val intent = Intent(ACTION_VIEW, Uri.parse(this))
            var flags = FLAG_ACTIVITY_NO_HISTORY or FLAG_ACTIVITY_MULTIPLE_TASK
            flags =
                flags or FLAG_ACTIVITY_NEW_DOCUMENT or FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(flags)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.not_found), Toast.LENGTH_SHORT)
                .show()
        }

    }

    @SuppressLint("Recycle")
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        return if (cursor == null) { // Source is Dropbox or other similar local file path
            uri.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }


    fun englishNumbers(text: String): String =

        text.replace('٠', '0')
            .replace('١', '1')
            .replace('٢', '2')
            .replace('٣', '3')
            .replace('٤', '4')
            .replace('٥', '5')
            .replace('٦', '6')
            .replace('٧', '7')
            .replace('٨', '8')
            .replace('٩', '9')

    @SuppressLint("InlinedApi")
    fun rateApp(mContext: Context) {

        val uri: Uri = Uri.parse("market://details?id=${mContext.packageName}")
        val goToMarket = Intent(ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            FLAG_ACTIVITY_NO_HISTORY or
                    FLAG_ACTIVITY_NEW_DOCUMENT or
                    FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            mContext.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            mContext.startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${mContext.packageName}")
                )
            )
        }

    }

/*
    fun openGoogleTrackingMap(mContext: Context, from: LatLng, to: LatLng) {

        val geoUri = "http://maps.google.com/maps?saddr=${from.latitude},${from.longitude}" +
                "&daddr=" + "${to.latitude},${to.longitude}"

        val intent = Intent(ACTION_VIEW, Uri.parse(geoUri))
        mContext.startActivity(intent)

    }
*/

    fun <V> Collection<V>.toArrayList(): ArrayList<V> {

        val list = ArrayList<V>()

        this.forEach { list.add(it) }

        return list
    }


    private fun loadMode(darkMode: Boolean) {

        if (darkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    /*NavOptions.Builder navBuilder =  new NavOptions.Builder();
         navBuilder.setEnterAnim(R.anim.slide_left).setExitAnim(R.anim.slide_right).setPopEnterAnim(R.anim.slide_left).setPopExitAnim(R.anim.slide_right);

         //Inside Activity
         NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
         navController.navigate(R.id.destinationFragmentId,null,navBuilder.build());
         //Inside Fragment

         NavHostFragment.findNavController(YoutFragment.this)
                             .navigate(R.id.destinationFragmentId, null, navBuilder.build());*/

    /* try {
            if (this.post.content.length > 120 || this.post.content.lines().size > 3)
                MySpannable.makeTextViewResizable(
                    mContext,
                    binding.postDetailsCon.postTxt,
                    3,
                    mContext.getString(R.string.see_more),
                    true
                )*/

    fun ipAddress(): String {
        var ip = ""
        try {
            val enumNetworkInterfaces: Enumeration<NetworkInterface> = NetworkInterface
                .getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = enumNetworkInterfaces
                    .nextElement()
                val enumInetAddress: Enumeration<InetAddress> = networkInterface
                    .inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()
                    if (inetAddress.isSiteLocalAddress) {
                        ip += inetAddress.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            ip += """
            Something Wrong! $e
            
            """.trimIndent()
        }

        Log.e(TAG, "ipAddress: $ip")

        return ip
    }

    fun Context.share(title: String, msg: String) {
        val share = Intent(ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(EXTRA_TEXT, msg)
        startActivity(createChooser(share, title))
    }

    fun String.copy(mContext: Context) {
        val clipboard: ClipboardManager? =
            getSystemService(mContext, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", this)
        clipboard?.setPrimaryClip(clip)
        toast(mContext, mContext.getString(R.string.copied))
    }

    fun Long.shortenTheValue(): String {
        val n = this
        Log.d(TAG, "n = $n")
        var x = n
        var c = -1
        while (x >= 1000) {
            x /= 1000
            c += 1
        }
        Log.d(TAG, "x = $x")
        Log.d(TAG, "c = $c")
        val s = arrayOf("K", "M", "B", "T", "Q", "E")
        return x.toString() + s[c]

    }

    fun String.findIndexOfFirstDigit(): Int {
        val n = this
        Log.d(TAG, "n = $n")
        var x = n
        var c = 0
        while (x[c] == '0' || x[c] == '.') {
            c += 1
        }
        Log.d(TAG, "x = $x")
        Log.d(TAG, "c = $c")
        return c

    }

    fun isRTLLanguage(activity: Activity): Boolean {
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration

        return config.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

//    fun keys(): ArrayList<CountryKey> {
//        val keys = ArrayList<CountryKey>()
//
//        keys.add(CountryKey("+20", " مصر"))
//        keys.add(CountryKey("+971", " الإمارات"))
//        keys.add(CountryKey("+974", " قطر"))
//        keys.add(CountryKey("+966", " السعودية"))
//        keys.add(CountryKey("+212", " المغرب"))
//        keys.add(CountryKey("+973", " البحرين"))
//        keys.add(CountryKey("+968", " عمان"))
//        keys.add(CountryKey("+965", " الكويت"))
//        keys.add(CountryKey("+962", " الأردن"))
//        keys.add(CountryKey("+963", " سوريا"))
//        keys.add(CountryKey("+216", " تونس"))
//        keys.add(CountryKey("+218", " ليبيا"))
//        keys.add(CountryKey("+970", " فلسطين"))
//        keys.add(CountryKey("+213", " الجزائر"))
//        keys.add(CountryKey("+222", " موريتانيا"))
//        keys.add(CountryKey("+961", " لبنان"))
//        keys.add(CountryKey("+249", " السودان"))
//        keys.add(CountryKey("+964", " العراق"))
//        keys.add(CountryKey("+967", " اليمن"))
//        keys.add(CountryKey("+253", " جيبوتي"))
//        keys.add(CountryKey("+252", " الصومال"))
//        keys.add(CountryKey("+269", " جزرالقمر"))
//
//        return keys
//    }

    private const val TAG = "UtilReference"

}