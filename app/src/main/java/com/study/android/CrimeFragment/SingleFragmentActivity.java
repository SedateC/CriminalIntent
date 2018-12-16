package com.study.android.CrimeFragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
          /*
        * 因为使用了支持库及 AppCompatActivity 类，所以这里调用了 getSupportFragmentManager()
          方法。如果不考虑旧版本的兼容性问题，可直接继承 Activity 类并调用 getFragmentManager()
          方法。
        * */
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        /*
            *   fragment事务被用来添加、移除、附加、分离或替换fragment队列中的fragment。这是使用
                fragment动态组装和重新组装用户界面的关键。 FragmentManager 管理着fragment事务回退栈。
                FragmentManager.beginTransaction() 方法创建并返回 FragmentTransaction 实例。
                FragmentTransaction 类支持流接口（fluent interface）的链式方法调用，以此配置 Fragment-
                Transaction 再返回它。因此，以上灰底代码可解读为：“创建一个新的fragment事务，执行一个
                fragment添加操作，然后提交该事务。”
                add(...) 方法是整个事务的核心，它有两个参数：容器视图资源ID和新创建的 CrimeFragment 。
                容器视图资源ID你应该很熟悉了，它是定义在activity_crime.xml中的 FrameLayout 组件的资源ID
            * */
        if (fragment==null){
            //抽象方法的使用关键点 交给实现类
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
