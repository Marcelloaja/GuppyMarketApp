package com.stiki.marcelloilham.guppymarketidn.menuAuction

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.stiki.marcelloilham.guppymarketidn.R

class DetailAuctionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_auction)

        val auctionId = intent.getIntExtra("auction_id", -1)
        val auctionTitle = intent.getStringExtra("auction_title")
        val auctionDescription = intent.getStringExtra("auction_description")

        findViewById<TextView>(R.id.tvAuctionTitle)?.text = auctionTitle
        findViewById<TextView>(R.id.tvAuctionDescription)?.text = auctionDescription
    }
}
