package com.mobilecloud.common;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mobilecloud.ext.ExtReactApplication;
import com.mobilecloud.pojo.AppPojo;
import com.mobilecloud.pojo.BundlePojo;
import com.mobilecloud.pojo.BundleUpdateRequestPojo;
import com.mobilecloud.pojo.update.AppUpdatePojo;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by WUGUOKAI on 2017/8/29.
 */

public class BundleManager {
    private final static String TAG = "BundleManager";
    private final static String BUNDLE_CONFIG = "bundle.json";
    private final static String BUNDLE_UPDATE_CONFIG = "bundle.update.json";
    private final static String BUNDLE_EXTENTION = ".android.bundle";
    private static String bundleVersion = "" ;
    private static BundleManager bundleManager;
    private Gson gson = new Gson();
    static String token = "";

    public synchronized static BundleManager getBundleManager() {
        if (bundleManager == null)
            bundleManager = new BundleManager();
        return bundleManager;
    }

    public static void setToken(String tokenStr){
        token = tokenStr;
    }

    public String downloadIcon(final AppPojo appPojo, final Application application){

        File fileMkdir = new File(getDiskCacheDir(application)+"/icon");
        fileMkdir.mkdir();
        final File file = new File(fileMkdir, "icon.zip");
        if (file != null && file.length() > 0) {
            file.delete();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            String urlstr = String.format("%s/getBundleFileList/%s",appPojo.getUrl(), appPojo.getId());
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization",token);
            connection.connect();

            InputStream fileInputStream = connection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            if (fileInputStream != null) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int len = 0;
                while ((len = fileInputStream.read(buffer)) != -1){
                    bout.write(buffer, 0, len);
                    fileOutputStream.write(buffer, 0, len);
                }
                fileInputStream.close();
                fileOutputStream.close();
            }
            unZipFiles(file, "");

            //修改下载的模块图标全部为png格式
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String filename = pathname.getName();
                   return filename.endsWith(".jpg");
                }
            };
            File icon = new File(file.getParent());
            File[] fileList = icon.listFiles(fileFilter);
            for (File file1 : fileList){
                File filename = new File(file1.getAbsolutePath().replace(".jpg", ".png"));
                file1.renameTo(filename);
            }

            return file.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bis != null){
                    bis.close();
                }
                if (bos != null){
                    bos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    // 更新Bundle
    //参数，1,bundle名称 2，更新url 3,

    public void updateBundle(final BundleUpdateRequestPojo bundleUpdateRequestPojo,final Application application,final HttpProcessCallBack httpProcessCallBack) {
        UpdateTask task = new UpdateTask(bundleUpdateRequestPojo.getName(), application, new HttpProcessCallBack() {
            @Override
            public void progress(float progress) {
                httpProcessCallBack.progress(progress);
            }

            @Override
            public void success(Object object) {
                File file = (File)object;
                updateBundleSuccess(application, bundleUpdateRequestPojo.getBundleId(),bundleUpdateRequestPojo.getName(),bundleVersion,file);
                httpProcessCallBack.success(object);
            }

            @Override
            public void failure(Object object) {
                httpProcessCallBack.failure(object);
            }
        });
        String url = String.format("%s/downFile/%s/%s",bundleUpdateRequestPojo.getAppUrl(),bundleUpdateRequestPojo.getAppId(),bundleUpdateRequestPojo.getBundleId());
//        String url = String.format("%s/downFile/%s",bundleUpdateRequestPojo.getAppUrl(),bundleUpdateRequestPojo.getBundleVersionId());
        task.execute(url);
    }

    //下载成功后改本地配置文件
    public synchronized void updateBundleSuccess(Application application, Integer bundleId, String name, String version, File bundleFile){
        try {
            AppPojo appPojo = getAppPojo(application);
            AppUpdatePojo appUpdatePojo = getAppUpdatePojo(application);
            Boolean appPojoChange = false;
            Boolean appUpdatePojoChange = false;
            if (name.equals(appPojo.getMainBundle().getName())) {
                appUpdatePojo.setMainBundleUpdate(null);
                appPojo.getMainBundle().setId(bundleId);
                appPojo.getMainBundle().setCurrentVersion(version);
                appPojo.getMainBundle().setPath(bundleFile.getAbsolutePath());
                appPojoChange = true;
                appUpdatePojoChange = true;
            } else {
                if (appUpdatePojo.getBundlesUpdate().get(name) != null) {
                    appUpdatePojo.getBundlesUpdate().remove(name);
                    appUpdatePojoChange = true;
                }
                if (appPojo.getBundles().get(name) != null) {
                    BundlePojo bundlePojo = appPojo.getBundles().get(name);
                    bundlePojo.setId(bundleId);
                    bundlePojo.setCurrentVersion(version);
                    bundlePojo.setPath(bundleFile.getAbsolutePath());
                    appPojoChange = true;
                } else {
                    BundlePojo bundlePojo = new BundlePojo();
                    bundlePojo.setId(bundleId);
                    bundlePojo.setCurrentVersion(version);
                    bundlePojo.setName(name);
                    bundlePojo.setPath(bundleFile.getAbsolutePath());
                    appPojo.getBundles().put(name, bundlePojo);
                    appPojoChange = true;
                }
            }

        bundleVersion = "";
        if(appPojoChange){
            writeAppPojo(application,appPojo);
        }
        if(appUpdatePojoChange){
            writeAppUpdatePojo(application,appUpdatePojo);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //通过application获取AppPojo，通过bundle.json
    public AppPojo getAppPojo(Application application){
        File file = new File(getDiskCacheDir(application), BUNDLE_CONFIG);
        if (file != null && file.length() > 0) {
            try {
                return gson.fromJson(new BufferedReader(new FileReader(file)), AppPojo.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //获取AppUpdatePojo，通过bundle.update.json
    public AppUpdatePojo getAppUpdatePojo(Application application){
        File file = new File(getDiskCacheDir(application), BUNDLE_UPDATE_CONFIG);
        if (file != null && file.length() > 0) {
            try {
                return gson.fromJson(new BufferedReader(new FileReader(file)), AppUpdatePojo.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //把AppPojo写入到bundle.json中
    public void writeAppPojo(Application application,AppPojo appPojo){
        File file = new File(getDiskCacheDir(application), BUNDLE_CONFIG);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(gson.toJson(appPojo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //把AppUpdatePojo写入到bundle.update.json中
    public void writeAppUpdatePojo(Application application,AppUpdatePojo appUpdatePojo){
        File file = new File(getDiskCacheDir(application), BUNDLE_UPDATE_CONFIG);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(gson.toJson(appUpdatePojo));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //加载bundle
    public String loadMainBundle(final Application application) {
        final AppPojo appPojo = syncBundleConfig(application);
        final AppUpdatePojo appUpdatePojo = syncBundleUpdateConfig(application);
        checkBundleConfigUpdate(application,appPojo,new HttpProcessCallBack(){
            @Override
            public void progress(float progress) {

            }

            @Override
            public void success(Object object) {
                //初始化的时候如果主模块有更新就下载
                /*AppUpdatePojo appUpdatePojo = (AppUpdatePojo)object;
                if(appUpdatePojo.getMainBundleUpdate()!=null){
                    BundleUpdateRequestPojo bundleUpdateRequestPojo = new BundleUpdateRequestPojo(appPojo.getId(), appPojo.getName(), appPojo.getCurrentVersion(), appPojo.getUrl(),appPojo.getMainBundle().getName(),appUpdatePojo.getMainBundleUpdate().getTargetVersion(), 0);
                    updateBundle(bundleUpdateRequestPojo, application, new HttpProcessCallBack() {

                        @Override
                        public void progress(float progress) {

                        }

                        @Override
                        public void success(Object object) {
                            final File file = (File)object;
                            loadBundle(((MainApplication)application).getActivity(MainActivity.class.getName()),file);
                        }

                        @Override
                        public void failure(Object object) {

                        }
                    });
                }*/
            }

            @Override
            public void failure(Object object) {

            }
        });
        return appPojo.getMainBundle().getPath();
    }

    //第一次启动，同步配置文件与bundle文件
    //并且加锁，单线程通过
    public synchronized AppPojo syncBundleConfig(Application application) {
        //(Arrays.asList(application.getAssets().list(BUNDLE_CONFIG)).isEmpty())

        try {
            File file = new File(getDiskCacheDir(application), BUNDLE_CONFIG);
            if (file != null && file.length() > 0) {
                return gson.fromJson(new BufferedReader(new FileReader(file)), AppPojo.class);
            }
            //BufferedReader br = new BufferedReader(new FileReader(application.getAssets().open(BUNDLE_CONFIG)));
            BufferedReader br = new BufferedReader(new InputStreamReader(application.getAssets().open(BUNDLE_CONFIG), "UTF-8"));
            AppPojo appPojo = gson.fromJson(br, AppPojo.class);
            Assertions.assertNotNull(appPojo.getMainBundle());
            //复制index.android.bundle到手机路径下
            appPojo.getMainBundle().setPath(copyAssetsBundle(application,appPojo.getMainBundle().getName(), appPojo.getMainBundle().getPath()));
            //复制bundle.json到手机路径下
            for (Map.Entry<String, BundlePojo> bundleConfig : appPojo.getBundles().entrySet()) {
                bundleConfig.getValue().setPath(copyAssetsBundle(application, bundleConfig.getValue().getName(), bundleConfig.getValue().getPath()));
                //TODO 对自带模块解压时复制图标到icon文件夹
                String bundleName = bundleConfig.getValue().getName();
                String bundlePath = getDiskCacheDir(application)+"/"+bundleName;
                File iconFile = new File(bundlePath,bundleName+".png");
                File iconMkdir = new File(getDiskCacheDir(application)+"/icon");
                iconMkdir.mkdir();
                File icon = new File(iconMkdir,bundleName+".png");
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(iconFile));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(icon));
                int len = -1;
                byte[] buffer = new byte[512];
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                    bos.flush();
                }
            }
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(appPojo));
            writer.close();
            return appPojo;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized AppUpdatePojo syncBundleUpdateConfig(Application application) {
        //(Arrays.asList(application.getAssets().list(BUNDLE_CONFIG)).isEmpty())

        try {
            File file = new File(getDiskCacheDir(application), BUNDLE_UPDATE_CONFIG);
            if (file != null && file.length() > 0) {
                return gson.fromJson(new BufferedReader(new FileReader(file)), AppUpdatePojo.class);
            }
            //BufferedReader br = new BufferedReader(new FileReader(application.getAssets().open(BUNDLE_CONFIG)));
            BufferedReader br = new BufferedReader(new InputStreamReader(application.getAssets().open(BUNDLE_UPDATE_CONFIG), "UTF-8"));
            AppUpdatePojo appUpdatePojo = gson.fromJson(br, AppUpdatePojo.class);
            //Assertions.assertNotNull(appUpdatePojo.mainBundleUpdate);
            // for (Map.Entry<String, BundleUpdatePojo> bundleConfig : appUpdatePojo.bundles.entrySet()) {
            //     bundleConfig.getValue() = copyAssetsBundle(application, bundleConfig.getValue());
            // }
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(appUpdatePojo));
            writer.close();
            return appUpdatePojo;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断模块是否需要更新
    public void checkBundleConfigUpdate(final Application application, AppPojo appPojo,final HttpProcessCallBack httpProcessCallBack) {
        Assertions.assertNotNull(appPojo);
        Callback callback = new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
                httpProcessCallBack.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String result = response.body().string();
                File file = new File(getDiskCacheDir(application), BUNDLE_UPDATE_CONFIG);
                FileWriter writer = new FileWriter(file);
                writer.write(result);
                writer.close();
                httpProcessCallBack.success(gson.fromJson(result, AppUpdatePojo.class));
            }
        };
        String bundleConfig = gson.toJson(appPojo);
        MediaType jsonType
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonType, bundleConfig);
        Request request = new Request.Builder()
                .url(appPojo.getUrl() + "/checkBundle")
                .addHeader("Authorization",token)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    //生成或者获取路径com.mobilecloud/cache
    public String getDiskCacheDir(Application application) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = application.getExternalCacheDir().getPath();
        } else {
            cachePath = application.getCacheDir().getPath();
        }
        return cachePath;
    }


    //复制文件到手机路径path下
    private String copyAssetsBundle(Application application, String name, String path) {
        String fileName = path.replaceAll("assets://", "");

        File fileMkdir = new File(getDiskCacheDir(application)+"/"+name);
        fileMkdir.mkdir();
        File file = new File(fileMkdir,fileName);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(application.getAssets().open(fileName));
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = -1;
            byte[] buffer = new byte[512];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
            unZipFiles(file, "");
            File returnFile = new File(fileMkdir,name+BUNDLE_EXTENTION);
            return returnFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }


    private class UpdateTask extends AsyncTask<String, Float, File> {
        private HttpProcessCallBack httpProcessCallBack;
        private Application application;
        private String bundleName;

        private UpdateTask(String bundleName ,Application application, HttpProcessCallBack httpProcessCallBack) {
            this.application = application;
            this.httpProcessCallBack = httpProcessCallBack;
            this.bundleName = bundleName;
        }

        public String getDiskCacheDir() {
            String cachePath = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = application.getExternalCacheDir().getPath();
            } else {
                cachePath = application.getCacheDir().getPath();
            }
            return cachePath;
        }

        @Override
        protected File doInBackground(String... params) {
            return downloadBundle(params[0]);
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (httpProcessCallBack != null && values != null && values.length > 0) {
                httpProcessCallBack.progress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(File file) {
            if (httpProcessCallBack != null) httpProcessCallBack.progress(100f);
            //重写初始化rn组件
            httpProcessCallBack.success(file);
        }


        /**
         * 模拟bundle下载链接url
         *
         * @param remoteUrl
         */
        @Nullable
        private File downloadBundle(String remoteUrl) {
            //删除以前的文件
            File fileMkdir = new File(getDiskCacheDir()+"/"+bundleName);
            fileMkdir.mkdir();
            File file = new File(fileMkdir, bundleName+".NEW.android.zip");
            if (file != null && file.length() > 0) {
                file.delete();
            }
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(remoteUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization",token);
                connection.connect();

                Map<String, List<String>> map = connection.getHeaderFields();
                List<String> contentDis = map.get("Content-Disposition");
                bundleVersion = contentDis.get(0).substring(contentDis.get(0).indexOf("-")+1,contentDis.get(0).length()-4);

                // getting file length
                InputStream fileInputStream = connection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int size = Integer.parseInt(map.get("Content-Size").get(0));
                if(fileInputStream!=null){
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    int len = 0;
                    long total = 0;
                    while((len = fileInputStream.read(buffer)) !=-1)
                    {
                        total += len;
                        bout.write(buffer, 0, len);
                        fileOutputStream.write(buffer, 0, len);
                        float progress = total * 1.0f / size;
                        publishProgress(progress);
                        //Thread.sleep(10);
                    }
                    String result = new String(bout.toByteArray(),"UTF-8");
                    fileInputStream.close();
                    fileOutputStream.close();
                }

                //删除以前的文件
                File lastFile = new File(fileMkdir, bundleName+".android.zip");
                if (lastFile != null && lastFile.length() > 0) {
                    lastFile.delete();
                }
                file.renameTo(lastFile);
                unZipFiles(lastFile,"");
                File returnFile = new File(fileMkdir,bundleName+BUNDLE_EXTENTION);
                return returnFile;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) throws IOException {

        ZipFile zip = new ZipFile(zipFile);//解决中文文件夹乱码
        String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('/'));

        File pathFile = new File(descDir+name);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + name +"/"+ zipEntryName).replaceAll("\\*", "/");

            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // 输出文件路径信息
//          System.out.println(outPath);

            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
        return;
    }

    private void loadBundleLegacy(final Activity currentActivity) {
        if (currentActivity == null) {
            // The currentActivity can be null if it is backgrounded / destroyed, so we simply
            // no-op to prevent any null pointer exceptions.
            return;
        }

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.recreate();
            }
        });
    }

    // Use reflection to find the ReactInstanceManager. See #556 for a proposal for a less brittle way to approach this.
    private ReactInstanceManager resolveInstanceManager(Activity activity) throws NoSuchFieldException, IllegalAccessException {
        return  ((ExtReactApplication)activity.getApplication()).getReactNativeHost(activity).getReactInstanceManager();
    }

    // Use reflection to find and set the appropriate fields on ReactInstanceManager. See #556 for a proposal for a less brittle way
    // to approach this.
    private void setJSBundle(ReactInstanceManager instanceManager, String latestJSBundleFile) throws IllegalAccessException {
        try {
            Field bundleLoaderField = instanceManager.getClass().getDeclaredField("mBundleLoader");
            Field JSMainModuleNameField = instanceManager.getClass().getDeclaredField("mJSMainModuleName");
            Class<?> jsBundleLoaderClass = Class.forName("com.facebook.react.bridge.JSBundleLoader");
            Method createFileLoaderMethod = null;
            String createFileLoaderMethodName = latestJSBundleFile.toLowerCase().startsWith("assets://")
                    ? "createAssetLoader" : "createFileLoader";

            Method[] methods = jsBundleLoaderClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(createFileLoaderMethodName)) {
                    createFileLoaderMethod = method;
                    break;
                }
            }

            if (createFileLoaderMethod == null) {
                throw new NoSuchMethodException("Could not find a recognized 'createFileLoader' method");
            }

            int numParameters = createFileLoaderMethod.getGenericParameterTypes().length;
            Object latestJSBundleLoader;
            //不支持asset
            if (numParameters == 1) {
                // RN >= v0.34
                latestJSBundleLoader = createFileLoaderMethod.invoke(jsBundleLoaderClass, latestJSBundleFile);
            //} else if (numParameters == 2) {
                // AssetLoader instance
            //    latestJSBundleLoader = createFileLoaderMethod.invoke(jsBundleLoaderClass, getReactApplicationContext(), latestJSBundleFile);
            } else {
                throw new NoSuchMethodException("Could not find a recognized 'createFileLoader' method");
            }

            bundleLoaderField.setAccessible(true);
            bundleLoaderField.set(instanceManager, latestJSBundleLoader);
            JSMainModuleNameField.setAccessible(true);
            JSMainModuleNameField.set(instanceManager,null);
        } catch (Exception e) {
            throw new IllegalAccessException("Could not setJSBundle");
        }
    }

    private void resetReactRootViews(ReactInstanceManager instanceManager) throws NoSuchFieldException, IllegalAccessException {
        Field mAttachedRootViewsField = instanceManager.getClass().getDeclaredField("mAttachedRootViews");
        mAttachedRootViewsField.setAccessible(true);
        List<ReactRootView> mAttachedRootViews = (List<ReactRootView>)mAttachedRootViewsField.get(instanceManager);
        for (ReactRootView reactRootView : mAttachedRootViews) {
            reactRootView.removeAllViews();
            reactRootView.setId(View.NO_ID);
        }
        mAttachedRootViewsField.set(instanceManager, mAttachedRootViews);
    }

    //加载bundle文件，并显示
    public void loadBundle(final Activity activity, File file) {
        try {
            // #1) Get the ReactInstanceManager instance, which is what includes the
            //     logic to reload the current React context.
            final ReactInstanceManager instanceManager = resolveInstanceManager(activity);
            if (instanceManager == null) {
                return;
            }

            String latestJSBundleFile = file.getAbsolutePath();

            // #2) Update the locally stored JS bundle file path
            setJSBundle(instanceManager, latestJSBundleFile);
            //#3) Get the context creation method and fire it on the UI thread (which RN enforces)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        // This workaround has been implemented in order to fix https://github.com/facebook/react-native/issues/14533
                        // resetReactRootViews allows to call recreateReactContextInBackground without any exceptions
                        // This fix also relates to https://github.com/Microsoft/react-native-code-push/issues/878
                        resetReactRootViews(instanceManager);
                        instanceManager.recreateReactContextInBackground();
                    } catch (Exception e) {
                        // The recreation method threw an unknown exception
                        // so just simply fallback to restarting the Activity (if it exists)
                        loadBundleLegacy(activity);
                    }
                }
            });

        } catch (Exception e) {
            // Our reflection logic failed somewhere
            // so fall back to restarting the Activity (if it exists)
            e.printStackTrace();
            loadBundleLegacy(activity);
        }
    }
}
