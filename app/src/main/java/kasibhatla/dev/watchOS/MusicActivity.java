package kasibhatla.dev.watchOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.khrystal.library.widget.CircleRecyclerView;
import me.khrystal.library.widget.CircularViewMode;
import me.khrystal.library.widget.ItemViewMode;

public class MusicActivity extends AppCompatActivity {

    File MusicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    ArrayList<File> musicList  = new ArrayList<>();



    private static final String TAG = "music-player";
    ImageButton btnViewIndicator;
    TextView altTextView;
    LinearLayout linearLayout;

    CircleRecyclerView musicRecycler;
    private ItemViewMode mItemViewMode;
    private LinearLayoutManager mLayoutManager;
    ConstraintLayout musicPlayer;

    private static int smallLinearLayoutWidth = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initializeParameters();
    }

    static boolean CurrentViewList = true;

    A recyclerAdapter = new A();
    RecyclerTouchListener recyclerTouchListener;


    protected void initializeParameters(){
        getSupportActionBar().hide();
        btnViewIndicator = findViewById(R.id.imgBtnViewSwitcher);
        altTextView = findViewById(R.id.txtAltText);

        linearLayout = findViewById(R.id.linearLayout2);
        smallLinearLayoutWidth = linearLayout.getWidth();
        musicPlayer=findViewById(R.id.constraintMusicView);
        musicRecycler =  findViewById(R.id.musicRecycler);
        mLayoutManager = new LinearLayoutManager(MusicActivity.this, RecyclerView.VERTICAL,false);
        musicRecycler.setLayoutManager(mLayoutManager);
        mItemViewMode = new CircularViewMode();
        musicRecycler.setViewMode(mItemViewMode);
        musicRecycler.setNeedCenterForce(true);
        musicRecycler.setNeedLoop(false);
        musicRecycler.setAdapter(recyclerAdapter);

        musicList.addAll(Arrays.asList(MusicDirectory.listFiles()));
        setOnTouchListener();
        showMusicInterface();

    }

    protected void showMusicInterface(){
        //1st check which interface to show
        if(CurrentViewList){
            //make sure music interface is disabled
            Glide.clear(btnViewIndicator);
            Glide.with(MusicActivity.this)
                    .load(R.drawable.icon_music_play)
                    .bitmapTransform(new CropCircleTransformation(MusicActivity.this))
                    .into(btnViewIndicator);
            if(musicList.size()==0){
                altTextView.setVisibility(View.VISIBLE);
                musicRecycler.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                altTextView.setText("No Music Files! Add some now!");
            }else{
                //set linear layout to small again
                ViewGroup.LayoutParams lp = linearLayout.getLayoutParams();
                lp.width=smallLinearLayoutWidth;
                linearLayout.setLayoutParams(lp);
                altTextView.setVisibility(View.GONE);
                musicPlayer.setVisibility(View.GONE);
                musicRecycler.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                //altTextView.setText("Your Music Files will appear here");
            }

        }else{
            ViewGroup.LayoutParams lp = linearLayout.getLayoutParams();
            lp.width= ViewGroup.LayoutParams.MATCH_PARENT;
            linearLayout.setLayoutParams(lp);

            Glide.clear(btnViewIndicator);
            Glide.with(MusicActivity.this)
                    .load(R.drawable.icon_music_list)
                    .bitmapTransform(new CropCircleTransformation(MusicActivity.this))
                    .into(btnViewIndicator);
            altTextView.setVisibility(View.VISIBLE);
            altTextView.setText("Music player interface");
            musicRecycler.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            musicPlayer.setVisibility(View.VISIBLE);


        }
    }

    protected void setOnTouchListener(){
        recyclerAdapter.notifyDataSetChanged();
        musicRecycler.removeOnItemTouchListener(recyclerTouchListener);

        recyclerTouchListener = new RecyclerTouchListener(MusicActivity.this, musicRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int i) {
                Log.i(TAG, "opening + "+musicList.get(i).getName());
            }

            @Override
            public void onLongClick(View view, int i) {

            }
        });
        musicRecycler.addOnItemTouchListener(recyclerTouchListener);

    }

    public void setBtnViewIndicator(View v){
        CurrentViewList = !CurrentViewList;
        showMusicInterface();
    }


    class A extends RecyclerView.Adapter<MusicActivity.VH> {

        @Override
        public MusicActivity.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            MusicActivity.VH h = new MusicActivity.VH(LayoutInflater.from(MusicActivity.this)
                    .inflate(R.layout.item_c_v, parent, false));
            return h;
        }

        @Override
        public void onBindViewHolder(MusicActivity.VH holder, int i) {
            holder.tv.setTextSize(15);
            holder.tv.setText(musicList.get(i).getName());
            Glide.with(MusicActivity.this)
                    .load(R.drawable.icon_music_file)
                    .bitmapTransform(new CropCircleTransformation(MusicActivity.this))
                    .into(holder.iv);

        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }
    }


    class VH extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;

        public VH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_text);
            iv = itemView.findViewById(R.id.item_img);
        }
    }


}
