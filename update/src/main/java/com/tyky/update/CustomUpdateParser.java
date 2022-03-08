package com.tyky.update;

import com.blankj.utilcode.util.StringUtils;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.listener.IUpdateParseCallback;
import com.xuexiang.xupdate.proxy.IUpdateParser;

public class CustomUpdateParser implements IUpdateParser {
    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        if (!StringUtils.isEmpty(json)) {
            //return new UpdateEntity()
            //        .setHasUpdate(result.hasUpdate)
            //        .setIsIgnorable(result.isIgnorable)
            //        .setVersionCode(result.versionCode)
            //        .setVersionName(result.versionName)
            //        .setUpdateContent(result.updateLog)
            //        .setDownloadUrl(result.apkUrl)
            //        .setSize(result.apkSize);
        }

        return null;
    }

    @Override
    public void parseJson(String json, IUpdateParseCallback callback) throws Exception {

    }

    @Override
    public boolean isAsyncParser() {
        return false;
    }
}