package hr.tvz.quiz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    // Identifier for the camera and external storage permission request
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    int CameraPermission, StoragePermission;

    private APIClient client = APIClient.getInstance();

    private UserLocalStore userLocalStore;
    private User user;

    private EditText EmailEditText;
    private Button EditProfileButton, TakePictureButton, ChoosePictureButton;

    private ImageView imgPreview;

    String filePath;
    private File file;
    private Uri file_uri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        EmailEditText = (EditText) findViewById(R.id.EmailEditTextEditProfile);
        TakePictureButton = (Button) findViewById(R.id.TakePictureButton);
        ChoosePictureButton = (Button) findViewById(R.id.ChoosePictureButton);
        EditProfileButton = (Button) findViewById(R.id.EditProfileButton);
        imgPreview = (ImageView) findViewById(R.id.ProfilePhotoImageViewEditProfile);

        if (! user.getImage().equals("")) {
            Glide.with(this).load(APIClient.getURL() + "resources/" + user.getId() + "/" + user.getImage()).into(imgPreview);
        }

        EmailEditText.setText(user.getEmail());

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            CameraPermission = 0;
            StoragePermission = 0;
        } else {
            CameraPermission = 1;
            StoragePermission = 1;
        }

        TakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionForCamera();
                getPermissionForStorage();

                if (CameraPermission == 1 && StoragePermission == 1) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file_uri = Uri.fromFile(getOutputMediaFile());
                    i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                    startActivityForResult(i, 10);
                }
            }
        });

        ChoosePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionForStorage();

                if (StoragePermission == 1) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                }
            }
        });


        EditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.exists()) {
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

                    Call<User> call = client.getApiService().updateUserPhoto(user.getId(), image);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            int statusCode = response.code();
                            System.out.println("statusCode " + statusCode);
                            System.out.println("response " + response);
                            if (statusCode == 200) {
                                User user = response.body();
                                userLocalStore.storeUserData(user);
                                System.out.println("Uspjeh.");
                                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                System.out.println("Nije 200.");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Image Upload Error", t.toString());
                        }
                    });
                } else {
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Called when the user wants to take a picture
    public void getPermissionForCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show our own UI to explain to the user why we need to get the Camera permission
                // before actually requesting the permission and showing the default UI

                showMessageOKCancel("You need to allow access to the camera if you want to take a picture.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Fire off an async request to actually get the permission
                                // This will show the standard permission request dialog UI
                                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA);
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        } else {
            CameraPermission = 1;
        }
    }

    // Called when the user wants to get a picture from storage
    public void getPermissionForStorage(){

        //checking for the storage permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to get the Camera permission
                // before actually requesting the permission and showing the default UI

                showMessageOKCancel("You need to allow access to the external storage to get the picture from your phone.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Fire off an async request to actually get the permission
                                // This will show the standard permission request dialog UI
                                ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            StoragePermission = 1;
        }

    }


    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original REQUEST_CAMERA request
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraPermission = 1;
                Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                CameraPermission = 0;
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


        // Make sure it's our original REQUEST_WRITE_EXTERNAL_STORAGE request
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StoragePermission = 1;
                Toast.makeText(this, "External storage permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                StoragePermission = 0;
                Toast.makeText(this, "External storage permission denied.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("I understand", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void previewMedia() {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger images
        //options.inSampleSize = 8;
        options.inPurgeable = true;

        filePath = file_uri.getPath();

        bitmap = BitmapFactory.decodeFile(filePath, options);

        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageBitmap(bitmap);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            CropImage.activity(file_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //.setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
            previewMedia();
        }

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            file_uri = data.getData();
            CropImage.activity(file_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //.setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                file_uri = result.getUri();
                filePath = file_uri.getPath();
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    options.inPurgeable = true;
                    AssetFileDescriptor fileDescriptor = null;
                    try {
                        fileDescriptor = this.getContentResolver().openAssetFileDescriptor(file_uri, "r");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                            fileDescriptor.close();
                            imgPreview.setVisibility(View.VISIBLE);
                            imgPreview.setImageBitmap(bitmap);

                            file = new File(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                System.out.println(e.getMessage());
            }
        }
    }

    private File getOutputMediaFile() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String image = "IMG_" + timeStamp + ".jpg";

        // External sdcard location - create a media file name
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image);

        return file;
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        super.onBackPressed();
    }
}
