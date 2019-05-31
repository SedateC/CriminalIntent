package com.study.android.CrimeFragment;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
* 抽象类子类
* 你可以创建 ListRow 来获取自定义的 mThumbnail 和 RecyclerView.ViewHolder 超类传
    入的 itemView ，如代码清单8-14所示。 ViewHolder 为 itemView 而生：它引用着传给
    super(view) 的整个 View 视图。
* */
public class ListRow extends RecyclerView.ViewHolder {
    public ImageView mThumbnail;//极小的
    public ListRow(@NonNull View itemView) {
        super(itemView);
    }
}
