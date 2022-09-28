package com.tyky.debugger;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.MetaDataUtils;
import com.didichuxing.doraemonkit.DoKit;
import com.tencent.bugly.Bugly;
import com.tyky.debugger.utils.DoKitUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public class DebuggerInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        //Bugly初始化
        String appId = MetaDataUtils.getMetaDataInApp("BUGLY_APPID");
        //最后的参数代表是否开启debuger模式
        Bugly.init(context, appId, true);
        //初始化Litepal的ORM框架
        LitePal.initialize(context);

        //自定义kit（菜单）
        //List<AbstractKit> list = new ArrayList<>();
        //list.add(new MyKit());
        //LinkedHashMap<String, List<AbstractKit>> map = new LinkedHashMap<>();
        //map.put("太极", list);
        new DoKit.Builder((Application) (context))
                //.customKits(map)
                .build();

        //获取本地sp保存的数据
        boolean flag = DoKitUtil.getOpenOption();
        if (!flag) {
            DoKit.hide();
        }

        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
