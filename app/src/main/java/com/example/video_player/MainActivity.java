 package com.example.video_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

 public class MainActivity extends AppCompatActivity implements SelectListener{

    RecyclerView recyclerView;
    List<File> fileList;
    File path = new File(System.getenv("EXTERNAL_STORAGE"));
    customAdaptor customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
    }

    // Dexter Library for Asking the Permission
     private void askPermission() {
         Dexter.withContext(this)
                 .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                 .withListener(new MultiplePermissionsListener() {
                     @Override
                     public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                         if (multiplePermissionsReport.areAllPermissionsGranted()) {
                             // do you work now
                             displayFiles();
                         }
                         // check for permanent denial of any permission
                         if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                             // permission is denied permanently,
                             // we will show user a dialog message.
                             showSettingsDialog();
                         }
                     }

                     @Override
                     public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                         permissionToken.continuePermissionRequest();
                     }
                 }).onSameThread().check();
     }

     private void displayFiles() {
        recyclerView=findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        fileList = new ArrayList<>();
        fileList.addAll(findVideos(path));
        customAdapter = new customAdaptor(this,fileList,this);
        customAdapter.setHasStableIds(true);
        recyclerView.setAdapter(customAdapter);
     }
    private ArrayList<File> findVideos(File file){
        ArrayList<File> myVideos = new ArrayList<>();
        File[] allFiles = file.listFiles();
        for(File singleFile : allFiles){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                myVideos.addAll(findVideos(singleFile));
            }
            else if(singleFile.getName().toLowerCase().endsWith(".mp4")){
                myVideos.add(singleFile);
            }
        }
        return myVideos;
    }
     private void showSettingsDialog() {
         AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

         // below line is the title
         // for our alert dialog.
         builder.setTitle("Need Permissions");

         // below line is our message for our dialog
         builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
         builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 // this method is called on click on positive
                 // button and on clicking shit button we
                 // are redirecting our user from our app to the
                 // settings page of our app.
                 dialog.cancel();
                 // below is the intent from which we
                 // are redirecting our user.
                 Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                 Uri uri = Uri.fromParts("package", getPackageName(), null);
                 intent.setData(uri);
                 startActivityForResult(intent, 101);
             }
         });
         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 // this method is called when
                 // user click on negative button.
                 dialog.cancel();
             }
         });
         // below line is used
         // to display our dialog
         builder.show();
     }

     @Override
     public void onFileClicked(File file) {
            startActivity(new Intent(MainActivity.this,PlayerActivity.class)
            .putExtra("VIDEO",file.getAbsolutePath()));
     }
 }