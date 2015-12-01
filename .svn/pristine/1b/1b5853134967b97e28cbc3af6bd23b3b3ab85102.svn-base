package com.sixin.widgets.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.Stack;

public class FragmentStackCompatImpl implements IFragmentStack {

    private FragmentActivity mActivity;
    private FragmentManager mManager;
    private int mId;
    private Stack<Fragment> mStack;
    private View mContainer;

    private boolean mFinished;

    /**
     * construct a FragmentStackCompatImpl object
     *
     * @param activity a FragmentActivity
     * @param id       a ViewGroup's ID in activity's ContentView
     */
    public FragmentStackCompatImpl(FragmentActivity activity, int id) {
        if (activity != null && !activity.isFinishing()) {
            mActivity = activity;
            mId = id;
            if (mId == 0) {
                throw new FragmentStackError("The view id of container cannot be zero");
            }
            mContainer = mActivity.findViewById(mId);
            if (mContainer == null) {
                throw new FragmentStackError("This activity doesn't contain a view with id " + id);
            }
            if (!(mContainer instanceof ViewGroup)) {
                throw new FragmentStackError("The view is not a ViewGroup that can hold child views");
            }
            mManager = mActivity.getSupportFragmentManager();
            mStack = new Stack<Fragment>();
        }
    }

    /**
     * get the fragment in the top of Fragment Stack
     *
     * @return top fragment in Fragment Stack
     */
    @Override
    public Fragment peek() {

        if (!mFinished) {
            if (!mStack.isEmpty()) {
                return mStack.peek();
            }
            return null;
        } else return null;

    }

    /**
     * get the fragment in the top of Fragment Stack and remove the view of the fragment from
     * the container which is determined by the id param in constructor
     *
     * @return top fragment in Fragment Stack
     */
    @Override
    public synchronized Fragment pop() {
        if (!mFinished) {
            Fragment fragment = null;
            if (!mStack.isEmpty()) {
                fragment = mStack.pop();
                mManager.beginTransaction().remove(fragment).commit();
            }


            return fragment;

        } else return null;
    }


    /**
     * push a fragemnt into the Fragment Stack,and add the fragment's view to the container
     * which is determined by the id param in constructor
     *
     * @param fragment Which fragment should be pushed in?
     * @return The fragment to be added on top of the stack.
     */
    @Override
    public synchronized Fragment push(Fragment fragment) {
        if (!mFinished) {
            if (fragment == null || fragment.isAdded()) {
                return null;
            }
            mManager.beginTransaction().add(mId, fragment).commit();
            return mStack.push(fragment);
        } else return null;


    }

    @Override
    public void clear() {
        if (mStack.isEmpty()) return;
        ((ViewGroup) mContainer).removeAllViews();
        FragmentTransaction ft = mManager.beginTransaction();
        for (Fragment f : mStack) {
            ft.remove(f);
        }
        ft.commit();
    }


    /**
     * finish the management of FragmentStackCompatImpl,after this methods is called,to call other methods is useless
     */
    @Override
    public synchronized void finish() {
        if (!mFinished) {
            mActivity = null;
            mManager = null;
            mStack.clear();
            mStack = null;
            mFinished = true;
        }

    }

}
