package com.zkh.costtime_plugin;

import com.android.ddmlib.Log;

public class Test {


    public void add(){
        long startTime = System.currentTimeMillis();

        // dosomething

        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[0];
        long costTime = System.currentTimeMillis() - startTime;
        Log.e("zkh", String.format(" %s.%s(%s)方法耗时 %d ms",
                thisMethodStack.getClassName(),
                thisMethodStack.getMethodName(),
                thisMethodStack.getLineNumber(),
                costTime));
    }
}
