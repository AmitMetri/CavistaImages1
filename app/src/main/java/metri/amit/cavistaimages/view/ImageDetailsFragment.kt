package metri.amit.cavistaimages.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import metri.amit.cavistaimages.Constants.EXTRA_IMAGE_DATA
import metri.amit.cavistaimages.R
import metri.amit.cavistaimages.databinding.FragmentImageDetailsBinding
import metri.amit.cavistaimages.db.DatabaseHelper
import metri.amit.cavistaimages.model.Image
import metri.amit.cavistaimages.model.ImageDetails
import metri.amit.cavistaimages.util.CommentsAdapter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ImageDetailsFragment : Fragment() {
    private var binding: FragmentImageDetailsBinding? = null
    private var imageData: Image? = null
    internal var context: Context? = null
    private var commentsAdapter: CommentsAdapter? = null
    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = getContext()
        if (arguments != null) {
            imageData = requireArguments().getSerializable(EXTRA_IMAGE_DATA) as Image?
        }

        // Needed to init the transition animations in the fragment
        this@ImageDetailsFragment.sharedElementEnterTransition = TransitionInflater.from(
            context
        ).inflateTransition(android.R.transition.move)

        // Postpone the enter transition until image is data is loaded
        postponeEnterTransition()
    }

    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageDetailsBinding.inflate(inflater, container, false)
        init()
        return binding!!.root
    }

    private fun init() {
        // Initialize recyclerView to list the user comments
        val imageDetails: List<ImageDetails> = ArrayList()
        commentsAdapter = CommentsAdapter(imageDetails)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerView.layoutManager = layoutManager
        binding!!.recyclerView.adapter = commentsAdapter

        //Initialize the toolbar data and behaviour
        binding!!.toolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_left, null)
        binding!!.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Back pressed")
            NavHostFragment.findNavController(this@ImageDetailsFragment).popBackStack()
        }
        if (imageData!!.title != null && imageData!!.title!!.isNotEmpty()) binding!!.toolbar.title =
            imageData!!.title
        binding!!.toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))

        //Load the image data using Glide
        if (imageData != null) {
            Glide.with(this)
                .load(imageData!!.link)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        //when you know for sure that the data is loaded, you can call
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        //when you know for sure that the data is loaded, you can call
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding!!.imageView)
        }
        dbOperations()
    }

    private fun dbOperations() {
        try {
            databaseHelper = DatabaseHelper.getInstance(requireActivity().application)
        } catch (e: Exception) {
            Log.e(TAG, "Error: $e", e)
        }

        //insert the comment into table on click of Submit
        binding!!.submit.setOnClickListener {
            val imageDetails = ImageDetails()
            imageDetails.id = imageData!!.id
            imageDetails.comment = binding!!.editText.text.toString()
            imageDetails.dateTime = System.currentTimeMillis()
            databaseHelper!!.insertComment(imageDetails)
        }

        //Observe the Live Data from Room DB table.
        databaseHelper!!.fetchImageDetails(imageData!!.id).observeForever { imageDetails ->
            for (imageDetails1 in imageDetails) {
                Log.d(TAG, "ID: " + imageDetails1.id)
                Log.d(TAG, "Comment: " + imageDetails1.comment)
                Log.d(TAG, "DateTime: " + imageDetails1.dateTime)
            }
            commentsAdapter!!.updateList(imageDetails)
            binding!!.editText.text.clear()
        }
    }

    companion object {
        private const val TAG = "ImageDetailsFragment"
    }
}