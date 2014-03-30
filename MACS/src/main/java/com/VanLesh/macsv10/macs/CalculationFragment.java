//TODO: Emailing(report), correctly use Soil and Vehicles
package com.VanLesh.macsv10.macs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;


/**
 *
 * Created by samvanryssegem on 2/24/14.
 */
public class CalculationFragment extends Fragment{
    Calculation mCalculation;
    EditText mTitleField;
    EditText mEngineerName;
    EditText mJobsite;

    EditText mVehicleclass;
    EditText mVehicletype;
    EditText mVehicleHeight;
    EditText mCOGDistance;
    EditText mWeight;
    EditText mTrackL;
    EditText mTrackW;
    EditText mBladeW;

    EditText mSoilType;
    EditText mSoilUnitWeight;
    EditText mSoilFrictionAngle;
    EditText mSoilCohesionFactor;

    EditText mSlopeAngle;
    EditText mAnchorAngle;
    EditText mAnchorHeight;
    EditText mAnchorSetback;
    EditText mBladeEmbedment;

    TextView mAnchorCapacity;
    TextView mRollOver;
    Button mDateButton;
    Callbacks mCallbacks;
    Button mCalculateButton;

    ImageButton HgQuestion;
    ImageButton TgQuestion;
    ImageButton DbQuestion;
    ImageButton WbQueston;
    ImageButton WvQuestion;
    ImageButton LaQuestion;
    ImageButton HaQuestion;
    ImageButton CgQuestion;
    ImageButton PhiQuestion;
    ImageButton CohesionQuestion;
    ImageButton UnitWeightQuestion;


    private static final int REQUEST_DATE = 0;
    public static String EXTRA_CALCULATION_ID = "calculationintent.calculation_id";

    private static final String DIALOG_DATE = "date";

    public interface Callbacks{
        void onCalculationUpdated(Calculation calculation);

    }

