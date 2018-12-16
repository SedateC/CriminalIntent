package com.study.android.CrimeFragment;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
*
*
* */
public class CrimeLab {
    //注意 sCrimeLab 变量的 s 前缀。这是Android开发的命名约定，一看到此前缀，我们就
    //知道 sCrimeLab 是一个静态变量。
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for (int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0); //true and flase
            mCrimes.add(crime);
        }
    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID uuid){
        for (Crime crime:mCrimes){
            if (crime.getId().equals(uuid)){
                return crime;
            }
        }
        return null;
    }
}
