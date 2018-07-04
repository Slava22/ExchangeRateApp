package andrushkva.com.exchangerate

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel : ViewModel() {
    private var date = MutableLiveData<String>()
    private var listCurrencies = MutableLiveData<List<Currency>>()
    private var apiService = ApiService.provideRetrofit()

    fun refreshData() {
        apiService.getDailyExRates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    date.value = result.date
                    listCurrencies.value = result.currencies
                }, { error ->
                    EventBus.getDefault().post(MessageEvent("Error"))
                })
    }

    fun getDate(): LiveData<String> {
        return date
    }

    fun getCurrencies(): LiveData<List<Currency>> {
        return listCurrencies
    }

    fun getTime(): String {
        val time = System.currentTimeMillis()
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}