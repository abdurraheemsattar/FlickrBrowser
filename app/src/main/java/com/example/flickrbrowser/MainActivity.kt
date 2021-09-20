package com.example.flickrbrowser

//import android.content.ContentValues.TAG
import android.graphics.LinearGradient
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrbrowser.R.id.recycler_view
import com.example.flickrbrowser.databinding.ActivityMainBinding
import java.lang.Exception

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete, GetFlickrJsonData.OnDataAvailable {

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    lateinit var recyclerView: RecyclerView



    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate method called")


        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(binding.toolbar)

//        recycler_view.layoutManager = LinearLayoutManager(this)
//        recycler_view.adapter = flickrRecyclerViewAdapter


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        recyclerView = findViewById(recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = flickrRecyclerViewAdapter


        val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne?", "android,oreo", "en-us", true)
        val getRawData = GetRawData(this)
        getRawData.execute(url)

//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        Log.d(TAG,"onCreate method ends")
    }


    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.d(TAG, ".createUri starts")

        var uri = Uri.parse(baseURL)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()

        return uri.toString()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG,"onCreateOptionsMenu method called")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG,"onOptionsItemSelected method called")

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }

//    companion object {
//        private const val TAG = "MainActivity"
//    }

    // MainAvtivity implements onDownloadComplete() interface defined in GetRawData
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete called")

            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            //Download failed
            Log.d(TAG, "onDownloadComplete failed, status is: $status. Error message is: $data")
        }
    }

    // MainAvtivity implements OnDataAvailable() interface defined in GetFlickrJsonData
    override fun OnDataAvailable(data: List<Photo>) {
        Log.d(TAG, "OnDataAvailable called")

        flickrRecyclerViewAdapter.loadNewData(data)

        Log.d(TAG, "OnDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG,"onError called with ${exception.message}" )

    }



















}