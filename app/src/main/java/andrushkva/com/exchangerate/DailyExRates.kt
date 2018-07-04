package andrushkva.com.exchangerate

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "DailyExRates", strict = false)
data class DailyExRates @JvmOverloads constructor(

        @field: Attribute(name = "Date")
        var date: String = "",

        @field: ElementList(entry = "Currency", inline = true, required = false)
        var currencies: List<Currency>

) {
    constructor() : this("", mutableListOf<Currency>())
}

@Root(name = "Currency", strict = false)
data class Currency(

        @field: Attribute(name = "Id")
        var id: Int = 0,

        @field: Element(name = "NumCode")
        var numCode: Int = 0,

        @field: Element(name = "CharCode")
        var charCode: String = "",

        @field: Element(name = "Scale")
        var scale: Int = 0,

        @field: Element(name = "Name")
        var name: String = "",

        @field: Element(name = "Rate")
        var rate: Double = 0.0

)


