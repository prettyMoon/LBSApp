package com.example.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.config.AppConfiguration;
import com.example.myapplication.tools.MyDialog;
import com.example.myapplication.tools.SelfDialog;
import com.example.myapplication.tools.ShowToast;
import com.example.myapplication.tools.UserTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hongli on 2018/5/18.
 */

public class UploadDataActivity extends Activity implements View.OnClickListener, SelfDialog.SelfDialogListener {
    private Button btnCamera, btnAlbum;
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 1;
    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;
    private EditText editText;
    private ImageView imgAdd;
    private SelfDialog selfDialog;
    private String mPhotoPath;
    private File mPhotoFile;
    private MyDialog diaLog;
    private TextView tvLeft, tvMiddle, tvRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_data);
        initView();
        initDialog();
    }

    private void initDialog() {
        diaLog = new MyDialog(UploadDataActivity.this);
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }

    private void initView() {
        editText = findViewById(R.id.up_text);
        btnCamera = this.findViewById(R.id.camera);
        btnAlbum = this.findViewById(R.id.album);
        imgAdd = this.findViewById(R.id.img_add);
        btnCamera.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        selfDialog = new SelfDialog(UploadDataActivity.this);
        selfDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selfDialog.setCancelable(true);
        selfDialog.setListener(this);
        tvLeft = this.findViewById(R.id.left);
        tvMiddle = this.findViewById(R.id.middle);
        tvRight = this.findViewById(R.id.right);
        tvMiddle.setText("上传进度");
        tvRight.setText("上传");
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                selfDialog.show();
                break;
            case R.id.left:
                finish();
                break;
            case R.id.right:
                if (!diaLog.isShowing()) {
                    diaLog.show();
                }
                postTask(AppConfiguration.url_upload_data);
                break;

        }
    }

    private void useCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
        mPhotoPath = getSDPath() + "/" + System.currentTimeMillis() + ".jpg";//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
        mPhotoFile = new File(mPhotoPath);
        String str = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!mPhotoFile.exists()) {
            try {
                mPhotoFile.createNewFile();//创建新文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);//跳转界面传回拍照所得数据
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }


    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                //拍照
                case CAMERA_REQUEST_CODE: {
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mPhotoFile)));
                        imgAdd.setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case GALLERY_REQUEST_CODE: {
                    try {
                        //该uri是上一个Activity返回的
                        imageUri = data.getData();
                        if (imageUri != null) {
                            mPhotoFile = getFileByUri(imageUri, UploadDataActivity.this);
                            Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mPhotoFile)));
                            imgAdd.setImageBitmap(bit);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public File getFileByUri(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                useCamera();
            } else {
                Toast.makeText(UploadDataActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                Toast.makeText(UploadDataActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void cameraCallback() {
        if (ContextCompat.checkSelfPermission(UploadDataActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(UploadDataActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_CODE);
        } else {
            useCamera();
        }
        if (selfDialog.isShowing()) {
            selfDialog.dismiss();
        }
    }

    @Override
    public void albumCallback() {
        if (ContextCompat.checkSelfPermission(UploadDataActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(UploadDataActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_CODE);
        } else { //权限已经被授予，在这里直接写要执行的相应方法即可
            choosePhoto();
        }
        if (selfDialog.isShowing()) {
            selfDialog.dismiss();
        }
    }

    public void postTask(String url) {
        AjaxParams params = new AjaxParams();
        params.put("msg", editText.getText().toString());
        params.put("task_id", getIntent().getExtras().getString("task_id"));
        try {
            params.put("file", mPhotoFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                ShowToast.showToast(UploadDataActivity.this, "发布失败！");
                if (diaLog.isShowing()) {
                    diaLog.dismiss();
                }
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object t) {
                try {
                    JSONObject jsonObject = new JSONObject(t.toString());
                    Log.e("zhl", jsonObject.toString());
                    if (jsonObject.getInt("status") == 1) {
                        ShowToast.showToast(UploadDataActivity.this, "发布成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    } else {
                        ShowToast.showToast(UploadDataActivity.this, "发布失败！");
                    }
                    if (diaLog.isShowing()) {
                        diaLog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
