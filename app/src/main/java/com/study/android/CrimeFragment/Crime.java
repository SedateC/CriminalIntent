package com.study.android.CrimeFragment;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2018-11-29.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSupect;
    private String mCallContact;

    public Crime() {
      this(UUID.randomUUID());
    }
    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getSupect() {
        return mSupect;
    }

    public String getCallContact() {
        return mCallContact;
    }

    public void setCallContact(String callContact) {
        mCallContact = callContact;
    }

    public void setSupect(String supect) {
        mSupect = supect;
    }

    public String getPhotoFileName(){
        return "IMG_"+getId().toString()+".jpg";
    }
    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
