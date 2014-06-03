/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace\\com.nf.framework\\src\\com\\nf\\framework\\http\\IHttpRequestService.aidl
 */
package com.nf.framework.http;
public interface IHttpRequestService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.nf.framework.http.IHttpRequestService
{
private static final java.lang.String DESCRIPTOR = "com.nf.framework.http.IHttpRequestService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.nf.framework.http.IHttpRequestService interface,
 * generating a proxy if needed.
 */
public static com.nf.framework.http.IHttpRequestService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.nf.framework.http.IHttpRequestService))) {
return ((com.nf.framework.http.IHttpRequestService)iin);
}
return new com.nf.framework.http.IHttpRequestService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_startManage:
{
data.enforceInterface(DESCRIPTOR);
this.startManage();
reply.writeNoException();
return true;
}
case TRANSACTION_isRunningTask:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isRunningTask(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isWaitingTask:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isWaitingTask(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_addTaskByGet:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.addTaskByGet(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_addTaskByPost:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.util.List _arg2;
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_arg2 = data.readArrayList(cl);
this.addTaskByPost(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_deleteTask:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.deleteTask(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopManage:
{
data.enforceInterface(DESCRIPTOR);
this.stopManage();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.nf.framework.http.IHttpRequestService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void startManage() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startManage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isRunningTask(java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_isRunningTask, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isWaitingTask(java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_isWaitingTask, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addTaskByGet(java.lang.String url, int priority) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
_data.writeInt(priority);
mRemote.transact(Stub.TRANSACTION_addTaskByGet, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addTaskByPost(java.lang.String url, int priority, java.util.List list) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
_data.writeInt(priority);
_data.writeList(list);
mRemote.transact(Stub.TRANSACTION_addTaskByPost, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void deleteTask(java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_deleteTask, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopManage() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopManage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startManage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isRunningTask = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isWaitingTask = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_addTaskByGet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_addTaskByPost = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_deleteTask = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_stopManage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public void startManage() throws android.os.RemoteException;
public boolean isRunningTask(java.lang.String url) throws android.os.RemoteException;
public boolean isWaitingTask(java.lang.String url) throws android.os.RemoteException;
public void addTaskByGet(java.lang.String url, int priority) throws android.os.RemoteException;
public void addTaskByPost(java.lang.String url, int priority, java.util.List list) throws android.os.RemoteException;
public void deleteTask(java.lang.String url) throws android.os.RemoteException;
public void stopManage() throws android.os.RemoteException;
}
