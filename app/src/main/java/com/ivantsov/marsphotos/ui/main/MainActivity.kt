package com.ivantsov.marsphotos.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ivantsov.marsphotos.data.api.RetrofitBuilder
import com.ivantsov.marsphotos.data.api.impl.ApiHelperImpl
import com.ivantsov.marsphotos.data.model.PhotoItem
import com.ivantsov.marsphotos.databinding.ActivityMainBinding
import com.ivantsov.marsphotos.ui.main.adapter.PhotoAdapter
import com.ivantsov.marsphotos.ui.main.intent.MainIntent
import com.ivantsov.marsphotos.ui.main.viewmodel.MainViewModel
import com.ivantsov.marsphotos.ui.main.viewstate.MainState
import com.ivantsov.marsphotos.util.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding by lazy { _binding!! }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(
            ApiHelperImpl(
                RetrofitBuilder.apiService
            )
        )
    }
    private var adapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupUI()
        observeViewModel()
        setupClicks()
    }

    private fun setupClicks() {
        binding.buttonFetchUser.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchPhoto)
            }
        }
    }


    private fun setupUI() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {

                    }
                    is MainState.Loading -> {
                        binding.buttonFetchUser.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is MainState.Photos -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonFetchUser.visibility = View.GONE
                        renderList(it.photoList)
                    }
                    is MainState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonFetchUser.visibility = View.VISIBLE
                        it.error?.printStackTrace()
                        Toast.makeText(this@MainActivity, it.error?.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun renderList(photoList: List<PhotoItem>) {
        binding.recyclerView.visibility = View.VISIBLE
        photoList.let { listOfUsers -> listOfUsers.let { adapter.addData(it) } }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}