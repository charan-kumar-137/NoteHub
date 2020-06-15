package com.darkcodedclan.notehub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload_Download extends AppCompatActivity {
    private EditText file_name;
    private Button file_submit;
    private TextView file_chooser;
    private TextView file_save_warn;
    private Uri file_path;
    private StorageReference mStorageRef;
    private String user_pre_name;
    private FirebaseUser currentuser;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload__download);

        Intent intent = getIntent();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        currentuser = FirebaseAuth.getInstance().getCurrentUser();

        file_name = findViewById(R.id.file_name_input);
        file_submit = findViewById(R.id.file_submit);
        file_chooser = findViewById(R.id.file_chooser);
        file_save_warn = findViewById(R.id.file_save_warn);

        user_pre_name_F();
        String operation = intent.getStringExtra("key");
        try {
            if (operation != null) {
                operation(operation);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void user_pre_name_F() {    // set path to download {user}
        if (currentuser != null){
            user_pre_name = currentuser.getEmail();
        }
    }

    private void operation(final String s) {    // Perform operation based on intent passed
        if (s.equals("upload")) {    // If intent passed is upload
            // user has to see warn text and choose file option

            file_chooser.setOnClickListener(new View.OnClickListener() { // User can click the text to choose file
                @Override
                public void onClick(View v) {
                    selectFile();
                }
            });
        }
        else {    // If intent passed is download
            //user must not see warn text and choose file option
            file_chooser.setEnabled(false);
            file_save_warn.setEnabled(false);
            file_save_warn.setVisibility(View.INVISIBLE);
            file_chooser.setVisibility(View.INVISIBLE);
        }

        file_submit.setOnClickListener(new View.OnClickListener() {    // submit button
            @Override
            public void onClick(View v) {
                name = file_name.getText().toString().trim(); // file name
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Upload_Download.this, "File name empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s.equals("upload")) {
                    upload();
                }
                if (s.equals("download")) {
                    download();
                }
            }
        });

    }

    private void selectFile(){
        // Creating a intent as get content

        Intent my_file = new Intent();
        my_file.setType("application/pdf");
        my_file.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(my_file,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifying request code ...
        if (requestCode == 12 && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            // Set the path of the file
            file_path = data.getData();
        }
        else {
            Toast.makeText(this, "Error Occurred!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(){
        // Uploading the file { user / file}

        String s = user_pre_name + "/" + name;
        final StorageReference ref  = mStorageRef.child(s);

        ref.putFile(file_path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Upload_Download.this, "Upload Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Upload_Download.this,Home_Screen.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Download.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Upload_Download.this,Home_Screen.class));
            }
        });
    }

    private void download(){
        // Choose the File and Download using url

        String s = user_pre_name + "/" + name ;
        final String ext = ".pdf";
        StorageReference ref = mStorageRef.child(s);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Toast.makeText(Upload_Download.this, "Download In Progress", Toast.LENGTH_SHORT).show();
                downloadFile(Upload_Download.this,name,ext,Environment.DIRECTORY_DOWNLOADS,url);
                startActivity(new Intent(Upload_Download.this,Home_Screen.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Download.this, "Download Failed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Upload_Download.this,Home_Screen.class));
            }
        });
    }

    private void downloadFile(Context context,String filename,String file_ext,String dest_dir,String url){
        // Get the url and ...
        // Use DownloadManager to Download using url

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,dest_dir,filename+file_ext);

        if (dm != null) {
            dm.enqueue(request);
        }
    }
}