import com.example.betapp.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
     val Base_url="https://works.diginspire.in/jannat/api/"
    val instance:ApiService by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(Base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}