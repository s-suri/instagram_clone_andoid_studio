package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompitionExamDataUpload extends AppCompatActivity {


 //   University Name = Toppers Name;

    String names[] = {};
    String courseName[] = {"B-TECH","BE COURSE","BSC COURSE","BSC(HONC)","MSE","ME","M-TECH","BCOM","BA","MBA",
            "BA COURSE","BA(HONC)","BBA","BCOM","BA LLB","BBA LLB ","BCOM LLB","BCA"};
    String universityName[] = {"ICFAI UNIVERSITY JAIPUR"};
    String semesterName[] = {"FIRST SEMESTER","SECOND SEMESTER","THIRD SEMESTER","FORTH SEMESTER","FIFTH SEMESTER"
            ,"SIXTH SEMESTER","SEVENTH SEMESTER","EIGHT SEMESTER"};
    String branchName[] = {"Computer Science and Engineering","CSE","Electronics and Communication Engineering","ECE","Electrical and Electronics Engineering","EEE","Mechanical Engineering","ME",
            "Information Technology Engineering","ITE","Civil Engineering","CE","Chemical Engineering","CE","Aeronautical Engineering","AE"
            ,"Agriculture Engineering","AE","Mining Engineering","ME","Biochemical Engineering","BE","Bsc IT","Bsc Computer science","bSC CS"
            ,"Bsc Nursing","General","Economics","Political Science","Sociology","Data Science","Internet of Things","Artificial Intelligence"
            ,"Chemistry","Forensic Sciences","Mathematics","Physics","General","Family Business and Enterpreneurship",
    };
    String testName[] = {"First TEST","SECOND TEST","THIRD TEST","FOURTH TEST","FIFTH TEST"
            ,"SIXTH TEST"};
    String subjectName[] = {};




    private static final int CHOOSE_IMAGE = 1;
    private Button  btnUploadImage;
    private TextView viewGallery,chooseImage;
    private ImageView imgPreview;
    private EditText imgUpi_id,upi_holder_name,amount;

    AutoCompleteTextView imgUniversityName,imgCourseName,imgSemesterName,imgBranchName,imgTestName,imgSubjectName;
    private ProgressBar uploadProgress;

    private String  cheker = "", myUrl = "", saveCurrentTime, saveCurrentDate;
    private Uri fileUri;
    private StorageTask uploadTask;


    FirebaseUser fuser;
    DatabaseReference RootRef;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compition_exam_data_upload);

        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }



        fuser = FirebaseAuth.getInstance().getCurrentUser();

        uploadProgress = findViewById(R.id.uploadProgress);
        chooseImage = findViewById(R.id.chooseImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);



        viewGallery = findViewById(R.id.viewGallery);


        imgUpi_id = findViewById(R.id.imgPayment_id);
        imgUniversityName = findViewById(R.id.imgUniversityName);
        imgCourseName = findViewById(R.id.imgCourseName);
        imgBranchName = findViewById(R.id.imgBranch);
        imgTestName = findViewById(R.id.imgTest);
        imgSubjectName =findViewById(R.id.imgSubject);
        imgSemesterName = findViewById(R.id.imgSemesterName);
        upi_holder_name = findViewById(R.id.imgUpiHoldername);
        amount = findViewById(R.id.amount);


        imgUniversityName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, universityName));
        imgUniversityName.setThreshold(1);


        imgCourseName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseName));
        imgCourseName.setThreshold(1);

        imgBranchName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, branchName));
        imgBranchName.setThreshold(1);

        imgTestName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, testName));
        imgTestName.setThreshold(1);


        imgSemesterName.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, semesterName));
        imgSemesterName.setThreshold(1);



        imgPreview = findViewById(R.id.imgPreview);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompitionExamDataUpload.this, MainActivityInstagram.class);
                startActivity(intent);
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String web_payment_id = imgUpi_id.getText().toString();
                String web_University = imgUniversityName.getText().toString();
                String web_Course = imgCourseName.getText().toString();
                String web_branch = imgBranchName.getText().toString();
                String web_test = imgTestName.getText().toString();
                String web_subject = imgSubjectName.getText().toString();
                String web_semester = imgSemesterName.getText().toString();
                String web_upiHolderName = upi_holder_name.getText().toString();
                String web_amount = amount.getText().toString();


                if (TextUtils.isEmpty(web_upiHolderName) || TextUtils.isEmpty(web_payment_id) || TextUtils.isEmpty(web_University) || TextUtils.isEmpty(web_Course) || TextUtils.isEmpty(web_branch)
                        || TextUtils.isEmpty(web_test) || TextUtils.isEmpty(web_subject) || TextUtils.isEmpty(web_semester)  || TextUtils.isEmpty(web_amount)){
                    Toast.makeText(CompitionExamDataUpload.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPdf(web_payment_id,web_University,web_Course,web_branch,web_test,web_subject,web_semester,web_upiHolderName,web_amount);

                }

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(CompitionExamDataUpload.this, "Upload in progress", Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheker = "PDF";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent.createChooser(intent, "Select PDF"), 438);


            }
        });
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            Picasso.get().load(R.drawable.pdf_portal).into(imgPreview);




        }
    }

    private  void uploadPdf(String payment_id,String university,String course,String branch,String testName,String subject,String semester,String holderName,String rapay) {

        if (fileUri != null) {

            if (!cheker.equals("image")) {


                RootRef = FirebaseDatabase.getInstance().getReference().child("Pdfs").child(university);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");



                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {


                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd yyyy");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            HashMap<String, Object> groupMessageKey = new HashMap<>();
                            RootRef.updateChildren(groupMessageKey);


                            Map messageTextBody = new HashMap();



                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("url", myUrl);
                            messageTextBody.put("paymentId",payment_id);
                            messageTextBody.put("search",course.toLowerCase()+" " +semester.toLowerCase()+" " +branch.toLowerCase()+" "+subject.toLowerCase());
                            messageTextBody.put("title",course+" "+semester+" "+branch+" "+subject+" "+subject+" "+testName);
                            messageTextBody.put("universityName",university);
                            messageTextBody.put("courseName",course);
                            messageTextBody.put("branchName",branch);
                            messageTextBody.put("semesterName",semester);
                            messageTextBody.put("subjectName",subject);
                            messageTextBody.put("testName",testName);
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);
                            messageTextBody.put("upiHolderName",holderName);
                            messageTextBody.put("type", "web_pdf");
                            messageTextBody.put("location","pdf");
                            messageTextBody.put("amount", rapay);




                            RootRef.child("suri").child(messagePushID).updateChildren(messageTextBody)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {


                                            if (task.isSuccessful()){

                                                final String messagePushID1 = RootRef.push().getKey();


                                                Map messageTextBody1 = new HashMap();

                                                messageTextBody1.put("message", myUrl);
                                                messageTextBody1.put("url", myUrl);
                                                messageTextBody1.put("paymentId",payment_id);
                                                messageTextBody1.put("search",course.toLowerCase()+" "+subject.toLowerCase()+" " +semester.toLowerCase()+" " +branch.toLowerCase());
                                                messageTextBody1.put("title", course+" "+subject+" " +semester+" " +branch+" "+testName);
                                                messageTextBody1.put("universityName",university);
                                                messageTextBody1.put("courseName",course);
                                                messageTextBody1.put("semesterName",semester);
                                                messageTextBody1.put("subjectName",subject);
                                                messageTextBody1.put("branchName",branch);
                                                messageTextBody1.put("testName",testName);
                                                messageTextBody1.put("sender", fuser.getUid());
                                                messageTextBody1.put("messageID", messagePushID1);
                                                messageTextBody1.put("time", saveCurrentTime);
                                                messageTextBody1.put("date", saveCurrentDate);
                                                messageTextBody1.put("upiHolderName",holderName);
                                                messageTextBody1.put("type", "web_pdf");
                                                messageTextBody1.put("location","pdf");
                                                messageTextBody1.put("amount", rapay);

                                                RootRef.child("suri").child(messagePushID1).updateChildren(messageTextBody1).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {


                                                        if (task.isSuccessful()){

                                                            final String messagePushID2 = RootRef.push().getKey();


                                                            Map messageTextBody2 = new HashMap();

                                                            messageTextBody2.put("message", myUrl);
                                                            messageTextBody2.put("url", myUrl);
                                                            messageTextBody2.put("paymentId",payment_id);
                                                            messageTextBody2.put("search",course.toLowerCase()+" " +branch.toLowerCase()+" "+subject.toLowerCase()+" " +semester.toLowerCase());
                                                            messageTextBody2.put("title", course+" " +branch+" "+subject+" " +semester+" "+testName);
                                                            messageTextBody2.put("universityName",university);
                                                            messageTextBody2.put("courseName",course);
                                                            messageTextBody2.put("semesterName",semester);
                                                            messageTextBody2.put("subjectName",subject);
                                                            messageTextBody2.put("branchName",branch);
                                                            messageTextBody2.put("testName",testName);
                                                            messageTextBody2.put("sender", fuser.getUid());
                                                            messageTextBody2.put("messageID", messagePushID2);
                                                            messageTextBody2.put("time", saveCurrentTime);
                                                            messageTextBody2.put("date", saveCurrentDate);
                                                            messageTextBody2.put("upiHolderName",holderName);
                                                            messageTextBody2.put("type", "web_pdf");
                                                            messageTextBody2.put("location","pdf");
                                                            messageTextBody2.put("amount", rapay);

                                                            RootRef.child("suri").child(messagePushID2).updateChildren(messageTextBody2).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {


                                                                    if (task.isSuccessful()){

                                                                        final String messagePushID3 = RootRef.push().getKey();


                                                                        Map messageTextBody3 = new HashMap();

                                                                        messageTextBody3.put("message", myUrl);
                                                                        messageTextBody3.put("url", myUrl);
                                                                        messageTextBody3.put("paymentId",payment_id);
                                                                        messageTextBody3.put("search",semester.toLowerCase()+" " +branch.toLowerCase()+" "+subject.toLowerCase()+" "+course.toLowerCase());
                                                                        messageTextBody3.put("title", semester+" " +branch+" "+subject+" "+course+" "+testName);
                                                                        messageTextBody3.put("universityName",university);
                                                                        messageTextBody3.put("courseName",course);
                                                                        messageTextBody3.put("branchName",branch);
                                                                        messageTextBody3.put("semesterName",semester);
                                                                        messageTextBody3.put("subjectName",subject);
                                                                        messageTextBody3.put("testName",testName);
                                                                        messageTextBody3.put("sender", fuser.getUid());
                                                                        messageTextBody3.put("messageID", messagePushID3);
                                                                        messageTextBody3.put("time", saveCurrentTime);
                                                                        messageTextBody3.put("date", saveCurrentDate);
                                                                        messageTextBody3.put("upiHolderName",holderName);
                                                                        messageTextBody3.put("type", "web_pdf");
                                                                        messageTextBody3.put("location","pdf");
                                                                        messageTextBody3.put("amount", rapay);


                                                                        RootRef.child("suri").child(messagePushID3).updateChildren(messageTextBody3).addOnCompleteListener(new OnCompleteListener() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task task) {


                                                                                if (task.isSuccessful()){

                                                                                    final String messagePushID4 = RootRef.push().getKey();


                                                                                    Map messageTextBody4 = new HashMap();

                                                                                    messageTextBody4.put("message", myUrl);
                                                                                    messageTextBody4.put("url", myUrl);
                                                                                    messageTextBody4.put("paymentId",payment_id);

                                                                                    messageTextBody4.put("search",semester.toLowerCase()+" "+course.toLowerCase()+" " +branch.toLowerCase()+" "+subject.toLowerCase());
                                                                                    messageTextBody4.put("title", semester+" "+course+" " +branch+" "+subject+" "+testName);
                                                                                    messageTextBody4.put("universityName",university);
                                                                                    messageTextBody4.put("semesterName",semester);
                                                                                    messageTextBody4.put("subjectName",subject);
                                                                                    messageTextBody4.put("courseName",course);
                                                                                    messageTextBody4.put("branchName",branch);
                                                                                    messageTextBody4.put("testName",testName);
                                                                                    messageTextBody4.put("sender", fuser.getUid());
                                                                                    messageTextBody4.put("messageID", messagePushID4);
                                                                                    messageTextBody4.put("time", saveCurrentTime);
                                                                                    messageTextBody4.put("date", saveCurrentDate);
                                                                                    messageTextBody4.put("upiHolderName",holderName);
                                                                                    messageTextBody4.put("type", "web_pdf");
                                                                                    messageTextBody4.put("location","pdf");
                                                                                    messageTextBody4.put("amount", rapay);

                                                                                    RootRef.child("suri").child(messagePushID4).updateChildren(messageTextBody4).addOnCompleteListener(new OnCompleteListener() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task task) {


                                                                                            if (task.isSuccessful()){

                                                                                                final String messagePushID5 = RootRef.push().getKey();


                                                                                                Map messageTextBody5 = new HashMap();

                                                                                                messageTextBody5.put("message", myUrl);
                                                                                                messageTextBody5.put("url", myUrl);
                                                                                                messageTextBody5.put("paymentId",payment_id);

                                                                                                messageTextBody5.put("search",semester.toLowerCase()+" "+subject.toLowerCase()+" "+course.toLowerCase()+" " +branch.toLowerCase());
                                                                                                messageTextBody5.put("title", semester+" "+subject+" "+course+" " +branch+" "+testName);
                                                                                                messageTextBody5.put("universityName",university);
                                                                                                messageTextBody5.put("semesterName",semester);
                                                                                                messageTextBody5.put("subjectName",subject);
                                                                                                messageTextBody5.put("courseName",course);
                                                                                                messageTextBody5.put("branchName",branch);
                                                                                                messageTextBody5.put("testName",testName);
                                                                                                messageTextBody5.put("sender", fuser.getUid());
                                                                                                messageTextBody5.put("messageID", messagePushID5);
                                                                                                messageTextBody5.put("time", saveCurrentTime);
                                                                                                messageTextBody5.put("date", saveCurrentDate);
                                                                                                messageTextBody5.put("upiHolderName",holderName);
                                                                                                messageTextBody5.put("type", "web_pdf");
                                                                                                messageTextBody5.put("location","pdf");
                                                                                                messageTextBody5.put("amount", rapay);

                                                                                                RootRef.child("suri").child(messagePushID5).updateChildren(messageTextBody5).addOnCompleteListener(new OnCompleteListener() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task task) {


                                                                                                        if (task.isSuccessful()){

                                                                                                            final String messagePushID6 = RootRef.push().getKey();


                                                                                                            Map messageTextBody6 = new HashMap();

                                                                                                            messageTextBody6.put("message", myUrl);
                                                                                                            messageTextBody6.put("url", myUrl);
                                                                                                            messageTextBody6.put("paymentId",payment_id);

                                                                                                            messageTextBody6.put("search",branch.toLowerCase()+" " +semester.toLowerCase()+" "+subject.toLowerCase()+" "+course.toLowerCase());
                                                                                                            messageTextBody6.put("title", branch+" " +semester+" "+subject+" "+course+" "+testName);
                                                                                                            messageTextBody6.put("universityName",university);
                                                                                                            messageTextBody6.put("semesterName",semester);
                                                                                                            messageTextBody6.put("subjectName",subject);
                                                                                                            messageTextBody6.put("courseName",course);
                                                                                                            messageTextBody6.put("branchName",branch);
                                                                                                            messageTextBody6.put("testName",testName);
                                                                                                            messageTextBody6.put("sender", fuser.getUid());
                                                                                                            messageTextBody6.put("messageID", messagePushID6);
                                                                                                            messageTextBody6.put("time", saveCurrentTime);
                                                                                                            messageTextBody6.put("date", saveCurrentDate);
                                                                                                            messageTextBody6.put("upiHolderName",holderName);
                                                                                                            messageTextBody6.put("type", "web_pdf");
                                                                                                            messageTextBody6.put("location","pdf");
                                                                                                            messageTextBody6.put("amount", rapay);

                                                                                                            RootRef.child("suri").child(messagePushID6).updateChildren(messageTextBody6).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task task) {


                                                                                                                    if (task.isSuccessful()){

                                                                                                                        final String messagePushID7 = RootRef.push().getKey();


                                                                                                                        Map messageTextBody7 = new HashMap();

                                                                                                                        messageTextBody7.put("message", myUrl);
                                                                                                                        messageTextBody7.put("url", myUrl);
                                                                                                                        messageTextBody7.put("paymentId",payment_id);

                                                                                                                        messageTextBody7.put("search",branch.toLowerCase()+" "+course.toLowerCase()+" " +semester.toLowerCase()+" "+subject.toLowerCase());
                                                                                                                        messageTextBody7.put("title", branch+" "+course+" " +semester+" "+subject+" "+testName);
                                                                                                                        messageTextBody7.put("universityName",university);
                                                                                                                        messageTextBody7.put("semesterName",semester);
                                                                                                                        messageTextBody7.put("subjectName",subject);
                                                                                                                        messageTextBody7.put("courseName",course);
                                                                                                                        messageTextBody7.put("branchName",branch);
                                                                                                                        messageTextBody7.put("testName",testName);
                                                                                                                        messageTextBody7.put("sender", fuser.getUid());
                                                                                                                        messageTextBody7.put("messageID", messagePushID7);
                                                                                                                        messageTextBody7.put("time", saveCurrentTime);
                                                                                                                        messageTextBody7.put("date", saveCurrentDate);
                                                                                                                        messageTextBody7.put("upiHolderName",holderName);
                                                                                                                        messageTextBody7.put("type", "web_pdf");
                                                                                                                        messageTextBody7.put("location","pdf");
                                                                                                                        messageTextBody7.put("amount", rapay);

                                                                                                                        RootRef.child("suri").child(messagePushID7).updateChildren(messageTextBody7).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task task) {


                                                                                                                                if (task.isSuccessful()){

                                                                                                                                    final String messagePushID8 = RootRef.push().getKey();


                                                                                                                                    Map messageTextBody8 = new HashMap();

                                                                                                                                    messageTextBody8.put("message", myUrl);
                                                                                                                                    messageTextBody8.put("url", myUrl);
                                                                                                                                    messageTextBody8.put("paymentId",payment_id);

                                                                                                                                    messageTextBody8.put("search",branch.toLowerCase()+" "+subject.toLowerCase()+" "+course.toLowerCase()+" " +semester.toLowerCase());
                                                                                                                                    messageTextBody8.put("title", branch+" "+subject+" "+course+" " +semester+" "+testName);
                                                                                                                                    messageTextBody8.put("universityName",university);
                                                                                                                                    messageTextBody8.put("semesterName",semester);
                                                                                                                                    messageTextBody8.put("subjectName",subject);
                                                                                                                                    messageTextBody8.put("courseName",course);
                                                                                                                                    messageTextBody8.put("branchName",branch);
                                                                                                                                    messageTextBody8.put("testName",testName);
                                                                                                                                    messageTextBody8.put("sender", fuser.getUid());
                                                                                                                                    messageTextBody8.put("messageID", messagePushID8);
                                                                                                                                    messageTextBody8.put("time", saveCurrentTime);
                                                                                                                                    messageTextBody8.put("date", saveCurrentDate);
                                                                                                                                    messageTextBody8.put("upiHolderName",holderName);
                                                                                                                                    messageTextBody8.put("type", "web_pdf");
                                                                                                                                    messageTextBody8.put("location","pdf");
                                                                                                                                    messageTextBody8.put("amount", rapay);

                                                                                                                                    RootRef.child("suri").child(messagePushID8).updateChildren(messageTextBody8).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task task) {


                                                                                                                                            if (task.isSuccessful()){

                                                                                                                                                final String messagePushID9 = RootRef.push().getKey();


                                                                                                                                                Map messageTextBody9 = new HashMap();

                                                                                                                                                messageTextBody9.put("message", myUrl);
                                                                                                                                                messageTextBody9.put("url", myUrl);
                                                                                                                                                messageTextBody9.put("paymentId",payment_id);

                                                                                                                                                messageTextBody9.put("search",subject.toLowerCase()+" "+course.toLowerCase()+" " +semester.toLowerCase()+" "+branch.toLowerCase());
                                                                                                                                                messageTextBody9.put("title", subject+" "+course+" " +semester+" "+branch+" "+testName);
                                                                                                                                                messageTextBody9.put("universityName",university);
                                                                                                                                                messageTextBody9.put("semesterName",semester);
                                                                                                                                                messageTextBody9.put("subjectName",subject);
                                                                                                                                                messageTextBody9.put("courseName",course);
                                                                                                                                                messageTextBody9.put("branchName",branch);
                                                                                                                                                messageTextBody9.put("testName",testName);
                                                                                                                                                messageTextBody9.put("sender", fuser.getUid());
                                                                                                                                                messageTextBody9.put("messageID", messagePushID9);
                                                                                                                                                messageTextBody9.put("time", saveCurrentTime);
                                                                                                                                                messageTextBody9.put("date", saveCurrentDate);
                                                                                                                                                messageTextBody9.put("upiHolderName",holderName);
                                                                                                                                                messageTextBody9.put("type", "web_pdf");
                                                                                                                                                messageTextBody9.put("location","pdf");
                                                                                                                                                messageTextBody9.put("amount", rapay);

                                                                                                                                                RootRef.child("suri").child(messagePushID9).updateChildren(messageTextBody9).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onComplete(@NonNull Task task) {


                                                                                                                                                        if (task.isSuccessful()){

                                                                                                                                                            final String messagePushID10 = RootRef.push().getKey();


                                                                                                                                                            Map messageTextBody10 = new HashMap();

                                                                                                                                                            messageTextBody10.put("message", myUrl);
                                                                                                                                                            messageTextBody10.put("url", myUrl);
                                                                                                                                                            messageTextBody10.put("paymentId",payment_id);
                                                                                                                                                            messageTextBody10.put("search",subject.toLowerCase()+" "+branch.toLowerCase()+" "+course.toLowerCase()+" " +semester.toLowerCase());
                                                                                                                                                            messageTextBody10.put("title", subject+" "+branch+" "+course+" " +semester+" "+testName);
                                                                                                                                                            messageTextBody10.put("universityName",university);
                                                                                                                                                            messageTextBody10.put("semesterName",semester);
                                                                                                                                                            messageTextBody10.put("subjectName",subject);
                                                                                                                                                            messageTextBody10.put("courseName",course);
                                                                                                                                                            messageTextBody10.put("branchName",branch);
                                                                                                                                                            messageTextBody10.put("testName",testName);
                                                                                                                                                            messageTextBody10.put("sender", fuser.getUid());
                                                                                                                                                            messageTextBody10.put("messageID", messagePushID10);
                                                                                                                                                            messageTextBody10.put("time", saveCurrentTime);
                                                                                                                                                            messageTextBody10.put("date", saveCurrentDate);
                                                                                                                                                            messageTextBody10.put("upiHolderName",holderName);
                                                                                                                                                            messageTextBody10.put("type", "web_pdf");
                                                                                                                                                            messageTextBody10.put("location","pdf");
                                                                                                                                                            messageTextBody10.put("amount", rapay);

                                                                                                                                                            RootRef.child("suri").child(messagePushID10).updateChildren(messageTextBody10).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                                                @Override
                                                                                                                                                                public void onComplete(@NonNull Task task) {


                                                                                                                                                                    if (task.isSuccessful()){

                                                                                                                                                                        final String messagePushID11 = RootRef.push().getKey();


                                                                                                                                                                        Map messageTextBody11 = new HashMap();

                                                                                                                                                                        messageTextBody11.put("message", myUrl);
                                                                                                                                                                        messageTextBody11.put("url", myUrl);
                                                                                                                                                                        messageTextBody11.put("paymentId",payment_id);
                                                                                                                                                                        messageTextBody11.put("search",branch.toLowerCase()+" " +semester.toLowerCase()+" "+subject.toLowerCase()+" "+course.toLowerCase());
                                                                                                                                                                        messageTextBody11.put("title", branch+" " +semester+" "+subject+" "+course+" "+testName);
                                                                                                                                                                        messageTextBody11.put("universityName",university);
                                                                                                                                                                        messageTextBody11.put("semesterName",semester);
                                                                                                                                                                        messageTextBody11.put("subjectName",subject);
                                                                                                                                                                        messageTextBody11.put("courseName",course);
                                                                                                                                                                        messageTextBody11.put("branchName",branch);
                                                                                                                                                                        messageTextBody11.put("testName",testName);
                                                                                                                                                                        messageTextBody11.put("sender", fuser.getUid());
                                                                                                                                                                        messageTextBody11.put("messageID", messagePushID11);
                                                                                                                                                                        messageTextBody11.put("time", saveCurrentTime);
                                                                                                                                                                        messageTextBody11.put("date", saveCurrentDate);
                                                                                                                                                                        messageTextBody11.put("upiHolderName",holderName);
                                                                                                                                                                        messageTextBody11.put("type", "web_pdf");
                                                                                                                                                                        messageTextBody11.put("location","pdf");
                                                                                                                                                                        messageTextBody11.put("amount", rapay);

                                                                                                                                                                        RootRef.child("suri").child(messagePushID11).updateChildren(messageTextBody11).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onComplete(@NonNull Task task) {
                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                    Toast.makeText(CompitionExamDataUpload.this, "upload successfully", Toast.LENGTH_SHORT).show();

                                                                                                                                                                                    if (task.isSuccessful()) {

                                                                                                                                                                                        final String messagePushID11 = RootRef.push().getKey();


                                                                                                                                                                                        Map messageTextBody11 = new HashMap();

                                                                                                                                                                                        messageTextBody11.put("message", myUrl);
                                                                                                                                                                                        messageTextBody11.put("url", myUrl);
                                                                                                                                                                                        messageTextBody11.put("paymentId", payment_id);
                                                                                                                                                                                        messageTextBody11.put("search", branch.toLowerCase() + " " + semester.toLowerCase() + " " + subject.toLowerCase() + " " + course.toLowerCase());
                                                                                                                                                                                        messageTextBody11.put("title", branch + " " + semester + " " + subject + " " + course + " " + testName);
                                                                                                                                                                                        messageTextBody11.put("universityName", university);
                                                                                                                                                                                        messageTextBody11.put("semesterName", semester);
                                                                                                                                                                                        messageTextBody11.put("subjectName", subject);
                                                                                                                                                                                        messageTextBody11.put("courseName", course);
                                                                                                                                                                                        messageTextBody11.put("branchName", branch);
                                                                                                                                                                                        messageTextBody11.put("testName", testName);
                                                                                                                                                                                        messageTextBody11.put("sender", fuser.getUid());
                                                                                                                                                                                        messageTextBody11.put("messageID", messagePushID11);
                                                                                                                                                                                        messageTextBody11.put("time", saveCurrentTime);
                                                                                                                                                                                        messageTextBody11.put("date", saveCurrentDate);
                                                                                                                                                                                        messageTextBody11.put("upiHolderName", holderName);
                                                                                                                                                                                        messageTextBody11.put("type", "web_pdf");
                                                                                                                                                                                        messageTextBody11.put("location","pdf");
                                                                                                                                                                                        messageTextBody11.put("amount", rapay);

                                                                                                                                                                                        RootRef.child("suggestion").child(messagePushID11).updateChildren(messageTextBody11).addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                                                                            @Override
                                                                                                                                                                                            public void onComplete(@NonNull Task task) {
                                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                                    Toast.makeText(CompitionExamDataUpload.this, "upload successfully", Toast.LENGTH_SHORT).show();

                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        });

                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            }                                                                                                                                                                            });
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            });
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                });                                                                                                                                                }
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });








                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CompitionExamDataUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (cheker.equals("image")) {


                RootRef = FirebaseDatabase.getInstance().getReference().child("Weburls");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

                DatabaseReference userMessageKeyRef = RootRef;

                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(CompitionExamDataUpload.this, "upload successfully", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd yyyy");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            HashMap<String, Object> groupMessageKey = new HashMap<>();
                            RootRef.updateChildren(groupMessageKey);


                            Map messageTextBody = new HashMap();

                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("url", myUrl);
                            messageTextBody.put("title", "title");
                            messageTextBody.put("description","hi");
                            messageTextBody.put("universityName","uni");
                            messageTextBody.put("CourseName","course");
                            messageTextBody.put("branchName","branchName");
                            messageTextBody.put("testName","testName");
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);
                            messageTextBody.put("type", cheker);



                            RootRef.child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        messageTextBody.put("message", myUrl);
                                        messageTextBody.put("url", myUrl);
                                        messageTextBody.put("title", "title");
                                        messageTextBody.put("description","hi");
                                        messageTextBody.put("universityName","uni");
                                        messageTextBody.put("CourseName","course");
                                        messageTextBody.put("branchName","branchName");
                                        messageTextBody.put("testName","testName");
                                        messageTextBody.put("sender", fuser.getUid());
                                        messageTextBody.put("messageID", messagePushID);
                                        messageTextBody.put("time", saveCurrentTime);
                                        messageTextBody.put("date", saveCurrentDate);
                                        messageTextBody.put("type", cheker);

                                    }
                                }
                            });


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CompitionExamDataUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CompitionExamDataUpload.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}