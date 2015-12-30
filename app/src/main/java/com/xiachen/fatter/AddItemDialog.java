package com.xiachen.fatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiachen on 15/12/6.
 */
public class AddItemDialog extends DialogFragment {

    private EditText mWeight;
    private CheckBox mSnack;
    private CheckBox mBath;
    private CheckBox mDefecation;
    private itemsUpdateListener mCallback;
    private SimpleDateFormat mFormat;
    private Bean mBean;

    public interface itemsUpdateListener {
        void update(int position);
        void refresh(Bean bean);
    }

    public itemsUpdateListener getCallback() {
        return mCallback;
    }

    public void setCallback(itemsUpdateListener callback) {
        mCallback = callback;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bean bean = (Bean) getArguments().get(Constant.GET_BEAN);
            if (bean != null) {
                Utils.showLog(this.getClass(), "edit item");
                mBean = bean;
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_additem, null);
        mWeight = (EditText) view.findViewById(R.id.weight);
        mSnack = (CheckBox) view.findViewById(R.id.snack);
        mBath = (CheckBox) view.findViewById(R.id.bath);
        mDefecation = (CheckBox) view.findViewById(R.id.defecation);

        if (mBean != null) {
            mWeight.setText(String.valueOf(mBean.weight));
            mSnack.setChecked(mBean.ifSnack);
            mBath.setChecked(mBean.ifBath);
            mDefecation.setChecked(mBean.ifDefecate);
        }

        builder.setView(view).setPositiveButton(R.string.dialog_posi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mBean == null) {
                    insertItem();
                    if (mCallback != null) {
                        mCallback.update(0);
                    }
                } else {
                    editItem();
                    if (mCallback != null) {
                        mCallback.refresh(mBean);
                    }
                }


            }
        }).setNegativeButton(R.string.dialog_nega, null);
        return builder.create();

    }

    private void editItem() {
        mBean.weight = Integer.parseInt(mWeight.getText().toString());
        mBean.ifSnack = mSnack.isChecked();
        mBean.ifBath = mBath.isChecked();
        mBean.ifDefecate = mDefecation.isChecked();
        mBean.save();
    }

    private void insertItem() {
        Utils.showToast(getActivity(), R.string.dialog_insertsuccess);
        Bean bean = new Bean(Integer.parseInt(mWeight.getText().toString()), mSnack.isChecked(), mBath.isChecked(), mDefecation.isChecked());
        bean.setDate(Utils.formatDate(new Date()));
        bean.save();
    }

    private boolean checkValid(String s) {
//        String weightReg = "^[1-9]/d{2,3}/.[1-9]{1,2}/d$";
//        String weightReg = "^[1-9]/d{1,2}/.?[1-9]?$";
        String weightReg = "^[1-9]";
        Pattern p = Pattern.compile(weightReg);
        Matcher matcher = p.matcher(s);
        if (matcher.matches()) {
            return true;
        } else {
            Utils.showToast(getActivity(), R.string.invalid);
            return false;
        }
    }
}
