package com.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClicklistener)
    : RecyclerView.SimpleOnItemTouchListener() {

    private val TAG = "RecyclerItemListener"

    interface OnRecyclerClicklistener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongCLick(view: View, position: Int)
    }


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, ".onInterceptTouchEvent: starts $e")
        return super.onInterceptTouchEvent(rv, e)
    }
}