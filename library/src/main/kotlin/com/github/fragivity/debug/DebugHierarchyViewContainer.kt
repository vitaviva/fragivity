package com.github.fragivity.debug

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.res.use
import com.github.fragivity.R
import java.util.*

internal class DebugHierarchyViewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    private val hScrollView = HorizontalScrollView(context)
    private val linearLayout = LinearLayout(context)

    init {
        linearLayout.orientation = LinearLayout.VERTICAL
        hScrollView.addView(linearLayout)
        addView(hScrollView)
    }

    private val itemHeight = 50.dp
    private val itemPadding = 16.dp

    private val titleLayout by lazy(LazyThreadSafetyMode.NONE) {
        val titleLayout = LinearLayout(context).apply {
            setPadding(24.dp, 25.dp, 0, 8.dp)
            orientation = LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val title = TextView(context).apply {
            @SuppressLint("SetTextI18n")
            text = "Stack"
            textSize = 20f
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER_VERTICAL }
        }
        titleLayout.addView(title)

        titleLayout
    }

    fun bindFragmentRecords(fragmentRecordList: List<DebugFragmentRecord>) {
        linearLayout.removeAllViews()
        linearLayout.addView(titleLayout)
        setView(fragmentRecordList)
    }

    private fun setView(
        fragmentRecordList: List<DebugFragmentRecord>,
        hierarchy: Int = 0,
        tvItem: TextView? = null
    ) {
        fragmentRecordList.forEach { fragmentRecord ->
            var tempHierarchy = hierarchy
            val childTvItem = getTextView(fragmentRecord, tempHierarchy)
            childTvItem.setTag(R.id.hierarchy, tempHierarchy)

            val childFragmentRecordList = fragmentRecord.childrenList
            if (childFragmentRecordList.isNotEmpty()) {
                tempHierarchy++
                childTvItem.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_arrow_right_24, 0, 0, 0
                )

                val finalChildHierarchy = tempHierarchy
                childTvItem.setOnClickListener {
                    if (it.getTag(R.id.is_expand) != null) {
                        val isExpand = it.getTag(R.id.is_expand) as? Boolean ?: true
                        if (isExpand) {
                            childTvItem.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_baseline_arrow_right_24, 0, 0, 0
                            )

                            val startIndex = linearLayout.indexOfChild(childTvItem)
                            removeView(startIndex, finalChildHierarchy)
                        } else {
                            handleExpandView(
                                childFragmentRecordList,
                                finalChildHierarchy,
                                childTvItem
                            )
                        }
                        it.setTag(R.id.is_expand, !isExpand)
                    } else {
                        childTvItem.setTag(R.id.is_expand, true)
                        handleExpandView(childFragmentRecordList, finalChildHierarchy, childTvItem)
                    }
                }
            } else {
                childTvItem.setPadding(childTvItem.paddingLeft + itemPadding, 0, itemPadding, 0)
            }

            if (tvItem == null) {
                linearLayout.addView(childTvItem)
            } else {
                linearLayout.addView(childTvItem, linearLayout.indexOfChild(tvItem) + 1)
            }
        }
    }

    private fun getTextView(
        fragmentRecord: DebugFragmentRecord,
        hierarchy: Int
    ): TextView {
        val tvItem = TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight)
            if (hierarchy == 0) {
                setTextColor(Color.parseColor("#333333"))
                textSize = 16f
            }
            gravity = Gravity.CENTER_VERTICAL
            setPadding((itemPadding + hierarchy * itemPadding * 1.5).toInt(), 0, itemPadding, 0)
            compoundDrawablePadding = itemPadding / 2
        }

        context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground)).use {
            tvItem.background = it.getDrawable(0)
        }

        tvItem.text = fragmentRecord.fragmentName
        return tvItem
    }

    private fun handleExpandView(
        childFragmentRecordList: List<DebugFragmentRecord>,
        finalChildHierarchy: Int,
        childTvItem: TextView
    ) {
        setView(childFragmentRecordList, finalChildHierarchy, childTvItem)
        childTvItem.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_arrow_drop_down_24, 0, 0, 0
        )
    }

    private fun removeView(startIndex: Int, hierarchy: Int) {
        val size = linearLayout.childCount

        val deque = ArrayDeque<View>(size - 1 - startIndex)

        var find = false
        for (i in startIndex until size) {
            val child = linearLayout.getChildAt(i)
            val childHierarchy = child.getTag(R.id.hierarchy) as? Int ?: continue
            if (childHierarchy >= hierarchy) {
                deque.offerLast(child)
                find = true
            } else if (find && childHierarchy < hierarchy) {
                break
            }
        }

        while (deque.isNotEmpty()) {
            linearLayout.removeView(deque.pollLast())
        }
    }

    private inline val Int.dp: Int get() = toFloat().dp(context)
}

internal fun Float.dp(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    ).toInt()
}