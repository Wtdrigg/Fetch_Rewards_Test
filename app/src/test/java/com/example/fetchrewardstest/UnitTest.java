package com.example.fetchrewardstest;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest
{
    @Test
    public void testDataItem()
    {
        DataItem dataItem = new DataItem();
        for (int i = 0; i < 100; i++)
        {
            dataItem.name = "Item " + i;
            assertEquals(i, dataItem.nameAsInt());
        }

    }

}