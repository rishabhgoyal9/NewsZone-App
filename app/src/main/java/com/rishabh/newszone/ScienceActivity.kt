package com.rishabh.newszone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rishabh.newszone.model.News

class ScienceActivity : AppCompatActivity() {
    lateinit var recyclerScience : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayoutscience : SwipeRefreshLayout
//    lateinit var spinner : Spinner
//    lateinit var txtspin : TextView


    lateinit var recyclerAdapter : DashboardRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_science)
        swipeRefreshLayoutscience = findViewById(R.id.swiperefreshscience)
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=e8129de639b948d7830b429a9ac2e74f"
        recyclerScience = findViewById(R.id.recyclerscience)
        layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressbarscience)
        val newsInfoList = ArrayList<News>()
        fetchdatabus(newsInfoList,url)
        swipeRefreshLayoutscience.setOnRefreshListener {
            swipeRefreshLayoutscience.isRefreshing = true
//            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            Toast.makeText(this,"Refreshing Feed...",Toast.LENGTH_SHORT).show()


            fetchdatabus(newsInfoList,url)
            swipeRefreshLayoutscience.isRefreshing = false
        }

    }
    fun fetchdatabus(newsInfoList: ArrayList<News>, url: String) {
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
                    recyclerScience.adapter = recyclerAdapter
                    recyclerScience.layoutManager = layoutManager
                    recyclerScience.addItemDecoration(
                        DividerItemDecoration(
                            recyclerScience.context, (layoutManager as LinearLayoutManager).orientation
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