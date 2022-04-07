package com.salamtak.app.ui.component.splash

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class BackgroundWorkerService(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    val TAG = "WorkerService"
    override fun doWork(): Result {
        try {
            doSomething()
            return Result.success()
//        return Result.failure()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            return Result.failure()
        }
    }

    private fun doSomething() {
        Log.e(TAG, "doSomething")
    }
}