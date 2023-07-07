package com.example.e_padhai;

import android.net.Uri;

public class NumbersView {

    // the resource ID for the imageView
    private int ivNumbersImageId;

    // TextView 1
    private String mNumberInDigit;

    // TextView 1
    private String mNumbersInText;

    private Uri uri;

    // create constructor to set the values for all the parameters of the each single view
    public NumbersView(int NumbersImageId, Uri uri, String NumbersInDigit, String NumbersInText) {
        ivNumbersImageId = NumbersImageId;
        mNumberInDigit = NumbersInDigit;
        mNumbersInText = NumbersInText;
        this.uri=uri;
    }

    public Uri geturi() {
        return uri;
    }
    // getter method for returning the ID of the imageview
    public int getNumbersImageId() {
        return ivNumbersImageId;
    }

    // getter method for returning the ID of the TextView 1
    public String getNumberInDigit() {
        return mNumberInDigit;
    }

    // getter method for returning the ID of the TextView 2
    public String getNumbersInText() {
        return mNumbersInText;
    }
}

