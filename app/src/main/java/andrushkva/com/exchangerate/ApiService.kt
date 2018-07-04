package andrushkva.com.exchangerate

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.SimpleXmlConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("Services/XmlExRates.aspx/")
    fun getDailyExRates(): Observable<DailyExRates>

    companion object Factory {

        fun provideRetrofit(): ApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(provideOkHttpClient())
                    .baseUrl("http://www.nbrb.by/")
                    .build()

            return retrofit.create(ApiService::class.java)
        }

        fun provideOkHttpClient(): OkHttpClient {
            val client = OkHttpClient().newBuilder()
            client.connectTimeout(30, TimeUnit.SECONDS)
            client.readTimeout(30, TimeUnit.SECONDS)
            client.writeTimeout(30, TimeUnit.SECONDS)
            return client.build()
        }
    }
}