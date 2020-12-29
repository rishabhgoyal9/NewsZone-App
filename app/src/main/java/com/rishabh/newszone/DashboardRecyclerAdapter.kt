package com.rishabh.newszone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.rishabh.newszone.model.News
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class DashboardRecyclerAdapter(val context: Context) : RecyclerView.Adapter<DashboardRecyclerAdapter.Dashboardviewholder>() {

    val items : ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dashboardviewholder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_dashboard_single_row,
            parent,
            false
        )
        val viewHolder = Dashboardviewholder(view)
        return viewHolder
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Dashboardviewholder, position: Int) {
        val news = items[position]
        holder.txtnewsAuthor.text = news.newsAuthor
        holder.txtnewsTitle.text = news.newsTitle
        holder.txtnewsDescription.text = news.newsDescription
        holder.txtnewsTime.text = news.newsPublishedAt
        val url = news.urloflink
        Picasso.get().load(news.newsImage).into(holder.imgNewsImage)
        holder.linearid.setOnClickListener{
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
        holder.imgShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
            Toast.makeText(context, "Sharing.... ", Toast.LENGTH_SHORT).show()

        }

    }
    fun updateNews(updatedNews: ArrayList<News>) {
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

    class Dashboardviewholder(view: View) : RecyclerView.ViewHolder(view){
        val txtnewsAuthor : TextView = view.findViewById(R.id.txtAuthor)
        val txtnewsTitle : TextView = view.findViewById(R.id.txtTitle)
        val txtnewsDescription : TextView = view.findViewById(R.id.txtDesc)
        val txtnewsTime : TextView = view.findViewById(R.id.txtTime)
        val imgNewsImage : ImageView = view.findViewById(R.id.imgDisplay)
        val linearid : LinearLayout = view.findViewById(R.id.linearid)
        val imgShare : ImageView = view.findViewById(R.id.imgShare)
    }
    interface NewsItemClicked {
        fun onItemClicked(item: News)
    }

}

