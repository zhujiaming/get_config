package com.zhujm.getconfig.sample;

public enum ScriptSource {
    //获取github共享的文件内容
    _GITHUB("(function g(){try{return document.getElementsByClassName('blob-code-content')[0].innerText}catch(e){}})()"),

    //获取youdao分享的笔记内容
    _NOTE("(function g(){try{return document.getElementsByTagName('iframe')[0].contentDocument.getElementsByClassName('bulb-editor bulb-editor-inner')[0].innerText}catch(e){}})()");
//    SHIMO("(function g(){try{return document.getElementsByClassName('ql-editor notranslate')[0].innerText}catch(e){}})()");

    private String script;

    ScriptSource(String srt) {
        script = srt;
    }

    public String getScript() {
        return script;
    }
}
