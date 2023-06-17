package com.example.contactaddressdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.loader.content.CursorLoader
import com.example.contactaddressdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission.launch(arrayOf(android.Manifest.permission.READ_CONTACTS))

    }

    override fun onResume() {
        super.onResume()
        binding.btnGet.setOnClickListener {
            getAllContacts()
        }
    }

    fun getAllContacts() {
        val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            } else {
                ContactsContract.Contacts.DISPLAY_NAME
            },
        )

        val PROJECTION2 = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        )
        // The column index for the _ID column
        val CONTACT_ID_INDEX: Int = 0
        // The column index for the CONTACT_KEY column
        val CONTACT_KEY_INDEX: Int = 1

        val cursorLoader =
//            CursorLoader(this, ContactsContract.Data.CONTENT_URI, PROJECTION2, null, null, null)
            CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION2, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        cursor?.let {
            while(it.moveToNext()) {
                val number = cursor.getString(1)
                val name = cursor.getString(2)
                Log.d("number", number)
                Log.d("name", name)
            }
        }
    }

    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var granted = true
        val values = it.values
        values.forEach {
            if(!it) granted = false
        }
    }
}