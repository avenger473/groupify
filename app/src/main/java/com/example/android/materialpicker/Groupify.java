package com.example.android.materialpicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.materialpicker.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Groupify extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupify);
        Button b=(Button)findViewById(R.id.button2);
       // Button b2= (Button)findViewById(R.id.button3) ;


        Intent intent = getIntent();

        final String fp = intent.getStringExtra("path");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(Groupify.this,
                        Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Groupify.this,
                            Manifest.permission.WRITE_CONTACTS)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(Groupify.this,
                                new String[]{Manifest.permission.WRITE_CONTACTS},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }


                String DisplayName;
                String MobileNumber;
               // String EmailId;




                InputStream is; String ids[];
                FileReader fr=null;
                File fl= new File(fp);
                try {
                    fr = new FileReader(fl);

                }
                catch(Exception e){};
                //is=getResources().openRawResource(R.raw.cont);

                BufferedReader br=new BufferedReader(fr);
                try
                {
                    String csvL;
                    csvL=br.readLine();
                    int count=0;
                    while((csvL=br.readLine())!=null){
                        ids=csvL.split(",");
                        DisplayName=ids[0];
                        DisplayName= "__"+ DisplayName;
                        MobileNumber=ids[1];
                        //EmailId=ids[2];
                        ArrayList<ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();

                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());

                        //------------------------------------------------------ Names
                        if (DisplayName != null) {
                            ops.add(ContentProviderOperation.newInsert(
                                    ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(
                                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                            DisplayName).build());
                        }

                        //------------------------------------------------------ Mobile Number
                        if (MobileNumber != null) {
                            ops.add(ContentProviderOperation.
                                    newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build());
                        }



                        //------------------------------------------------------ Home Numbers


                        // Asking the Contact provider to create a new contact
                        try {
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                            count++;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Groupify.this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    String count_s= String.valueOf(count);
                    Toast.makeText(Groupify.this, count_s + " contacts added", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, fp, Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {

                }




            }
        });
    }
}