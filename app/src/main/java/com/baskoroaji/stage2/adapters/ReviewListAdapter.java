package com.baskoroaji.stage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baskoroaji.stage2.R;
import com.baskoroaji.stage2.models.Review;

import java.util.List;

/**
 * Created by Macpro on 7/27/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ItemViewHolder>{

    public interface CLickListener{
        public void onClickItem(int adapterPosition);
    }

    private List<Review> mData;
    private CLickListener mClickListener;
    private Context mContext;

    public ReviewListAdapter(){

    }
    public void setData(List<Review> data){
        mData = data;
        Log.d("data review count ",""+data.size());
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_recyler_item,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.Bind(position);
    }

    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;
        }

        return mData.size();
    }



    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mReviewerNameTextView;
        private TextView mReviewContentTextView;

        public ItemViewHolder(View itemView){
            super(itemView);

            mReviewerNameTextView = (TextView) itemView.findViewById(R.id.tv_reviewer_name);
            mReviewContentTextView = (TextView) itemView.findViewById(R.id.tv_reviewcontent);
        }

        public void Bind(int position){
            mReviewContentTextView.setText(mData.get(position).reviewContent);
            mReviewerNameTextView.setText(mData.get(position).reviewName);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClickItem(getAdapterPosition());
        }
    }
}
