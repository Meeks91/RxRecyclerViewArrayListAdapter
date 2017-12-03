package com.example.micah.rxRecyclerViewArrayListAdaper

import android.app.Activity
import android.support.v7.widget.RecyclerView
import com.example.micah.rxrecyclerviewarraylistadapter.updateEvents.ClearAll
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

/**
* Created by Micah on 20/08/2017.
*/

class RxRecyclerViewArrayList<T>: ArrayList<T> {

   private val disposeBag = CompositeDisposable()
   private val arrayListDataUpdatesSubject = PublishSubject.create<RxRecyclerViewArrayListDataUpdateHolder>()

    constructor(): super()

    constructor(collection: MutableCollection<T>) : super(collection)

    override fun add(element: T): Boolean {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(AddItemUpdate())

        return super.add(element)
    }

    override fun removeAt(index: Int): T {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(RemoveAt(index))

        return super.removeAt(index)
    }

    override fun add(index: Int, element: T) {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(AddItemAtIndexUpdate(index))

        super.add(index, element)
    }

    override fun addAll(elements: Collection<T>): Boolean {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(AddAll(elements.size))

        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(AddAllAtIndex(index, elements.size))

        return super.addAll(index, elements)
    }

    override fun clear() {

        //send data change to trigger recyclerView update
        arrayListDataUpdatesSubject.onNext(ClearAll())

        super.clear()
    }
    /**
     * Binds this RxRecyclerViewArrayList to the specified [rv]. It sets up the the [rv] and inits the
     * subscription to the global [arrayListDataUpdatesSubject] so that data updates can be passed to the created
     * adapter. It also inits a subscription to the adapter so that the onBindViewHolder() updates  can be passed to
     * the given [onDataChange] callback where the caller can update their generic [VH] with the given data [element].
     */
    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified VH: RecyclerView.ViewHolder> bind(rv: RecyclerView, layout: Int, layoutConfig: LayoutConfig, noinline onDataChange: (item: VH, element: T) -> Unit): CompositeDisposable  {

        //create adapter and set up the recyclerView:
        val rvAdapter = RxArrayListRecyclerAdapter<VH, T>(this, layout, VH::class.constructors.first())
        setupRecyclerViewWith(rvAdapter, layoutConfig, rv)

        //subscribe to internal data updates to update the adapter
        initDataUpdatesSubscriptionWith(rvAdapter, rv.context as Activity)

        //subscribe to adapter updates so the bind caller can receive them
        initAdapterUpdatesSubscriptionWith(rvAdapter, onDataChange)

        return disposeBag
    }

    /**
     * Sets up the specified [rv] with the given [rvAdapter] and [layoutConfig]
     */
    private fun <VH: RecyclerView.ViewHolder> setupRecyclerViewWith(rvAdapter: RxArrayListRecyclerAdapter<VH, T>, layoutConfig: LayoutConfig, rv: RecyclerView){

        rv.adapter = rvAdapter
        rv.layoutManager = layoutConfig.generateLayoutManagerUsing(rv.context)
    }

    /**
     * Inits the global [arrayListDataUpdatesSubject] to receive internal
     * data which are passed to the the specified [rvAdapter]
     */
    private fun <VH: RecyclerView.ViewHolder> initDataUpdatesSubscriptionWith(rvAdapter: RxArrayListRecyclerAdapter<VH, T>, activity: Activity){

        //send updates to the rvAdapter:
        arrayListDataUpdatesSubject.subscribe { dataUpdateHolder ->

            activity.runOnUiThread {

                rvAdapter.notifyDataUpdateUsing(dataUpdateHolder)
            }

        }.addTo(disposeBag)
    }

    /**
     * Subscribes to the [rvAdapter.layoutUpdateSubject] to receive onBindViewHolder()
     * updates. These updates are passed to the [onDataChange] callback.
     */
    private fun <VH: RecyclerView.ViewHolder> initAdapterUpdatesSubscriptionWith(rvAdapter: RxArrayListRecyclerAdapter<VH, T>, onDataChange: (item: VH, element: T) -> Unit){

        //receive onBindViewHolder calls from the rvAdapter:
        rvAdapter.layoutUpdateSubject.subscribe { update ->

            //send the onBind notification to the onDataChange callback
            onDataChange(update.default, this.get(update.position))

        }.addTo(disposeBag)
    }
}