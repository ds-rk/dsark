package com.logging;

public interface InterLogger {

    void info(Object obj);
    void error(Object obj);
    void error(Object obj, Throwable e);
    void warn(Object obj);
}
