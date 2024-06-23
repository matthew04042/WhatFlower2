package com.example.whatflower.ui;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.whatflower.MainActivity;
import com.example.whatflower.R;
import com.example.whatflower.ml.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Objects;

public class HomeFragment extends Fragment {
    Button uploadBtn, captureBtn;
    ImageView imageView;
    TextView predictionTextView;
    String [] labels = new String[1001];
    Bitmap bitmap;

    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the  layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadBtn = view.findViewById(R.id.uploadButton);
        captureBtn = view.findViewById(R.id.captureButton);
        imageView = view.findViewById(R.id.imageView);
        predictionTextView = view.findViewById(R.id.predictionTextView);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requireActivity().getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            int cnt = 0;
            while (line != null) {
                labels[cnt] = line;
                cnt++;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        uploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });
        captureBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 12);
        });
    }
    @SuppressLint("SetTextI18n")
    private void predict(Bitmap bitmap){
        try {
            MainActivity main  = (MainActivity) getActivity();
            Intent intent1 = main.getIntent();
            String username = intent1.getStringExtra("username");
            String email = intent1.getStringExtra("email");
            String name = intent1.getStringExtra("name");
            System.out.println(username);
            Model model = Model.newInstance(HomeFragment.this.requireContext());
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            bitmap = Bitmap.createScaledBitmap(bitmap, 224,224,true);
            inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());


            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] arr = outputFeature0.getFloatArray();
            DecimalFormat df = new DecimalFormat("0.000000");
            for (int i = 0; i<arr.length ; i++){
                System.out.println(labels[i]);
                System.out.println(df.format(arr[i]));
            }
            String result = labels[getMax(outputFeature0.getFloatArray())].replaceAll("\\d", "");
            new Thread(() -> {
                StringBuilder result1 = new StringBuilder();
                try {
                    URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts|pageimages&exintro=''&explaintext=''&indexpageids=''&redirects=1&pithumbsize=500&titles=" + result.replaceAll(" ", "%20"));
                    URLConnection connection = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        JSONObject jsonObject = new JSONObject(inputLine);
                        System.out.println(jsonObject);
                        String pageId = jsonObject.getJSONObject("query").getJSONArray("pageids").getString(0);
                        System.out.println(pageId);
                        String extract = jsonObject.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getString("extract");
                        System.out.println(extract);
                        result1.append(extract).append("\n");
                        System.out.println(result1);
                    }
                    if (username != null) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("my_flowers");
                        JSONObject jsonObject = new JSONObject();
                        JSONArray array = new JSONArray();
                        JSONObject flower = new JSONObject();
                        flower.put("flowerName", result);
                        flower.put("flowerDescription", result1.toString());
                        array.put(flower);
                        jsonObject.put("name", name);
                        jsonObject.put("email", email);
                        jsonObject.put("username", username);
                        jsonObject.put("flowers", array);
                        reference.child(username).setValue(jsonObject.toString());
                    }
                    in.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                requireActivity().runOnUiThread(() -> predictionTextView.setText(labels[getMax(outputFeature0.getFloatArray())].replaceAll("\\d", "")+ "\n \n"+result1.toString()));
            }).start();
//            predictionTextView.setText(result);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            System.out.println(e);
        }
    }
    int getMax(float[] arr){
        int max=0;
        for(int i=0; i<arr.length; i++){
            if(arr[i]>arr[max]) max=i;
        }
        return max;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri uri = Objects.requireNonNull(data).getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                predict(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 12 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(bitmap);
            predict(bitmap);
        }
    }

}
