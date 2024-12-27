package com.stiki.marcelloilham.guppymarketidn.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stiki.marcelloilham.guppymarketidn.R
import com.stiki.marcelloilham.guppymarketidn.adapter.BestSellerAdapter
import com.stiki.marcelloilham.guppymarketidn.adapter.FilterAdapter
import com.stiki.marcelloilham.guppymarketidn.adapter.GridAdapter
import com.stiki.marcelloilham.guppymarketidn.adapter.SpaceItemDecoration
import com.stiki.marcelloilham.guppymarketidn.menuHome.BottomDialogFilter
import com.stiki.marcelloilham.guppymarketidn.menuHome.DetailActivity
import com.stiki.marcelloilham.guppymarketidn.menuHome.Item
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stiki.marcelloilham.guppymarketidn.menuHome.KeranjangHome
import com.stiki.marcelloilham.guppymarketidn.menuProfile.MyProductsActivity
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var textUserName: TextView
    private lateinit var textGreeting: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var carouselRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var filterHome: ImageView
    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var gridViewRecycler: RecyclerView
    private lateinit var textNoData: TextView
    private lateinit var gridAdapter: GridAdapter

    private val filters = listOf(
        "All", "Solid", "Albino", "Tuxedo", "Grass",
        "Lace", "Cobra", "Mozaik", "Metal Head", "Sword Tail",
        "Round Tail", "Halfmoon", "Crown Tail", "Koi", "Big Ear",
        "Dumbo Ear", "Short Body", "Female"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)
        setupSwipeRefresh()
        setupFirebase()
        setupUserName()
        updateGreeting()
        setupCarousel()
        setupFilterButton()
        setupFilterRecyclerView()
        setupGridViewRecycler()

        return view
    }

    private fun initViews(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        textUserName = view.findViewById(R.id.textUserName)
        textGreeting = view.findViewById(R.id.textGreeting)
        carouselRecyclerView = view.findViewById(R.id.carouselRecyclerView)
        filterHome = view.findViewById(R.id.filterHome)
        filterRecyclerView = view.findViewById(R.id.filterRecyclerView)
        gridViewRecycler = view.findViewById(R.id.gridViewRecycler)
        textNoData = view.findViewById(R.id.textNoData)
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
    }

    private fun setupFirebase() {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    private fun setupUserName() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("user").document(userId).get()
                .addOnSuccessListener { document ->
                    val prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val userName = document.getString("name")
                    textUserName.text = if (userName != null) {
                        "Hi, $userName!"
                    } else {
                        "Hi, ${prefs.getString("name", "User")}!"
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                    textUserName.text = "Hi, User!"
                }
        } else {
            textUserName.text = "Hi, User!"
        }
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        textGreeting.text = when (hour) {
            in 0..11 -> "Good Morning!"
            in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }

    private fun setupCarousel() {
        val images = listOf(
            R.drawable.bestseller1,
            R.drawable.bestseller1,
            R.drawable.bestseller1
        )

        val adapter = BestSellerAdapter(images)
        carouselRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        carouselRecyclerView.layoutManager = layoutManager
        carouselRecyclerView.isNestedScrollingEnabled = false

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(carouselRecyclerView)
    }

    private fun setupFilterButton() {
        val clickAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_click)
        filterHome.setOnClickListener { button ->
            button.startAnimation(clickAnimation)
            BottomDialogFilter().show(parentFragmentManager, "BottomSheetFragment")
        }
    }

    private fun setupFilterRecyclerView() {
        val adapter = FilterAdapter(requireContext(), filters) { filter ->
            applyFilter(filter)
        }
        filterRecyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.layoutManager = layoutManager
        filterRecyclerView.isNestedScrollingEnabled = false

        // Apply item decoration for spacing
        val spaceDecoration = SpaceItemDecoration(12)
        filterRecyclerView.addItemDecoration(spaceDecoration)
    }

    private fun setupGridViewRecycler() {
        val items = listOf(
            Item(R.drawable.skyblue, "Sky Blue","Albino","Rp. 75.000"),
            Item(R.drawable.gsrts, "GSRTS","Round Tail","Rp. 100.000"),
            Item(R.drawable.platinumrd,"Platinum RD","Halfmoon","Rp. 125.000"),
            Item(R.drawable.mssrl, "MSSRL","Metal Head","Rp. 95.000")
        )

        gridAdapter = GridAdapter(requireContext(), items)
        gridViewRecycler.adapter = gridAdapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        gridViewRecycler.layoutManager = layoutManager
        gridViewRecycler.isNestedScrollingEnabled = false

        gridAdapter.setOnItemClickListener(object : GridAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = gridAdapter.getItem(position)
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("ITEM_NAME", item.name)
                    putExtra("ITEM_IMAGE", item.image)
                }
                startActivity(intent)
            }
        })
    }

    private fun applyFilter(filter: String) {
        gridAdapter.filterItems(filter)
        if (gridAdapter.itemCount == 0) {
            gridViewRecycler.visibility = View.GONE
            textNoData.visibility = View.VISIBLE
        } else {
            gridViewRecycler.visibility = View.VISIBLE
            textNoData.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                    // putString(ARG_PARAM2, param2)
                }
            }
    }
}
