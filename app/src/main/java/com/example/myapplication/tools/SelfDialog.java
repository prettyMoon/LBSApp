package com.example.myapplication.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * Created by hongli on 2018/5/19.
 */

public class SelfDialog extends Dialog {
    private Context context;
    private TextView tv_camera, tv_album;
    public SelfDialogListener listener;

    public SelfDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setListener(SelfDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.up_dialog_layout);
        tv_camera = this.findViewById(R.id.tv_camera);
        tv_album = this.findViewById(R.id.tv_album);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cameraCallback();
                }
            }
        });
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.albumCallback();
                }
            }
        });
    }

    public interface SelfDialogListener {
        public void cameraCallback();

        public void albumCallback();
    }
}

