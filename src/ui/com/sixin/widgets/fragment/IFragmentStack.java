package com.sixin.widgets.fragment;

import android.support.v4.app.Fragment;

public interface IFragmentStack {
    public static final int STACK_SIZE = 10;

    Fragment peek();

    Fragment pop();

    Fragment push(Fragment fragment);

    void clear();

    void finish();
}

