package com.example.planner.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation


class TaskAnimator : SimpleItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val anim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        anim.duration = 500
        holder.itemView.startAnimation(anim)
        return true
    }

    override fun animateChange(
        p0: RecyclerView.ViewHolder,
        p1: RecyclerView.ViewHolder,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Int
    ): Boolean {
        return false
    }

    override fun animateMove(holder: RecyclerView.ViewHolder, p1: Int, p2: Int, p3: Int, p4: Int): Boolean {
        val anim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        anim.duration = 500
        holder.itemView.startAnimation(anim)
        return false
    }

    override fun animateRemove(p0: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun endAnimation(p0: RecyclerView.ViewHolder) {

    }

    override fun endAnimations() {

    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun runPendingAnimations() {

    }
}