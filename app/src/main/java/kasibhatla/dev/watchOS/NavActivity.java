package kasibhatla.dev.watchOS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        initializeParameters();
    }
    protected void initializeParameters(){
        getSupportActionBar().hide();
    }

}
