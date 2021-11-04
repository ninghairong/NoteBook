package com.nhr.notebook.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditActivity";
    private File imageFile;
    private Uri mImageUriFromFile;
    private Uri mImageUri;
    private Diary diary;
    private EditText title;
    private EditText context;
    private ImageView take_photo;
    private ImageView upload;
    private ImageView save;
    private String text;
    private DiaryDao diaryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        diaryDao = new DiaryDao(this);
        diary = (Diary) intent.getSerializableExtra("clickDiary");
        title = findViewById(R.id.edit_tittle);
        context = findViewById(R.id.edit_context);
        title.setText(diary.getTittle());
        context.setText(setMyText(diary.getContext()));

        Editable var = context.getText();

        upload = findViewById(R.id.upload_picture);
        upload.setOnClickListener(this);

        take_photo = findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);

        save = findViewById(R.id.save);
        save.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                save();
                break;
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.upload_picture:
                upLoad();
                break;
        }
    }

    private void save() {
//        diary.setContext();

        context.

        Log.e(TAG, "save: "+Html.toHtml(setMyText(diary.getContext())) );
        diaryDao.update(diary);
    }


    private void upLoad() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, 1);//打开相册
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data == null) {//如果没有选取照片，则直接返回
                return;
            }
            Log.i("TAG", "onActivityResult: ImageUriFromAlbum: " + data.getData());
            if (resultCode == RESULT_OK) {
                Log.e("TAG", "onActivityResult: " + "4.4 +++");
                handleImageOnKitKat(data);//4.4之后图片解析
            }
        }

    }


    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.e(TAG, "handleImageOnKitKat: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则提供document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            Log.e(TAG, "handleImageOnKitKat: 则提供document id处理 " + docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                Log.e(TAG, "handleImageOnKitKat: 数字格式" + id);
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.e(TAG, "selection: " + selection);
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        Log.e(TAG, "handleImageOnKitKat: " + imagePath);
        copyImgToMyDisk(imagePath);
    }


    public void copyImgToMyDisk(String imagePath) {
        Bitmap bitmap;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Diary" + timeStamp;
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } else {
            Toast.makeText(this, "获取不到图片", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getFilesDir() + File.separator, imageFileName + ".jpg");
        if (file == null) {
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.e(TAG, "copyImgToMyDisk: file path = " + file.getPath());
            String str = diary.getContext();
            diary.setContext(str + "<img src='" + file.getPath() + "'/>");
            context.setText(setMyText(diary.getContext()));
            diaryDao.update(diary);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            imageFile = createImageFile();//创建用来保存照片的文件
            mImageUriFromFile = Uri.fromFile(imageFile);
            Log.e(TAG, "copyImgToMyDisk: file path = " + imageFile.getPath());
            Log.i("TAG", "takePhoto: uriFromFile " + mImageUriFromFile);
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, "com.nhr.notebook.fileprovider", imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, 2);//打开相机
            }
        }
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Diary" + timeStamp + "_";
        File storageDir = getFilesDir();
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private Spanned setMyText(String text) {
        return Html.fromHtml(text, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                Drawable drawable = null;

                Log.e("TAG", "getDrawable: " + getFilesDir());
                ;
                Log.e("TAG", "getDrawable: " + s);
                drawable = Drawable.createFromPath(s); //显示本地图片
                if (drawable == null) {
                    return null;
                } else
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                            .getIntrinsicHeight());
                return drawable;
            }
        }, null);
    }


}