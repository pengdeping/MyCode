// IBinderPoolManager.aidl
package com.android.binderpool;

interface IBinderPoolManager {
    IBinder queryBinder(int binderCode);
}