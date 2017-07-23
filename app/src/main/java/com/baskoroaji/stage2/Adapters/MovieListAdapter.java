package com.baskoroaji.stage2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baskoroaji.stage2.Models.Movie;
import com.baskoroaji.stage2.R;
import com.baskoroaji.stage2.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Macpro on 7/1/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {

    public interface ClickRecyclerListener{
        void onClickItem(int index);
    }

    private List<Movie> movies;
    private ClickRecyclerListener mClickListener;
    private Context mContext;

    public MovieListAdapter(ClickRecyclerListener clickListener){
        mClickListener = clickListener;
    }
    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_recycler_item,parent,false);
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (movies == null){
            return 0;
        }
        return movies.size();
    }

    public void setMovieData(List<Movie> data){
        movies = data;
        notifyDataSetChanged();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImagePoster;
        public MovieItemViewHolder(View itemView) {
            super(itemView);

            mImagePoster = (ImageView) itemView.findViewById(R.id.img_thumbnail_recycleritem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClickItem(getAdapterPosition());
        }

        void bind(int listIndex) {

            String imgUrl = NetworkUtils.BASE_URL_IMAGE + movies.get(listIndex).imgPoster;
            Picasso.with(mContext).load(imgUrl).into(mImagePoster);
          //  Log.d("Url image", "bind: "+imgUrl);
        }


    }
}
