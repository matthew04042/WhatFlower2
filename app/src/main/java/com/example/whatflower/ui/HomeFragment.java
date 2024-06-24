package com.example.whatflower.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.config.ToastUtils;
import com.example.whatflower.ui.picture.PictureBean;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase;
    private AppData appData;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imageView;
    private Uri photoUri;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.ADD_FRIENDS);
        appData = AppData.getInstance();

        view.findViewById(R.id.iv_home_camera).setOnClickListener( v -> {
            openCamera(REQUEST_IMAGE_CAPTURE);
        });
        view.findViewById(R.id.iv_home_gallery).setOnClickListener( v -> {
            openGallery();
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
//                photoUri = FileProvider.getUriForFile(requireContext(),
//                        "com.ak.plant.fileprovider",
//                        photoFile);
                photoUri = FileProvider.getUriForFile(requireContext(),
                        "com.ak.plant.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    String imgPath = "";
    private void openCamera(int type) {
        File imgDir = new File(getFilePath(null, getActivity()));
        String photoName = System.currentTimeMillis() + ".png";
        File picture = new File(imgDir, photoName);
        if (!picture.exists()) {
            try {
                picture.createNewFile();
            } catch (IOException e) {
                Log.e("TAG", "choosePictureTypeDialog: Failed on created File", e);
            }
        }
        imgPath = picture.getAbsolutePath();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), "com.example.whatflower.provider", picture));
        startActivityForResult(camera, type);
    }


    public String getFilePath(String dir, Context context) {
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = context.getExternalFilesDir(dir).getAbsolutePath();
        } else {
            path = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }



    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(null);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    private void showResultDialog(Uri selectedImage){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_home_result, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        ImageView resultImage = dialogView.findViewById(R.id.dialog_home_image);
        Button dialogButton = dialogView.findViewById(R.id.dialog_button_ok);
        Button dialogButtonCancel = dialogView.findViewById(R.id.dialog_button_cancel);

        if (selectedImage != null){
            resultImage.setImageURI(selectedImage);
        }else {
            Glide.with(getActivity())
                    .load(imgPath)
                    .placeholder(R.drawable.ic_bg2)
                    .error(R.drawable.ic_bg2)
                    .into(resultImage);
        }


        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appData.getLogin()){
                    uploadImage(selectedImage);
                }
                alertDialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener( v -> {
            alertDialog.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Log.i("TAG", "imgPath REQUEST_IMAGE_CAPTURE");
                Log.i("TAG imgPath", "imgPath REQUEST_IMAGE_CAPTURE"+ imgPath);
                Log.i("TAG photoUri", "imgPath REQUEST_IMAGE_CAPTURE"+ photoUri);

                showResultDialog(photoUri);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Log.i("TAG", "imgPath REQUEST_IMAGE_PICK");
                Uri selectedImage = data.getData();
                showResultDialog(selectedImage);
            }
        }
    }



    public void uploadImage(Uri fileUri) {
        String imagePath = getPathFromUri(fileUri, getActivity());
        Uri file = Uri.fromFile(new File(imagePath));



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putFile(file);

        uploadTask.addOnFailureListener(exception -> {
            Log.e("Upload", "Upload failed", exception);
            Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                saveImageUrlToDatabase(downloadUrl);
                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                Log.e("Upload", "Failed to get download URL", exception);
                Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void saveImageUrlToDatabase(String downloadUrl) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("images").push();
        myRef.setValue(downloadUrl).addOnSuccessListener(aVoid -> {
            Toast.makeText(getActivity(), "URL saved to database", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("Database", "Failed to save URL", e);
            Toast.makeText(getActivity(), "Failed to save URL", Toast.LENGTH_SHORT).show();
        });
    }


    private String getPathFromUri(Uri uri, Context context){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }



    private String getFileExtension(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = contentResolver.getType(uri);
        if (type == null) {
            String path = uri.getPath();
            if (path != null) {
                type = mime.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path));
            }
        }
        return mime.getExtensionFromMimeType(type);
    }

    private void saveData(String imgUrl, String result, String similarity, String wjImg){
        PictureBean pictureBean = new PictureBean(imgUrl,result,similarity,wjImg);
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("pictures").child(appData.getUserBean().getAccount());
        conversationsRef.push().setValue(pictureBean)
                .addOnSuccessListener(aVoid -> Log.d("ChatHelper", "Message successfully sent!"))
                .addOnFailureListener(e -> Log.w("ChatHelper", "Error sending message", e));

        ToastUtils.showToast(getActivity(),"识别成功");
    }

}