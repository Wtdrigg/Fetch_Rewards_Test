package com.example.fetchrewardstest;

// Small class that structures each JSON item pulled from the sample data. This is used by the GSON
// object to create a DataItem object for each JSON item. Each item holds an ID, ListID, and Name.
public class DataItem
{
    public int id;
    public int listId;
    public String name;

    // Used to slice the string "Name" from the name members, then return the remaining number as
    // an int.
    public int nameAsInt()
    {
        return Integer.parseInt(name.substring(5));
    }
}

