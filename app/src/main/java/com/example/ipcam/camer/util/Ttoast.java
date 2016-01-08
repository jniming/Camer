package com.example.ipcam.camer.util;

import android.content.Context;
import android.widget.Toast;

public class Ttoast {
    public static Toast toast;
    ;

    public static void show(Context context, String str) {

        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else
            toast.setText(str);
        toast.show();
    }

    public static void CheckErrorCode(Context context, int code) {
        switch (code) {
            case 3501:
                Ttoast.show(context, "�˺Ų�����");
                break;
            case 3502:
                Ttoast.show(context, "�˺��Ѵ���");
                break;
            case 3503:
                Ttoast.show(context, "�˺Ų��Ϸ�");
                break;
            case 3504:
                Ttoast.show(context, "�������");
                break;
            case 3505:
                Ttoast.show(context, "��֤�����");
                break;
            case 3506:
                Ttoast.show(context, "��֤���ѹ���");
                break;
            case 3507:
                Ttoast.show(context, "���䲻�Ϸ�");
                break;
            case 3508:
                Ttoast.show(context, "�ֻ�Ϸ�");
                break;

            default:
                break;
        }
    }
}
