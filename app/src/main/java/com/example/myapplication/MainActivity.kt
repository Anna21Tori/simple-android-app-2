package com.example.myapplication

import android.R.attr.duration
import android.R.attr.text
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    private val myReceiver = ConnectivityReceiver()

    companion object {
        val READ_CONTACTS_PERMISSION_REQUEST_CODE = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mian_activity)

        this.initFetchData()
        this.initReadAllContacts()

        //First approach to read network connection state
        /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo;
            Log.d(TAG, "Is connected: ${networkInfo?.isConnected}")
            Log.d(TAG, "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
        */

        registerReceiver(myReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onStop() {
        unregisterReceiver(myReceiver)
        super.onStop()
    }
    private fun initFetchData() {
        val btn = findViewById<Button>(R.id.btn_fetch_data)

        btn.setOnClickListener{

            val url = URL("https://jsonplaceholder.typicode.com/posts")

            GlobalScope.launch {
                with(url.openConnection() as HttpURLConnection) {
                    inputStream.bufferedReader().use {
                        it.lines().forEach { line -> Log.d("ACT", line)
                        }
                    }
                }
            }
        }
    }

    private fun initReadAllContacts() {
        val btn = findViewById<Button>(R.id.btn_read_all_contacts);

        btn.setOnClickListener {

            //request
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )

           //this.loadContacts()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                val toast = Toast.makeText(this /* MyActivity */, "The app doesn't have access to contacts!", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    private fun loadContacts() {
        val contactsCursor = contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val contactId = contactsCursor.getString(idIndex)
                val displayName = contactsCursor.getString(nameIndex)
                Log.d("CONTACTS", "Contact $contactId $displayName")
            }
            contactsCursor.close()
        }
    }
}
