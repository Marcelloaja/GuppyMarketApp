package com.stiki.marcelloilham.guppymarketidn.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stiki.marcelloilham.guppymarketidn.R
import com.stiki.marcelloilham.guppymarketidn.menuAuction.DetailAuctionActivity
import java.util.concurrent.TimeUnit

class AuctionFragment : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // TextView dan VideoView untuk Lelang Pertama
    private lateinit var tvDays1: TextView
    private lateinit var tvHours1: TextView
    private lateinit var tvMinutes1: TextView
    private lateinit var tvSeconds1: TextView
    private lateinit var videoView1: VideoView
    private lateinit var btnPlayVideo1: ImageButton
    private lateinit var btnDetails1: TextView

    // TextView dan VideoView untuk Lelang Kedua
    private lateinit var tvDays2: TextView
    private lateinit var tvHours2: TextView
    private lateinit var tvMinutes2: TextView
    private lateinit var tvSeconds2: TextView
    private lateinit var videoView2: VideoView
    private lateinit var btnPlayVideo2: ImageButton
    private lateinit var btnDetails2: TextView

    // CountDownTimer untuk Lelang Pertama dan Kedua
    private var countdownTimer1: CountDownTimer? = null
    private var countdownTimer2: CountDownTimer? = null

    // Waktu akhir dalam millis untuk Lelang Pertama dan Kedua
    private var endTimeInMillis1: Long = 0
    private var endTimeInMillis2: Long = 0

    // Daftar lelang
    private val auctionList = arrayListOf(
        AuctionItem(1, "AuctionFragment 1 Title", "AuctionFragment 1 Description"),
        AuctionItem(2, "AuctionFragment 2 Title", "AuctionFragment 2 Description")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auction, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        // Inisialisasi TextView dan VideoView untuk Lelang Pertama
        tvDays1 = view.findViewById(R.id.tvDays1)
        tvHours1 = view.findViewById(R.id.tvHours1)
        tvMinutes1 = view.findViewById(R.id.tvMinutes1)
        tvSeconds1 = view.findViewById(R.id.tvSeconds1)
        videoView1 = view.findViewById(R.id.videoView1)
        btnPlayVideo1 = view.findViewById(R.id.btnPlayVideo1)
        btnDetails1 = view.findViewById(R.id.btnDetails1)

        // Inisialisasi TextView dan VideoView untuk Lelang Kedua
        tvDays2 = view.findViewById(R.id.tvDays2)
        tvHours2 = view.findViewById(R.id.tvHours2)
        tvMinutes2 = view.findViewById(R.id.tvMinutes2)
        tvSeconds2 = view.findViewById(R.id.tvSeconds2)
        videoView2 = view.findViewById(R.id.videoView2)
        btnPlayVideo2 = view.findViewById(R.id.btnPlayVideo2)
        btnDetails2 = view.findViewById(R.id.btnDetails2)

        // Pengaturan SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }

        // Restore waktu akhir dari SharedPreferences untuk Lelang Pertama dan Kedua
        val sharedPreferences =
            requireActivity().getSharedPreferences("auction_prefs", Context.MODE_PRIVATE)
        endTimeInMillis1 =
            sharedPreferences.getLong("end_time1", System.currentTimeMillis() + 86400000)
        endTimeInMillis2 =
            sharedPreferences.getLong("end_time2", System.currentTimeMillis() + 86400000 / 2)

        // Memulai countdown timer untuk Lelang Pertama dan Kedua
        startCountdownTimer1()
        startCountdownTimer2()

        // Setup video view untuk Lelang Pertama
        val videoUri1 =
            Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.video1)
        videoView1.setVideoURI(videoUri1)

        // Setup video view untuk Lelang Kedua
        val videoUri2 =
            Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.video1)
        videoView2.setVideoURI(videoUri2)

        // Mengatur OnClickListener untuk tombol Play/Pause video untuk Lelang Pertama
        btnPlayVideo1.setOnClickListener {
            if (videoView1.isPlaying) {
                videoView1.pause()
                btnPlayVideo1.setImageResource(R.drawable.ic_play)
            } else {
                videoView1.start()
                btnPlayVideo1.setImageResource(R.drawable.ic_pause)
            }
        }

        // Mengatur OnClickListener untuk tombol Play/Pause video untuk Lelang Kedua
        btnPlayVideo2.setOnClickListener {
            if (videoView2.isPlaying) {
                videoView2.pause()
                btnPlayVideo2.setImageResource(R.drawable.ic_play)
            } else {
                videoView2.start()
                btnPlayVideo2.setImageResource(R.drawable.ic_pause)
            }
        }

        // OnClickListener untuk Details more... pada lelang pertama
        btnDetails1.setOnClickListener {
            navigateToAuctionDetail(auctionList[0])
        }

        // OnClickListener untuk Details more... pada lelang kedua
        btnDetails2.setOnClickListener {
            navigateToAuctionDetail(auctionList[1])
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Simpan waktu akhir countdown ke SharedPreferences saat Fragment dihancurkan
        val sharedPreferences =
            requireActivity().getSharedPreferences("auction_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("end_time1", endTimeInMillis1)
            putLong("end_time2", endTimeInMillis2)
            apply()
        }
        // Hentikan countdown timer jika ada
        countdownTimer1?.cancel()
        countdownTimer2?.cancel()
    }

    // Memulai countdown timer untuk Lelang Pertama
    private fun startCountdownTimer1() {
        val currentTime = System.currentTimeMillis()
        val timeDiff = endTimeInMillis1 - currentTime

        countdownTimer1 = object : CountDownTimer(timeDiff, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                tvDays1.text = String.format("%02d", days)
                tvHours1.text = String.format("%02d", hours)
                tvMinutes1.text = String.format("%02d", minutes)
                tvSeconds1.text = String.format("%02d", seconds)
            }

            override fun onFinish() {
                // Handle selesai countdown (opsional)
            }
        }.start()
    }

    // Memulai countdown timer untuk Lelang Kedua
    private fun startCountdownTimer2() {
        val currentTime = System.currentTimeMillis()
        val timeDiff = endTimeInMillis2 - currentTime

        countdownTimer2 = object : CountDownTimer(timeDiff, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                tvDays2.text = String.format("%02d", days)
                tvHours2.text = String.format("%02d", hours)
                tvMinutes2.text = String.format("%02d", minutes)
                tvSeconds2.text = String.format("%02d", seconds)
            }

            override fun onFinish() {
                // Handle selesai countdown (opsional)
            }
        }.start()
    }

    // Metode untuk navigasi ke halaman detail auction
    private fun navigateToAuctionDetail(auctionItem: AuctionItem) {
        val intent = Intent(activity, DetailAuctionActivity::class.java)
        intent.putExtra("auction_id", auctionItem.id)
        intent.putExtra("auction_title", auctionItem.title)
        intent.putExtra("auction_description", auctionItem.description)
        startActivity(intent)
    }

    // Data class untuk AuctionItem
    data class AuctionItem(val id: Int, val title: String, val description: String)
}
