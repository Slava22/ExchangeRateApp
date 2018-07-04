package andrushkva.com.exchangerate

import android.app.ActionBar
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar_layout.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private val ORDER_ITEMS = "order_items"

    private var newActionBar: View? = null
    private var curreciesAdapter: CurrenciesAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var dragManageAdapter: DragManageAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel()::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar_layout)
        newActionBar = supportActionBar?.customView

        curreciesAdapter = CurrenciesAdapter(applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        dragManageAdapter = DragManageAdapter(curreciesAdapter!!, ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
                ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        itemTouchHelper = ItemTouchHelper(dragManageAdapter)

        val orderCurrencies = getOrderCurrencies()
        if (!orderCurrencies.get(0).equals("")) {
            curreciesAdapter!!.setOrderItems(orderCurrencies)
        }

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = curreciesAdapter
        itemTouchHelper!!.attachToRecyclerView(recyclerView)

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        /*mainActivityViewModel.getDate().observe(this, Observer { dateExRates ->
            //testTextView.setText(dateExRates)
        })*/

        mainActivityViewModel.getCurrencies().observe(this, Observer { currencies ->
            if (curreciesAdapter!!.itemCount == 0) {
                curreciesAdapter!!.addAllCurrencies(currencies!!)
            } else {
                curreciesAdapter!!.refreshData(currencies!!)
            }
            setRefresh(false)
        })

        refreshData()
    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }

    override fun onStop() {
        setOrderCurrencies(curreciesAdapter!!.listCurrenciesId)
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    fun getOrderCurrencies(): List<String> {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val orderItemsText = sPref.getString(ORDER_ITEMS, "")
        return orderItemsText.split(",".toRegex())
    }

    fun setOrderCurrencies(orderItemsList: List<Int>) {
        if (orderItemsList.size > 0) {
            val sPref = getPreferences(Context.MODE_PRIVATE)
            val ed = sPref.edit()
            ed.putString(ORDER_ITEMS, orderItemsList.joinToString(","))
            ed.commit()
        }
    }

    fun refreshData() {
        if (isConnectAvailable()) {
            setRefresh(true)
            mainActivityViewModel.refreshData()
        } else {
            showAlertDialog("No internet connection")
            setRefresh(false)
        }
    }

    fun setRefresh(isRefreshing: Boolean) {
        if (isRefreshing) {
            swipeRefreshLayout.isRefreshing = true
        } else {
            swipeRefreshLayout.isRefreshing = false
            val timeString = mainActivityViewModel.getTime()
            newActionBar!!.time.text = "Обновлено в $timeString"
        }
    }

    fun isConnectAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showAlertDialog(text: String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Ooops!")
        builder.setMessage(text)
        builder.setPositiveButton("Ok") { dialog, which -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        showAlertDialog(event.message)
    }
}