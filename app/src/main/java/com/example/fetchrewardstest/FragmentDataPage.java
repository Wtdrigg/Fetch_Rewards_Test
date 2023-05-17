package com.example.fetchrewardstest;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentDataPage extends Fragment
{
    private final Handler handler = new Handler();

    TextView textViewCol1;
    TextView textViewCol2;
    TextView textViewCol3;


    Button button;
    Animation fadeIn;
    DataProcessing dataProcessing;

    private final Runnable runnable = new Runnable()
    {
        // After the beginRepeating method is called, the run method will call 1 time every second.
        public void run()
        {
            // if the second thread that is running the dataProcessing object is still active,
            // then nothing will happen. If that thread is finished (and therefore no longer active)
            // then the setAndDisplay method is called, and the repeated calling of the run method
            // is stopped.
            if (!dataProcessing.isAlive())
            {
                setAndDisplayData();
                stopRepeating();
            }
        }
    };

    // Inflates the view from the XML file.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_data_page, container, false);
    }

    //Calls after the view has been created
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        setMembers(view);
        startWorkerThread();
    }

    // Gets references to each of the textViews on the second fragment, as well as the button.
    // Also creates another fade in animation object and creates a DataProcessing object using
    // The textView references as arguments.
    public void setMembers(View view)
    {
        textViewCol1 = view.findViewById(R.id.DataText);
        textViewCol2 = view.findViewById(R.id.DataText2);
        textViewCol3 = view.findViewById(R.id.DataText3);
        button = view.findViewById(R.id.AccessDataButton);
        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        dataProcessing = new DataProcessing(textViewCol1, textViewCol2, textViewCol3);
    }

    // Fades the button to help visually hide the fragment transition, then starts the DataProcessing
    // object's second thread. This new thread is going to be used to handle the HTTP request and
    // also the sorting/filtering of the data. The second thread is needed to keep the UI responsive
    // while awaiting the HTTP response.
    public void startWorkerThread()
    {
        button.setEnabled(false);
        dataProcessing.start();
        beginRepeating();
    }

    // updates the 3 textView objects on screen to include the data produced by the dataProcessing
    // object, then plays the fade in animation created in the setMembers method.
    // Because of the handler object, this method will only be called after the second thread has
    // finished.
    public void setAndDisplayData()
    {
        textViewCol1.setText(dataProcessing.results[0]);
        textViewCol2.setText(dataProcessing.results[1]);
        textViewCol3.setText(dataProcessing.results[2]);
        textViewCol1.startAnimation(fadeIn);
        textViewCol2.startAnimation(fadeIn);
        textViewCol3.startAnimation(fadeIn);
    }

    // repeats the run() method every 1 second until stopRepeating is called. This does not block
    // the main thread and allows the UI to remain responsive while checking the status of the second
    // Thread.
    public void beginRepeating()
    {
        handler.postDelayed(runnable, 1000);
    }

    // stops the run() method from repeating.
    public void stopRepeating()
    {
        handler.removeCallbacks(runnable);
    }
}