package com.quintus.labs.datingapp.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.rest.ProgressRequestBody;
import com.quintus.labs.datingapp.rest.RequestModel.EditProfileUpdateRequest;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class EditProfileActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private static final String TAG = "EditProfileActivity";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    //firebase
    private static final int REQUEST_PERMISSION_SETTING = 101;
    Button man, woman,everyone,other;
    TextView man_text, women_text,everyone_text,toolbartag,other_text;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6,imageViewOptions;

    TextView textViewAddImages;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext = EditProfileActivity.this;
    private ImageView back;

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private Context context;
    private UserData userInfo;
    private ImageModel selectedImageModel;
    private RelativeLayout control_profile,logout;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        requestMultiplePermissions();
        imageView1 = findViewById(R.id.image_view_1);
        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);
        imageView5 = findViewById(R.id.image_view_5);
        imageView6 = findViewById(R.id.image_view_6);
        imageViewOptions = findViewById(R.id.imageViewOptions);
        textViewAddImages = findViewById(R.id.textViewAddImages);
        toolbartag = findViewById(R.id.textViewTitle);
        control_profile = findViewById(R.id.control_profile);
        logout = findViewById(R.id.logout);

        toolbartag.setText("Manage Profile");
        imageViewOptions.setVisibility(View.GONE);

        man = findViewById(R.id.man_button);
        woman = findViewById(R.id.woman_button);
        everyone = findViewById(R.id.everyone_button);
        other = findViewById(R.id.other_button);
        man_text = findViewById(R.id.man_text);
        women_text = findViewById(R.id.woman_text);
        everyone_text = findViewById(R.id.everyone_text);
        other_text = findViewById(R.id.other_text);

        back = findViewById(R.id.imageViewBack);
        context = this;
        userInfo=TempStorage.getUser();

        recyclerView = findViewById(R.id.rvNumbers);
        adapter = new MyRecyclerViewAdapter(this);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug(e.getMessage());
        }

        back.setOnClickListener(v -> onBackPressed());
        textViewAddImages.setOnClickListener(v->{
            proceedAfterPermission();
        });



        control_profile.setOnClickListener(v -> {
            Intent i = new Intent(context,EditProfileNewActivity.class);
            startActivity(i);


        });
        if(userInfo.getInterested().equalsIgnoreCase("Male")){
            setUpGeneder(man_text,man,women_text,woman,everyone_text,everyone,other_text,other);

        }else if(userInfo.getInterested().equalsIgnoreCase("Female")){
            setUpGeneder(women_text,woman,man_text,man,everyone_text,everyone,other_text,other);

        }
        else if(userInfo.getInterested().equalsIgnoreCase("Other")){
            setUpGeneder(other_text,other,women_text,woman,man_text,man,everyone_text,everyone);

        }
        else{
            setUpGeneder(everyone_text,everyone,women_text,woman,man_text,man,other_text,other);
        }

        woman.setOnClickListener(v -> {
            userInfo.setInterested("Female");
            setUpGeneder(women_text,woman,man_text,man,everyone_text,everyone,other_text,other);
            updateProfile();

        });

        man.setOnClickListener(v -> {
            userInfo.setInterested("Male");
            setUpGeneder(man_text,man,women_text,woman,everyone_text,everyone,other_text,other);
            updateProfile();


        });
        everyone.setOnClickListener(v -> {
            userInfo.setInterested("All");
            setUpGeneder(everyone_text,everyone,women_text,woman,man_text,man,other_text,other);
            updateProfile();


        });
        other.setOnClickListener(v -> {
            userInfo.setInterested("Other");
            setUpGeneder(other_text,other,everyone_text,everyone,women_text,woman,man_text,man);
            updateProfile();


        });
        logout.setOnClickListener(v -> {
            TempStorage.logoutUser(mContext);
            Intent i = new Intent(mContext, Login.class);
            startActivity(i);
            finish();
        });


//        imageView1.setOnClickListener(v -> {
//            imageView = imageView1;
//            proceedAfterPermission();
//
//        });
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView = imageView2;
//                proceedAfterPermission();
//
//            }
//        });
//
//        imageView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView = imageView3;
//                proceedAfterPermission();
//
//            }
//        });
//
//        imageView4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView = imageView4;
//                proceedAfterPermission();
//
//            }
//        });
//
//        imageView5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView = imageView5;
//                proceedAfterPermission();
//
//            }
//        });
//
//        imageView6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView = imageView6;
//                proceedAfterPermission();
//
//            }
//        });

        setUpImages();
    }

    private void setUpGeneder(TextView selectedText,Button selectedButton,TextView otherText,Button otherButton,TextView EveryOneText,Button everyButton,TextView otherTXT,Button otherBTN){

        selectedText.setTextColor(getResources().getColor(R.color.colorAccent));
        selectedButton.setBackgroundResource(R.drawable.ic_check_select);
        otherText.setTextColor(getResources().getColor(R.color.black));
        otherButton.setBackgroundResource(R.drawable.ic_check_unselect);
        EveryOneText.setTextColor(getResources().getColor(R.color.black));
        everyButton.setBackgroundResource(R.drawable.ic_check_unselect);
        otherTXT.setTextColor(getResources().getColor(R.color.black));
        otherBTN.setBackgroundResource(R.drawable.ic_check_unselect);
    }


    private void setUpImages(){
//        imageView1.setTag(null);
//        imageView2.setTag(null);
//        imageView3.setTag(null);
//        imageView4.setTag(null);
//        imageView5.setTag(null);
//        imageView6.setTag(null);


        if(userInfo!=null&&userInfo.getMedia()!=null&&userInfo.getMedia().size()>0){
            adapter.clearImages();
            adapter.addImages(userInfo.getMedia());
            adapter.notifyDataSetChanged();
//           for(int i=0;i<6;i++){
//               String url=null;
//               int tagId=-1;
//               try{
//                   if(userInfo.getMedia().get(i)!=null){
//                       url = "http://"+userInfo.getMedia().get(i).getUrl();
//                       tagId = userInfo.getMedia().get(i).getId();
//                   }
//               }catch (Exception e){
//
//               }
//
//
//               switch (i){
//                   case 0:
//
//                       setUpImage(url,tagId,imageView1);
//
//
//                       break;
//                   case 1:
//                       setUpImage(url,tagId,imageView2);
//
//
//                       break;
//                   case 2:
//                       setUpImage(url,tagId,imageView3);
//
//
//
//                       break;
//                   case 3:
//                       setUpImage(url,tagId,imageView4);
//
//
//                       break;
//                   case 4:
//                       setUpImage(url,tagId,imageView5);
//
//
//                       break;
//                   case 5:
//                       setUpImage(url,tagId,imageView6);
//                       break;
//               }
//           }
        }
    }

