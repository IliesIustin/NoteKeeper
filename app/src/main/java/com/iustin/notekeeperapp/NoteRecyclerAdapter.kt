package com.iustin.notekeeperapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



class NoteRecyclerAdapter(private val context: Context,private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list,p0,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val note = notes[p1]
        p0.textCourse?.text = note.course?.title
        p0.textTitle?.text = note.title
        p0.notePosition = p1

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textCourse = itemView.findViewById<TextView>(R.id.textCourse)
        val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        var notePosition = 0
        init {
            itemView.setOnClickListener{
                val intent = Intent(context,NoteActivity::class.java)
                intent.putExtra(NOTE_POSITION,notePosition)
                context.startActivity(intent)
            }
            itemView.setOnLongClickListener{

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Deleting a note!")
                builder.setMessage("Are you sure you want to delete this note ?")
                builder.setIcon(R.drawable.ic_note_pink_24dp)
                builder.setPositiveButton("YES"){
                        dialog,which ->
                    val subList = ArrayList<NoteInfo>()
                    for(note:NoteInfo in notes){
                        if(note.title!!.equals(notes[notePosition].title)){
                            subList.add(note)
                        }
                    }
                    if(!subList.isEmpty()){
                        DataManager.notes.removeAll(subList)
                        notifyItemRemoved(notePosition)
                    }
                }
                builder.setNegativeButton("NO"){
                        dialog,which ->null
                }
                val dialog: AlertDialog = builder.create()

                dialog.show()
                return@setOnLongClickListener true
            }
        }
    }

}