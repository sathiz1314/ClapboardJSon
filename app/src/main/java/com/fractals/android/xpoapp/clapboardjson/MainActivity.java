package com.fractals.android.xpoapp.clapboardjson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    ListView listView;
    TextView tv1;
    ImageView imageview;
    String title,id,date,comment_count,comment_status,modified,thumbnail;
    String data[];
    ArrayAdapter<String> arrayAdapter;

    String img, url;
    JSONArray jsonArray;
    private static String urldata = "http://clapboard.co.in/?json=get_recent_posts&exclude=content,thumbnail_images,thumbnail_size,custom_fields,attachments,tags,categories,excerpt,title_plain,status,url,slug,type,author,comments";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   tv1 = (TextView) findViewById(R.id.textview);
       imageview = (ImageView) findViewById(R.id.imageView);
        listView = (ListView) findViewById(R.id.listView);
        new GetContacts().execute();

    }

    public void save(View view) {


        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inTempStorage = new byte[16*1024];
        imageview.setImageBitmap(BitmapFactory.decodeFile(thumbnail,bOptions));
        imageview.buildDrawingCache();

        Bitmap bm =imageview.getDrawingCache();


        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Punch");
        imagesFolder.mkdirs();
        String fileName = "image"  + ".jpg";
        File output = new File(imagesFolder, fileName);
        Uri uriSavedImage = Uri.fromFile(output);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        OutputStream fos = null;

        try {
            fos = getContentResolver().openOutputStream(uriSavedImage);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {}
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeservicecall(urldata);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    jsonArray = jsonObj.getJSONArray("posts");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);


                        if (i == 3) {
                            id= c.getString("id");
                            title = c.getString("title");
                            date = c.getString("date");
                            modified = c.getString("modified");
                            comment_count = c.getString("comment_count");
                            comment_status = c.getString("comment_status");
                            thumbnail =c.getString("thumbnail");

                            data = new String[]{id,title,date,modified,comment_count,comment_status};




                          //  JSONObject jsonObject = c.getJSONObject("thumbnail_images");
                         //   JSONObject jsonObject1 = jsonObject.getJSONObject("full");
                          //  url = jsonObject1.getString("url");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            pDialog.dismiss();
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,data);
            listView.setAdapter(arrayAdapter);
          //  tv1.setText(title);
            //  Log.i("url: ", img);
           // Glide.with(MainActivity.this).load(thumbnail).into(imageview);
            Picasso.with(MainActivity.this)
                    .load(thumbnail)
                    .into(imageview);



        }
    }



}
