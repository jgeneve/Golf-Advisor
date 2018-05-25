package com.golf.dss.golf_project.DialogWindows;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.golf.dss.golf_project.R;

public class CustomDialogMessage {

    public void dialogClubAdvise(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_advise_club);

        ImageButton dialogbBnValidateShoot = dialog.findViewById(R.id.dialogbBnValidateShoot);
        dialogbBnValidateShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessage c = new CustomDialogMessage();
                c.dialogWalkBall(context);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void dialogWalkBall(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_walk_ball);
        dialog.show();

        ImageButton dialogBtnGetBall = dialog.findViewById(R.id.dialogBtnGetBall);
        ImageButton dialogBtnFinish = dialog.findViewById(R.id.dialogBtnFinish);

        dialogBtnGetBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Get ball action
                dialog.cancel();
            }
        });

        dialogBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Finish action
                dialog.cancel();
            }
        });
    }
}
