package com.study.android.CrimeFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*
        * 我们把视图绑定工作放入 CrimeHolder 类里。绑定之前，首先实例化相关组件。由于这是一
          次性任务，因此直接放在构造方法里处理
        * */
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            //父类够构造方法很多，这里需要个VIEW
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            //ViewHolder 父类中有itemView 就是list_item_crime中的id
            mTitleTextView = itemView.findViewById(R.id.Crime_title);
            mDateTextView = itemView.findViewById(R.id.Crime_date);
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
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //这里告诉你只能处理CrimeHolder类型T的类
    //适配器分配处理一个短数据
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
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
            //这个方法在
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {//先执创建适配器 在创建holeder 然后到总数量getItemCount 然后适配器数据onCreateViewHolder 然后创建CrimeHolder 循环 这一步数据已经先行导入适配器
            return mCrimes.size();
        }
    }
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
        //这里创建的时候就给mCrimeRecycleVie设置了管理器和适配器
        //mCrimeRecycleView 设置完后和view关联返回给VIEW 这时候view inflater是xml文件包含 fragment_crime_list id中的text
        //返回VIEW 包含mCrimeRecycleView
        mCrimeRecycleView = view.findViewById(R.id.crime_recycle_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecycleView.setAdapter(mAdapter);
    }


}
