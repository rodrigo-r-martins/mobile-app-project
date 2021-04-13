package com.example.millennialnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Source;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText etSearch;
    private RecyclerView rvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        Log.d("MAIN_ACTIVITY", "initRecyclerView: init recyclerview");

        rvNews = findViewById(R.id.rvNews);
//        if (etSearch.getText().toString().isEmpty()) {
            List<NewsArticle> articleList = getFreshNews();
            NewsAdapter newsAdapter = new NewsAdapter(articleList);
            rvNews.setAdapter(newsAdapter);
            rvNews.setLayoutManager(new LinearLayoutManager(this));
//        } else {
//            List<NewsArticle> searchedArticleList = getNews();
//            NewsAdapter newsAdapter = new NewsAdapter(searchedArticleList);
//            rvNews.setAdapter(newsAdapter);
//            rvNews.setLayoutManager(new LinearLayoutManager(this));
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSettings) {
            openSettingsActivity(item);
            return true;
        }
        if (id == R.id.menuLogIn) {
            openLogInActivity(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLogInActivity(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public List<NewsArticle> getFreshNews() {
        List<NewsArticle> articleList = new ArrayList<NewsArticle>();

        NewsApiClient newsApiClient = new NewsApiClient("8b88ab2e81df4547abfc23f6fc69311c");

        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country("us")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        for (int i = 0; i < response.getArticles().size(); i++) {

                            Source source = response.getArticles().get(i).getSource();
                            String sourceName = source.getName();
                            String author = response.getArticles().get(i).getAuthor();
                            String title = response.getArticles().get(i).getTitle();
                            String description = response.getArticles().get(i).getDescription();
                            String date = response.getArticles().get(i).getPublishedAt();
                            String image = response.getArticles().get(i).getUrlToImage();

                            NewsArticle article = new NewsArticle(source, author, title, description, date, image);
                            articleList.add(article);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                });
        return articleList;
    }

    public List<NewsArticle> getNews() {
        String queryString = etSearch.getText().toString();

        List<NewsArticle> articleList = new ArrayList<NewsArticle>();

        NewsApiClient newsApiClient = new NewsApiClient("8b88ab2e81df4547abfc23f6fc69311c");

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q(queryString)
                        .sortBy("popularity")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        for (int i = 0; i < response.getArticles().size(); i++) {

                            Source source = response.getArticles().get(i).getSource();
                            String sourceName = source.getName();
                            String author = response.getArticles().get(i).getAuthor();
                            String title = response.getArticles().get(i).getTitle();
                            String description = response.getArticles().get(i).getDescription();
                            String date = response.getArticles().get(i).getPublishedAt();
                            String image = response.getArticles().get(i).getUrlToImage();

                            NewsArticle article = new NewsArticle(source, author, title, description, date, image);
                            articleList.add(article);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
        return articleList;
    }
}