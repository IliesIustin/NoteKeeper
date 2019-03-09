package com.iustin.notekeeperapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_course.*
import kotlinx.android.synthetic.main.content_course.*

class CourseActivity : AppCompatActivity() {

    private var courseKey = KEY_NOT_SET
    private var coursePosition = POSITION_NOT_SET
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)
        setSupportActionBar(toolbar)

        coursePosition = savedInstanceState?.getInt(COURSE_POSITION, POSITION_NOT_SET) ?:intent.getIntExtra(COURSE_POSITION, POSITION_NOT_SET)
        val keys = DataManager.courses.keys
        if(coursePosition != POSITION_NOT_SET){
            courseKey=keys.elementAt(coursePosition)
            displayCourse()
        }else{
            DataManager.courses.set(ID_NOT_SET, CourseInfo(KEY_NOT_SET,TITLE_NOT_SET))
            courseKey = keys.last()
        }


    }
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(COURSE_POSITION,coursePosition)
    }
    private fun displayCourse(){
        val course = DataManager.courses.get(courseKey)
        courseTitleEditText.setText(course?.title)

    }

    override fun onPause() {
        super.onPause()
        saveCourse()

    }

    private fun saveCourse(){
        val title=courseTitleEditText.text.toString()
        val courseID = title.replace(" ","_")
        val course = CourseInfo(courseID,title)
        DataManager.courses.put(courseKey,course)

    }
}