    public static CalculationFragment newInstance(UUID calculationId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CALCULATION_ID, calculationId);
        CalculationFragment fragment = new CalculationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks =(Callbacks)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID calculationId = (UUID)getArguments().getSerializable(EXTRA_CALCULATION_ID);
        mCalculation = CalculationLab.get(getActivity()).getCalculation(calculationId);
        setHasOptionsMenu(true);
    }

    public void updateDate(){
        mDateButton.setText(mCalculation.getDate().toString());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_calculation, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                 getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTitleField = (EditText)v.findViewById(R.id.calculation_title);
        mTitleField.setText(mCalculation.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int before, int count) {
                //blank
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setTitle(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
                }

            public void afterTextChanged(Editable editable) {
                //blank
            }
        });

        mEngineerName = (EditText)v.findViewById(R.id.calculation_engineer);
        mEngineerName.setText(mCalculation.getEngineerName());
        mEngineerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setEngineerName(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mJobsite = (EditText)v.findViewById(R.id.calculation_jobsite);
        mJobsite.setText(mCalculation.getJobSite());
        mJobsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setJobSite(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicleclass = (EditText)v.findViewById(R.id.input_vehicle_class);
        mVehicleclass.setText(mCalculation.getVehicle().getVehicleClass());
        mVehicleclass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setVehicleClass(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicletype = (EditText)v.findViewById(R.id.input_vehicle_type);
        mVehicletype.setText(mCalculation.getVehicle().getVehicleType());
        mVehicletype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setType(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicleHeight = (EditText)v.findViewById(R.id.input_vehicle_height);
        if (mCalculation.getVehicle().getHg() != 0)
            mVehicleHeight.setText(Double.toString(mCalculation.getVehicle().getHg()));

        mVehicleHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sVehicleHeight = mVehicleHeight.getText().toString();
                if (! sVehicleHeight.matches(""))
                     mCalculation.mVehicle.setHg(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCOGDistance = (EditText)v.findViewById(R.id.Cg);
        if(mCalculation.getVehicle().getCg() != 0)
             mCOGDistance.setText(Double.toString(mCalculation.getVehicle().getCg()));

        mCOGDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sCOGDistance = mCOGDistance.getText().toString();
                if (! sCOGDistance.matches(""))
                    mCalculation.getVehicle().setCg(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //TODO null validation
        mWeight = (EditText)v.findViewById(R.id.Wv);
        if(mCalculation.getVehicle().getWv() != 0)
            mWeight.setText(Double.toString(mCalculation.getVehicle().getWv()));
        mWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sWeight = mWeight.getText().toString();
                if (! sWeight.matches(""))
                    mCalculation.getVehicle().setWv(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //TODO null validation
        mTrackL = (EditText)v.findViewById(R.id.Tl);
        if(mCalculation.getVehicle().getTrackL() !=0)
            mTrackL.setText(Double.toString(mCalculation.getVehicle().getTrackL()));
        mTrackL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sTrackLength = mTrackL.getText().toString();
                if (! sTrackLength.matches(""))
                    mCalculation.getVehicle().setTrackL(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //TODO: null validation
        mTrackW = (EditText)v.findViewById(R.id.Tw);
        if(mCalculation.getVehicle().getTrackW() != 0)
            mTrackW.setText(Double.toString(mCalculation.getVehicle().getTrackW()));
        mTrackW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sTrackWidth = mTrackW.getText().toString();
                if (! sTrackWidth.matches(""))
                     mCalculation.getVehicle().setTrackW(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBladeW =(EditText)v.findViewById(R.id.Wb);
        if (mCalculation.getVehicle().getBladeW() !=0)
            mBladeW.setText(Double.toString(mCalculation.getVehicle().getBladeW()));
        mBladeW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sBladeWidth = mBladeW.getText().toString();
                if (! sBladeWidth.matches("")){
                    mCalculation.getVehicle().setBladeW(Double.parseDouble(charSequence.toString()));
                }
                mCallbacks.onCalculationUpdated(mCalculation);
            }


            @Override
            public void afterTextChanged(Editable editable){
        }

        });

        mSoilType = (EditText)v.findViewById(R.id.SoilName);
        mSoilType.setText(mCalculation.getSoil().getName());
        mSoilType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sSoilType = mSoilType.getText().toString();
                if (! sSoilType.matches(""))
                     mCalculation.getSoil().setName(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilUnitWeight = (EditText)v.findViewById(R.id.Soilunitwt);
        if (mCalculation.getSoil().getunitW() != 0)
            mSoilUnitWeight.setText(Double.toString(mCalculation.getSoil().getunitW()));
        mSoilUnitWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sSoilUnitWeight = mSoilType.getText().toString();
                if (! sSoilUnitWeight.matches(""))
                    mCalculation.getSoil().setunitW(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilCohesionFactor = (EditText)v.findViewById(R.id.Soilcohesion);
        if (mCalculation.getSoil().getC() !=0 )
             mSoilCohesionFactor.setText(Double.toString(mCalculation.getSoil().getC()));
        mSoilCohesionFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sSoilCohesionFactor = mSoilCohesionFactor.getText().toString();
                if (!sSoilCohesionFactor.matches(""))
                    mCalculation.getSoil().setC(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilFrictionAngle = (EditText)v.findViewById(R.id.Soilfricta);
        if (mCalculation.getSoil().getfrictA() != 0)
             mSoilFrictionAngle.setText(Integer.toString(mCalculation.getSoil().getfrictA()));
        mSoilFrictionAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sSoilFrictionAngle = mSoilFrictionAngle.getText().toString();
                if (!sSoilFrictionAngle.matches(""))
                     mCalculation.getSoil().setfrictA(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSlopeAngle = (EditText)v.findViewById(R.id.Beta);
        if (mCalculation.getBeta() != 0)
            mSlopeAngle.setText(Integer.toString(mCalculation.getBeta()));
        mSlopeAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sSlopeAngle = mSlopeAngle.getText().toString();
                if (!sSlopeAngle.matches("") && !sSlopeAngle.matches("-"))
                    mCalculation.setBeta(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorAngle = (EditText)v.findViewById(R.id.Theta);
        if (mCalculation.getTheta() != 0)
            mAnchorAngle.setText(Integer.toString(mCalculation.getTheta()));

        mAnchorAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sAnchorAngle = mAnchorAngle.getText().toString();
                if (!sAnchorAngle.matches("") && !sAnchorAngle.matches("-"))
                     mCalculation.setTheta(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorHeight = (EditText)v.findViewById(R.id.Ha);
        if (mCalculation.getHa() !=0)
            mAnchorHeight.setText(Double.toString(mCalculation.getHa()));

        mAnchorHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sAnchorHeight = mAnchorHeight.getText().toString();
                if (! sAnchorHeight.matches(""))
                    mCalculation.setHa(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorSetback = (EditText)v.findViewById(R.id.La);
        if (mCalculation.getLa() != 0)
            mAnchorSetback.setText(Double.toString(mCalculation.getLa()));

        mAnchorSetback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sAnchorSetback = mAnchorSetback.getText().toString();
                if (! sAnchorSetback.matches(""))
                    mCalculation.setLa(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBladeEmbedment = (EditText)v.findViewById(R.id.Db);
        if (mCalculation.getD_b() != 0)
            mBladeEmbedment.setText(Double.toString(mCalculation.getD_b()));

        mBladeEmbedment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String sBladeEmbedment = mBladeEmbedment.getText().toString();
                if (!sBladeEmbedment.matches(""))
                     mCalculation.setD_b(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCalculateButton = (Button)v.findViewById(R.id.calc_button);
        mAnchorCapacity = (TextView)v.findViewById(R.id.slide_answer);
        mRollOver = (TextView)v.findViewById(R.id.roll_answer);
        mDateButton = (Button)v.findViewById(R.id.calculation_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCalculation.getDate());
                dialog.setTargetFragment(CalculationFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });


        mCalculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double A1 = mCalculation.Alpha1();
                double A2 = mCalculation.Alpha2();
                DecimalFormat df = new DecimalFormat("#.##");

                double Pp =mCalculation.Pp(mCalculation.getVehicle(),mCalculation.getSoil());
                double AnchCap = mCalculation.anchor_capacity(A1,A2,Pp);
                double tipover = mCalculation.tip_over_moment(mCalculation.getVehicle(), Pp);
                mAnchorCapacity.setText(df.format(AnchCap)+"KGf");
                mRollOver.setText(df.format(tipover)+"KGf");

                if (AnchCap <= tipover) {
                    mAnchorCapacity.setTextColor(Color.RED);
                    mAnchorCapacity.setHighlightColor(Color.YELLOW);
                    }
                else {
                    mRollOver.setTextColor(Color.RED);
                    mRollOver.setHighlightColor(Color.YELLOW);
                }

            }

        });

        //Block that provides functionality for the Question Mark Buttons
        //We assign them the correct ID and provide the relevant string and ID for the toast
        HgQuestion = (ImageButton)v.findViewById(R.id.question_hg);
        ToastMaker(R.string.hg_popup,HgQuestion);

        DbQuestion = (ImageButton)v.findViewById(R.id.question_db);
        ToastMaker(R.string.db_popup,DbQuestion);

        WbQueston = (ImageButton)v.findViewById(R.id.question_wb);
        ToastMaker(R.string.wb_popup,WbQueston);

        WvQuestion = (ImageButton)v.findViewById(R.id.question_wv);
        ToastMaker(R.string.wv_popup,WvQuestion);

        LaQuestion = (ImageButton)v.findViewById(R.id.question_la);
        ToastMaker(R.string.la_popup,LaQuestion);

        HaQuestion = (ImageButton)v.findViewById(R.id.question_ha);
        ToastMaker(R.string.ha_popup,HaQuestion);

        CgQuestion = (ImageButton)v.findViewById(R.id.question_cg);
        ToastMaker(R.string.cg_popup,CgQuestion);

        HgQuestion = (ImageButton)v.findViewById(R.id.question_hg);
        ToastMaker(R.string.hg_popup,HgQuestion);

        PhiQuestion = (ImageButton)v.findViewById(R.id.question_fricta);
        ToastMaker(R.string.fricta_popup,PhiQuestion);

        CohesionQuestion = (ImageButton)v.findViewById(R.id.question_cohesion);
        ToastMaker(R.string.cohesion_popup,CohesionQuestion);

        UnitWeightQuestion = (ImageButton)v.findViewById(R.id.question_ws);
        ToastMaker(R.string.ws_popup,UnitWeightQuestion);



        setRetainInstance(true);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCalculation.setDate(date);
            mCallbacks.onCalculationUpdated(mCalculation);
            updateDate();
        }

    }

    // A Simple toast function, displays text by resource Id depending on what imagebutton was clicked
    public void ToastMaker(final int display, ImageButton button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),getResources().getString(display),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        CalculationLab.get(getActivity()).saveCalculations();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
