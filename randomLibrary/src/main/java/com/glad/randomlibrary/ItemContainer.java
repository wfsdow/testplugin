package com.glad.randomlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 *@date 2019/10/10
 *@author ningwei
 *@description 选项的容器
 */
public class ItemContainer extends ViewGroup {
    public ItemContainer(Context context) {
        super(context);
    }

    public ItemContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
