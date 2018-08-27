package br.com.monitoramento.bruxismo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import br.com.monitoramento.bruxismo.ArquivoDialogListener;
import br.com.monitoramento.bruxismo.ConfigActivity;
import br.com.monitoramento.bruxismo.R;

public class ArquivoDialog extends AppCompatDialogFragment {

    EditText et_nomearquivo;
    ArquivoDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog,null);

        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nomeArquivo = et_nomearquivo.getText().toString();
                        listener.applyTexts(nomeArquivo);
                    }
                });
        et_nomearquivo = view.findViewById(R.id.et_nomearquivo);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener =(ArquivoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
            "deve implementar ArquivoDialogListner");
        }
    }
}
