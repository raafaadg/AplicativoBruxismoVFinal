
package br.com.monitoramento.bruxismo.client.GetLeitura;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Cesar on 29/03/2017.
 */

public interface GetLeituraInterface {
    @GET("mestrado/{codigo}")
//    @GET("{codigo}")
    Call<GetLeituraResponse> getLeitura(@Path("codigo") String codigo);
}

