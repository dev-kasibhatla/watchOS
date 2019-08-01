package kasibhatla.dev.watchOS;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.khrystal.library.widget.CircleRecyclerView;
import me.khrystal.library.widget.CircularHorizontalBTTMode;
import me.khrystal.library.widget.CircularViewMode;
import me.khrystal.library.widget.CircularViewRTLMode;
import me.khrystal.library.widget.ItemViewMode;

import static java.security.AccessController.getContext;

public class FileActivity extends AppCompatActivity {

    //circular recycler
    private CircleRecyclerView mCircleRecyclerView;
    private ItemViewMode mItemViewMode;
    private LinearLayoutManager mLayoutManager;
    File upFile;

    private static final String TAG = "file-activity";
    File rootFile = Environment.getExternalStorageDirectory();

    ImageButton imageBackButton;
   // LinearLayout fileLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_file);

        initializeParameters();
    }

    protected void initializeParameters(){
       // getSupportActionBar().hide();
        mCircleRecyclerView =  findViewById(R.id.recycleFile);
        mItemViewMode = new CircularViewMode();
        mLayoutManager = new LinearLayoutManager(this);
        imageBackButton = findViewById(R.id.imageBackButton);
        //startRecyclerView();
        File appPath = new File(rootFile , "/Android/data/kasibhatla.dev.watchOS");
        if(!appPath.exists()){
            appPath.mkdir();
        }
        upFile = appPath;

        startCircularRecycler();
        Glide.clear(imageBackButton);
        Glide.with(FileActivity.this)
                    .load(R.drawable.button_back)
                    .bitmapTransform(new CropCircleTransformation(FileActivity.this))
                    .into(imageBackButton);

    }


   // ArrayList<String> fileNames = new ArrayList<>();

    File[] currList;
    protected void startCircularRecycler(){
        mCircleRecyclerView.setLayoutManager(mLayoutManager);
        mCircleRecyclerView.setViewMode(mItemViewMode);
        mCircleRecyclerView.setNeedCenterForce(true);

        mCircleRecyclerView.setNeedLoop(false);

        //get a file list
        currList = rootFile.listFiles();
        //get an arraylist
        /*for(File f:currList){
            fileNames.add(f.getName());
        }*/
        //mCircleRecyclerView.setAdapter(new A());

       /* mCircleRecyclerView.setOnCenterItemClickListener(new CircleRecyclerView.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClick(View v) {
                Toast.makeText(FileActivity.this, "Center clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
       setOnTouchListener();


    }

    A recyclerAdapter = new A();

    RecyclerTouchListener recyclerTouchListener;
    protected void setOnTouchListener (){
        recyclerAdapter.notifyDataSetChanged();
        mCircleRecyclerView.setAdapter(recyclerAdapter);

        mCircleRecyclerView.removeOnItemTouchListener(recyclerTouchListener);
        recyclerTouchListener = new RecyclerTouchListener(FileActivity.this,
        mCircleRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int i) {
                File t = currList[i];
                if(t.isDirectory()){
                   // Toast.makeText(FileActivity.this, t.getName()+" pressed", Toast.LENGTH_SHORT).show();
                    if(t.list().length == 0){
                        //folder is empty
                        currList = new File[1];
                        currList[0] = upFile;
                    }else {
                        currList = t.listFiles();
                    }
                    setOnTouchListener();
                }else{
                    if(t.getAbsolutePath().equals(upFile.getAbsolutePath())){
                        Log.i(TAG,"CLicked empty");
                    }
                    Toast.makeText(FileActivity.this, "Can't open (yet)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        mCircleRecyclerView.addOnItemTouchListener(recyclerTouchListener);
    }

    public void btnBackPressed(View v){
        if(! (currList[0].getParentFile().getAbsolutePath().equals(rootFile+""))) {
            currList = currList[1].getParentFile().getParentFile().listFiles();
            setOnTouchListener();
        }else{
            //Toast.makeText(FileActivity.this, "already root", Toast.LENGTH_SHORT).show();
        }
    }


    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH h = null;

            //useful if you want to change views

            /*if (mCircleRecyclerView.getLayoutManager().canScrollHorizontally()) {
                h = new VH(LayoutInflater.from(FileActivity.this)
                        .inflate(R.layout.item_h, parent, false));
            } else if (mCircleRecyclerView.getLayoutManager().canScrollVertically()) {
                if (mItemViewMode instanceof CircularViewMode) {

                    Log.i(TAG, "using item_h");
                }

                else if (mItemViewMode instanceof CircularViewRTLMode)
                    h = new VH(LayoutInflater.from(FileActivity.this)
                            .inflate(R.layout.item_c_rtl_v, parent, false));
                else if (mItemViewMode instanceof CircularHorizontalBTTMode) {
                    h = new VH(LayoutInflater.from(FileActivity.this)
                            .inflate(R.layout.item_h_btt, parent, false));
                }
                else
                    h = new VH(LayoutInflater.from(FileActivity.this)
                            .inflate(R.layout.item_v, parent, false));
            }*/
            h = new VH(LayoutInflater.from(FileActivity.this)
                    .inflate(R.layout.item_c_v, parent, false));
            return h;
        }

        @Override
        public void onBindViewHolder(VH holder, int i) {
            holder.tv.setTextSize(15);
            if(currList[i] == upFile){
                holder.tv.setText("Empty folder");
            }else {
                holder.tv.setText(currList[i].getName());
            }

            if(currList[i].isDirectory()){
                Glide.with(FileActivity.this)
                        .load(R.drawable.folder_icon)
                        .bitmapTransform(new CropCircleTransformation(FileActivity.this))
                        .into(holder.iv);
            }else{
                Glide.with(FileActivity.this)
                        .load(R.drawable.file_icon)
                        .bitmapTransform(new CropCircleTransformation(FileActivity.this))
                        .into(holder.iv);
            }

        }

        @Override
        public int getItemCount() {
            return currList.length;
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
