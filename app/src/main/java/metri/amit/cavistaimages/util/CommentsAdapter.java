package metri.amit.cavistaimages.util;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import metri.amit.cavistaimages.R;
import metri.amit.cavistaimages.model.ImageDetails;

/**
 * Created by amitmetri on 14,November,2020
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private List<ImageDetails> imageDetailsList;

    public CommentsAdapter(List<ImageDetails> imageDetailsList) {
        this.imageDetailsList = imageDetailsList;
    }

    public void updateList(List<ImageDetails> imageDetailsList) {
        this.imageDetailsList = imageDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item, parent, false);
        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewComment.setText(imageDetailsList.get(position).getComment());
        try {
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(
                    imageDetailsList.get(position).getDateTime(), now, DateUtils.MINUTE_IN_MILLIS);
            holder.textViewDateTime.setText(ago);
        } catch (NullPointerException e) {
            String TAG = "CommentsAdapter";
            Log.e(TAG, "Error: " + e, e);
        }
    }

    @Override
    public int getItemCount() {
        return imageDetailsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewComment, textViewDateTime;

        private MyViewHolder(View view) {
            super(view);
            textViewComment = view.findViewById(R.id.textViewComment);
            textViewDateTime = view.findViewById(R.id.textViewDateTime);
        }
    }
}
