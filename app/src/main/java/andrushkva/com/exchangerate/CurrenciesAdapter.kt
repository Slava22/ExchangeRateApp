package andrushkva.com.exchangerate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrenciesAdapter(val context: Context) : RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    var listCurrencies = arrayListOf<Currency>()
    var listCurrenciesId = arrayListOf<Int>()

    fun addAllCurrencies(listItems: List<Currency>) {
        if (listCurrenciesId.size > 0) {
            val iteratorListItems = listItems.iterator()
            val listItemsId = arrayListOf<Int>()
            iteratorListItems.forEach {
                listItemsId.add(it.id)
            }
            val iteratorListCurrenciesId = listCurrenciesId.iterator()
            var itPosition = 0
            iteratorListCurrenciesId.forEach {
                val positionItem = listItemsId.indexOf(it)
                if (positionItem >= 0) {
                    listCurrencies.add(itPosition, listItems.get(positionItem))
                } else {
                    listCurrencies.add(listItems.get(positionItem))
                }
                itPosition++
            }
        } else {
            listCurrencies.addAll(listItems)
            val iterator = listItems.iterator()
            iterator.forEach {
                listCurrenciesId.add(it.id)
            }
        }
        notifyDataSetChanged()
    }

    fun setOrderItems(listOrderItems: List<String>) {
        val iteratorListOrderItems = listOrderItems.iterator()
        iteratorListOrderItems.forEach {
            listCurrenciesId.add(it.toInt())
        }
    }

    fun refreshData(listItems: List<Currency>) {
        val iterator = listItems.iterator()
        iterator.forEach {
            val position = listCurrenciesId.indexOf(it.id)
            if (position >= 0) {
                if (listCurrencies.get(position) != it) {
                    listCurrencies.set(position, it)
                }
            } else {
                listCurrencies.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_currency, parent, false))
    }

    override fun getItemCount(): Int {
        return listCurrencies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = listCurrencies.get(position)
        val charCode = currency.charCode
        val scale = currency.scale
        val name = currency.name
        val rate = currency.rate
        holder.charCode.text = "$charCode"
        holder.textRate.text = "$rate BYN"
        holder.textNameAndScale.text = "$name за $scale ед."
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val charCode = view.charCode
        val textRate = view.text1
        val textNameAndScale = view.text2
    }
}