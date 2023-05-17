package com.example.fetchrewardstest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FragmentWelcomePage extends Fragment
{
    TextView textView;

    // Inflates the view from the XML layout files.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_welcome_page, container, false);
    }

    // OnViewCreated is called after the inflating of the view is completed.
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        // Creates a reference to the textView object on the welcome page.
        textView = view.findViewById(R.id.WelcomeText);

        // Creates an animation object using one of the anim XML files.
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

        // Plays the animation on the textView, creating a fade in effect.
        textView.startAnimation(fadeIn);

        // Creates a reference to the button on the welcome page, and sets up an event listener
        // That calls when the button is clicked on.
        view.findViewById(R.id.BeginButton).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                // Creates an animation object using another one of the anim XML files.
                Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                // Creates an event listener that calls methods depending on the status of the animation
                fadeOut.setAnimationListener(new Animation.AnimationListener()
                {
                    // Calls at the beginning of the animation
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        // Creates a reference to the button and disables it so that it can no
                        // longer be interacted with.
                        Button button = view.findViewById(R.id.BeginButton);
                        button.setEnabled(false);
                    }

                    // Calls when the Animation finishes.
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        // Uses the NavController object to change to the next fragment.
                        NavHostFragment.findNavController(FragmentWelcomePage.this).navigate(R.id.action_WelcomeFragment_to_DataFragment);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                        //Not Used, but required to be here as part of the event listener.
                    }

                });

                // Finally, plays the animation created on line 45. Calls onAnimationStart at the
                // Beginning of the animation and onAnimationEnd at the end of the animation.
                textView.startAnimation(fadeOut);
            }
        });
    }
}