package com.nhr.notebook.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;

import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditActivity";
    private boolean isNewDiary = false;
    private File imageFile;
    private Uri mImageUriFromFile;
    private Uri mImageUri;
    private Diary diary;
    private EditText title;
    private EditText context;
    private ImageView take_photo;
    private ImageView upload;
    private ImageView save;
    private TextView date;
    private TextView author;
    private DiaryDao diaryDao;
    private ImageSpan imageSpan;
    private SpannableString spannableStringContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_edit);
        diary = null;
        Intent intent = getIntent();
        diaryDao = new DiaryDao(this);
        diary = (Diary) intent.getSerializableExtra("clickDiary");
        if (diary == null) {

            SharedPreferences sharedPreferences = getSharedPreferences("my_info", Context.MODE_PRIVATE);

          String name = sharedPreferences.getString("username", "????????????");

            /*??????????????????????????????????????????*/
            diary = new Diary();
            //        diaryDao.insert(new Diary(1,"??????","???????????????","2019-01-01","?????????",photoArrayList));
            diary.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            diary.setAuthor(name);

            isNewDiary = true;
        }



        title = findViewById(R.id.edit_tittle);
        context = findViewById(R.id.edit_context);
        author = findViewById(R.id.edit_author);
        date = findViewById(R.id.edit_date);

        author.setText(diary.getAuthor());
        date.setText(diary.getDate());

        if(diary.getTittle()!=null&&diary.getTittle()!=""){
            title.setText(diary.getTittle());
        }
        else{
            title.setHint("????????????????????????");
        }
        Log.e(TAG, "onCreate: " + diary.getContext());
        context.setText(Tool.contextToSpSpannableString(diary.getContext()));
//        photo_List = new ArrayList<>();


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
        diary.setContext(context.getText().toString());
        diary.setTittle(title.getText().toString());
        diary.setPhoto(Tool.getFirstPhoto(diary.getContext()));
        if(diary.getTittle().equals("") && diary.getContext().equals("")){
            if(!isNewDiary){
                diaryDao.delete(diary.getId());
                Toast.makeText(this,"????????????",Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            intent.putExtra("diary",diary);
            setResult(RESULT_OK,intent);
            finish();
            return;
        }



        if (isNewDiary) {
            diaryDao.insert(diary);
        } else {
            diaryDao.update(diary);
        }
        Toast.makeText(this,"????????????",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("diary",diary);
        setResult(RESULT_OK,intent);
        finish();
//        diary.setPhoto(photo_List);

    }


    private void upLoad() {
        diary.setTittle(title.getText().toString());
        diary.setContext(context.getText().toString());


        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, 1);//????????????
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&resultCode==RESULT_OK) {
            if (data == null) {//??????????????????????????????????????????
                return;
            }
            Log.i("TAG", "onActivityResult: ImageUriFromAlbum: " + data.getData());
            if (resultCode == RESULT_OK) {
                Log.e("TAG", "onActivityResult: " + "4.4 +++");
                handleImageOnKitKat(data);//4.4??????????????????
            }
        }
        if(requestCode==2&&resultCode==RESULT_OK){
            context.setText(Tool.contextToSpSpannableString(diary.getContext()));
        }

    }


    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.e(TAG, "handleImageOnKitKat: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //?????????document?????????Uri????????????document id??????
            String docId = DocumentsContract.getDocumentId(uri);
            Log.e(TAG, "handleImageOnKitKat: ?????????document id?????? " + docId);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//????????????????????????id
                Log.e(TAG, "handleImageOnKitKat: ????????????" + id);
                String selection = MediaStore.Images.Media._ID + "=" + id;
                Log.e(TAG, "selection: " + selection);
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //?????????content?????????uri????????????????????????
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //?????????file?????????uri????????????????????????
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
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
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

            context.setText(Tool.contextToSpSpannableString(diary.getContext()));

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            String question = "???????????????????????????????????????";
            if(isNewDiary){
                diary.setContext(context.getText().toString());
                diary.setTittle(title.getText().toString());
                if((diary.getTittle().equals("") && diary.getContext().equals(""))||(diary.getTittle()==null&&diary.getContext()==null)){
                    finish();
                    return true;
                }
            }




        AlertDialog builder = null;
        SpannableString sure = new SpannableString("??????");
        ForegroundColorSpan span = new ForegroundColorSpan(Color.GREEN);
        sure.setSpan(span,0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString cancel = new SpannableString("?????????");
        ForegroundColorSpan span1 = new ForegroundColorSpan(Color.RED);
        cancel.setSpan(span1,0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            AlertDialog finalBuilder = builder;
            builder = new AlertDialog.Builder(EditActivity.this)
                    .setTitle("???????????????")
                    .setMessage(question)
                    .setPositiveButton(sure,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    save();
                                }
                            })
                    .setNegativeButton(cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
        }
        return true;
    }

    private void takePhoto() {
        diary.setTittle(title.getText().toString());
        diary.setContext(context.getText().toString());

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//???????????????Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            imageFile = createImageFile();//?????????????????????????????????
            //???????????????
            String str = diary.getContext();
            diary.setContext(str + "<img src='" + imageFile.getPath() + "'/>");



            mImageUriFromFile = Uri.fromFile(imageFile);
            Log.e(TAG, "copyImgToMyDisk: file path = " + imageFile.getPath());
            Log.i("TAG", "takePhoto: uriFromFile " + mImageUriFromFile);
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0???????????????FileProvider???File?????????Uri*/
                    mImageUri = FileProvider.getUriForFile(this, "com.nhr.notebook.fileprovider", imageFile);
                } else {
                    /*7.0?????????????????????Uri???fromFile?????????File?????????Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//????????????????????????Uri???????????????
                startActivityForResult(takePhotoIntent, 2);//????????????
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

//    private Spanned setMyText1(String text) {
//        return Html.fromHtml(text, new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String s) {
//                Drawable drawable = null;
//
//                Log.e("TAG", "getDrawable: " + getFilesDir());
//                ;
//                Log.e("TAG", "getDrawable: " + s);
//                drawable = Drawable.createFromPath(s); //??????????????????
//                if (drawable == null) {
//                    return null;
//                } else
//                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
//                            .getIntrinsicHeight());
//                return drawable;
//            }
//        }, null);
//    }


}