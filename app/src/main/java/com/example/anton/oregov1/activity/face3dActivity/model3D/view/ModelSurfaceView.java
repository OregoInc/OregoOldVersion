package com.example.anton.oregov1.activity.face3dActivity.model3D.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.opengl.GLSurfaceView;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.MotionEvent;

import com.example.anton.oregov1.activity.face3dActivity.model3D.controller.TouchController;
import com.example.anton.oregov1.activity.face3dActivity.model3D.modelRender.ModelRender;
import com.example.anton.oregov1.activity.face3dActivity.model3D.portrait.headComposition.HeadComposition;
import com.example.anton.oregov1.activity.face3dActivity.model3D.util.ClientMultipartFormPost;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@SuppressLint("ViewConstructor")
public final class ModelSurfaceView extends GLSurfaceView {

    private ModelRender mRenderer;
    private ModelActivity parent;
    private TouchController touchHandler;

    public ModelSurfaceView(final ModelActivity parent, HeadComposition headComposition) {
        super(parent);
        this.parent = parent;
        setEGLContextClientVersion(3);
        mRenderer = new ModelRender(this, headComposition);
        setRenderer(mRenderer);
        touchHandler = new TouchController(this, mRenderer);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchHandler.onTouchEvent(event);
    }


    public ModelActivity getModelActivity() {
        return parent;
    }

    public ModelRender getmRenderer() {
        return mRenderer;
    }

}