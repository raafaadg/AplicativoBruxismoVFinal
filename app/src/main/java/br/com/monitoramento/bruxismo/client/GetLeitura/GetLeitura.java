
package br.com.monitoramento.bruxismo.client.GetLeitura;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import br.com.monitoramento.bruxismo.CadastroActivity;
import br.com.monitoramento.bruxismo.MainActivity2;
import br.com.monitoramento.bruxismo.SetupDadosActivity;
import br.com.monitoramento.bruxismo.TimeGraficoActivity;
import br.com.monitoramento.bruxismo.client.APIError;
import br.com.monitoramento.bruxismo.client.ConnectionUtil;
import br.com.monitoramento.bruxismo.client.CustomRequest;
import br.com.monitoramento.bruxismo.client.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLeitura extends CustomRequest {

    public GetLeitura(Fragment fragment, ProgressDialog progressDialog) {
        super(fragment, progressDialog);
    }

    public GetLeitura(Context context, ProgressDialog progressDialog) {
        super(context, progressDialog);
    }

    public GetLeitura(Fragment fragment) {
        super(fragment);
    }

    public GetLeitura(Context context) {
        super(context);
    }

    @Override
    public void request_call() {
        try {

            GetLeituraInterface client = ServiceGenerator.createService(GetLeituraInterface.class);
            //String coma = ((CadastroActivity) getContext()).getComando();
//            String coma = "json3";
            String coma = "tele";
            Call<GetLeituraResponse> call = client.getLeitura(coma);
            call.enqueue(new Callback<GetLeituraResponse>() {

                @Override
                public void onResponse(Call<GetLeituraResponse> call, Response<GetLeituraResponse> response) {
                    if (response.isSuccessful()) {
                        // Ler os dados aqui, eles vão estar em response.body()
                        dismissProgressDialog();
                        //((CadastroActivity) getContext()).setInfo(response.body());
//                        ((TimeGraficoActivity) getContext()).setInfo(response.body());
                        ((SetupDadosActivity) getContext()).setInfo(response.body());
                        //Toast.makeText(getContext(), "Dado Aquisistado", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 401) {
                        //Toast.makeText(getContext(), "Não autorizado", Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    } else {
                        APIError error = ConnectionUtil.parseError(response);
                        Toast.makeText(getContext(), error.message(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetLeituraResponse> call, Throwable t) {
                    Log.d("error message", t.getMessage());
                    t.printStackTrace();
                    createMessageDialog(t.getMessage());
                    dismissProgressDialog();
                }
            });

            super.request_call();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

