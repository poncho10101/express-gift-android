package com.alfonsosotelo.expressgift.data.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
/**
 * Base adapter to handle a list of Items
 * T: Entity type of the List
 * VH: ViewHolder inherited from BaseViewHolder
 * @param layoutRes Layout Resource Id
 * @param diffCallback an ItemCallback to differentiate between T objects, if T is a RealmObject and implements BaseEntity you can use DefaultDiffCallback()
 */
abstract class BaseAdapter<T, VH: BaseAdapter<T, VH>.BaseViewHolder>(
    @LayoutRes private var layoutRes: Int,
    diffCallback: DiffUtil.ItemCallback<T>
): ListAdapter<T, VH>(diffCallback) {

    protected val clickSubject = PublishSubject.create<ClickListener>() // record click observable
    protected val longClickSubject = PublishSubject.create<ClickListener>() // record long click observable

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: VH, position: Int) {
        val element = getItem(position)


        holder.itemView.setOnClickListener {
            clickSubject.onNext(ClickListener(element, holder)) // notifies to click observable
        }

        holder.itemView.setOnLongClickListener {
            longClickSubject.onNext(ClickListener(element, holder)) // notifies to long click observable
            true
        }

        holder.bind(element)
        onBindVH(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return instanceViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    /**
     * to get the Click Observable from outside the class
     */
    fun getClickObservable(): Observable<ClickListener> = clickSubject

    /**
     * to get the LongClick Observable from outside the class
     */
    fun getLongClickObservable(): Observable<ClickListener> = longClickSubject


    /**
     * To add things to onBindViewHolder, child can be empty
     */
    protected abstract fun onBindVH(holder: VH, position: Int)

    /**
     * @return a new Instance of ViewHolder
     */
    protected abstract fun instanceViewHolder(view: View): VH

    /**
     * Base ViewHolder
     */
    abstract inner class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        /**
         * In this method you have to load the data from the entity to the view
         */
        abstract fun bind(element: T)
    }

    /**
     * ClickListener class
     * @param entity entity of the record clicked
     * @param viewHolder view holder of the record clicked
     * @param selected this flag is used in selection mode
     */
    inner class ClickListener(val entity: T, val viewHolder: VH, val selected: Boolean = true)

    /**
     * This class is a Default Diff Item Callback for RealmObjects implementing BaseEntity
     */
    class DefaultDiffCallback<T>: DiffUtil.ItemCallback<T>() where T: BaseEntity {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem.getPrimaryKey() != null || newItem.getPrimaryKey() != null)
                oldItem.getPrimaryKey() == newItem.getPrimaryKey()
            else
                oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }
}