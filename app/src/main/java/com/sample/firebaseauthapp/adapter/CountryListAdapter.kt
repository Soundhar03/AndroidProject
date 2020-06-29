package com.sample.firebaseauthapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.activity.StateCityActivity
import com.sample.firebaseauthapp.model.CountryBO
import com.sample.firebaseauthapp.interactor.CountryListInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList


class CountryListAdapter(context: Context,
                         private var country: List<CountryBO>?
) : BaseAdapter() {
    var mContext: Context? = context


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertLayoutView = LayoutInflater.from(mContext).inflate(R.layout.country_list_item, parent, false);
        val countryName= convertLayoutView.findViewById<TextView>(R.id.tvCountryName);
        countryName.text=country?.get(position)?.country_name

        countryName.setOnClickListener {
            val intent=Intent(mContext, StateCityActivity::class.java)
            intent.putExtra("countryName",country?.get(position)?.country_name)
            mContext?.startActivity(intent)
        }
        return convertLayoutView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return country!!.size
    }

    fun searchCountry(text: String, mView: CountryListInteractor.View?, compositeDisposable: CompositeDisposable) {
          if (country?.size == 0)
            country =mView?.getCountryList()
        if (country?.size!! > 0) {
            val disposable = Observable.fromIterable(mView?.getCountryList() as ArrayList<CountryBO>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { countryList ->
                    countryList.country_name?.toLowerCase(Locale.ENGLISH)!!.contains(
                        text.toLowerCase(
                            Locale.ENGLISH
                        )
                    )

                }
                .toList()
                .subscribe { countryList ->
                    val filterList = ArrayList<CountryBO>()
                    filterList.addAll(countryList)
                    mView.setAdapter(filterList)
                }
            compositeDisposable.add(disposable)

        }
    }
}