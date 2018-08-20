package br.com.monitoramento.bruxismo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SetupStartActivity extends AppCompatActivity {

    TextView tv_iniciar_setup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_setup_start);
        // Carregue todas as views do seu layout
        loadViews();
        // Seta o que o botão vai fazer
        tv_iniciar_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupStartActivity.this, SetupDadosActivity.class));
            }
        });

    }

    private void loadViews() {
        tv_iniciar_setup = (TextView) findViewById(R.id.tv_iniciar_setup);
    }
}