//    private void setUpImage(String url,int tag,ImageView imageView){
//        if(url!=null){
//            Glide.with(context).load(url).into(imageView);
//            imageView6.setTag(tag);
//        }
//
//    }


    private void requestMultiplePermissions() {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {

//        if(imageView.getTag()!=null){
//            int id = (int) imageView.getTag();
//
//            CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel","Delete Image"};
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
//
//            builder.setTitle("Add Photo!");
//
//            builder.setItems(options, new DialogInterface.OnClickListener() {
//
//                @Override
//
//                public void onClick(DialogInterface dialog, int item) {
//
//                    if (options[item].equals("Take Photo"))
//
//                    {
//
//                        cameraIntent();
//
//                    } else if (options[item].equals("Choose from Gallery"))
//
//                    {
//
//                        galleryIntent();
//
//
//                    } else if (options[item].equals("Cancel")) {
//
//                        dialog.dismiss();
//
//                    }
//                    else if (options[item].equals("Delete Image")) {
//
//                        dialog.dismiss();
//                        deleteImage(id);
//
//                    }
//
//                }
//
//            });
//
//            builder.show();
//        }
//        else{
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

            builder.setTitle("Add Photo!");

            builder.setItems(options, (dialog, item) -> {

                if (options[item].equals("Take Photo"))

                {

                    cameraIntent();

                } else if (options[item].equals("Choose from Gallery"))

                {

                    galleryIntent();


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            });

            builder.show();
    //}






    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //imageView.setImageBitmap(thumbnail);
        try {
            uploadImage(getFile(thumbnail));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       // imageView.setImageBitmap(bm);
        try {
            uploadImage(getFile(bm));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(Bitmap bitmap) throws IOException {
        File f = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
        f.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

      return f;
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void uploadImage(File file){
//        if(imageView.getTag()!=null){
//            int id = (int) imageView.getTag();
//            deleteImage(id);
//        }
        try {
            file = new Compressor.Builder(this)
                    .setMaxWidth(1280)
                    .setMaxHeight(1280)
                    .setQuality(90)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(context.getCacheDir().getPath())
                    .build()
                    .compressToFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                LogUtils.debug("Progress:- "+percentage);

            }

            @Override
            public void onError() {
            }

            @Override
            public void onFinish() {
            }
        });

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Call<ResponseModel<ImageModel>> call = RestServiceFactory.createService().uploadImage(body);
        call.enqueue(new Callback<ResponseModel<ImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Response<ResponseModel<ImageModel>> response) {
                Log.d("Response: " , response.message());
                 getUser();


            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Throwable t) {
                Log.d("Exception: ",t.getMessage());
            }
        });
    }

    private void deleteImage(int imageId){
        Call<ResponseModel<ImageModel>> responseModelCall = RestServiceFactory.createService().deleteImage(imageId);
        responseModelCall.enqueue(new RestCallBack<ResponseModel<ImageModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<ImageModel>> call, String message) {
                ToastUtils.show(mContext, message);

            }

            @Override
            public void onResponse(Call<ResponseModel<ImageModel>> call, Response<ResponseModel<ImageModel>> restResponse, ResponseModel<ImageModel> response) {
                if (RestCallBack.isSuccessFull(response)) {
                   getUser();

                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }


    private void getUser(){

        Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().getUserDetails();
        responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
            @Override
            public void onFailure(Call<ResponseModel<UserData>> call, String message) {
                ToastUtils.show(mContext, message);

            }

            @Override
            public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    TempStorage.setUserData(response.data);
                    TempStorage.userData = response.data;
                    userInfo = TempStorage.getUser();
                    setUpImages();

                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }

    private void updateProfile(){
        EditProfileUpdateRequest updateRequest = new EditProfileUpdateRequest(userInfo.getEmail(), userInfo.getFullName(),"USER",userInfo.getGender(),userInfo.getDob(),userInfo.getInterested(),
                userInfo.getMinRange(),userInfo.getMaxRange(), userInfo.getDistance(),userInfo.getMobile() );
        Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().updateUser(updateRequest);
        responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
            @Override
            public void onFailure(Call<ResponseModel<UserData>> call, String message) {
                ToastUtils.show(mContext, message);
                // hobbiesContinueButton.setText("Register");

            }

            @Override
            public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
                //hobbiesContinueButton.setText("Register");
                if (RestCallBack.isSuccessFull(response)) {
                    TempStorage.setUserData(response.data);
                    TempStorage.userData = response.data;
                    ToastUtils.show(mContext, "Profile updated successfully");

                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, ImageModel model) {


        switch (view.getId()){
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you really want to remove this picture?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    deleteImage(model.getId());

                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                builder.show();

                break;
        }

    }
}


