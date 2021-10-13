package com.some.studychats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.Nullable;
public class MachinLearnigTextRecognoge extends AppCompatActivity {
    Button getImage_btn;
    Button getText_btn;
    ImageView showImage_img;
    TextView showText_txt;
    static final int REQUEST_IMAGE_CAMERA=1;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machin_learnig_text_recognize);
        //Bind your XML view here
        getImage_btn=findViewById(R.id.btn_takePic);
       getText_btn=findViewById(R.id.btn_gettext);
       showText_txt=findViewById(R.id.txt_show_text);
       showImage_img=findViewById(R.id.img_imageview);
       //Set OnClick event for getImage_btn Button to take image from camera
        getImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeImage(); //Function To capture image
            }
        });
        //Set OnClick event for getText_btn Button to get Text from image

        getText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //       GetTextFromImageFunction();
            }
        });

    }



    private void TakeImage() {
        Intent takeImageIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeImageIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takeImageIntent,REQUEST_IMAGE_CAMERA);
        }
    }

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==REQUEST_IMAGE_CAMERA && resultCode==RESULT_OK && data!=null)
        {
            //Crop image
            CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){//Here change reultCode to requestCode
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK )//Please Change requestCode to resultCode
            {
                Uri resultURI=result.getUri();

                showImage_img.setImageURI(resultURI);//IT show image to image view
                bitmapDrawable=(BitmapDrawable)showImage_img.getDrawable();
                bitmap=bitmapDrawable.getBitmap();
            }
            else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e=result.getError();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void GetTextFromImageFunction() {
        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational())
        {
            Toast.makeText(this, "Error occur", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame=new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray=textRecognizer.detect(frame);
            StringBuilder stringBuilder=new StringBuilder();
            for (int i=0; i<textBlockSparseArray.size(); i++)
            {
                TextBlock textBlock=textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            //Show the text to TextView
            showText_txt.setText(stringBuilder.toString());
            //Thats All
        }
    }

     */
}
