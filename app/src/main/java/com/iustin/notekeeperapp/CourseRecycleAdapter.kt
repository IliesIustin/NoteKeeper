package com.iustin.notekeeperapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class CourseRecycleAdapter(private val context: Context,private val courses :List<CourseInfo>) :
    RecyclerView.Adapter<CourseRecycleAdapter.ViewHolder>(){
    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_course_list,p0,false)
        return ViewHolder(itemView)
    }


    override fun getItemCount() = courses.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val course = courses[p1]
        p0.textCourse.text = course.title
        p0.coursePosition = p1
    }

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textCourse = itemView.findViewById<TextView>(R.id.textCourse)
        var coursePosition = 0
        init {
            itemView.setOnClickListener {
                val intent = Intent(context,CourseActivity::class.java)
                intent.putExtra(COURSE_POSITION,coursePosition)
                context.startActivity(intent)
            }
            itemView.setOnLongClickListener{

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Deleting a course!")
                builder.setMessage("Are you sure you want to delete this course ? \n All the notes will be lost! ")
                builder.setIcon(R.drawable.ic_grid_off_pink_24dp)
                builder.setPositiveButton("YES"){
                    dialog,which ->
                    for(course:CourseInfo in courses){
                        if(course.title.equals(courses[coursePosition].title)) {
                            deleteNotes(courses[coursePosition])
                            DataManager.courses.remove(course.courseId!!,course)
                            notifyItemRemoved(coursePosition)
                            (context as ItemsActivity).recreate()


                        }
                    }

                }
                builder.setNegativeButton("NO"){
                        dialog,which ->null
                }
                val dialog: AlertDialog = builder.create()
                notifyItemRangeRemoved(coursePosition,1)
                dialog.show()
                return@setOnLongClickListener true
            }

        }

           private fun deleteNotes(course: CourseInfo) {

               val indexes = ArrayList<Int>()
               val subList = ArrayList<NoteInfo>()
               for(note:NoteInfo in DataManager.notes){
                   var index = DataManager.notes.indexOf(note)
                   indexes.add(index)
                   if(note.course!!.courseId.equals(course.courseId)){
                       subList.add(note)
                   }
               }
               DataManager.notes.removeAll(subList)
               for(index:Int in indexes){
                   notifyItemRemoved(index)
               }

       }

    }

}