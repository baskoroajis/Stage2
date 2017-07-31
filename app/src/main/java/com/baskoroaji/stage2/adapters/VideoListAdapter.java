package com.baskoroaji.stage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baskoroaji.stage2.R;
import com.baskoroaji.stage2.models.Video;

import java.util.List;

/**
 * Created by Macpro on 7/25/17.
 */

public class VideoListAdapter extends  RecyclerView.Adapter<VideoListAdapter.VideoItemHolder>{

    public interface ClickRecyclerListener{
        void onClickItem(int index);
    }

    private ClickRecyclerListener mClickListener;
    private List<Video> mData;
    private Context mContext;
    public VideoListAdapter(ClickRecyclerListener listener){
        mClickListener = listener;
        Log.d("","assign listener");
    }

    public void SetVideoData(List<Video> data){
        mData = data;
        notifyDataSetChanged();
        Log.d("data is ", ""+mData.size());
    }

    @Override
    public VideoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.video_recycler_item,parent,false);
        VideoListAdapter.VideoItemHolder viewHolder = new VideoListAdapter.VideoItemHolder(view);
        Log.d("adapter created","");
        return viewHolder;
      //  return null;
    }

    @Override
    public void onBindViewHolder(VideoItemHolder holder, int position) {
        holder.Bind(position);
    }

    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;
        }

        return mData.size();

    }


    class VideoItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle;
        public VideoItemHolder(View itemView){
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_video_tittle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClickItem(getAdapterPosition());
            Log.d("click 1","");
        }

        public void Bind(int position){
            mTitle.setText(mData.get(position).name);
           // mTitle.setText("position "+position);

        }
    }
}
