package kasibhatla.dev.watchOS;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        //startRecyclerView();
        File appPath = new File(rootFile , "/Android/data/kasibhatla.dev.watchOS");
        if(!appPath.exists()){
            appPath.mkdir();
        }
        upFile = appPath;

        startCircularRecycler();

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
                    Toast.makeText(FileActivity.this, t.getName()+" pressed", Toast.LENGTH_SHORT).show();
                    currList = t.listFiles();
                    setOnTouchListener();
                }else{
                    Toast.makeText(FileActivity.this, "This is a file", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        mCircleRecyclerView.addOnItemTouchListener(recyclerTouchListener);
    }


    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH h = null;
            if (mCircleRecyclerView.getLayoutManager().canScrollHorizontally()) {
                h = new VH(LayoutInflater.from(FileActivity.this)
                        .inflate(R.layout.item_h, parent, false));
            } else if (mCircleRecyclerView.getLayoutManager().canScrollVertically()) {
                if (mItemViewMode instanceof CircularViewMode)
                    h = new VH(LayoutInflater.from(FileActivity.this)
                            .inflate(R.layout.item_c_v, parent, false));
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
            }



            return h;
        }

        @Override
        public void onBindViewHolder(VH holder, int i) {
            holder.tv.setTextSize(15);
            holder.tv.setText(currList[i].getName());
            /*Glide.with(getContext())
                    .load(mImgList.get(position % mImgList.size()))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(holder.iv);
*/
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
            tv = (TextView) itemView.findViewById(R.id.item_text);
           // iv = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
