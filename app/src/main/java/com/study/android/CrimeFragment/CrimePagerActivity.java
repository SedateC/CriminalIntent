package com.study.android.CrimeFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

//ViewPager 在某种程度上类似于 RecyclerView 。 RecyclerView 需借助于 Adapter 提供视图。
//同样， ViewPager 需要 PagerAdapter 的支持。
//不过，相较于 RecyclerView 与 Adapter 间的协同工作， ViewPager 与 PagerAdapter 间的配
//合要复杂得多。好在Google提供了 PagerAdapter 的子类 FragmentStatePagerAdapter ，它能协
//助处理许多细节问题。
//FragmentStatePagerAdapter 化繁为简，提供了两个有用的方法： getCount() 和 getItem
//(int) 。调用 getItem(int) 方法，获取并显示crime数组中指定位置的 Crime 时，它会返回配置
//过的 CrimeFragment 来显示指定的 Crime 。
public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID =
            "com.study.android.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mbtn_start,mbtn_end;
    public static Intent newIntent(Context context, UUID crimeId){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_paper);
        //getCount mViewPager调用顺序
        //startUpdate
        //instantiateItem--->getItem（FragmentPagerAdapter）
        //destoryItem
        //setPrimaryItem
        //finishUpdate
        mViewPager = findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        final UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //如前所述， FragmentStatePagerAdapter 是
        //我们的代理，负责管理与 ViewPager 的对话并协同工作。代理需首先将 getItem(int) 方法返回
        //的fragment添加给activity，然后才能使用fragment完成自己的工作。这也就是创建代理实例时，
        //需要 FragmentManager 的原因。
        //实现左右滑动功能
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {//这个是后面才调用的
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //ViewPager 默认只显示 PagerAdapter 中的第一个列表项。要显示选中的列表项，可设置
        //ViewPager 当前要显示的列表项为crime数组中指定位置的列表项。
        //在 CrimePagerActivity.onCreate(Bundle) 方法的末尾，循环检查crime的ID，找到所选
        //crime在数组中的索引位置。如果 Crime 实例的 mId 与intent extra的 crimeId 相匹配，设置显示指定
        //位置的列表项
        for (int i = 0; i < mCrimes.size(); i++) { // handles returning to the proper crime upon exit and reentry
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
        mbtn_start = findViewById(R.id.start);
        mbtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mbtn_end = findViewById(R.id.end);
        mbtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int  position, float positionOffset, int positionOffsetPixels) {
                if(position == 0){
                    mbtn_start.setVisibility(View.INVISIBLE);
                    mbtn_end.setVisibility(View.VISIBLE);
                }else if (position == mCrimes.size()-1){
                    mbtn_start.setVisibility(View.VISIBLE);
                    mbtn_end.setVisibility(View.INVISIBLE);
                }else {
                    mbtn_start.setVisibility(View.VISIBLE);
                    mbtn_end.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
