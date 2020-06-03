package com.androcrush.todo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {
    val currentDateTime = LocalDateTime.now()
    private var listNotes = ArrayList<todo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val textView = findViewById<TextView>(R.id.dateView)
        textView.setText(currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)).toString())

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }
        loadQueryAll()

        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listNotes[position].title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadQueryAll() {
        var dbManager = DBmanager(this)
        val cursor = dbManager.queryAll()

        listNotes.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val content = cursor.getString(cursor.getColumnIndex("Content"))

                listNotes.add(todo(id, title, content))

            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listNotes)
        notesAdapter.notifyDataSetChanged()
        lvNotes.adapter = notesAdapter
    }
    inner class NotesAdapter : BaseAdapter {

        private var notesList = ArrayList<todo>()
        private var context: Context? = null

        constructor(context: Context, notesList: ArrayList<todo>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.listview, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]

            vh.tvTitle.text = mNote.title
            vh.tvContent.text = mNote.content


            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private fun updateNote(note: todo) {
        var intent = Intent(this, AddDataActivity::class.java)
        intent.putExtra("MainActId", note.id)
        intent.putExtra("MainActTitle", note.title)
        intent.putExtra("MainActContent", note.content)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
        }

        // with API 26
//        init {
//            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
//            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
//            this.ivEdit = view?.findViewById<ImageView>(R.id.ivEdit) as ImageView
//            this.ivDelete = view?.findViewById<ImageView>(R.id.ivDelete) as ImageView
    }
}
