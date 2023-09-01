package com.example.acrousthetime.ui.components.calendrier

import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class CustomRecylerView(context: Context) : RecyclerView(context) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // print "left" or "right" depending on the side of screen touch was made
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (event.x < width / 2) {
                println("left")
            } else {
                println("right")
            }
        }

        return true // Return 'true' if you consume the touch event, 'false' otherwise
    }
}