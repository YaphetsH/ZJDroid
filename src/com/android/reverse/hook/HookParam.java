package com.android.reverse.hook;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class HookParam {
	public Member method;
	public Object thisObject;
	public Object[] args;
	private Object mResult;
	private Throwable mThrowable;
	private boolean mHasResult = false;
	private boolean mHasThrowable = false;
	private Map<String, Object> mExtra = null;

	private HookParam() {
	}

	public static HookParam fromXposed(MethodHookParam param) {
		HookParam xparam = new HookParam();
		xparam.method = param.method;
		xparam.thisObject = param.thisObject;
		xparam.args = param.args;
		xparam.mResult = param.getResult();
		xparam.mThrowable = param.getThrowable();
		return xparam;
	}

	public boolean doesReturn(Class<?> result) {
		if (this.method instanceof Method)
			return (((Method) this.method).getReturnType().equals(result));
		return false;
	}

	public void setResult(Object result) {
		if (result instanceof Throwable) {
			setThrowable((Throwable) result);
		} else {
			mResult = result;
			mHasResult = true;
		}
	}

	public boolean hasResult() {
		return mHasResult;
	}

	public Object getResult() {
		return mResult;
	}

	public boolean doesThrow(Class<?> ex) {
		if (this.method instanceof Method)
			for (Class<?> t : ((Method) this.method).getExceptionTypes())
				if (t.equals(ex))
					return true;
		return false;
	}

	public void setThrowable(Throwable ex) {
		mThrowable = ex;
		mHasThrowable = true;
	}

	public boolean hasThrowable() {
		return mHasThrowable;
	}

	public Throwable getThrowable() {
		return mThrowable;
	}

	public Object getExtras() {
		return mExtra;
	}

	@SuppressWarnings("unchecked")
	public void setExtras(Object extra) {
		mExtra = (Map<String, Object>) extra;
	}

	public void setObjectExtra(String name, Object value) {
		if (mExtra == null)
			mExtra = new HashMap<String, Object>();
		mExtra.put(name, value);
	}

	public Object getObjectExtra(String name) {
		return (mExtra == null ? null : mExtra.get(name));
	}


}
