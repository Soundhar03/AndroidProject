package com.sample.firebaseauthapp.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.adapter.CountryListAdapter
import com.sample.firebaseauthapp.model.CountryBO
import com.sample.firebaseauthapp.interactor.CountryListInteractor
import com.sample.firebaseauthapp.presenter.CountryPesenter
import com.sample.firebaseauthapp.databinding.ActivityCountryListBinding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_country_list.*
import java.util.ArrayList


class CountryActivity : AppCompatActivity(),
    CountryListInteractor.View {

    private lateinit var binding: ActivityCountryListBinding
    private lateinit var mView: CountryListInteractor.View
    private lateinit var adapter: CountryListAdapter
    private var mPresenter: CountryListInteractor.Presenter? = null
    lateinit var compositeDisposable : CompositeDisposable
    private var country: List<CountryBO>? =ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_country_list)

        //Toolbar
        binding.toolbar?.setTitle(getString(R.string.country_list))
        binding.toolbar?.setTitleTextColor(resources.getColor(R.color.white))

        //init
        mView=this
        mPresenter= CountryPesenter()
        mPresenter?.setView(this)
        compositeDisposable= CompositeDisposable()

       //call Country list api
        mPresenter?.callCountryListApi()!!

        //search option
        binding.edtSearch?.apply {
            setHintTextColor(resources.getColor(R.color.grey))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(searchQuery: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val text = searchQuery.toString()
                    if (text.isEmpty()){
                        mPresenter?.callCountryListApi()!!
                    }
                    else{
                        adapter.searchCountry(text, mView, compositeDisposable)

                    }
                }
            })
        }

    }

    override fun setAdapter(country: List<CountryBO>) {
        adapter = CountryListAdapter(this, country)
        listView.setAdapter(adapter)

    }

    override fun showErrorMessage(msg:String) {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }

    override fun getCountryList(): List<CountryBO> {
        return country!!
    }

    override fun setCountryList(country: List<CountryBO>) {
        this.country=country

    }

    override fun showProgressbar() {
        binding.pbCountry?.visibility= View.VISIBLE
    }

    override fun hidePogressbar() {
        binding.pbCountry?.visibility= View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        binding.edtSearch?.text?.clear()

    }
}
