package com.rishabh.newszone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rishabh.newszone.model.News

class SportsActivity : AppCompatActivity() {
    lateinit var recyclerSports : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayoutsports: SwipeRefreshLayout
//    lateinit var spinner : Spinner
//    lateinit var txtspin : TextView


    lateinit var recyclerAdapter : DashboardRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)
        recyclerSports = findViewById(R.id.recyclersports)
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=e8129de639b948d7830b429a9ac2e74f"
        swipeRefreshLayoutsports = findViewById(R.id.swiperefrshsports)
        layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressbarsports)
        val newsInfoList = ArrayList<News>()
        fetchdatasports(newsInfoList,url)
        swipeRefreshLayoutsports.setOnRefreshListener {
            swipeRefreshLayoutsports.isRefreshing = true
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            Toast.makeText(this,"Refreshing Feed...",Toast.LENGTH_SHORT).show()


            fetchdatasports(newsInfoList,url)
            swipeRefreshLayoutsports.isRefreshing = false
        }


    }
    fun fetchdatasports(newsInfoList: ArrayList<News>, url: String){
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val status = it.getString("status")
                if(status.equals("ok") ){
                    val articles = it.getJSONArray("articles")
                    for (i in 0 until articles.length()) {
                        val newsJsonObject = articles.getJSONObject(i)
                        val newsObject = News(
                            //                            newsJsonObject.getString("source"),
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("title") ,
                            newsJsonObject.getString("description"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage"),
                            newsJsonObject.getString("publishedAt")
                        )


                        newsInfoList.add(newsObject)
                    }
                    recyclerAdapter = DashboardRecyclerAdapter(this)
                    recyclerAdapter.updateNews(newsInfoList)
                    recyclerSports.adapter = recyclerAdapter
                    recyclerSports.layoutManager = layoutManager
                    recyclerSports.addItemDecoration(
                        DividerItemDecoration(
                            recyclerSports.context, (layoutManager as LinearLayoutManager).orientation
                        )
                    )
                    progressBar.visibility = View.GONE

                }else{
                    Toast.makeText(this, "some error occured", Toast.LENGTH_LONG
                    ).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "some error occured", Toast.LENGTH_LONG
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

}