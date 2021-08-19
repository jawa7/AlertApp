package com.example.alertapp

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

internal class MyLifecycleOwner: SavedStateRegistryOwner {
    private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var mSavedStateRegistryController: SavedStateRegistryController = SavedStateRegistryController.create(this)


    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        mLifecycleRegistry.handleLifecycleEvent(event)
    }

    override fun getSavedStateRegistry(): SavedStateRegistry {
        return mSavedStateRegistryController.savedStateRegistry
    }

    fun performRestore(savedState: Bundle?) {
        mSavedStateRegistryController.performRestore(savedState)
    }
}