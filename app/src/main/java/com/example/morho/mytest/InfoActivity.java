package com.example.morho.mytest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView list_info, list_ctrl;
    private TextView textView;
    private ImageView imageView;
    private final String path = "/myCampus/avatar.jpg";
    private Button btn_logout;
    protected final int CHOOSE_PHOTO = 0;
    protected final int MY_PERMISSION = 11;
    protected final int PHOTO_REQUEST_CUT = 1;
    protected final int CROP_PHOTO = 2;
    protected Context context;
    protected File file;
    protected Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.info_bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.list_info = (ListView) findViewById(R.id.my_info_list);
        this.btn_logout = (Button) findViewById(R.id.button_logout);
        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> data, data1, data2;
        SharedPreferences share_data = getSharedPreferences("data", Context.MODE_PRIVATE);
        data = new HashMap<>();
        data1 = new HashMap<>();
        data2 = new HashMap<>();
        data.put("name", "我的昵称");
        list.add(data);
        data1.put("name", "我的用户名");
        data1.put("text", share_data.getString("USR_NAME", null));
        list.add(data1);
        data2.put("name", "个性签名");
        list.add(data2);
        TypedArray array = getResources().obtainTypedArray(R.array.info_img);
        myInfoAdapter adapter = new myInfoAdapter(list, array, this);
        list_info.setAdapter(adapter);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("计算机工程学院");
        arrayList.add("网络工程142");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, arrayList);
        this.textView = (TextView) findViewById(R.id.info_change);
        this.context = getBaseContext();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(InfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(InfoActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, MY_PERMISSION);
                File image = new File(Environment.getExternalStorageDirectory(), "output.jpg");
                file = image;
                if (image.exists()) {
                    image.delete();
                }
                try {
                    image.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Uri imageUri = Uri.fromFile(image);
                InfoActivity.this.imageUri = imageUri;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_PHOTO);
            }
        });
        this.imageView = (ImageView) findViewById(R.id.info_my_img);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void do_logout(View v) {
        System.out.println("You have clicked it");
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "用户已登出！", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    InputStream inputStream = null;
                    try {
                        inputStream  = context.getContentResolver().openInputStream(data.getData());
                        OutputStream os = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        while(true) {
                            if(inputStream.available() < 1024) { // 剩余的数据比1024字节少，一位一位读出再写入目的文件
                                int c = -1;
                                while((c = inputStream.read()) != -1) {
                                    os.write(c);
                                }
                                break;
                            }
                            else {
                                // 从来源文件读取数据至缓冲区
                                inputStream.read(buffer);
                                // 将数组数据写入目的文件
                                os.write(buffer);
                            }
                        }
                        os.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                    }
                    //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //imageView.setImageBitmap(bitmap);
                    //System.out.println("The uri is " + imageUri);
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/jpeg");
                    intent.putExtra("crop", "true");
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //The proportion of the crop box is 1:1
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    //Crop the output image size
//                    intent.putExtra("outputX", 1280);
//                    intent.putExtra("outputY", 1280);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            }
            case CROP_PHOTO: {
                System.out.println("Crop Photo!");
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(CircleImageView.getCroppedBitmap(bitmap, 1280));
                    System.out.println("IMG has been set!");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
