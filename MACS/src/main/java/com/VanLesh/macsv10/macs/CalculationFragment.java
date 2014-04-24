//TODO: Emailing(report)
package com.VanLesh.macsv10.macs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 *
 * Created by samvanryssegem on 2/24/14.
 *
 * This Class in short defines the behavior of the "form" style view.
 * It also defines how the two dialog popups interface back in. I originally planned to make these
 * seperate fragments but that proved to be cumbersome.
 */
public class CalculationFragment extends Fragment{
    Calculation mCalculation;
    Vehicle mVehicle;
    EditText mTitleField;
    EditText mEngineerName;
    EditText mJobsite;

    EditText mSlopeAngle;
    EditText mAnchorAngle;
    EditText mAnchorHeight;
    EditText mAnchorSetback;
    EditText mBladeEmbedment;

    TextView mAnchorCapacity;
    TextView mRollOver;
    Button mDateButton;
    ToggleButton mUnitsButton;
    Callbacks mCallbacks;
    Button mCalculateButton;
    Button mReportButton;

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

    TextView latituteField;
    TextView longitudeField;
    double lat;
    double longi;

    GPSTracker gps;


    String AnswerUnits;
    boolean isimperial =false;
    boolean hasbeencalculated =false;




    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_VEHICLE = 10;

    public static String EXTRA_CALCULATION_ID = "calculationintent.calculation_id";

    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_VEHICLE = "vehicle";

    private ArrayList<Vehicle> mVehicles;
    private ArrayList<Soil> mSoils;


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

        mSoils = SoilLab.get(getActivity()).getSoils();
        staticSoils(mSoils);

