package com.study.android.CrimeFragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;


/**
 * CrimeFragment 类是与模型及视图对象交互的控制器，用于显示特定crime的明细信息，并在
 用户修改这些信息后立即进行更新。
 import android.support.v4.app.Fragment;
 */


public class CrimeFragment extends Fragment {//选择支持库版Fragment V4
    private Crime mCrime;
    private EditText mEditText;
    private Button mDateButton; //显示crime 发生的日期
    private CheckBox mSolvedCheckBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {//创建覆盖方法
        //其次，类似于activity，fragment同样具有保存及获取状态的bundle。如同使用 Activity.
        //onSaveInstanceState(Bundle) 方法那样，你也可以根据需要覆盖 Fragment.onSaveInstance-
        //State(Bundle) 方法。
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    /*
    * 该 方 法 实 例 化 fragment 视 图 的 布 局 ， 然 后 将 实 例 化 的 View 返 回 给 托 管 activity 。
    LayoutInflater 及 ViewGroup 是实例化布局的必要参数。 Bundle 用来存储恢复数据，可供该方
    法从保存状态下重建视图
    agment的视图是直接通过调用 LayoutInflater.inflate(...)
    方法并传入布局的资源ID生成的。第二个参数是视图的父视图，我们通常需要父视图来正确配置
    组件。第三个参数告诉布局生成器是否将生成的视图添加给父视图。这里，传入了 false 参数，
    因为我们将以代码的方式添加视图。
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_crime,container,false);
        mEditText = v.findViewById(R.id.crime_title);
        /*
        * 添加内容变换方法
        * */
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        mDateButton.setText(new Date().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);//这里自动生成ischecked
            }
        });
        //这里一个Fragment已经写好 用Fragmanager管理
        return v;
    }

}
