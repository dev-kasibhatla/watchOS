package kasibhatla.dev.watchOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeParameters();
    }

    protected void initializeParameters(){
        getSupportActionBar().hide();
    }

    public void openFileActivity(View v){
        Intent i = new Intent(MainActivity.this, FileActivity.class);
        startActivity(i);
    }


}
