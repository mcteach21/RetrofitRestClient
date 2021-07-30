package cda.apps.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cda.apps.retrofit.tools.Repo;
import cda.apps.retrofit.tools.RestApiClient;
import cda.apps.retrofit.tools.RestApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "retrofit";
    private MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("(Retrofit) Api Rest Client");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnStart = findViewById(R.id.btnStartHttpClient);
        btnStart.setOnClickListener(v-> startApiCall());

        handleRecyclerview();
    }

    private void startApiCall() {

        RestApiInterface apiInterface = RestApiClient.getInstance();
        Call<List<Repo>> call = apiInterface.listRepos("mcteach21");
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

                adapter.reset();
                if(response.isSuccessful()) {
                    List<Repo> repos = response.body();
                    repos.forEach(repo -> Log.i(TAG , String.valueOf(repo)));
                    repos.forEach(repo -> adapter.add(repo));
                } else {
                    Log.i(TAG , String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Nothing implemented yet! :)", Toast.LENGTH_SHORT).show();
                break;
            default:
                finish();
        }
        return true;
    }


    /**
     * List
     */
    private void handleRecyclerview() {
        /**
         * RecyclerView (list items : users)
         */
        RecyclerView recyclerview = findViewById(R.id.list);
        ListItemClickListener item_listener = (id) -> {
            Toast.makeText(this, "You clicked item #"+id, Toast.LENGTH_SHORT).show();
        };

        adapter = new MyCustomAdapter(item_listener);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down);
        recyclerview.setLayoutAnimation(animation);
    }

    interface ListItemClickListener {
        void onClick(int position);
    }
    private class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder> {
        private static final long FADE_DURATION = 1000;
        List<Repo> items = new ArrayList<Repo>();

        private ListItemClickListener listener;
        public MyCustomAdapter(ListItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item_view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_layout, parent, false);
            return new MyCustomViewHolder(item_view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
            Repo repo = items.get(position);

            holder.name.setText(repo.name);
            holder.url.setText(repo.html_url);

            setScaleAnimation(holder.itemView);
        }

        /**
         * Animtions
         * @return
         */
        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
        private void setScaleAnimation(View view) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void reset() {
            this.items.clear();
            notifyDataSetChanged();
        }

        public void add(Repo repo) {
            this.items.add(repo);
            notifyDataSetChanged();
        }

        class MyCustomViewHolder extends RecyclerView.ViewHolder {
            TextView name, url;
            public MyCustomViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tvName);
                url = itemView.findViewById(R.id.tvUrl);

                itemView.setOnClickListener(v->listener.onClick(getAdapterPosition()));
            }
        }
    }

}