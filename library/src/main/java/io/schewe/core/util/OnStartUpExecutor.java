package io.schewe.core.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class OnStartUpExecutor {

    private static List<Callback.CallbackWithParameters> tasks = new LinkedList<>();

    private OnStartUpExecutor(){}

    public static OnStartUpExecutor getInstance(){ return new OnStartUpExecutor(); }

    public void addTask(Callback.CallbackWithParameters callback){ OnStartUpExecutor.tasks.add(callback); }

    public void executeOnAppStart(Activity activity){ for(Callback.CallbackWithParameters callback : OnStartUpExecutor.tasks) callback.execute(activity); }
}
