package com.example.hhs.hotelapp;

/**
 * Created by hhs on 25/2/17.
 */

public class DataObject {
    private String mText1;
    private String mText2;
    private String mText3;
    private int mText4;

    public int getmText4() {
        return mText4;
    }

    public void setmText4(int mText4) {
        this.mText4 = mText4;
    }

    DataObject (String text1, String text2, String text3){
        mText1 = text1;
        mText2 = text2;
        mText3 = "Price = "+text3+" /-";

        mText4=Integer.parseInt(text3);
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getmText3() {
        return mText3;
    }

    public void setmText3(String mText3) {
        this.mText3 = mText3;
    }

}