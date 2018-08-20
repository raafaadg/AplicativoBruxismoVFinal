package br.com.monitoramento.bruxismo.client.GetLeitura;

import java.lang.reflect.Array;
import java.util.List;

// Só um exemplo, não sei como vai estar os seus dados
// Imaginando que o seu json seja:
// {
//     "result":[
//         {
//         "nome":"Pessoa",
//         "data_hora":"29/03/2017 10:50:00",
//         "valor":1.5
//         },
//         {
//         "nome":"Pessoa2",
//         "data_hora":"29/03/2017 08:45:00",
//         "valor":2.78
//         }
//     ],
//     "status":"ok",
//     "message":"Dados buscados com sucesso"
// }
// Use esse site pra verificar se o JSON é válido: https://jsonformatter.curiousconcept.com/
// Você usa uma classe como essa ↓ pra receber esses dados
public class GetLeituraResponse {
//    public String comando;
//    public String nome;
//    public String idade;
//    public String peso;
//    public String tipo;
//    public String valor;
//    public List<String> valor;
    public String nomeSSID;
    public float bateria;
    public String nomeArquivo;
    public int tamanhoArquivo;
}
