package br.com.monitoramento.bruxismo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;


/**
 * Created by rafae on 08/11/2017.
 */

public class CompartilharActivity extends AppCompatActivity {

    ImageView im_share_wa;
    ImageView im_share_gm;
    ImageView im_share_gs;
    String results = "";
    Context context;
    Uri selectedImagePath;
    boolean gmail = false, whats = false;
    private static final int REQUEST_CODE = 3132;
    private static final int MY_INTENT_CLICK = 23122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_compartilhar);
        // Carregue todas as views do seu layout
        loadViews();
//         Seta o que o botão vai fazer
        im_share_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whats = true;
                getImage();
            }
        });
        im_share_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmail = true;
                getImage();
            }
        });
        im_share_gs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(CompartilharActivity.this, null, null, 1);
                ArrayList<String> result = dbHandler.loadHandler();
                results = "";
                for(String aux:result) {
                    results += aux;
                    results += ";";
                }
                //new SendRequest().execute();
                //new SendRequest().doInBackground();
                new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //new SendRequest().execute();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_INTENT_CLICK)
            if(data != null) {
                selectedImagePath = data.getData();
                if(gmail)
                    shareGMail("raafaadg@gmail.com","Dados Aquisição","Segue em " +
                        "anexo arquivo contendo dados da aquicição");
                if (whats)
                    shareWhats();
            }
        //Log.d("URI",selectedImagePath.toString());
    }

    private void loadViews() {
        im_share_wa = (ImageView) findViewById(R.id.im_share_wa);
        im_share_gm = (ImageView) findViewById(R.id.im_share_gm);
        im_share_gs = (ImageView) findViewById(R.id.im_share_gs);
    }
    private void shareWhats(){
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/*");
            String text = "Estou compartilhando os dados obtidos";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            //waIntent.putExtra(Intent.EXTRA_TEXT, text);
            waIntent.putExtra(Intent.EXTRA_STREAM,selectedImagePath);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }finally {
            whats = false;
        }
    }

    public void shareGMail(String email, String subject, String content) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        //emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("content://com.android.externalstorage.documents/document/primary%3ALOGS%2FNOMETESTE3.txt"));
        emailIntent.putExtra(Intent.EXTRA_STREAM,selectedImagePath);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
        gmail =false;
    }

    private void getImage() {
        //MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("text/xls");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    MY_INTENT_CLICK);
        }catch (Exception e) {
            e.getMessage();
        }
    }

    public class SendRequest extends AsyncTask<Void, Void, String> {


        protected void onPreExecute(){
            Log.v("PreExec","Entrou na pré execução");
            Thread.currentThread().setPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);
        }

        protected String doInBackground(Void... resultss) {

            try{

                URL url = new URL("https://script.google.com/macros/s/" +
                        "AKfycbwwFPcE_5aSqtaNOSANNlA-vuvRvMMbUwSRR-c46iMpwOSEwTos/exec");
                JSONObject postDataParams = new JSONObject();

                String id = "1KIja0pt1IegzG4stnzRZkQ8xoVQuQyUUtHIr_Gno2rY";

                ArrayList<String> result = new ArrayList<String>();
                String buffer = "";
                for(char res : results.toCharArray()){
                    if(res != ';')
                        buffer += res;
                    else{
                        result.add(buffer);
                        buffer = "";
                    }
                }
                postDataParams.put("id", id);
                postDataParams.put("nome", result.get(0));
                postDataParams.put("idade", result.get(1));
                postDataParams.put("peso", result.get(2));
                postDataParams.put("genero", result.get(3));
                postDataParams.put("email", result.get(4));
                postDataParams.put("data", result.get(5));
                postDataParams.put("sheet", "Sheet1");

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public void addSheet(){
        String msg="";

        try{

            URL url = new URL("https://script.google.com/macros/s/" +
                    "AKfycbwwFPcE_5aSqtaNOSANNlA-vuvRvMMbUwSRR-c46iMpwOSEwTos/exec");
            JSONObject postDataParams = new JSONObject();

            String id = "1KIja0pt1IegzG4stnzRZkQ8xoVQuQyUUtHIr_Gno2rY";

            ArrayList<String> result = new ArrayList<String>();
            String buffer = "";
            for(char res : results.toCharArray()){
                if(res != ',')
                    buffer += res;
                else{
                    result.add(buffer);
                    buffer = "";
                }
            }
            postDataParams.put("id", id);
            postDataParams.put("nome", result.get(0));
            postDataParams.put("idade", result.get(1));
            postDataParams.put("peso", result.get(2));
            postDataParams.put("genero", result.get(3));
            postDataParams.put("email", result.get(4));
            postDataParams.put("sheet", "Sheet1");

            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                msg = sb.toString();

            }
            else {
                msg = new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            msg = new String("Exception: " + e.getMessage());
        }

        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();

    }

}


