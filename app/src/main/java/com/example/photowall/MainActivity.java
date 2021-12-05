package com.example.photowall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.photowall.api.ApiUtilities;
import com.example.photowall.madel.Image;
import com.example.photowall.madel.Search;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Unsplash app
//Access key - 7mszpXRPZ_SAN3qbALolUI3-IPOkFTO0zmj1__FqKeQ
//Secret key - 5vjFBnCSaMB5KxeV7pWbWvVLotIdrv6XSRh0kboc66M

//Get photos -
// https://api.unsplash.com/photos/?client_id=7mszpXRPZ_SAN3qbALolUI3-IPOkFTO0zmj1__FqKeQ&page=1&per_page=30

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Image> images;
    private GridLayoutManager layoutManager;
    private ImageAdapter imageAdapter;
    private int pageNum = 1;
    private ProgressDialog progressDialog;

    private int pagesSize = 30;
    private Boolean isLoading;
    private Boolean isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.imageRecyclerView);
        images = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, images);
        layoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imageAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = layoutManager.getChildCount();
                int totalItem = layoutManager.getItemCount();
                int firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition();

                if(!isLoading && !isLastPage) {
                    if((visibleItem+firstVisibleItemPos >= totalItem)
                    && firstVisibleItemPos >= 0
                    && totalItem >= pagesSize) {
                        pageNum++;
                        getData();
                    }
                }
            }
        });

    }

    private void getData() {
        isLoading = true;
        ApiUtilities.getApiInterface().getImages(pageNum, 30)
                .enqueue(new Callback<List<Image>>() {
                    @Override
                    public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                        if(response.body() != null) {
                            images.addAll(response.body());
                            imageAdapter.notifyDataSetChanged();

                        }
                        isLoading = false;
                        progressDialog.dismiss();

                        if(images.size() > 0) {
                            isLastPage = images.size() < pagesSize;
                        } else isLastPage = true;
                    }

                    @Override
                    public void onFailure(Call<List<Image>> call, Throwable t) {
                        progressDialog.dismiss();;
                        Toast.makeText(MainActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //****Dismiss the dialog
                progressDialog.show();
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void searchData(String query) {
        progressDialog.dismiss();
        ApiUtilities.getApiInterface().searchImage(query)
                .enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {
                        images.clear();
                        images.addAll(response.body().getResults());
                        imageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {

                    }
                });
    }
}