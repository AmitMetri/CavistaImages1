package metri.amit.cavistaimages.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import metri.amit.cavistaimages.R;
import metri.amit.cavistaimages.databinding.FragmentImageBinding;
import metri.amit.cavistaimages.model.Image;
import metri.amit.cavistaimages.util.ImageFragmentAdapter;
import metri.amit.cavistaimages.util.Utilities;
import metri.amit.cavistaimages.viewmodel.ImageViewModel;

import static metri.amit.cavistaimages.Constants.EXTRA_IMAGE_LIST;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private static String TAG = "ImageFragment";
    private FragmentImageBinding binding;
    private ImageViewModel imageViewModel;
    private ImageFragmentAdapter adapter;
    private int currentItems, totalItems, scrollOutItems, maxScrollOutItems;
    private GridLayoutManager gridLayoutManager;
    private int page = 1;
    private String queryText = "";
    private Context context;
    private List<Image> imageList;
    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        imageViewModel = new ViewModelProvider(ImageFragment.this)
                .get(ImageViewModel.class);
        // Handle the back button event
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (binding == null) {
            binding = FragmentImageBinding.inflate(inflater, container, false);
            init();
        }
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_IMAGE_LIST, (Serializable) imageList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            binding.imageViewBackground.setVisibility(View.INVISIBLE);
            imageList = (List<Image>) savedInstanceState.getSerializable(EXTRA_IMAGE_LIST);
            adapter.updateList(imageList);

        }
    }

    private void initRecyclerView(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "ORIENTATION_LANDSCAPE");
            adapter = new ImageFragmentAdapter(this, imageList, 4);
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "ORIENTATION_PORTRAIT");
            adapter = new ImageFragmentAdapter(this, imageList, 7);
            gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        }
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initRecyclerView(newConfig.orientation);
    }

    private void init() {
        imageList = new ArrayList<>();
        // Checks the orientation of the screen
        int orientation = this.getResources().getConfiguration().orientation;
        initRecyclerView(orientation);
        initListeners();
        subScribeError();
    }

    private void initListeners() {
        binding.searchView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.recyclerView.setBackgroundColor(getResources().getColor(android.R.color.white, null));
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                if (Utilities.isNetworkAvailable(context)) {
                    observeImages(page, query);
                    queryText = query;
                    binding.searchView.clearFocus();
                    adapter.clearItems();
                    imageList.clear();
                    binding.imageViewBackground.setVisibility(View.INVISIBLE);
                } else {
                    Snackbar.make(binding.progressCircular, R.string.please_check_network, Snackbar.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                return false;
            }
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    currentItems = gridLayoutManager.getChildCount();
                    totalItems = gridLayoutManager.getItemCount();
                    scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                    if ((currentItems + scrollOutItems == totalItems)) {
                        page++;
                        observeImages(page, queryText);
                    }
                }
            }
        });
    }

    //Observe the error data
    private void subScribeError() {
        imageViewModel.getErrorData().observe(getViewLifecycleOwner(), s -> {
            Log.d(TAG, "Error:" + s);
            binding.progressCircular.setVisibility(View.INVISIBLE);
            Snackbar.make(binding.progressCircular, s, Snackbar.LENGTH_LONG).show();
        });
    }

    /* Observes the live data from network response.
     * */
    private void observeImages(int page, String query) {
        if (query != null && !query.isEmpty()) {
            binding.progressCircular.setVisibility(View.VISIBLE);
            imageViewModel.fetchImages(page, query).observe(getViewLifecycleOwner(), imageList -> {
                Log.d(TAG, "Success");
                binding.progressCircular.setVisibility(View.INVISIBLE);
                adapter.updateList(imageList);
            });
        }
    }

}