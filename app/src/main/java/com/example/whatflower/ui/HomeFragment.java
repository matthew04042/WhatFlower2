package com.example.whatflower.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.whatflower.ml.Model;
import com.example.whatflower.ui.picture.PictureBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

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

    private Bitmap bitmap;

    private String labels[] = new String[1001];
    private String imagePath;
    private FirebaseAuth mAuth;

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
        ImageView captureButton, uploadButton;
        super.onViewCreated(view, savedInstanceState);
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requireActivity().getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            int cnt = 0;
            while (line != null){
                labels[cnt] = line;
                cnt++;
                line = bufferedReader.readLine();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.ADD_FRIENDS);
        appData = AppData.getInstance();
        captureButton = view.findViewById(R.id.iv_home_camera);
        uploadButton = view.findViewById(R.id.iv_home_gallery);

        captureButton.setOnClickListener((View v) ->{
            File imgDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String photoName  = System.currentTimeMillis() + ".jpg";
            File picture = new File(imgDir, photoName);
            if(!picture.exists()){
                try{
                    picture.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            imagePath = picture.getAbsolutePath();
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", picture);
            camera.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
        });
        uploadButton.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });
    }

    private void showResultDialog(Uri selectedImage, int requestCode){
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
                    .load(imagePath)
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
                    System.out.println("User is logged in");
                    uploadImage(selectedImage, requestCode);
                }
                alertDialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener( v -> {
            alertDialog.dismiss();
        });
    }
    public  void uploadImage(Uri fileUri, int requestCode){
        Log.i("TAG", "uploadImage: " + requestCode);
        float similarity = 0;
        try {
            System.out.println("File Uri: " + fileUri);
            Model model = Model.newInstance(HomeFragment.this.requireContext());
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] output = outputFeature0.getFloatArray();
            DecimalFormat df = new DecimalFormat("0.000000");
            for(int i=0; i<output.length; i++){
                System.out.println("Output: " + labels[i]);
                System.out.println("Output: " + df.format(output[i]));
            }
            float max = getMax(outputFeature0.getFloatArray());
            float sim = output[getMax(outputFeature0.getFloatArray())];
            if(sim-100 > 0){
                similarity = sim-100;
            }else {
                similarity = sim/100;
            }
            String flowerName = labels[(int) max].replaceAll("\\d", "");
            System.out.println("Flower Name: " + flowerName);
            String details = getDetails(flowerName);
            System.out.println("Details: " + details);
            String imgPath = imagePath;
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                imgPath = imagePath;
            }else if(requestCode == REQUEST_IMAGE_PICK) {
                imgPath = getPathFromUri(fileUri, getActivity());
            }
            Uri file = Uri.fromFile(new File(imgPath));
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + file.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(file);
            String wjUrl = "https://en.wikipedia.org/wiki/" + flowerName.replaceAll(" ", "%20");
            float finalSimilarity = similarity;
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Log.i("TAG", "uploadImage: " + exception.getMessage());
                Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.i("TAG", "uploadImage: " + uri);
                    String downloadUrl = uri.toString();
                    Log.i("TAG", "uploadImage: " + downloadUrl);
                    saveImageUrlToDatabase(downloadUrl);
                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                    saveData(downloadUrl, flowerName, String.valueOf(finalSimilarity), wjUrl, details);
                }).addOnFailureListener(exception -> {
                    Log.i("TAG", "Failed to ge download URL: " + exception.getMessage());
                    Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                });
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    int getMax(float[] arr){
        int max=0;
        for(int i=0; i<arr.length; i++){
            if(arr[i]>arr[max]) max=i;
        }
        return max;
    }
    public  String getDetails(String flowerName){
        StringBuilder Details = new StringBuilder();
        try{
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts|pageimages&exintro=''&explaintext=''&indexpageids=''&redirects=1&pithumbsize=500&titles=" + flowerName.replaceAll(" ", "%20"));
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                JSONObject jsonObject = new JSONObject(inputLine);
                String pageId = jsonObject.getJSONObject("query").getJSONArray("pageids").getString(0);
                String extract = jsonObject.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getString("extract");
                Details.append(extract).append("\n");
                System.out.println(Details);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Details.toString();
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

    private void saveData(String imgUrl, String result, String similarity, String wjImg, String Detail){
        PictureBean pictureBean = new PictureBean(imgUrl,result,similarity,wjImg, Detail);
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("pictures").child(appData.getUserBean().getAccount());
        conversationsRef.push().setValue(pictureBean)
                .addOnSuccessListener(aVoid -> Log.d("ChatHelper", "Message successfully sent!"))
                .addOnFailureListener(e -> Log.w("ChatHelper", "Error sending message", e));

        ToastUtils.showToast(getActivity(),"Predict success!");
    }
    private String getPathFromUri(Uri uri, Context context){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        System.out.println("Cursor: " + cursor);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
        Log.i("TAG", "onActivityResult: " + imagePath);
        Log.i("TAG", "onActivityResult: " + photoUri);
        showResultDialog(photoUri, requestCode);
       } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
           Log.i("TAG", "imgPath REQUEST_IMAGE_PICK");
           Uri selectedImage = data.getData();
           showResultDialog(selectedImage, requestCode);
       }
    }

}