package com.andreabardella.aifaservicesconsumer.base;

import android.support.annotation.CallSuper;

public abstract class BasePresenter implements Presenter {

    private PresenterStatus status = PresenterStatus.CREATED;

    /**
     * ***********************************************************************************<br>
     * WHY IS IT USEFUL TO KNOW IF A PRESENTER INSTANCE HAS JUST BEEN CREATED OR RESUMED? <br>
     * ***********************************************************************************<br>
     * A presenter is usually injected into one or more "views" (w.r.t. the MVP model).
     * If the "view" is an Activity the presenter (dependency) should be "bound" somehow to the
     * Activity lifecycle in order to be resumed if already available as the activity is resumed
     * after a configuration change or after it has gone in background
     * <br>
     * It might happen that the user comes back to the activity <b>after the system killed
     * the process of the related application</b>.
     * This means that the activity will be resumed (savedInstanceState != null) but a new instance
     * of the presenter will be injected.
     * Thus if the activity was waiting for replies (from the presenter) to already performed requests,
     * it should recognize that <i>it is no more speaking with the original presenter but with a new instance</i>,
     * and in this case there will be not incoming replies.
     * <br><br>
     * Some notes about Android OS will be useful in order to understand:
     * <ul>
     * <li>what an application is</li>
     * <li>difference between a process and an application</li>
     * <li>what happen to tasks management (performed by Android OS) and the related process(es)</li>
     * </ul>
     * <br><br>
     * <a href="http://android-developers.blogspot.it/2010/04/multitasking-android-way.html">Multitasking the Android Way</a> (follows excerpt)<br>
     * When does an application "stop"?
     * A common misunderstanding about Android multitasking is the difference between a process and an application.
     * In Android these are not tightly coupled entities: <b>applications may seem present to the user
     * without an actual process currently running the app</b>; multiple applications may share processes,
     * or one application may make use of multiple processes depending on its needs;
     * the process(es) of an application may be kept around by Android even when that application is not actively doing something.<br>
     * The fact that you can see an application's process "running" does not mean the application is running or doing anything.
     * It may simply be there because Android needed it at some point,
     * and has decided that it would be best to keep it around in case it needs it again.
     * Likewise, you may leave an application for a little bit and return to it from where you left off,
     * and during that time Android may have needed to get rid of the process for other things.<br>
     * A key to how Android handles applications in this way is that processes don't shut down cleanly.
     * When the user leaves an application, its process is kept around in the background,
     * allowing it to continue working (for example downloading web pages) if needed,
     * and come immediately to the foreground if the user returns to it.
     * If a device never runs out of memory, then Android will keep all of these processes around,
     * truly leaving all applications "running" all of the time.<br>
     * Of course, there is a limited amount of memory, and to accommodate this
     * Android must decide when to get rid of processes that are not needed.
     * This leads to Android's process lifecycle, the rules it uses to decide how important each process is
     * and thus the next one that should be dropped. These rules are based on both how important a process is
     * for the user's current experience, as well as how long it has been since the process was last needed by the user.
     * <br><br>
     * <a href="http://developer.android.com/guide/topics/manifest/activity-element.html">look at alwaysRetainTaskState</a> (follows excerpt)<br>
     * <code>android:alwaysRetainTaskState</code><br>
     * Whether or not the state of the task that the activity is in will always be maintained by the system —<br>
     * "true" if it will be, and "false" if the system is allowed to reset the task to its initial state in certain situations.
     * The default value is "false". This attribute is meaningful only for the root activity of a task; it's ignored for all other activities.<br>
     * Normally, the system clears a task (removes all activities from the stack above the root activity)
     * in certain situations when the user re-selects that task from the home screen.
     * Typically, this is done if the user hasn't visited the task for a certain amount of time, such as 30 minutes.<br>
     * However, when this attribute is "true", users will always return to the task in its last state,
     * regardless of how they get there. This is useful, for example, in an application like the web browser
     * where there is a lot of state (such as multiple open tabs) that users would not like to lose.
     * <br><br>
     * When memory is low on an Android device, the OS can and will begin to kill processes (background first)
     * <br>
     * Internally Linux allocate memory in form of pages from buddy system.
     * When memory is not available OS enters to out of memory state and it selects a process,
     * based on certain algorithm, and kills that process and release the resources of that particular process.
     * <br><br>
     * See <a href="http://developer.android.com/training/articles/memory.html">android memory management</a>
     * and <a href="https://mobworld.wordpress.com/2010/07/05/memory-management-in-android/">this external resource</a>
     * (follows an excerpt)
     * <br>
     * Random-access memory (RAM) is a valuable resource in any software development environment,
     * but it's even more valuable on a mobile operating system where physical memory is often constrained.
     * Although Android's Dalvik virtual machine performs routine garbage collection,
     * this doesn't allow you to ignore when and where your app allocates and releases memory.<br>
     * In order for the garbage collector to reclaim memory from your app,
     * you need to avoid introducing memory leaks (usually caused by holding onto object references
     * in global members) and release any Reference objects at the appropriate time (as defined
     * by lifecycle callbacks discussed further below).
     * For most apps, the Dalvik garbage collector takes care of the rest:
     * the system reclaims your memory allocations when the corresponding objects leave the scope
     * of your app's active threads.<br>
     * This document explains how Android manages app processes and memory allocation,
     * and how you can proactively reduce memory usage while developing for Android.
     * For more information about general practices to clean up your resources when programming in Java,
     * refer to other books or online documentation about managing resource references.
     * If you’re looking for information about how to analyze your app’s memory once you’ve already built it,
     * read Investigating Your RAM Usage.<br>
     * <i>How Android Manages Memory:</i><br>
     * Android does not offer swap space for memory,
     * but it does use <a href="https://en.wikipedia.org/wiki/Paging">paging</a>
     * and <a href="https://en.wikipedia.org/wiki/Memory-mapped_file">memory-mapping</a> (mmapping)
     * to manage memory.
     * This means that any memory you modify—whether by allocating new objects
     * or touching m-mapped pages—remains resident in RAM and cannot be paged out.
     * So the only way to completely release memory from your app is to release object references
     * you may be holding, making the memory available to the garbage collector.
     * That is with one exception: any files m-mapped in without modification, such as code,
     * can be paged out of RAM if the system wants to use that memory elsewhere.
     * <br><br>
     * <b>{@link android.app.Activity Something about Activity Lifecycle}</b> (follows an excerpts)
     * <ul>
     * <li>If an activity in the foreground of the screen (at the top of the stack), it is active or running.</li>
     * <li>If an activity has lost focus but is still visible (that is, a new non-full-sized or transparent activity has focus on top of your activity), it is paused. A paused activity is completely alive (it maintains all state and member information and remains attached to the window manager), but can be killed by the system in extreme low memory situations.</li>
     * <li>If an activity is completely obscured by another activity, it is stopped. It still retains all state and member information, however, it is no longer visible to the user so its window is hidden and it will often be killed by the system when memory is needed elsewhere.</li>
     * <li>If an activity is paused or stopped, the system can drop the activity from memory by either asking it to finish, or simply killing its process. When it is displayed again to the user, it must be completely restarted and restored to its previous state.</li>
     * </ul>
     * There are a few scenarios in which your activity is destroyed due to normal app behavior,
     * such as when the user presses the Back button or your activity signals its own destruction by calling finish().
     * <i>The system may also destroy your activity</i> if it's currently stopped and
     * hasn't been used in a long time or the foreground activity requires more resources
     * so the system must shut down background processes to recover memory.
     * <br>
     * When your activity is destroyed because the user presses Back or the activity finishes itself,
     * the system's concept of that Activity instance is gone forever because the behavior indicates
     * the activity is no longer needed.
     * However, <i>if the system destroys the activity due to system constraints</i>
     * (rather than normal app behavior), then although the actual Activity instance is gone,
     * the system remembers that it existed such that if the user navigates back to it,
     * the system creates a new instance of the activity using a set of saved data
     * that describes the state of the activity when it was destroyed.
     * The saved data that the system uses to restore the previous state is called the "instance state"
     * and is a collection of key-value pairs stored in a Bundle object.
     */
    @Override
    public PresenterStatus getStatus() {
        return status;
    }

    @CallSuper
    @Override
    public void resume() {
        status = PresenterStatus.RESUMED;
    }

    @CallSuper
    @Override
    public void pause() {
        status = PresenterStatus.PAUSED;
    }

    @CallSuper
    @Override
    public void release() {
        status = PresenterStatus.RELEASED;
    }
}
