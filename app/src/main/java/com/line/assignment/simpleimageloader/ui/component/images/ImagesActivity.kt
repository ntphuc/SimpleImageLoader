package com.line.assignment.simpleimageloader.ui.component.images

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import com.line.assignment.simpleimageloader.R
import com.line.assignment.simpleimageloader.data.dto.download.DownloadResult
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem
import com.line.assignment.simpleimageloader.databinding.ImagesActivityBinding
import com.line.assignment.simpleimageloader.ui.ViewModelFactory
import com.line.assignment.simpleimageloader.ui.base.BaseActivity
import com.line.assignment.simpleimageloader.utils.BitmapUtils
import com.line.assignment.simpleimageloader.utils.SimpleImageDownloader
import com.line.assignment.simpleimageloader.utils.observe
import javax.inject.Inject

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by PhucNguyen
 */

class ImagesActivity : BaseActivity() {
    private val PERMISSIONS = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_REQUEST_CODE = 1
    private val DOWNLOAD_FILE_CODE = 2

    @Inject
    lateinit var viewModel: ImagesViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var downloadManager: SimpleImageDownloader

    private lateinit var binding: ImagesActivityBinding

    private var currentIndexImage: Int = 0


    override fun initViewBinding() {
        binding = ImagesActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMovieItem()
    }


    override fun observeViewModel() {
        observe(viewModel.movieData, ::initializeView)
    }

    override fun initializeViewModel() {
        viewModel = viewModelFactory.create(viewModel::class.java)
    }

    private fun initializeView(movieItem: MovieItem) {
        binding.tvName.text = movieItem.title

        binding.ivMovieImage.setOnClickListener(View.OnClickListener {
            if (binding.pbLoading.visibility == GONE) {
                currentIndexImage++
                loadNextImage()
            }
        })

        loadNextImage()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                val imageUrl: String = getCurrentImageUrl()
                if (!TextUtils.isEmpty(imageUrl))
                    refreshImage(imageUrl)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hasPermissions(context: Context?, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            return permissions.all { permission ->
                ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        }

        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && hasPermissions(this, PERMISSIONS)) {
            loadNextImage()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DOWNLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                this?.let { context ->
                    loadNextImage()

                }
            }
        }
    }

    private fun loadNextImage() {
        val imageUrl: String = getCurrentImageUrl()
        if (!TextUtils.isEmpty(imageUrl))
            loadImage(imageUrl)
    }


    private fun getCurrentImageUrl(): String {
        if (viewModel.getListImages()?.size!! > 0) {
            if (currentIndexImage == viewModel.getListImages()?.size!!) {
                currentIndexImage = 0
            }
            return viewModel.getListImages()?.get(currentIndexImage)!!
        }else{
            showToast(getString(R.string.no_data))
        }
        return ""

    }

    private fun loadImage(imageUrl: String) {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            downloadManager.displayImage(imageUrl,binding.ivMovieImage).collect {
                withContext(Dispatchers.Main) {
                    updateView(it)
                }
            }
        }
    }

    private fun refreshImage(imageUrl: String) {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            downloadManager.loadImageNetwork(imageUrl,binding.ivMovieImage).collect {
                withContext(Dispatchers.Main) {
                    updateView(it)
                }
            }
        }
    }

    private fun updateView(it: DownloadResult) {
        when (it) {
            is DownloadResult.Success -> {
                showLoading(false)
                binding.tvDescription.text = ""
                binding.ivMovieImage.setImageBitmap(it.bitmap)

            }

            is DownloadResult.Error -> {
                showLoading(false)
                showToast("Error "+ it.message)

            }

            is DownloadResult.Progress -> {
                binding.tvDescription.text =  "${it.progress} kb downloading "
            }
        }

    }

    private fun showLoading(isShow: Boolean) {
        binding.pbLoading.visibility = if (isShow) VISIBLE else GONE
    }

    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }

}
