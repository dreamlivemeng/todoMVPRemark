package com.junze.mvp.demo.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 2018/6/14 11:10 / mengwei
 */
public class ActivityUtils {

    /**
     * 添加fragment到Activity
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        //检查是否为空
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        //添加fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
