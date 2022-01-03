package com.zhujm.getconfig;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
public abstract class GecJsonResultCallback implements GecResultCallback {
    @Override
    public void onResult(String result) {
        onJsonResult(string2Json(result));
    }

    private String string2Json(String jsonString) {
//        return JSONObject.parse(jsonString).toString();
        return jsonString;
    }

    public abstract void onJsonResult(String jsonData);
}
