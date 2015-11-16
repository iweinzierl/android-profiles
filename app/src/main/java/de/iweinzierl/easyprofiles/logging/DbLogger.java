package de.iweinzierl.easyprofiles.logging;

import android.content.Context;
import android.os.AsyncTask;

import com.orm.SugarRecord;

import java.util.Date;

import de.inselhome.android.logging.AndroidLogger;
import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.persistence.LogEntity;

public class DbLogger extends AndroidLogger {

    private enum LogLevel {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public DbLogger() {
        super();
    }

    public DbLogger(Context context, String logTag, String name) {
        super(context, logTag, name);
    }

    @Override
    public void trace(String msg) {
        super.trace(msg);
        //writeToDb(LogLevel.TRACE, msg);
    }

    @Override
    public void trace(String format, Object arg) {
        super.trace(format, arg);
        //writeToDb(LogLevel.TRACE, buildMessage(format, arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        super.trace(format, arg1, arg2);
        //writeToDb(LogLevel.TRACE, buildMessage(format, arg1, arg2));
    }

    @Override
    public void trace(String format, Object... args) {
        super.trace(format, args);
        //writeToDb(LogLevel.TRACE, buildMessage(format, args));
    }

    @Override
    public void trace(String msg, Throwable t) {
        super.trace(msg, t);
        //writeToDb(LogLevel.TRACE, msg, t);
    }

    @Override
    public void debug(String msg) {
        super.debug(msg);
        writeToDb(LogLevel.DEBUG, buildMessage(msg));
    }

    @Override
    public void debug(String format, Object arg) {
        super.debug(format, arg);
        writeToDb(LogLevel.DEBUG, buildMessage(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        super.debug(format, arg1, arg2);
        writeToDb(LogLevel.DEBUG, buildMessage(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... args) {
        super.debug(format, args);
        writeToDb(LogLevel.DEBUG, buildMessage(format, args));
    }

    @Override
    public void debug(String msg, Throwable t) {
        super.debug(msg, t);
        writeToDb(LogLevel.DEBUG, buildMessage(msg), t);
    }

    @Override
    public void info(String msg) {
        super.info(msg);
        writeToDb(LogLevel.INFO, buildMessage(msg));
    }

    @Override
    public void info(String format, Object arg) {
        super.info(format, arg);
        writeToDb(LogLevel.INFO, buildMessage(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        super.info(format, arg1, arg2);
        writeToDb(LogLevel.INFO, buildMessage(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... args) {
        super.info(format, args);
        writeToDb(LogLevel.INFO, buildMessage(format, args));
    }

    @Override
    public void info(String msg, Throwable t) {
        super.info(msg, t);
        writeToDb(LogLevel.INFO, buildMessage(msg), t);
    }

    @Override
    public void warn(String msg) {
        super.warn(msg);
        writeToDb(LogLevel.WARN, buildMessage(msg));
    }

    @Override
    public void warn(String format, Object arg) {
        super.warn(format, arg);
        writeToDb(LogLevel.WARN, buildMessage(format, arg));
    }

    @Override
    public void warn(String format, Object... args) {
        super.warn(format, args);
        writeToDb(LogLevel.WARN, buildMessage(format, args));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        super.warn(format, arg1, arg2);
        writeToDb(LogLevel.WARN, buildMessage(format, arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        super.warn(msg, t);
        writeToDb(LogLevel.WARN, buildMessage(msg), t);
    }

    @Override
    public void error(String msg) {
        super.error(msg);
        writeToDb(LogLevel.ERROR, buildMessage(msg));
    }

    @Override
    public void error(String format, Object arg) {
        super.error(format, arg);
        writeToDb(LogLevel.ERROR, buildMessage(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        super.error(format, arg1, arg2);
        writeToDb(LogLevel.ERROR, buildMessage(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... args) {
        super.error(format, args);
        writeToDb(LogLevel.ERROR, buildMessage(format, args));
    }

    @Override
    public void error(String msg, Throwable t) {
        super.error(msg, t);
        writeToDb(LogLevel.ERROR, buildMessage(msg), t);
    }

    private void writeToDb(final LogLevel level, final String message) {
        if (!AndroidLoggerFactory.DEFAULT_LOG_TAG.equals(logTag)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    SugarRecord.save(new LogEntity(new Date(), level.name(), message, null));
                    return null;
                }
            }.execute();
        }
    }

    private void writeToDb(final LogLevel level, final String message, final Throwable t) {
        if (!AndroidLoggerFactory.DEFAULT_LOG_TAG.equals(logTag)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    SugarRecord.save(new LogEntity(new Date(), level.name(), message, t.toString()));
                    return null;
                }
            }.execute();
        }
    }
}
