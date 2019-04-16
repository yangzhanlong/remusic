package cn.droidlover.xdroidmvp.imageloader;

import android.content.Context;
import android.widget.ImageView;

import java.io.File;

public class GlideLoader implements ILoader{
    @Override
    public void init(Context context) {
    }

    @Override
    public void loadNet(ImageView target, String url, Options options) {

    }

    @Override
    public void loadNet(Context context, String url, Options options, LoadCallback callback) {

    }

    @Override
    public void loadResource(ImageView target, int resId, Options options) {

    }

    @Override
    public void loadAssets(ImageView target, String assetName, Options options) {

    }

    @Override
    public void loadFile(ImageView target, File file, Options options) {

    }

    @Override
    public void clearMemoryCache(Context context) {

    }

    @Override
    public void clearDiskCache(Context context) {

    }

    @Override
    public void resume(Context context) {

    }

    @Override
    public void pause(Context context) {

    }
}
