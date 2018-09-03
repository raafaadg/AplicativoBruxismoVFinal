package br.com.monitoramento.bruxismo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeitura;
import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeituraResponse;

public class SetupDadosActivity extends AppCompatActivity {
    EditText et_tele_ssid;
    EditText et_tele_bateria;
    EditText et_tele_nomearquivo;
    EditText et_tele_tamarquivo;
    TextView tv_dados_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_setup_dados);
        // Carregue todas as views do seu layout
        loadViews();
        // Seta o que o botão vai fazer
        tv_dados_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryHTTP("http://192.168.4.1/mestrado/online");
            }
        });
        new GetLeitura(SetupDadosActivity.this);

    }

    public void tryHTTP(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest putRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.equals("Online Desabilitado")){
                            Log.v("online","Modo online desativado");
                        }else {
                            Toast.makeText(SetupDadosActivity.this, response, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SetupDadosActivity.this,
                                    TimeGrafico2Activity.class));

                            Log.v("online", "Modo online Ativado");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.v("online",error.toString());

                    }
                }
        );

        queue.add(putRequest);
    }

    private void loadViews() {
        et_tele_ssid = (EditText) findViewById(R.id.et_tele_ssid);
        et_tele_bateria = (EditText) findViewById(R.id.et_tele_bateria);
        et_tele_nomearquivo = (EditText) findViewById(R.id.et_tele_nomearquivo);
        et_tele_tamarquivo = (EditText) findViewById(R.id.et_tele_tamarquivo);
        tv_dados_ok = (TextView) findViewById(R.id.tv_dados_ok);
        tv_dados_ok.setVisibility(View.INVISIBLE);
    }

    public void setInfo(GetLeituraResponse info) {
        et_tele_ssid.setText(info.nomeSSID);
        et_tele_bateria.setText(Float.toString(info.bateria));
        et_tele_nomearquivo.setText(info.nomeArquivo);
        et_tele_tamarquivo.setText(String.valueOf(info.tamanhoArquivo));
        if(verificaDados(info)){
            tv_dados_ok.setVisibility(View.VISIBLE);
        }

    }

    public boolean verificaDados(GetLeituraResponse info){
        boolean fbat = false, f2 = false, f3 = false, f4 = false;
        if(info.bateria < 3.8)
            fbat = true;
//        if(info.nomeSSID != )
        return true;
    }
}
