package com.dev.peter.kolo;

/**
 * Interface for Activities to allow other classes to request permissions through
 * the calling activity.
 */

interface PermissionRequester {
    void requestPermission(String identifier, PermissionCallback permissionCallback);
}
