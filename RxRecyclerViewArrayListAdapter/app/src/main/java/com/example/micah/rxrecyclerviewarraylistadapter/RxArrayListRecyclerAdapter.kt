package com.example.micah.rxRecyclerViewArrayListAdaper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.micah.rxrecyclerviewarraylistadapter.updateEvents.ClearAll
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KFunction

/**
 * A standard  RecyclerView.Adapter which is of generic type VH.
 * It passes the every call to onBindViewHolder to the layoutUpdateSubject
 * to be handled by any observers who have binded to the RxRecyclerViewArrayList
 *
 */
class RxArrayListRecyclerAdapter<VH:  RecyclerView.ViewHolder, T> (val rxRecyclerViewArrayList: RxRecyclerViewArrayList<T>, val layout: Int, var vhConstructor: KFunction<VH>): RecyclerView.Adapter<VH>() {
    val layoutUpdateSubject = PublishSubject.create<LayoutUpdateHolder<VH>>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {

        val view  = LayoutInflater.from(parent!!.context).inflate(layout, parent, false)

        return vhConstructor.call(view)
    }

    override fun getItemCount(): Int = rxRecyclerViewArrayList.size

    /**
     * handles the correct 'notify' method to use to notify the adapter that
     * the data set has changed.
     *
     * The [dataUpdateHolder] contains whether
     * we're adding or removing data and the index the change is occuring at
     */
    fun notifyDataUpdateUsing(dataUpdateHolder: RxRecyclerViewArrayListDataUpdateHolder){

        when(true) {

            dataUpdateHolder is AddItemUpdate -> notifyItemInserted(itemCount - 1)
            dataUpdateHolder is AddItemAtIndexUpdate -> notifyItemInserted((dataUpdateHolder as AddItemAtIndexUpdate).indexOfUpdate)
            dataUpdateHolder is AddAll -> notifyItemRangeInserted(itemCount - 1, (dataUpdateHolder as AddAll).amountOfAddedItems)
            dataUpdateHolder is AddAllAtIndex -> notifyItemRangeInserted((dataUpdateHolder as AddAllAtIndex).indexOfUpdate, dataUpdateHolder.amountOfAddedItems)
            dataUpdateHolder is RemoveAt -> notifyItemRemoved((dataUpdateHolder as RemoveAt).indexOfUpdate)
            dataUpdateHolder is ClearAll -> notifyItemRangeRemoved(0, itemCount)
        }
    }


    override fun onBindViewHolder(holder: VH, position: Int) {

        layoutUpdateSubject.onNext(LayoutUpdateHolder(holder, position))
    }
}