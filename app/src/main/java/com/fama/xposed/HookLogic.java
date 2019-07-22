package com.fama.xposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author DX
 * 注意：该类不要自己写构造方法，否者可能会hook不成功
 * 开发Xposed模块完成以后，建议修改xposed_init文件，并将起指向这个类,以提升性能
 * 所以这个类需要implements IXposedHookLoadPackage,以防修改xposed_init文件后忘记
 * Created by DX on 2017/10/4.
 */

public class HookLogic implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private final static String modulePackageName = HookLogic.class.getPackage().getName();
    private XSharedPreferences sharedPreferences;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if ("com.wonder.demo".equals(loadPackageParam.packageName)) {
            XposedHelpers.findAndHookMethod("com.wonder.demo.MainActivity", loadPackageParam.classLoader, "toastMessage", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(param.getResult().toString());
                    param.setResult("啦啦啦啦");
                    int x = sharedPreferences.getInt("example", 1);
                }
            });
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        this.sharedPreferences = new XSharedPreferences(modulePackageName, "default");
        XposedBridge.log(modulePackageName + " initZygote");
    }
}
