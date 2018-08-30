package com.example.anton.oregov1.activity.face3dActivity.model3D.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.anton.oregov1.activity.MainActivity;
import com.example.anton.oregov1.activity.camera.CameraActivity;
import com.example.anton.oregov1.activity.face3dActivity.model3D.portrait.headComposition.HeadComposition;
import com.example.anton.oregov1.activity.face3dActivity.model3D.util.ClientMultipartFormPost;
import com.example.anton.oregov1.activity.oregoPhotoManagement.OregoPhotoManager;
import com.example.anton.oregov1.fragment.CameraFragment;
import com.example.anton.oregov1.fragment.OregoGalleryFragment;
import com.example.anton.oregov1.screenshoot.ScreenShott;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public final class ModelActivity extends Activity {
    private ProgressDialog dialog;
    private ModelSurfaceView gLView;
    private String countModel;
    private ModelActivity THIS = this;
    private HeadComposition headComposition;

    @SuppressLint("ResourceAsColor")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle b = getIntent().getExtras();
        this.countModel = String.valueOf(b.getInt("countModel"));
        new AsynchronLoader().execute();
    }

    public String getCountModel() {
        return countModel;
    }

    public File getDirectory() {
        System.out.println(THIS.getCacheDir());
        return new File(new File(THIS.getCacheDir(), "/OREGO")
                , "/directory" + getCountModel());
    }


    class AsynchronLoader extends AsyncTask<Void, Integer, Void> {


        AsynchronLoader() {
            dialog = new ProgressDialog(THIS);
        }

        @Override
        protected final void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(0);
            try {
                File file = new File(getDirectory(), "resultObj.buf");
                publishProgress(1);
                if (!file.exists()) {
                    file = ClientMultipartFormPost.sendFile(getDirectory());
                }
                publishProgress(2);
                InputStream isFace = new FileInputStream(file);
                headComposition = new HeadComposition(isFace);
                publishProgress(3);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gLView = new ModelSurfaceView(THIS, headComposition);
                        setContentView(gLView);
                    }
                });
            } catch (IOException e) {
                publishProgress(404);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(THIS, "Что-то пошло не так... Проверьте соединение с интернетом.", Toast.LENGTH_LONG).show();
                    }
                });
                final File orego = new File(THIS.getExternalCacheDir(), "/OREGO");
                if (!orego.exists()) orego.mkdir();
                int count = OregoPhotoManager.INSTANCE.getSpacePhotos().size();
                File directoryPhoto = new File(orego, "directory" + count);
                if (!directoryPhoto.exists()) directoryPhoto.mkdir();
                final File photo = new File(directoryPhoto, "result.jpg");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CameraActivity.startForResult(THIS, photo, 0);
                    }
                });
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (values[0]) {
                case 0:
                    dialog.setMessage("Готовим Ваше фото");
                    break;
                case 1:
                    dialog.setMessage("Делаем магию с Вашей фотографией");
                    break;
                case 2:
                    dialog.setMessage("Рисуем ваше чудо-личико :)");
                    break;
                case 3:
                    dialog.setMessage("Готово");
                    break;
                case 404:
                    dialog.setMessage("Проблемы с сетью");
                    break;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