        mVehicles = VehicleLab.get(getActivity()).getVehicles();
        staticVehicles(mVehicles);

        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()){
            lat = gps.getLatitude();
            longi=gps.getLongitude();
        }else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    public void updateDate(){
        mDateButton.setText(mCalculation.getDate().toString());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        final View v = inflater.inflate(R.layout.fragment_calculation, parent, false);
        final Dialog soildialog = new Dialog(getActivity());
        final Dialog vehicledialog = new Dialog(getActivity());


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                 getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AnswerUnits= getResources().getString(R.string.metric_answer);

        latituteField = (TextView)v.findViewById(R.id.LatitudeText);
        longitudeField = (TextView)v.findViewById(R.id.LongitudeText);

        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(longi));

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

        mUnitsButton = (ToggleButton)v.findViewById(R.id.toggle_units);
        mUnitsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Unit textfield declarations for main view
                TextView HaUnit = (TextView)v.findViewById(R.id.Ha_unit);
                TextView LaUnit = (TextView)v.findViewById(R.id.La_unit);
                TextView DbUnit = (TextView)v.findViewById(R.id.Db_unit);
                //for the soil dialog

                //for the vehicles dialog

                // if it's checked lets set all of the textview to their imperial counterparts
                if (isChecked) {
                    HaUnit.setText(getResources().getString(R.string.imperial_distance));
                    LaUnit.setText(getResources().getString(R.string.imperial_distance));
                    DbUnit.setText(getResources().getString(R.string.imperial_distance));

                    AnswerUnits= getResources().getString(R.string.imperial_answer);
                    mCalculation.isimperial = true;
                    mCalculation.getVehicle().isimperial=true;
                    mCalculation.getSoil().isimperial=true;
                    hasbeencalculated =false;

                    // default behavior for the program is metric units
                } else {
                    HaUnit.setText(getResources().getString(R.string.metric_distance));
                    LaUnit.setText(getResources().getString(R.string.metric_distance));
                    DbUnit.setText(getResources().getString(R.string.metric_distance));

                    AnswerUnits= getResources().getString(R.string.metric_answer);
                    mCalculation.isimperial =false;
                    mCalculation.getVehicle().isimperial=false;
                    mCalculation.getSoil().isimperial=false;
                    hasbeencalculated =false;

                }
                // invalidating the view forces it to re-render. This will display our changes
                //without the user having to perform an action.
                v.invalidate();
            }
        });

        HaQuestion = (ImageButton)v.findViewById(R.id.question_ha);
        ToastMaker(R.string.ha_popup,HaQuestion,v);

        LaQuestion = (ImageButton)v.findViewById(R.id.question_la);
        ToastMaker(R.string.la_popup,LaQuestion,v);

        DbQuestion = (ImageButton)v.findViewById(R.id.question_db);
        ToastMaker(R.string.db_popup,DbQuestion,v);

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


        final ArrayList<String> vehicleoutputlist = new ArrayList<String>();
        String SingleVehicle;
        for (Vehicle vehicle : mVehicles){

            SingleVehicle = vehicle.getVehicleType();
            if (! vehicleoutputlist.contains(SingleVehicle))
                 vehicleoutputlist.add(SingleVehicle);

        }
        Collections.sort(vehicleoutputlist);

        final Spinner mVehicleSpin = (Spinner)v.findViewById(R.id.vehicle_spinner);
        final ArrayAdapter<String> vadapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,vehicleoutputlist);
        vadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mVehicleSpin.setAdapter(vadapter);

        mVehicleSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                  String check = (String) mVehicleSpin.getSelectedItem();
                  if(check.matches("new")){
                      //custom vehicle dialog

                      vehicledialog.setContentView(R.layout.fragment_vehicle_picker);
                      vehicledialog.setTitle("Enter Vehicle Data");

                      final EditText mVehicleclass;
                      final EditText mVehicletype;
                      final EditText mVehicleHeight;
                      final EditText mCOGDistance;
                      final EditText mWeight;
                      final EditText mTrackL;
                      final EditText mTrackW;
                      final EditText mBladeW;
                      TextView VehicleWeight = (TextView)vehicledialog.findViewById(R.id.VehicleWeight_unit);
                      TextView VehicleTrackLength = (TextView)vehicledialog.findViewById(R.id.VehicleTrackLength_unit);
                      TextView VehicleTrackWidth = (TextView)vehicledialog.findViewById(R.id.VehicleTrackWidth_unit);
                      TextView VehicleCg = (TextView)vehicledialog.findViewById(R.id.VehicleCg_unit);
                      TextView VehicleBladeWidth = (TextView)vehicledialog.findViewById(R.id.VehicleBladeWidth_unit);
                      TextView VehicleHeight = (TextView)vehicledialog.findViewById(R.id.VehicleHeight_unit);


                      if (mUnitsButton.isChecked()){
                          VehicleHeight.setText(getResources().getString(R.string.imperial_distance));
                          VehicleWeight.setText(getResources().getString(R.string.imperial_weight));
                          VehicleCg.setText(getResources().getString(R.string.imperial_distance));
                          VehicleBladeWidth.setText(getResources().getString(R.string.imperial_distance));
                          VehicleTrackLength.setText(getResources().getString(R.string.imperial_distance));
                          VehicleTrackWidth.setText(getResources().getString(R.string.imperial_distance));


                      }else{
                          VehicleHeight.setText(getResources().getString(R.string.metric_distance));
                          VehicleCg.setText(getResources().getString(R.string.metric_distance));
                          VehicleBladeWidth.setText(getResources().getString(R.string.metric_distance));
                          VehicleTrackLength.setText(getResources().getString(R.string.metric_distance));
                          VehicleTrackWidth.setText(getResources().getString(R.string.metric_distance));
                          VehicleWeight.setText(getResources().getString(R.string.metric_weight));



                      }

                      //this unfortunately long block defines all of our EditText fields
                      // each EditText is mapped to it's resource Id, we check if the field needs to be filled
                      // then assign the Calculation's appropriate vehicle parameter if something is entered into
                      //the field
                      mVehicleclass = (EditText)vehicledialog.findViewById(R.id.input_vehicle_class);
                      //mVehicleclass.setText(mCalculation.getVehicle().getVehicleClass());
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

                      mVehicletype = (EditText)vehicledialog.findViewById(R.id.input_vehicle_type);
                      //mVehicletype.setText(mCalculation.getVehicle().getVehicleType());
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

                      mVehicleHeight = (EditText)vehicledialog.findViewById(R.id.input_vehicle_height);
                     // if (mCalculation.getVehicle().getHg() != 0)
                     //     mVehicleHeight.setText(Double.toString(mCalculation.getVehicle().getHg()));

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

                      mCOGDistance = (EditText)vehicledialog.findViewById(R.id.Cg);
                    //  if(mCalculation.getVehicle().getCg() != 0)
                    //      mCOGDistance.setText(Double.toString(mCalculation.getVehicle().getCg()));

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
                      mWeight = (EditText)vehicledialog.findViewById(R.id.Wv);
                    //  if(mCalculation.getVehicle().getWv() != 0)
                    //      mWeight.setText(Double.toString(mCalculation.getVehicle().getWv()));
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
                      mTrackL = (EditText)vehicledialog.findViewById(R.id.Tl);
                    //  if(mCalculation.getVehicle().getTrackL() !=0)
                    //      mTrackL.setText(Double.toString(mCalculation.getVehicle().getTrackL()));
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
                      mTrackW = (EditText)vehicledialog.findViewById(R.id.Tw);
                    //  if(mCalculation.getVehicle().getTrackW() != 0)
                    //      mTrackW.setText(Double.toString(mCalculation.getVehicle().getTrackW()));
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

                      mBladeW =(EditText)vehicledialog.findViewById(R.id.Wb);
                     // if (mCalculation.getVehicle().getBladeW() !=0)
                     //     mBladeW.setText(Double.toString(mCalculation.getVehicle().getBladeW()));
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



                      Button mCancelButton =(Button)vehicledialog.findViewById(R.id.vehicle_cancel);
                      mCancelButton.setOnClickListener(new View.OnClickListener(){

                          public void onClick(View v){
                              mCalculation.setVehicle(null);
                              vehicledialog.dismiss();

                          }

                      });

                      Button mSaveButton = (Button)vehicledialog.findViewById(R.id.vehicle_save);
                      mSaveButton.setOnClickListener(new OnClickListener(){

                          public void onClick(View v){
                              mCalculation.getVehicle().convertToMetric();
                              mVehicles.add(mCalculation.getVehicle());
                              vehicleoutputlist.add(mCalculation.getVehicle().getVehicleType());
                              VehicleLab.get(getActivity()).saveVehicles();
                              vehicledialog.dismiss();

                          }
                      });


                      // this code block defines the "?" buttons
                      //We assign them the correct ID and provide the relevant string and ID for the toast

                      HgQuestion = (ImageButton)vehicledialog.findViewById(R.id.question_hg);
                      ToastMaker(R.string.hg_popup,HgQuestion, v);

                      WbQueston = (ImageButton)vehicledialog.findViewById(R.id.question_wb);
                      ToastMaker(R.string.wb_popup,WbQueston,  v);

                      WvQuestion = (ImageButton)vehicledialog.findViewById(R.id.question_wv);
                      ToastMaker(R.string.wv_popup,WvQuestion, v);

                      CgQuestion = (ImageButton)vehicledialog.findViewById(R.id.question_cg);
                      ToastMaker(R.string.cg_popup,CgQuestion, v);




                      vehicledialog.show();

                    // if a real vehicle is selected we'll loop through our array, match "types"
                    // and set our Calculation's vehicle to that vehicle
                  }else{
                        for (Vehicle checkvehicle : mVehicles){
                            if(check.matches(checkvehicle.getVehicleType())){
                                mCalculation.setVehicle(checkvehicle);

                            }
                        }

                      }

               }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {}

        });


        final ArrayList<String> SoilOutputList = new ArrayList<String>();
        String SingleSoil="";
        for (int i = 0; i <mSoils.size(); i++){
            Soil soil = mSoils.get(i);
            SingleSoil = soil.getName();
            if (! SoilOutputList.contains(SingleSoil))
                SoilOutputList.add(SingleSoil);
        }
        Collections.sort(SoilOutputList);

        final Spinner mSoilSpin = (Spinner)v.findViewById(R.id.soil_spinner);
        final ArrayAdapter<String> sadapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,SoilOutputList);
        sadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSoilSpin.setAdapter(sadapter);

        mSoilSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l){
                String check = (String)mSoilSpin.getSelectedItem();
                if(check.matches("new")) {
                    //custom soil dialog launches on new selection

                    soildialog.setContentView(R.layout.fragment_soil_picker);
                    soildialog.setTitle("Enter Soil Data");

                    final EditText mSoilType;
                    final EditText mSoilUnitWeight;
                    final EditText mSoilFrictionAngle;
                    final EditText mSoilCohesionFactor;

                    TextView SoilWtUnit = (TextView)soildialog.findViewById(R.id.SoilWt_unit);
                    TextView SoilCUnit = (TextView)soildialog.findViewById(R.id.Soilc_unit);

                    if (mUnitsButton.isChecked()){
                        SoilWtUnit.setText(getResources().getString(R.string.imperial_soilunitwt));
                        SoilCUnit.setText(getResources().getString(R.string.imperial_soilc));

                        //mCalculation.getSoil().isimperial = true;


                    }else{
                        SoilWtUnit.setText(getResources().getString(R.string.metric_soilunitwt));
                        SoilCUnit.setText(getResources().getString(R.string.metric_soilc));

                        //mCalculation.getSoil().isimperial =false;

                    }


                    mSoilType = (EditText)soildialog.findViewById(R.id.SoilName);
                //    mSoilType.setText(mCalculation.getSoil().getName());
                    mSoilType.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                            mCalculation.getSoil().setName(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    mSoilUnitWeight = (EditText)soildialog.findViewById(R.id.Soilunitwt);
                 //   if (mCalculation.getSoil().getunitW() != 0)
                 //       mSoilUnitWeight.setText(Double.toString(mCalculation.getSoil().getunitW()));
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

                    mSoilCohesionFactor = (EditText)soildialog.findViewById(R.id.Soilcohesion);
                //    if (mCalculation.getSoil().getC() !=0 )
                //        mSoilCohesionFactor.setText(Double.toString(mCalculation.getSoil().getC()));
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

                    mSoilFrictionAngle = (EditText)soildialog.findViewById(R.id.Soilfricta);
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

                    Button mCancelButton =(Button)soildialog.findViewById(R.id.soil_cancel);
                    mCancelButton.setOnClickListener(new View.OnClickListener(){

                        public void onClick(View v){
                            mCalculation.setSoil(null);
                            soildialog.dismiss();

                        }

                    });

                    Button mSaveButton = (Button)soildialog.findViewById(R.id.soil_save);
                    mSaveButton.setOnClickListener(new OnClickListener(){

                        public void onClick(View v){
                            mCalculation.getSoil().convertToMetric();
                            mSoils.add(mCalculation.getSoil());
                            SoilOutputList.add(mCalculation.getSoil().getName());
                            SoilLab.get(getActivity()).saveSoils();

                            soildialog.dismiss();

                        }
                    });

                    PhiQuestion = (ImageButton)soildialog.findViewById(R.id.question_fricta);
                    ToastMaker(R.string.fricta_popup,PhiQuestion,v);

                    CohesionQuestion = (ImageButton)soildialog.findViewById(R.id.question_cohesion);
                    ToastMaker(R.string.cohesion_popup,CohesionQuestion,v);

                    UnitWeightQuestion = (ImageButton)soildialog.findViewById(R.id.question_ws);
                    ToastMaker(R.string.ws_popup,UnitWeightQuestion,v);

                soildialog.show();

                }else{
                    for (int j=0; j<mSoils.size(); j++){
                        Soil checksoil = mSoils.get(j);
                        if(check.matches(checksoil.getName())){
                            mCalculation.setSoil(checksoil);

                        }
                    }
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        mCalculateButton = (Button)v.findViewById(R.id.calc_button);
        mCalculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                DecimalFormat df = new DecimalFormat("#.##");
                if (!hasbeencalculated) {

                    mCalculation.imperialconversion();
                    double AnchCap = mCalculation.anchor_capacity(isimperial);
                    double tipover = mCalculation.tip_over_moment(isimperial);
                    mAnchorCapacity.setText(df.format(AnchCap) +"     "+ AnswerUnits);
                    mRollOver.setText(df.format(tipover) +"     " +AnswerUnits);

                    if (AnchCap <= tipover) {
                        mAnchorCapacity.setTextColor(Color.RED);
                        mAnchorCapacity.setHighlightColor(Color.YELLOW);
                    } else {
                        mRollOver.setTextColor(Color.RED);
                        mRollOver.setHighlightColor(Color.YELLOW);
                    }
                    //hasbeencalculated = true;

                }

                }

        });

        mReportButton = (Button)v.findViewById(R.id.report_button);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Pdf.maker(mCalculation);
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                     new String[]{});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MACS Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This email sent by the MACS App");
                // Here my file name is shortcuts.pdf which i have stored in /res/raw folder
                Uri emailUri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath()+ "/Report.pdf");
                emailIntent.putExtra(Intent.EXTRA_STREAM, emailUri);
                emailIntent.setType("application/pdf");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            }
        });

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
    public void ToastMaker(final int display, ImageButton button, View view){
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
        VehicleLab.get(getActivity()).saveVehicles();

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


    private void staticVehicles(ArrayList<Vehicle> v){

        Vehicle ex1 = new Vehicle();
        ex1.setVehicleClass("Bulldozer");
        ex1.setType("Caterpillar D8");
        ex1.setWv(36199.93);
        ex1.setBladeW(3.255);
        ex1.setCg(3.51);
        ex1.setTrackL(3.255);
        ex1.setTrackW(0.62);

        Vehicle ex2 = new Vehicle();
        ex2.setVehicleClass("Excavator");
        ex2.setType("Caterpillar 320C");
        ex2.setWv(21821.93);
        ex2.setBladeW(1.55);
        ex2.setCg(7.75);
        ex2.setTrackL(3.72);
        ex2.setTrackW(0.62);

        Vehicle newbie = new Vehicle();
        newbie.setType("new");

        v.add(ex1);
        v.add(ex2);
        v.add(newbie);

    }

    private void staticSoils(ArrayList<Soil> s){

        Soil a = new Soil();
        a.setC(0);
        a.setfrictA(25);
        a.setunitW(1521.75);
        a.setName("Uncompacted, Loose Silt");

        Soil b = new Soil();
        b.setC(0);
        b.setfrictA(25);
        b.setunitW(1521.75);
        b.setName("Uncompacted, Loose Sand");

        Soil c = new Soil();
        c.setC(0);
        c.setfrictA(25);
        c.setunitW(1521.75);
        c.setName("Uncompacted, Loose Gravel");

        Soil newbie = new Soil();
        newbie.setName("new");

        if (! s.contains(a))
            s.add(a);
        if (! s.contains(b))
            s.add(b);
        if (! s.contains(c))
             s.add(c);
        if (!s.contains(newbie))
            s.add(newbie);


    }

}
