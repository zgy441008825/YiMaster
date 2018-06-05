package com.zou.yimaster.tools;

/**
 * Created by zougy on 05.30.030
 * 处理程序异常
 */
public class YiUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler handler;
    private static YiUncaughtExceptionHandler exceptionHandler;

    public static YiUncaughtExceptionHandler getInstance() {
        if (exceptionHandler == null) {
            synchronized (YiUncaughtExceptionHandler.class) {
                if (exceptionHandler == null) {
                    exceptionHandler = new YiUncaughtExceptionHandler();
                }
            }
        }
        return exceptionHandler;
    }

    private YiUncaughtExceptionHandler() {
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
    }

}
