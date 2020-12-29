package com.rishabh.newszone

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rishabh.newszone.model.News
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var spinner : Spinner
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var imgShare : ImageButton


    lateinit var recyclerAdapter : DashboardRecyclerAdapter
    val url = "https://newsapi.org/v2/top-headlines?country=in&category=general&apiKey=e8129de639b948d7830b429a9ac2e74f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerDashboard = findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressbar)
        swipeRefreshLayout = findViewById(R.id.swiperefresh)

        val newsInfoList = ArrayList<News>()
        spinner = findViewById(R.id.spin)

        val options = arrayOf(
            "General",
            "Entertainment",
            "Business",
            "Health",
            "Science",
            "Sports",
            "Technology"
        )

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item: String = p0?.getItemAtPosition(p2).toString()


                if (p2 == 0) {
                    fetchdata(newsInfoList, url)


                } else if (p2 == 1) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Entertainmentactivity::class.java)
                    startActivity(intent)

                } else if (p2 == 2) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, BusinessActivity::class.java)
                    startActivity(intent)
                } else if (p2 == 3) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, HeathActivity::class.java)
                    startActivity(intent)
                } else if (p2 == 4) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ScienceActivity::class.java)
                    startActivity(intent)
                } else if (p2 == 5) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, SportsActivity::class.java)
                    startActivity(intent)
                } else if (p2 == 6) {
                    Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, TechnologyActivity::class.java)
                    startActivity(intent)
                } else {

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            Toast.makeText(this@MainActivity,"Refreshing Feed...",Toast.LENGTH_SHORT).show()


            fetchdata(newsInfoList,url)
            swipeRefreshLayout.isRefreshing = false
        }
    }


    fun fetchdata(newsInfoList : ArrayList<News> , url : String ) {
            progressBar.visibility = View.VISIBLE
            val queue = Volley.newRequestQueue(this@MainActivity)


            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {
                    val status = it.getString("status")
                    if (status.equals("ok")) {
                        val articles = it.getJSONArray("articles")
                        for (i in 0 until articles.length()) {
                            val newsJsonObject = articles.getJSONObject(i)
                            val newsObject = News(
                                //                            newsJsonObject.getString("source"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("description"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage"),
                                newsJsonObject.getString("publishedAt")
                            )
                            newsInfoList.add(newsObject)
                        }
                        recyclerAdapter = DashboardRecyclerAdapter(this@MainActivity)
                        recyclerAdapter.updateNews(newsInfoList)
                        recyclerDashboard.adapter = recyclerAdapter
                        recyclerDashboard.layoutManager = layoutManager
                        recyclerDashboard.addItemDecoration(
                            DividerItemDecoration(
                                recyclerDashboard.context,
                                (layoutManager as LinearLayoutManager).orientation
                            )
                        )

                            progressBar.visibility = View.GONE

                    } else {
                        Toast.makeText(
                            this@MainActivity, "some error occured", Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(
                        this@MainActivity, "some error occured", Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                //            @Throws (AuthFailureError)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }

            }
        queue.add(jsonObjectRequest)
    }


    fun shareNews() {
        val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
    }

}