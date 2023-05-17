package com.example.fetchrewardstest;

import android.util.Log;
import android.widget.TextView;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class DataProcessing extends Thread
{
    int indexCounter;
    int arrLenCounter = 0;
    int[][] filteredArray;
    int[][] groupedArray;
    String[][] sortedArray;
    String[] results = new String[3];
    TextView textViewCol1;
    TextView textViewCol2;
    TextView textViewCol3;


    // Constructor - Sets textView class members to hold references to the textView objects passed in.
    public DataProcessing(TextView textViewCol1Param , TextView textViewCol2Param, TextView textViewCol3Param)
    {
        textViewCol1 = textViewCol1Param;
        textViewCol2 = textViewCol2Param;
        textViewCol3 = textViewCol3Param;
    }

    // Starts a second thread and calls the processData method in that thread
    @Override
    public void run()
    {
        processData();
    }

    // Calls the contained methods in order.
    public void processData()
    {
        getAndFilterData();
        groupAndSortData();
        updateViewText();
    }

    // This method is responsible for obtain the sample data via HTTP, then filtering out
    // Empty values.
    private void getAndFilterData()
    {
        try
        {
            // Creates a GSON object. This will be used to parse the incoming JSON and create Java
            // Objects from it.

            Gson gson = new Gson();
            // Submits the HTTP GET request and returns the JSON response. Then JSON is then placed
            // into an array.
            String testJSON = sendGetRequest();
            JSONArray array = new JSONArray(testJSON);

            // Create a new 2D array. The length of the first dimension matches the length of the
            // JSON array, the length of the second dimension is always 3 to hold the 3 columns of
            // sample data.
            filteredArray = new int[array.length()][3];

            // loop a number of times equal to the length of the JSON array.
            for(int i = 0; i < array.length(); i++)
            {
                // each loop, take the JSON object from the array and use the GSON object to make
                // it into a Java DataItem Object.
                JSONObject object = array.getJSONObject(i);
                DataItem dataItem = gson.fromJson(String.valueOf(object), DataItem.class);

                // each loop, if the name of the DataItem is not Null, and also not blank, then
                // place its 3 attributes into the second dimension of our 2D array.
                if (!Objects.equals(dataItem.name, "") && dataItem.name != null)
                {
                    // and increment a counter tracking the number of items that we are keeping.
                    arrLenCounter++;
                    filteredArray[i][0] = dataItem.id;
                    filteredArray[i][1] = dataItem.listId;
                    // Note that for the name member we are calling the DataItems, nameAsInt method,
                    // which slices the number from the name and converts it to an int, for
                    // easier sorting later.
                    filteredArray[i][2] = dataItem.nameAsInt();
                }
            }
        }

        // Debug Log any exceptions that might occur, required by Java Syntax.
        catch (IOException ioException)
        {
            Log.d("---EXCEPTION---", "IO Error");
        }

        catch (JSONException jsonException)
        {
            Log.d("---EXCEPTION---", "JSON Error");
        }
    }

    // This method takes our now filtered array of DataItem objects and groups and sorts them by
    // the ListID and then the Name (which is why name was converted to an int previously)
    private void groupAndSortData()
    {
        // Make a new array to hold the grouped data, the sizes of which matches our counter from
        // the getAndFilterData method.
        groupedArray = new int[arrLenCounter][3];
        // starts a new counter.
        indexCounter = 0;

        // loop 5 times
        for (int i = 1; i < 5; i++)
        {
            // each loop, iterate through the entire array of arrays holding filtered data.
            for (int[] ints : filteredArray)
            {
                // each iteration, if the value in index 1 matches the count of the loop.
                if (ints[1] == i)
                {
                    // then add the second dimension array to the new groupedArray and increment the
                    // counter
                    groupedArray[indexCounter] = ints;
                    indexCounter++;
                    // the result is that groupedArray will hold every item, grouped by their ListID
                }
            }
        }

        // calls the static sort method on the array created in the last step. Uses a lambda to sort
        // by the name integer.
        Arrays.sort(groupedArray, (a, b) ->
        {
            // if the values of index 1 match between both items being compared...
            if (a[1] == b[1])
            {
                // then compare by index 2.
                return Integer.compare(a[2], b[2]);
            }
            else
            {
                // otherwise compare by index 1.
                return Integer.compare(a[1], b[1]);
            }
        });
        // The result is groupedArray is now grouped by index 1 and sorted by index 2.
    }

    // this method builds the strings that will be displayed in the FragmentDataPage view.
    private void updateViewText()
    {
        // Create some StringBuilder variables to hold the results. StringBuilder objects are used
        // here to avoid using a += operator in a loop.
        StringBuilder display1 = new StringBuilder();
        StringBuilder display2 = new StringBuilder();
        StringBuilder display3 = new StringBuilder();

        // create a new counter and a final 2D array, whose length of the second dimension
        // matches the counter from GroupAndSortData
        indexCounter = 0;
        sortedArray = new String[arrLenCounter][3];

        // iterate through groupedArray
        for (int[] ints : groupedArray)
        {
            // each iteration, covert all stored items to strings and place in the final array
            // note that for index 2, "Item" is concatenated back on as it was sliced off earlier.
            sortedArray[indexCounter][0] = Integer.toString(ints[0]);
            sortedArray[indexCounter][1] = Integer.toString(ints[1]);
            sortedArray[indexCounter][2] = "Item " + ints[2];
            indexCounter++;
        }

        // iterate through the final array and add the categories back on for display readability.
        for (String[] strings : sortedArray)
        {
            display1.append("ID: ").append(strings[0]).append("\n");
            display2.append("ListID: ").append(strings[1]).append("\n");
            display3.append("Name: ").append(strings[2]).append("\n");
        }

        // save the final results to index 0, 1, and 2 of the results array. Once the thread is
        // finished, FragmentDataPage will pull each array index to display on screen. in
        // different columns.
        results[0] = display1.toString();
        results[1] = display2.toString();
        results[2] = display3.toString();

    }

    // Performs the HTTP GET request.
    public String sendGetRequest() throws IOException
    {
        // saves the target URL.
        URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");

        // establishes a connection and sets the header info.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");

        // creates a buffer to hold the result stream
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        // write the request response to the content StringBuilder.
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        // Close the buffer, convert content to a string and return.
        in.close();
        return content.toString();
    }
}

