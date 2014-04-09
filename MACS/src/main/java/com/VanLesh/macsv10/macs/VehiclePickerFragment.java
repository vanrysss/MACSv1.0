package com.VanLesh.macsv10.macs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.Selector;


public class VehiclePickerFragment extends DialogFragment implements TextView.OnEditorActionListener {



    Vehicle mVehicle;
    private Selector mClass;
    private EditText mType;
    private EditText mWeight;
    private EditText mTrackLength;
    private EditText mTrackWidth;
    private EditText mBladeWidth;
    private EditText mHeightCOG;
    private EditText mSetBackCOG;

    ImageButton HgQuestion;
    ImageButton TgQuestion;
    ImageButton DbQuestion;
    ImageButton WbQueston;
    ImageButton WvQuestion;
    ImageButton CgQuestion;

    public VehiclePickerFragment() {
    }

    public interface VehicleDialogListener{
        void onFinishedEditDialog(String inputText);
    }

    VehicleDialogListener mListener;

    public static VehiclePickerFragment newInstance(String title) {

        VehiclePickerFragment fragment = new VehiclePickerFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_vehicle_picker, container);


        HgQuestion = (ImageButton)v.findViewById(R.id.question_hg);
        ToastMaker(R.string.hg_popup,HgQuestion);

        DbQuestion = (ImageButton)v.findViewById(R.id.question_db);
        ToastMaker(R.string.db_popup,DbQuestion);

        WbQueston = (ImageButton)v.findViewById(R.id.question_wb);
        ToastMaker(R.string.wb_popup,WbQueston);

        WvQuestion = (ImageButton)v.findViewById(R.id.question_wv);
        ToastMaker(R.string.wv_popup,WvQuestion);

        CgQuestion = (ImageButton)v.findViewById(R.id.question_cg);
        ToastMaker(R.string.cg_popup,CgQuestion);

        return v;

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mListener = (VehicleDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement listener");
        }
    }
    @Override
    public boolean onEditorAction(TextView v, int ActionId ,KeyEvent event){
        if (EditorInfo.IME_ACTION_DONE == ActionId){
            //return input to calling fragment
            VehicleDialogListener listener = (VehicleDialogListener) getActivity();
            listener.onFinishedEditDialog(mType.getText().toString());
            dismiss();
            return true;

        }
        return false;

    }

    // A Simple toast function, displays text by resource Id depending on what imagebutton was clicked
    public void ToastMaker(final int display, ImageButton button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(display), Toast.LENGTH_LONG).show();
            }
        });

    }




}
