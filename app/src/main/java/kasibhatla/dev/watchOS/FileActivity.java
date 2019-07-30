package kasibhatla.dev.watchOS;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.RecyclerListener recyclerListener;
    CustomAdapter rvCustomAdapter;
    LinearLayoutManager rvLinearLayoutManager;

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
        recyclerView =  findViewById(R.id.recycleFile);
        startRecyclerView();
    }

    protected void startRecyclerView(){
        //get a file list
        File[] currList = rootFile.listFiles();
        //get an arraylist
        ArrayList<String> fileNames = new ArrayList<>();
        for(File f:currList){
            fileNames.add(f.getName());
        }
        rvLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLinearLayoutManager);
        rvCustomAdapter = new CustomAdapter(fileNames);
        recyclerView.setAdapter(rvCustomAdapter);
    }
}
