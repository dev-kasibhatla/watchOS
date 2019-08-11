package kasibhatla.dev.watchOS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {

    private final Integer[] iconSet = {
            R.drawable.icon_files,
            R.drawable.icon_music,
            R.drawable.icon_nav
    };

    ImageButton[] imageButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeParameters();
    }

    protected void initializeParameters(){
        getSupportActionBar().hide();
        imageButtons = new ImageButton[]{
                findViewById(R.id.btnMainOpenFiles),
                findViewById(R.id.btnMainOpenMusic),
                findViewById(R.id.btnMainOpenNav)
                //no extra button for now
        };
        for(int i=0;i<imageButtons.length;i++){
            Glide.with(MainActivity.this)
                    .load(iconSet[i])
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .into(imageButtons[i]);
        }
        }

    public void openFileActivity(View v){
        Intent i = new Intent(MainActivity.this, FileActivity.class);
        startActivity(i);
    }

    public void openMusicActivity(View v){
        Intent i = new Intent(MainActivity.this, MusicActivity.class);
        startActivity(i);
    }
    public void openNavActivity(View v){
        Intent i = new Intent(MainActivity.this, NavActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0;i<imageButtons.length;i++){
            Glide.clear(imageButtons[i]);
            Glide.with(MainActivity.this)
                    .load(iconSet[i])
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .into(imageButtons[i]);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        for(int i=0;i<imageButtons.length;i++){
            Glide.clear(imageButtons[i]);
            Glide.with(MainActivity.this)
                    .load(iconSet[i])
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .into(imageButtons[i]);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for(int i=0;i<imageButtons.length;i++){
            Glide.clear(imageButtons[i]);
            Glide.with(MainActivity.this)
                    .load(iconSet[i])
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .into(imageButtons[i]);
        }
    }
}
