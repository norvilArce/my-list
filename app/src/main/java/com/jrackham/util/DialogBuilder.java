package com.jrackham.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.jrackham.R;
import com.jrackham.model.Product;

public class DialogBuilder {
    public static Dialog getDialogConfirm(Context context, String title, String text) {
        // con este tema personalizado evitamos los bordes por defecto
        Dialog customDialog = new Dialog(context, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_confirm);

        TextView titulo = customDialog.findViewById(R.id.titulo);
        titulo.setText(title);

        TextView contenido = customDialog.findViewById(R.id.contenido);
        contenido.setText(text);
        return customDialog;
    }

    public static Dialog getDialogEdit(Context context, String title, String text, Product product) {
        // con este tema personalizado evitamos los bordes por defecto
        Dialog customDialog = new Dialog(context, R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_edit);

        TextView titulo = customDialog.findViewById(R.id.titulo);
        titulo.setText(title);

        TextView contenido = customDialog.findViewById(R.id.contenido);
        contenido.setText(text);
        return customDialog;
    }
}
