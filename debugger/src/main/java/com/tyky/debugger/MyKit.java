package com.tyky.debugger;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.didichuxing.doraemonkit.DoKit;
import com.didichuxing.doraemonkit.kit.AbstractKit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyKit extends AbstractKit {
    @Override
    public int getIcon() {
        return R.drawable.ic_launcher;
    }

    @Override
    public int getName() {
        //return R.string.my_debugger;
        return 0;
    }

    @Override
    public void onAppInit(@Nullable Context context) {

    }

    @Override
    public boolean onClickWithReturn(@NotNull Activity activity) {
        DoKit.hide();
        ActivityUtils.startActivity(SettingActivity.class);
        return true;

    }
}
