package com.sample.firebaseauthapp.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.databinding.ActivityStateCityListBinding
import com.sample.firebaseauthapp.interactor.StateCityListIntereactor
import com.sample.firebaseauthapp.presenter.StateCityPresenter
import com.sample.firebaseauthapp.model.CityBo
import com.sample.firebaseauthapp.model.StateBO
import com.sample.firebaseauthapp.adapter.StateCityAdapter
import io.reactivex.disposables.CompositeDisposable

class StateCityActivity : AppCompatActivity(), StateCityListIntereactor.View {

    private lateinit var binding: ActivityStateCityListBinding
    private lateinit var mPresenter: StateCityListIntereactor.Presenter
    private lateinit var mView: StateCityListIntereactor.View
    private lateinit var adapter: StateCityAdapter
    private var stateList: List<StateBO>? = ArrayList()
    private var intialStateList: List<StateBO>? = ArrayList()
    lateinit var compositeDisposable: CompositeDisposable
    var lastExpandedPosition = -1
    var countryName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_state_city_list)

        //Toolbar
        binding.toolbar?.setTitle(getString(R.string.state_city_list))
        binding.toolbar?.setTitleTextColor(resources.getColor(R.color.white))

        //setbackground to statusbar
        window.setStatusBarColor(getResources().getColor(R.color.black));

        //init
        mPresenter = StateCityPresenter()
        compositeDisposable = CompositeDisposable()
        mView = this
        mPresenter.setView(this)

        //intent from previous activity
        countryName = intent.getStringExtra("countryName")!!

        //Apicall to get statelist
        callApi(countryName)

       //Clickaction for Childview
        binding.expandableListView.setOnChildClickListener(object :
            ExpandableListView.OnChildClickListener {
            override fun onChildClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                //Intent to show weather Report
               /* val intent = Intent(this@StateCityActivity, WeatherActivity::class.java)
                intent.putExtra(
                    "city",
                    stateList?.get(groupPosition)?.cityBo?.get(childPosition)?.city_name
                )
                startActivity(intent)*/
                return true
            }
        })

        //Clickaction  for groupview
        binding.expandableListView.setOnGroupClickListener(object :
            ExpandableListView.OnGroupClickListener {
            override fun onGroupClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                id: Long
            ): Boolean {

                //To close previous expaandableview
                if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition
                ) {
                    binding.expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;


                //If citylist is already loaded no need to call api again
                if (stateList?.get(groupPosition)?.isLoaded!!) {
                    binding.expandableListView.expandGroup(groupPosition)
                } else {
                    //If not callapi to get citylist
                    mPresenter.callCityListApi(
                        this@StateCityActivity,
                        stateList?.get(groupPosition)?.state_name!!, groupPosition
                    )

                }

                return true
            }
        })

        //To search
        binding.edtSearch?.apply {
            setHintTextColor(resources.getColor(R.color.grey))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(searchQuery: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val text = searchQuery.toString()
                    if (text.isEmpty()) {
                        setAdapter(intialStateList!!)
                    } else {
                        adapter.getFilterUsingRX(text, mView, compositeDisposable)

                    }

                }
            })
        }


    }

    private fun callApi(countryName: String?) {
        if (!countryName.isNullOrEmpty())
            mPresenter.callStateListApi( countryName)
    }

    override fun setAdapter(stateList: List<StateBO>) {
        this.stateList = stateList
        adapter = StateCityAdapter(
            mView,
            mPresenter,
            stateList,
            this
        )
        binding.expandableListView.setAdapter(adapter)
    }

    override fun addCity(cityList: List<CityBo>, grouppos: Int) {
       //if there is no city for clicked state
        if (cityList.size == 0) {
            Toast.makeText(this, "There is no city in this state", Toast.LENGTH_SHORT).show()
        } else {
            stateList?.get(grouppos)?.cityBo = cityList
            stateList?.get(grouppos)?.isLoaded = true
            adapter.notifyDataSetChanged()
            binding.expandableListView.expandGroup(grouppos)
        }


    }

    override fun getStateList(): List<StateBO> {
        return intialStateList!!
    }

    override fun setStateList(stateList: List<StateBO>) {
        this.intialStateList = stateList
    }

    override fun showPogress() {
        binding.pbState.visibility=View.VISIBLE
    }

    override fun hideProgress() {
        binding.pbState.visibility=View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }

    override fun onResume() {
        super.onResume()
        binding.edtSearch?.text?.clear()
        callApi(countryName)

    }
}
