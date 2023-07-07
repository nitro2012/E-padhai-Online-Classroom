package com.example.e_padhai;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_padhai.NumbersView;
import com.example.e_padhai.NumbersViewAdapter;
import com.example.e_padhai.DatabaseUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareClass extends AppCompatActivity {

    private Button addFile, share;
    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private List<String> pdfName = new ArrayList<>();
    private Uri uri;
    NumbersViewAdapter adapter;
    ListView listView;
    ArrayList<NumbersView> listItems;
    private String time;
    private String date;
    private int fileNo=1;
    private List<String> urlList,nList;
    private EditText textField;
    private String classId;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        assert data != null;
                        uri = data.getData();


                        urlList.add(uri.toString());


                        Toast.makeText(this, "Added To List", Toast.LENGTH_SHORT).show();
                        String fileName="default";
                        if (uri.toString().startsWith("content://")) {
                            Cursor cursor;
                            try {
                                cursor = ShareClass.this.getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    fileName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                                    pdfName.add(fileName);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        } else if (uri.toString().startsWith("file://")) {
                            fileName=new File(uri.toString()).getName();
                            pdfName.add(fileName);
                        }

                        //listItems.add(fileName);
                        if(getMimeType(this, uri).equals("pdf"))
                        listItems.add(new NumbersView(R.drawable.ic_pdf1, uri, Integer.toString(fileNo), fileName));
                        else if(getMimeType(this, uri).equals("jpg"))
                            listItems.add(new NumbersView(R.drawable.ic_image, uri, Integer.toString(fileNo), fileName));
                        else
                            listItems.add(new NumbersView(R.drawable.ic_file, uri, Integer.toString(fileNo), fileName));
                        adapter.notifyDataSetChanged();
                    }
                });

        setContentView(R.layout.activity_share_class);

        classId = getIntent().getStringExtra("id");
        urlList = new ArrayList<String>();
        nList = new ArrayList<String>();
        pd = new ProgressDialog(this);
        databaseReference = DatabaseUtil.getDatabase().getReference().child("Classes");
        storageReference = FirebaseStorage.getInstance().getReference();

        listView = (ListView) findViewById(R.id.listView);
        listItems = new ArrayList<NumbersView>();
        /*listItems.add("First Item - added on Activity Create");*/
        /*adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);*/
         adapter = new NumbersViewAdapter(this, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((a, v, position, id) -> Toast.makeText(ShareClass.this, "Clicked", Toast.LENGTH_LONG)
                .show());
        textField = findViewById(R.id.textField);
        addFile = findViewById(R.id.addFile);
        share = findViewById(R.id.send);


        addFile.setOnClickListener(v -> {
            Intent intent;
            if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                intent.putExtra("CONTENT_TYPE", "*/*");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
            } else {

                String[] mimeTypes =
                        {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "application/zip", "application/vnd.android.package-archive"};

                intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            }


            someActivityResultLauncher.launch(intent);


        });
        share.setOnClickListener(v -> uploadData());
    }

    private void uploadData() {
        pd.setTitle("Please wait...");
        pd.setMessage("Uploading...");
        pd.show();
        int i = 0;
        if(urlList.size()==0){

            UploadToDb();
        }
        for (String URL : urlList) {
            String mt = getMimeType(this, Uri.parse(URL));

            storageReference.child("feed/" + "-" + System.currentTimeMillis() + "." + mt).putFile(Uri.parse(URL)).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri=uriTask.getResult();
                nList.add(String.valueOf(uri));
                UploadToDb();
            })


                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(ShareClass.this, "Something went Wrong storage", Toast.LENGTH_SHORT).show();
                    });

            i++;
        }



    }

    private void UploadToDb() {


        Calendar calForDate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        date = currentDate.format(calForDate.getTime());
        Calendar calForTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        time = currentTime.format(calForTime.getTime());


        feedData data = new feedData(textField.getText().toString(), time, date, getIntent().getStringExtra("name"), nList);
        databaseReference.child(classId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                classesData cd = snapshot.getValue(classesData.class);
                databaseReference.child(classId).child("fData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {

                        GenericTypeIndicator<ArrayList<feedData>> t = new GenericTypeIndicator<ArrayList<feedData>>() {};

                        ArrayList<feedData> fData=snapshot1.getValue(t);

                        //ArrayList<feedData> fData=snapshot1.getValue(ArrayList.class);
                        //ArrayList fData = (ArrayList) fff;
                        if (fData == null)
                            fData = new ArrayList<feedData>();
                        fData.add(data);

                        classesData post = new classesData(cd.getClassName(), cd.getSection(), cd.getTeacherName(), cd.getClassId(), cd.getStudentList(), fData);
                        Map<String, Object> postValues = post.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + classId, postValues);
                        databaseReference.updateChildren(childUpdates).addOnSuccessListener(o -> {
                            pd.dismiss();


                            Toast.makeText(ShareClass.this, "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                            finish();

                        }).addOnFailureListener(e -> Toast.makeText(ShareClass.this, "Something went wrong database", Toast.LENGTH_SHORT).show());

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                pd.dismiss();

            }
        });


    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
}






