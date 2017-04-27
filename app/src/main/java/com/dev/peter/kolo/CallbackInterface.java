package com.dev.peter.kolo;

/**
 * Simple Result/Error Callback Interface.
 */

interface CallbackInterface<T> {
    void onResult(T data);
    void onError();
}
