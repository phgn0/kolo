package com.dev.peter.kolo;

/**
 * Simple Result/Error SimpleCallback Interface.
 */

interface SimpleCallback<T> {
    void onResult(T data);
    void onError();
}

interface PermissionCallback {
    void onGranted();
    void onRejected();
}

interface SingleCallback {
    void function();
}

interface RequestCallback<A, B> {
    void request(A data, B callback);
}