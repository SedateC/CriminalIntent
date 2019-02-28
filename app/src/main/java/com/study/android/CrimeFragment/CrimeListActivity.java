package com.study.android.CrimeFragment;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    //继承这个抽象方法目的是把Activity交给Fragment管理
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
