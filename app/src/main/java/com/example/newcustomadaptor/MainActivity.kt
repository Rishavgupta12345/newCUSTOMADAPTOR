package com.example.newcustomadaptor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

const val FAVORITE_KEY = "FAVORITE_KEY"

class MainActivity : AppCompatActivity() {



    var cityData = fillCityData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadFavourites()

        val cities: ListView = findViewById(R.id.cities)
        var cityAdapter = CityAdapter(cityData)
        cities.adapter = cityAdapter

        cities.setOnItemClickListener{ parent, view, position, id ->
            val city: City? = cityAdapter.getItem(position)
            city?.let {
                city.favorite = !city.favorite
                cityAdapter.notifyDataSetChanged()
            }
            saveFavourites()
        }
    }

    private fun saveFavourites() {

        val favorites = cityData.filter { it.favorite }.map { it.country }

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putStringSet(FAVORITE_KEY,favorites.toSet())
            commit()
        }
    }

    private fun loadFavourites() {
        val sharedPref = getPreferences((Context.MODE_PRIVATE))
        val favorites = sharedPref.getStringSet(FAVORITE_KEY,null)

        favorites?.forEach{country ->
            val city = cityData.find {it.country == country}
            city?.favorite = true
        }
    }

    private fun fillCityData(): Array<City> {

        return arrayOf(
            City("India", "New Delhi"),
            City("USA", "New York"),
            City("France", "Paris"),
            City("Italy", "Rome"),
            City("Netherlands", "Amsterdam")
        )

    }
}