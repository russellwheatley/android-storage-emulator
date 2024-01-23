package io.flutter.plugins.firebase.tests;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.flutter.plugins.firebase.tests.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "YYYYYYYYY";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(this);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.useEmulator("10.0.2.2", 9199);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseApp app = FirebaseApp.getInstance();
                FirebaseStorage storage = FirebaseStorage.getInstance(app, "gs://flutterfire-e2e-tests-two");
                FirebaseFirestore firestore = FirebaseFirestore.getInstance(app);
                Log.d(TAG, "onClick: ");

                firestore.collection("flutter-tests").whereArrayContains("users", null).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Success");
                        Log.d(TAG, "onClick: " + task.getResult().getDocuments().size());
                    } else {
                        Log.d(TAG, "Failure");
                    }
                });
//                storage.getReference().child("flutter-tests").child("bar.txt").putBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}).addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d(TAG, "Success");
//                    } else {
//                        Log.d(TAG, "Failure");
//                    }
//                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}