package com.sample.firebaseauthapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.sample.firebaseauthapp.model.StateBO
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.interactor.StateCityListIntereactor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class StateCityAdapter(
    mView: StateCityListIntereactor.View,
    mPresenter: StateCityListIntereactor.Presenter,
    stateList: List<StateBO>
    , context: Context
) : BaseExpandableListAdapter() {

    private var view = mView
    private var presenter = mPresenter
    var mStateList = stateList
    var context = context

    override fun getGroup(groupPosition: Int): Any {

        return mStateList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertParentView = convertView
        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mConvertParentView = layoutInflater.inflate(R.layout.layout_stateview, null)
        val tvStateName = mConvertParentView?.findViewById<TextView>(R.id.tvStateName)
        tvStateName?.text=mStateList.get(groupPosition).state_name
        return mConvertParentView!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mStateList[groupPosition].cityBo!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mStateList[groupPosition].cityBo!![childPosition]

    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertChildView = convertView

        val layoutInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mConvertChildView = layoutInflater.inflate(R.layout.layout_city_view, null)
        var tvCityName = mConvertChildView?.findViewById<TextView>(R.id.tvCityName)
        tvCityName?.text=mStateList.get(groupPosition).cityBo?.get(childPosition)?.city_name
        return mConvertChildView!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return mStateList.size
    }
    fun getFilterUsingRX(text: String, mView: StateCityListIntereactor.View?, compositeDisposable: CompositeDisposable) {
        if (mStateList.size == 0)
            mStateList =mView?.getStateList()!!
        if (mStateList.size > 0) {
            val disposable = Observable.fromIterable(mView?.getStateList() as ArrayList<StateBO>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { statelist ->
                    statelist.state_name?.toLowerCase(Locale.ENGLISH)!!.contains(
                        text.toLowerCase(
                            Locale.ENGLISH
                        )
                    ) || statelist.state_name?.toLowerCase(Locale.ENGLISH)!!.contains(
                        text.toLowerCase(
                            Locale.ENGLISH
                        )
                    )
                }
                .toList()
                .subscribe { statelist ->
                    val filterList = ArrayList<StateBO>()
                    filterList.addAll(statelist!!)
                    mView.setAdapter(filterList)
                }
            compositeDisposable.add(disposable)

        }
    }

}
