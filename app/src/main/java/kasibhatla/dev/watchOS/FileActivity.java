package kasibhatla.dev.watchOS;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
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

    LinearLayout linearLayout;

    ImageButton imageBackButton;
    ImageButton[] imageButtons;
    File currPath;

    private static final char MODE_FILE='f', MODE_FOLDER='d';
    private int LINEAR_LAYOUT_SMALL = 80;

    private final Integer[] optionImages = {
            R.drawable.button_copy,
            R.drawable.button_paste,
            R.drawable.delete_final,
            R.drawable.new_folder
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_file);

        initializeParameters();
    }

    protected void initializeParameters(){
        mCircleRecyclerView =  findViewById(R.id.recycleFile);
        mItemViewMode = new CircularViewMode();
        mLayoutManager = new LinearLayoutManager(this);
        imageBackButton = findViewById(R.id.imageBackButton);
        linearLayout = findViewById(R.id.fileLinearLayout);
        imageButtons = new ImageButton[]{
                findViewById(R.id.imageCopyButton),
                findViewById(R.id.imagePasteButton),
                findViewById(R.id.imageDeleteButton),
                findViewById(R.id.imageNewFolderButton)
        };


        File appPath = new File(rootFile , "/Android/data/kasibhatla.dev.watchOS");
        if(!appPath.exists()){
            appPath.mkdir();
        }

        upFile = new File(appPath,"upFile");
        if(!upFile.exists()) {
            Log.i(TAG, "upfile doesn't exist");
            FileOutputStream os;
            try {
                os = new FileOutputStream(upFile);
                os.write("up".getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        startCircularRecycler();
        Glide.clear(imageBackButton);
        Glide.with(FileActivity.this)
                    .load(R.drawable.back_3d)
                    .bitmapTransform(new CropCircleTransformation(FileActivity.this))
                    .into(imageBackButton);

    }

    File[] currList;
    protected void startCircularRecycler(){
        mCircleRecyclerView.setLayoutManager(mLayoutManager);
        mCircleRecyclerView.setViewMode(mItemViewMode);
        mCircleRecyclerView.setNeedCenterForce(true);

        mCircleRecyclerView.setNeedLoop(false);

        //get a file list
        currList = rootFile.listFiles();
        currPath = rootFile;

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
                    currPath = t.getParentFile();
                    //Log.i(TAG,"currpath: " + currPath+"");
                    if(t.list().length == 0){
                        //folder is empty
                        currList = new File[1];
                        currList[0] = upFile;
                    }else {
                        currList = t.listFiles();
                        Log.i(TAG,"opened: " + t.getAbsolutePath());
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
            public void onLongClick(View view, int i) {
                File t = currList[i];
                if(t.isDirectory()){
                    showFileOptions(MODE_FOLDER,t);
                }else{
                    if(t.getAbsolutePath().equals(upFile.getAbsolutePath())){
                        Log.i(TAG,"CLicked empty");
                    }
                    Toast.makeText(FileActivity.this, "Can't open (yet)", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCircleRecyclerView.addOnItemTouchListener(recyclerTouchListener);
    }

    protected void showFileOptions(char mode, File selection){
        //expand linearlayout
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        LINEAR_LAYOUT_SMALL = linearLayout.getWidth();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout.setLayoutParams(layoutParams);



        //make scrollview visible
        final ScrollView scrollView = findViewById(R.id.fileScrollView);
        scrollView.setVisibility(View.VISIBLE);

        TextView tv = findViewById(R.id.txtShowFileName);
        tv.setText(selection.getName());

        //glide like a boss
        for(int i=0;i<imageButtons.length;i++){
            Glide.with(FileActivity.this)
                    .load(optionImages[i])
                    .bitmapTransform(new CropCircleTransformation(FileActivity.this))
                    .into(imageButtons[i]);
        }

        //set new meaning to the back button
        imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);

                //make it small again
                ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
                layoutParams.width = LINEAR_LAYOUT_SMALL;
                linearLayout.setLayoutParams(layoutParams);

                //set old meaning
                imageBackButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if(!currPath.getAbsolutePath().equals(rootFile.getParentFile().getAbsolutePath())) {
                            currList = currPath.listFiles();
                            currPath = currPath.getParentFile();
                            setOnTouchListener();
                        }
                    }
                });
            }
        });

        switch (mode){
            case MODE_FOLDER:
                break;
            case MODE_FILE:
                break;
        }
    }

    public void btnBackPressed(View v){
        if(!currPath.getAbsolutePath().equals(rootFile.getParentFile().getAbsolutePath())) {
            currList = currPath.listFiles();
            currPath = currPath.getParentFile();
            setOnTouchListener();
        }
    }


    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH h = new VH(LayoutInflater.from(FileActivity.this)
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
