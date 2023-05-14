package com.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParentLogger implements MyLogger {
    private static final Logger logger = LogManager.getLogger(ParentLogger.class.getName());


    private int line = 0;
    private final String s = "( LINE: ";

    @Override
    public void info(Object message) {
        Logger logger = getLogger();
        if (logger.isInfoEnabled()) {
            logger.log(Level.INFO, (message == null) ? "null" : s + line + ") " + message.toString());
        }

    }

    @Override
    public void error(Object message) {
        Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            logger.error((message == null) ? "null" : s + line + ") " + message.toString(), Level.ERROR);
        }
    }

    @Override
    public void error(Object message, Throwable object) {
        Logger logger = getLogger();
        if (logger.isErrorEnabled()) {
            logger.error((message == null) ? "null" : s + line + ") " + message.toString(), Level.ERROR, object);
        }
    }

    @Override
    public void warn(Object message) {
        Logger logger = getLogger();
        if (logger.isWarnEnabled()) {
            logger.warn((message == null) ? "null" : s + line + ") " + message.toString(), Level.WARN);
        }
    }

    private Logger getLogger() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        if (elements.length >= 3 && elements[3].getClassName() != null) {
            line = elements[3].getLineNumber();
            return LogManager.getLogger(elements[3].getClassName() + "." + elements[3].getMethodName());
        } else {
            return logger;
        }
    }
}
