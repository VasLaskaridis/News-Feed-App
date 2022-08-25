package com.example.periptero.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.periptero.R;
import com.example.periptero.model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {

    private Context context;
    private List<ArticleModel> articleList;
    private OnArticleClickListener onArticleClickListener;

    public NewsListAdapter(){
        articleList=new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public NewsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListAdapter.MyViewHolder holder, int position) {
        holder.article_title.setText(this.articleList.get(position).getTitle());
        holder.article_date.setText(this.articleList.get(position).getPublishedAt());
        holder.article_source.setText(this.articleList.get(position).getAuthor());
        Glide.with(context)
                .load(this.articleList.get(position).getUrlToImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.article_image);
    }

    @Override
    public int getItemCount() {
        if(this.articleList!=null){
            return articleList.size();
        }
        return 0;
    }

    public void setArticleList(List<ArticleModel> articleList){
        this.articleList=articleList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView article_title;
        TextView article_source;
        TextView article_date;
        ImageView article_image;
        CardView article_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            article_title = itemView.findViewById(R.id.article_title);
            article_image = itemView.findViewById(R.id.article_image);
            article_source = itemView.findViewById(R.id.article_source);
            article_date = itemView.findViewById(R.id.article_date);
            article_view = itemView.findViewById(R.id.articleView);
            article_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAbsoluteAdapterPosition();
                    if(onArticleClickListener!=null && position!= RecyclerView.NO_POSITION){
                        onArticleClickListener.onArticleClick(articleList.get(position),position);
                    }
                }
            });
        }
    }
    public interface OnArticleClickListener{
        void onArticleClick(ArticleModel article,int position);
    }

    public void setOnArticleClickListener(OnArticleClickListener listener){
        this.onArticleClickListener=listener;
    }
}
