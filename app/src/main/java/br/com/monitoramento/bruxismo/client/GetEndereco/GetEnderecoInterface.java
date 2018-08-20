
package br.com.monitoramento.bruxismo.client.GetEndereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetEnderecoInterface {
    @GET("ws/{cep}/json/")
    Call<GetEnderecoResponse> getEndereco(@Path("cep") String cep);
}

