package com.demonstration.table.featurebooking

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class BookingItemAnimator : DefaultItemAnimator() {
    private val animatorMap = HashMap<RecyclerView.ViewHolder, AnimatorState>()

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        val heightAnim: ValueAnimator
        val alphaAnim: ObjectAnimator
        val vh = newHolder as BookingItemViewHolder
        val expandableView = vh.itemView.findViewById<View>(R.id.expandable_view)
        val toHeight: Int
        if (vh.isExpanded()) {
            expandableView.visibility = View.VISIBLE
            expandableView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            toHeight = expandableView.measuredHeight
            alphaAnim = ObjectAnimator.ofFloat(expandableView, "alpha", 1f)
        } else {
            toHeight = 0
            alphaAnim = ObjectAnimator.ofFloat(expandableView, "alpha", 0f)
        }
        heightAnim = ValueAnimator.ofInt(expandableView.height, toHeight)
        heightAnim.addUpdateListener { animation ->
            expandableView.layoutParams.height = animation.animatedValue as Int
            expandableView.requestLayout()
        }
        val animSet = AnimatorSet()
            .setDuration(changeDuration)
        animSet.playTogether(heightAnim, alphaAnim)
        animSet.addListener(object : Animator.AnimatorListener {
            private var isCanceled = false
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (!vh.isExpanded() && !isCanceled) {
                    expandableView.visibility = View.GONE
                }
                dispatchChangeFinished(vh, false)
                animatorMap.remove(newHolder)
            }

            override fun onAnimationCancel(animation: Animator) {
                isCanceled = true
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        val animatorState = animatorMap[newHolder]
        if (animatorState != null) {
            animatorState.animSet.cancel()
            alphaAnim.currentPlayTime = animatorState.alphaAnim.currentPlayTime
            heightAnim.currentPlayTime = animatorState.heightAnim.currentPlayTime
            animatorMap.remove(newHolder)
        }
        animatorMap[newHolder] = AnimatorState(alphaAnim, heightAnim, animSet)
        dispatchChangeStarting(newHolder, false)
        animSet.start()
        return false
    }

    class AnimatorState(
        val alphaAnim: ValueAnimator,
        val heightAnim: ValueAnimator,
        val animSet: AnimatorSet
    )
}