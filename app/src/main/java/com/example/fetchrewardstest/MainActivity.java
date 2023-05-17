package com.example.fetchrewardstest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.fetchrewardstest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // Main Activity class, onCreate is called when the main activity is being initialized.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Calls the onCreate method of the parent class
        super.onCreate(savedInstanceState);

        // Inflates the xml file associated with the activity. Inflate here basically means to create
        // an object from the data specified in an XML file.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Creates the top action bar. This app only uses this to hold a back button to return to
        // the previous screen.
        setSupportActionBar(binding.toolbar);

        // Creates a NavController object, which is later used to move between fragments.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Configures the action bar to be able to interact with the navController object.
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    // Allows the back arrow in the top action bar to interact with the NacController object and is
    // used to return to the previous screen.
    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}