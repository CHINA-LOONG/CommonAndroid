package com.loong.common.update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by  on 2016/7/15.
 */
public class AlertDialogUtils {
    static Dialog dialog;
    public interface DialogListener{
        void Dialogok();
        void Dialogcancel();
    }
    public static void showDialog(String title, String msg, String ok, Context context, final DialogListener listener){
        try {
            if (dialog!=null)
                dialog.dismiss();
        }catch (Exception e){}
        dialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(msg).
                setPositiveButton(ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener!=null){
                            listener.Dialogok();
                        }
                    }
                }).
                create();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        dialog.show();
    }
    public static void showDialog(String title, String msg, String ok, String cancel, Context context, final DialogListener listener){
        try {
            if (dialog!=null)
                dialog.dismiss();
        }catch (Exception e){}
        dialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(msg).
                setPositiveButton(ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener!=null){
                            listener.Dialogok();
                        }
                    }
                }).
                setNegativeButton(cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener!=null){
                            listener.Dialogcancel();
                        }
                    }
                }).
                create();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        try {
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
