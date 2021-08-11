package com.example.newcustomadaptor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

//const val FAVORITE_KEY = "FAVORITE_KEY"
const val CITY_KEY = "CITY_KEY"

class MainActivity : AppCompatActivity() {

    val cityData = mutableListOf<City>()
    lateinit var cityAdapter: CityAdapter

    //var cityData = fillCityData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reset_list.setOnClickListener {
            resetList()
        }

        add_city.setOnClickListener {
            addCity()
        }

        loadCities()


        //loadFavourites()

        val cities: ListView = findViewById(R.id.cities)
        cityAdapter = CityAdapter(cityData)
        cities.adapter = cityAdapter

//        cities.setOnItemClickListener{ parent, view, position, id ->
//            val city: City? = cityAdapter.getItem(position)
//            city?.let {
//                city.favorite = !city.favorite
//                cityAdapter.notifyDataSetChanged()
//            }
//            saveFavourites()
//        }
    }

    private fun loadCities() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val cities = sharedPref.getStringSet(CITY_KEY,null)
        val gsoc = Gson()
        cities?.forEach{
            cityData.add(gsoc.fromJson(it,City::class.java))
        }
        cityData.sortBy { it.name }
    }

    private fun addCity() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.city_add,null)
        val countryName = dialogView.findViewById<EditText>(R.id.city_country)
        val cityName = dialogView.findViewById<EditText>(R.id.city_name)
        with(builder){
            setView(dialogView)
            setTitle("Add city")
            setPositiveButton("Add") { dialog, id ->
                val country = countryName.text.toString()
                val city = cityName.text.toString()
                if (country.isNotBlank() && city.isNotBlank()) {
                    cityData.add(City(country, city))
                    cityData.sortBy { it.name }
                    saveCities()
                }
            }
            setNegativeButton("cancel"
            ) { dialog, which -> }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }



    private fun saveCities() {
        val gson = Gson()
        val cities = cityData.map { gson.toJson(it) }

        val sharedPef = getPreferences(Context.MODE_PRIVATE)
        with(sharedPef.edit()) {
            putStringSet(CITY_KEY, cities.toSet())
            commit()
        }
        cityAdapter.notifyDataSetChanged()
    }

    private fun resetList() {

        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(getString(R.string.confirm_reset))
            setMessage(getString(R.string.confirm_reset_message))

            setPositiveButton(getString(R.string.yes)
            ) { dialog, which ->
                cityData.clear()
                saveCities()
            }
            setNegativeButton("No"
            ) { dialog, which -> }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}

//    private fun saveFavourites() {
//
//        val favorites = cityData.filter { it.favorite }.map { it.country }
//
//        val sharedPref = getPreferences(Context.MODE_PRIVATE)
//        with(sharedPref.edit()){
//            putStringSet(FAVORITE_KEY,favorites.toSet())
//            commit()
//        }
//    }
//
//    private fun loadFavourites() {
//        val sharedPref = getPreferences((Context.MODE_PRIVATE))
//        val favorites = sharedPref.getStringSet(FAVORITE_KEY,null)
//
//        favorites?.forEach{country ->
//            val city = cityData.find {it.country == country}
//            city?.favorite = true
//        }
//    }

//    private fun fillCityData(): Array<City> {
//
//        return arrayOf(
//            City("India", "New Delhi"),
//            City("USA", "New York"),
//            City("France", "Paris"),
//            City("Italy", "Rome"),
//            City("Netherlands", "Amsterdam")
//        )
//
//    }
