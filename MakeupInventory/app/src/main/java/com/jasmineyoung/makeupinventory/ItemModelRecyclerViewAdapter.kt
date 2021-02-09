package com.jasmineyoung.makeupinventory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.jasmineyoung.makeupinventory.ItemModelFragment.OnListFragmentInteractionListener
import com.jasmineyoung.makeupinventory.models.ItemModel

import kotlinx.android.synthetic.main.fragment_item_model.view.*

/**
 * [RecyclerView.Adapter] that can display a [ItemModel] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ItemModelRecyclerViewAdapter(
    private val mValues: List<ItemModel>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ItemModelRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ItemModel
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.name
        holder.mContentView.text = "- ${item.brand}"
        holder.mSubContentView.text = "Shade: ${item.shade}\tType: ${item.type}"

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.item_name
        val mContentView: TextView = mView.content
        val mSubContentView: TextView = mView.subContent

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
