package com.androcrush.todo

import android.content.ContentValues
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_data.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddDataActivity:AppCompatActivity()
{
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.add_data)

        try {
            var bundle: Bundle? = intent.extras
            if (bundle != null) {
                id = bundle.getInt("MainActId", 0)
            }
            if (id != 0) {
                if (bundle != null) {
                    name.setText(bundle.getString("MainActTitle"))
                }
                if (bundle != null) {
                    desc.setText(bundle.getString("MainActContent"))
                }
            }
        } catch (ex: Exception) {
        }

        button2.setOnClickListener {
            var dbManager = DBmanager(this)

            var values = ContentValues()
            values.put("Title", name.text.toString())
            values.put("Content", desc.text.toString())

            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note!", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "Id=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

