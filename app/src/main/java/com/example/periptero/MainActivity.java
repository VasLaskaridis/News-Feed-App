package com.example.periptero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.periptero.adapters.NewsListAdapter;
import com.example.periptero.databinding.ActivityMainBinding;
import com.example.periptero.model.ArticleModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private List<ArticleModel> articleList;

    private NewsListAdapter adapter;

    private MainActivityViewModel articleViewModel;

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar( binding.toolbar);

        permissionsCheck();

        articleViewModel=new ViewModelProvider(this).get(MainActivityViewModel.class);

        LinearLayoutManager layoutManager=new GridLayoutManager(this,1);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter= new NewsListAdapter();
        adapter.setArticleList(articleList);
        adapter.setOnArticleClickListener(new NewsListAdapter.OnArticleClickListener() {
            @Override
            public void onArticleClick(ArticleModel article, int position) {
                goToUrl(article.getUrl());
            }
        });
        binding.recyclerView.setAdapter(adapter);

        binding.search.setVisibility(View.GONE);

        binding.noResultText.setVisibility(View.INVISIBLE);

        articleViewModel.getArticleListObserver().observe(this, new Observer<List<ArticleModel>>() {
            @Override
            public void onChanged(List<ArticleModel> articleModels) {
                if(articleModels!=null){
                    articleList=articleModels;
                    adapter.setArticleList(articleModels);
                    if(adapter.getItemCount()==0){
                        binding.noResultText.setVisibility(View.VISIBLE);
                    }else{
                        binding.noResultText.setVisibility(View.INVISIBLE);
                    }
                }else{
                    binding.noResultText.setVisibility(View.VISIBLE);
                }
            }
        });
        articleViewModel.requestArticles();

        articleViewModel.getCallbackStateObserver().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("loading")){
                    binding.refreshLayout.setRefreshing(true);
                }else if(s.equals("success")){
                    binding.refreshLayout.setRefreshing(false);
                }else if(s.equals("error")){
                    binding.refreshLayout.setRefreshing(false);
                    Snackbar.make(binding.mainLayout, "A problem occurred with internet connection.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refreshLayout.setRefreshing(true);
                articleViewModel.requestArticles();
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.searchText.getText()!=null){
                    binding.refreshLayout.setRefreshing(true);
                    articleViewModel.requestArticlesWithKeyword(binding.searchText.getText().toString());
                    binding.search.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.searchText.getWindowToken(), 0);
                }
            }
        });
    }


    private void permissionsCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== MY_PERMISSIONS_REQUEST_INTERNET) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.mainLayout, "A problem occurred with internet connection.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(binding.search.getVisibility()== View.VISIBLE){
            binding.search.setVisibility(View.GONE);
        }else{
            binding.search.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        try{
            this.startActivity(launchBrowser);
        }
        catch(android.content.ActivityNotFoundException a){
            Snackbar.make(binding.mainLayout, "A problem occured with this link.", Snackbar.LENGTH_SHORT).show();
        }
    }

}