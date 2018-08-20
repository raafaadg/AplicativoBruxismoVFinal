
package br.com.monitoramento.bruxismo.client.GetEndereco;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import br.com.monitoramento.bruxismo.MainActivity;
import br.com.monitoramento.bruxismo.client.APIError;
import br.com.monitoramento.bruxismo.client.ConnectionUtil;
import br.com.monitoramento.bruxismo.client.CustomRequest;
import br.com.monitoramento.bruxismo.client.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Cesar on 29/03/2017.
 */

public class GetEndereco extends CustomRequest {

    public GetEndereco(Fragment fragment, ProgressDialog progressDialog) {
        super(fragment, progressDialog);
    }

    public GetEndereco(Context context, ProgressDialog progressDialog) {
        super(context, progressDialog);
    }

    public GetEndereco(Fragment fragment) {
        super(fragment);
    }

    public GetEndereco(Context context) {
        super(context);
    }

    @Override
    public void request_call() {
        try {

            GetEnderecoInterface client = ServiceGenerator.createService(GetEnderecoInterface.class);
            //String cep = ((MainActivity) getContext()).getCep();
            Call<GetEnderecoResponse> call = client.getEndereco("");
            call.enqueue(new Callback<GetEnderecoResponse>() {

                @Override
                public void onResponse(Call<GetEnderecoResponse> call, Response<GetEnderecoResponse> response) {
                    if (response.isSuccessful()) {
                        dismissProgressDialog();
                        //((MainActivity) getContext()).setEndereco(response.body());
                    } else if (response.code() == 401) {
                        Toast.makeText(getContext(), "Não autorizado", Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    } else {
                        APIError error = ConnectionUtil.parseError(response);
                        Toast.makeText(getContext(), error.message(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetEnderecoResponse> call, Throwable t) {
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

