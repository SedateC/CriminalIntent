package com.study.android.CrimeFragment;


import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecycleView;
    //在 CrimeListFragment 类中定义 ViewHolder 内部类，它会实例化并使用list_item_
    //crime布局，

    //搞定了 Adapter ，最后要做的就是将它和 RecyclerView 关联起来。实现一个设置 CrimeList-
    //Fragment 用户界面的 updateUI 方法，该方法创建 CrimeAdapter ，然后设置给 RecyclerView ，
    private CrimeAdapter mAdapter;
    private Boolean mSubTitleVisiable =false;
    private static final int REQUEST_CRIME = 1;
    private int mPostion;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private TextView mNullTextView;
    private Button mNullCrimeButton;
    //注意，没有 LayoutManager 的支持，不仅 RecyclerView 无法工作，还会导致应用崩溃。所
    //以， RecyclerView 视图创建完成后，就立即转交给了 LayoutManager 对象。
    //RecyclerView 类不会亲自摆放屏幕上的列表项。实际上，摆放的任务被委托给了
    //LayoutManager 。除了在屏幕上摆放列表项， LayoutManager 还负责定义屏幕滚动行为。因此，
    //没有 LayoutManager ， RecyclerView 也就没法正常工作。
    //除了一些Android操作系统内置版实现， LayoutManager 还有很多第三方库实现版本。我们
    //使用的是 LinearLayoutManager 类，它支持以竖直列表的形式展示列表项。我们在本书后续章
    //节中还会使用 GridLayoutManager 类，以网格形式展示列表项。
    //运行应用，应该还是看不到内容；现在看到的是一个 RecyclerView 空视图。要显示出crime
    //列表项，还需要完成 Adapter 和 ViewHolder 的实现。
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_crime_list,container,false);
        //旋转屏幕的时候提取原来工具栏的状态
        if (savedInstanceState!=null){
            mSubTitleVisiable = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        //这里创建的时候就给mCrimeRecycleVie设置了管理器和适配器
        //mCrimeRecycleView 设置完后和view关联返回给VIEW 这时候view inflater是xml文件包含 fragment_crime_list id中的text
        //返回VIEW 包含mCrimeRecycleView
        mCrimeRecycleView = view.findViewById(R.id.crime_recycle_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNullTextView = view.findViewById(R.id.null_crime_list);
        mNullCrimeButton = view.findViewById(R.id.null_newCrime);
        mNullCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
            }
        });
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter==null){
            mAdapter = new CrimeAdapter(crimes); //这里创建了每一项
            mCrimeRecycleView.setAdapter(mAdapter);
        }else {//使用 int 参数更新DATA
           // mAdapter.mCrimes = CrimeLab.get(getActivity()).getCrimes();
            mAdapter.setCrimes(crimes);
            mCrimeRecycleView.setAdapter(mAdapter);
          //mAdapter.notifyDataSetChanged();
           // mAdapter.notifyItemChanged(mPostion); //部分重绘
        }
        if (crimes.size()!=0){
            mNullCrimeButton.setVisibility(View.INVISIBLE);
            mNullTextView.setVisibility(View.INVISIBLE);
        }else {
            mNullCrimeButton.setVisibility(View.VISIBLE);
            mNullTextView.setVisibility(View.VISIBLE);
        }
        updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubTitleVisiable);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*
        * 我们把视图绑定工作放入 CrimeHolder 类里。绑定之前，首先实例化相关组件。由于这是一
          次性任务，因此直接放在构造方法里处理 CrimeHolder直接与界面交互连接适配器给holder赋值
        * */
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            //父类够构造方法很多，这里需要个VIEW
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            //ViewHolder 父类中有itemView 就是list_item_crime中的id
            mTitleTextView = itemView.findViewById(R.id.Crime_title);
            mDateTextView = itemView.findViewById(R.id.Crime_date);
            mSolvedImageView = itemView.findViewById(R.id.Crime_solved);
            itemView.setOnClickListener(this);//基本上每个类都有自己的itemVIEW inflate

        }
        public void bind(Crime crime){
            //现在，只要取到一个 Crime ， CrimeHolder 就会刷新显示 TextView 标题视图和 TextView 日
            //期视图。
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
         //从fragment中启动activity类似于从activity中启动activity。我们调用 Fragment.start
            //Activity(Intent) 方法，由它在后台再调用对应的 Activity 方法。
          //  Intent intent = new Intent(getActivity(),CrimeActivity.class);
           // Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId()); 使用配置启动 CrimePagerActivity
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
           // startActivity(intent); //如果想附带值 就在Start的Activity创建赋值的static方法 注释原因获取结果
            startActivityForResult(intent,REQUEST_CRIME);
            mPostion = mCrimeRecycleView.getChildAdapterPosition(v);//这里获取到点击的position 值
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

          if (requestCode==REQUEST_CRIME){

          }

    }

    //这里告诉你只能处理CrimeHolder类型T的类
    //适配器分配处理一个短数据
    //getItemCount 调用顺序
    //getItemViewType
    //onCreateViewHolder
    //onBindViewHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public int getItemCount() {//先执创建适配器 在创建holeder 然后到总数量getItemCount 然后适配器数据onCreateViewHolder 然后创建CrimeHolder 循环 这一步数据已经先行导入适配器
            return mCrimes.size();
        }
        @NonNull
        @Override
        //RecyclerView 需要新的 ViewHolder 来显示列表项时，会调用 onCreateViewHolder 方法。
        //在这个方法内部，我们创建一个 LayoutInflater ，然后用它创建 CrimeHolder 。
        //系统自动调用
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //getActivity()获取和Fragment绑定的Activity 可能为空
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater,parent);//这个holder返回给视图界面
        }


        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            //现在，只要取到一个 Crime ， CrimeHolder 就会刷新显示 TextView 标题视图和 TextView 日
            //期视图。 这里的CrimeHolder holder 代表即将传入的一个 new holder， LayoutInflater.from(getActivity())完到这个方法
            Crime crime = mCrimes.get(position);
            //这个方法在Holder内部类中 在适配器中给创建好的holder赋值
            holder.bind(crime);
        }


    }

    @Override
    // CrimeActivity 随即弹出栈外并被销毁 启动crimelist的该方法 刷新UI
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubTitleVisiable){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime c = new Crime();
                CrimeLab.get(getActivity()).addCrime(c);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), c.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubTitleVisiable = !mSubTitleVisiable;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
             default:
                 return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeLabCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeLabCount);
        if (!mSubTitleVisiable){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
