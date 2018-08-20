package br.com.monitoramento.bruxismo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;


/**
 * Created by rafae on 08/11/2017.
 */

public class CompartilharActivity extends AppCompatActivity {

    ImageView im_share_wa;
    ImageView im_share_gm;
    Context context;
    Uri selectedImagePath;
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
                getImage();
                //shareWhats();
            }
        });
        im_share_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareGMail("raafaadg@gmail.com","Dados Aquisição","Segue em" +
                        "anexo arquivo contendo dados da aquicição");
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_INTENT_CLICK) selectedImagePath = data.getData();
        Log.d("URI",selectedImagePath.toString());
    }

    private void loadViews() {
        im_share_wa = (ImageView) findViewById(R.id.im_share_wa);
        im_share_gm = (ImageView) findViewById(R.id.im_share_gm);
    }
    private void shareWhats(){
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void shareGMail(String email, String subject, String content) {


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("content://com.android.externalstorage.documents/document/primary%3ALOGS%2FNOMETESTE3.txt"));
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
    }

    private void getImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                MY_INTENT_CLICK);
    }

}
