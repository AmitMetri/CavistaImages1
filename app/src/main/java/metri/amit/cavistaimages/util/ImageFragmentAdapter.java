package metri.amit.cavistaimages.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import metri.amit.cavistaimages.R;
import metri.amit.cavistaimages.model.Image;
import metri.amit.cavistaimages.view.ImageFragment;

import static metri.amit.cavistaimages.Constants.EXTRA_IMAGE_DATA;

/**
 * Created by amitmetri on 11,November,2020
 */
public class ImageFragmentAdapter extends RecyclerView.Adapter<ImageFragmentAdapter.MyViewHolder> {

    private final ImageFragment imageFragment;
    private final List<Image> imageList;
    private final int spanCount;


    public ImageFragmentAdapter(ImageFragment imageFragment, List<Image> imageList, int spanCount) {
        this.imageFragment = imageFragment;
        this.imageList = imageList;
        this.spanCount = spanCount;
    }

    public void updateList(List<Image> imageList) {
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
        String TAG = "ImageFragmentAdapter";
        Log.d(TAG, "Total items count: " + this.imageList.size());
    }

    public void clearItems() {
        imageList.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("CutPasteId")
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        try{
            Objects.requireNonNull(imageFragment.requireActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }catch (Exception e){
            String TAG = "ImageFragmentAdapter";
            Log.e(TAG, "Error" +e,e );
        }

        int value = displaymetrics.widthPixels / spanCount;

        itemView.findViewById(R.id.imageView).getLayoutParams().width = value;
        itemView.findViewById(R.id.imageView).getLayoutParams().height =value;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final MyViewHolder holder, final int position) {

        if (!imageList.isEmpty()) {
            Glide.with(imageFragment)
                    .load(imageList.get(position).getLink())
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(view -> {
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(holder.imageView, "transitionImage")
                    .build();
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_IMAGE_DATA, imageList.get(position));
            Navigation.findNavController(holder.itemView).navigate(R.id.imageDetailsFragment, bundle, null, extras);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        private MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

}
