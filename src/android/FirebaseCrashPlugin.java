package by.chemerisuk.cordova.firebase;

import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;

import com.crashlytics.android.Crashlytics;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

public class FirebaseCrashPlugin extends ReflectiveCordovaPlugin {
    private final String TAG = "FirebaseCrashPlugin";

    @CordovaMethod(ExecutionThread.WORKER)
    private void log(String message, CallbackContext callbackContext) {
        Crashlytics.log(message);
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void logError(String message, String stack, CallbackContext callbackContext) {
        try {
            Exception ex = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                    new CordovaJsException24(message, stack) :
                    new CordovaJsException(message, stack);

            Crashlytics.logException(ex);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(new Exception(message, e));
        }

        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void setUserId(String userId, CallbackContext callbackContext) {
        Crashlytics.setUserIdentifier(userId);
        callbackContext.success();
    }

    static class CordovaJsExceptionBase extends Exception {

        @RequiresApi(api = Build.VERSION_CODES.N)
        CordovaJsExceptionBase(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
            super(message, null, true, true);
        }

        CordovaJsExceptionBase(String message) {
            super(message);
        }

        void setJsStackTrace(String jsStackTrace) {

            if(jsStackTrace == null || jsStackTrace.length() == 0) {
                this.fillInStackTrace();
                return;
            }

            String[] lines = jsStackTrace.split("[\\r\\n]+");

            ArrayList<StackTraceElement> list = new ArrayList<>();

            for (String line : lines) {

                String[] parts = line.trim().split(";");
                if (parts.length > 5) {
                    String className = parts[0];
                    String functionName = parts[1];
                    String fileName = parts[2];
                    int lineNumber = Integer.parseInt(parts[3]);
                    int columnNumber = Integer.parseInt(parts[4]);
                    boolean isEval = Integer.parseInt(parts[5]) > 0;

                    // for minimized sources
                    if (lineNumber == 1 && columnNumber > 0)
                        lineNumber = columnNumber;

                    StackTraceElement elm = new StackTraceElement(
                            className,
                            functionName,
                            fileName,
                            lineNumber
                    );

                    list.add(elm);
                }
            }

            if (list.size() > 0) {
                setStackTrace(list.toArray(new StackTraceElement[0]));
            } else
                fillInStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static class CordovaJsException24 extends CordovaJsExceptionBase {
        CordovaJsException24(String message, String jsStackTrace) {
            super(message, null, true, true);
            setJsStackTrace(jsStackTrace);
        }
    }

    public static class CordovaJsException extends CordovaJsExceptionBase {
        CordovaJsException(String message, String jsStackTrace) {
            super(message);
            setJsStackTrace(jsStackTrace);
        }
    }
}
