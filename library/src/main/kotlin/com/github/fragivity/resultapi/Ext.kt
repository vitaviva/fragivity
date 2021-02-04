package com.github.fragivity.resultapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.github.fragivity.requireParentFragmentManager

/**
 * Sets the given result for the [requestKey]. This result will be delivered to a
 * [FragmentResultListener] that is called given to [setFragmentResultListener] with the same
 * [requestKey]. If no [FragmentResultListener] with the same key is set or the Lifecycle
 * associated with the listener is not at least [androidx.lifecycle.Lifecycle.State.STARTED], the
 * result is stored until one becomes available, or [clearFragmentResult] is called with the same
 * requestKey.
 *
 * @param requestKey key used to identify the result
 * @param result the result to be passed to another fragment or `null` if you want to
 *               clear out any pending result.
 */
@Deprecated("using after fragment 1.3.0")
fun Fragment.setFragmentResult(
    requestKey: String,
    result: Bundle
) = requireParentFragmentManager().setFragmentResult(requestKey, result)

/**
 * Clears the stored result for the given requestKey.
 *
 * This clears a result that was previously set a call to [setFragmentResult].
 *
 * If this is called with a requestKey that is not associated with any result, this method
 * does nothing.
 *
 * @param requestKey key used to identify the result
 */
fun Fragment.clearFragmentResult(
    requestKey: String
) = requireParentFragmentManager().clearFragmentResult(requestKey)

/**
 * Sets the [FragmentResultListener] for a given [requestKey]. Once this Fragment is
 * at least in the [androidx.lifecycle.Lifecycle.State.STARTED] state, any results set by
 * [setFragmentResult] using the same [requestKey] will be delivered to the
 * [FragmentResultListener.onFragmentResult] callback. The callback will remain active until this
 * Fragment reaches the [androidx.lifecycle.Lifecycle.State.DESTROYED] state or
 * [clearFragmentResultListener] is called with the same requestKey..
 *
 * @param requestKey requestKey used to store the result
 * @param listener listener for result changes or `null` to remove any previously
 *                 registered listener.
 */
@Deprecated("using after fragment 1.3.0")
fun Fragment.setFragmentResultListener(
    requestKey: String,
    listener: ((requestKey: String, bundle: Bundle) -> Unit)
) {
    requireParentFragmentManager().setFragmentResultListener(
        requestKey,
        this,
        FragmentResultListener { requestKey, result -> listener(requestKey, result) })
}

/**
 * Clears the stored [FragmentResultListener] for the given requestKey.
 *
 * This clears a [FragmentResultListener] that was previously set a call to
 * [setFragmentResultListener].
 *
 * If this is called with a requestKey that is not associated with any [FragmentResultListener],
 * this method does nothing.
 *
 * @param requestKey key used to identify the result
 */
fun Fragment.clearFragmentResultListener(
    requestKey: String
) = requireParentFragmentManager().clearFragmentResultListener(requestKey)
