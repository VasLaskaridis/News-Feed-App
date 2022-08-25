package com.example.periptero;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.periptero.model.ArticleModel;
import com.example.periptero.model.GsonModel;
import com.example.periptero.network.APIService;
import com.example.periptero.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<List<ArticleModel>> articleList;

    private final MutableLiveData<String> callbackState;

    public MainActivityViewModel(){
        callbackState = new MutableLiveData<>();
        articleList=new MutableLiveData<>();
    }

    public MutableLiveData<List<ArticleModel>> getArticleListObserver(){
        return articleList;
    }

    public MutableLiveData<String> getCallbackStateObserver(){
        return callbackState;
    }

    public void requestArticles(){
        APIService apiService= RetrofitInstance.getRetroClient().create(APIService.class);
        Call<GsonModel> call=apiService.getHeadlines(
                "us",
                "8ff4c198731d45bd8c814e9360d5d92b");

        callbackState.setValue("loading");
        call.enqueue(new Callback<GsonModel>() {
            @Override
            public void onResponse(@NonNull Call<GsonModel> call, @NonNull Response<GsonModel> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    articleList.postValue(response.body().getArticles());
                    callbackState.setValue("success");
                }else{
                    callbackState.setValue("error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<GsonModel> call, @NonNull Throwable t) {
                callbackState.setValue("error");
            }
        });
    }

    public void requestArticlesWithKeyword(String s){
        APIService apiService= RetrofitInstance.getRetroClient().create(APIService.class);
        Call<GsonModel> call=apiService.getSpecificData(
                s,
                "8ff4c198731d45bd8c814e9360d5d92b");
        callbackState.setValue("loading");
        call.enqueue(new Callback<GsonModel>() {
            @Override
            public void onResponse(@NonNull Call<GsonModel> call, @NonNull Response<GsonModel> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    articleList.postValue(response.body().getArticles());
                    callbackState.setValue("success");
                }else{
                    callbackState.setValue("error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<GsonModel> call, @NonNull Throwable t) {
                callbackState.setValue("error");
            }
        });
    }

}
