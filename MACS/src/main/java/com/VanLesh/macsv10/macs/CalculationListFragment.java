package com.VanLesh.macsv10.macs;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationListFragment extends ListFragment{

    private ArrayList<Calculation> mCalculations;
    private static  final String TAG = "CalculationListFragment";
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.calculations_title);
        mCalculations = CalculationLab.get(getActivity()).getCalculations();

        CalculationAdapter adapter = new CalculationAdapter(mCalculations);

        setListAdapter(adapter);
        setRetainInstance(true);
        mSubtitleVisible = false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Calculation c = ((CalculationAdapter)getListAdapter()).getItem(position);

        //start a CalculationActivity
        Intent i = new Intent(getActivity(), CalculationPagerActivity.class);
        i.putExtra(CalculationFragment.EXTRA_CALCULATION_ID, c.getId());
        startActivity(i);
    }

        private class CalculationAdapter extends ArrayAdapter<Calculation>{
                public CalculationAdapter(ArrayList<Calculation> calculations){
                        super(getActivity(),0,calculations);
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                        //if we dont have a view inflate one
                        if(convertView == null){
                                convertView = getActivity().getLayoutInflater()
                                        .inflate(R.layout.list_item_calculation,null);
                        }

                        //configure view for this calculation
                        Calculation c = getItem(position);

                        TextView titleTextView =
                                (TextView)convertView.findViewById(R.id.calculation_list_item_titleTextView);
                        titleTextView.setText(c.getTitle());
                        TextView dateTextView =
                                (TextView)convertView.findViewById(R.id.calculation_list_item_dateTextView);
                        dateTextView.setText(c.getDate().toString());
                        CheckBox emailCheckBox =
                                (CheckBox)convertView.findViewById(R.id.calculation_list_item_emailCheckBox);
                        emailCheckBox.setChecked(c.isDoemail());

                        return convertView;
                }
        }



    @Override
    public void onResume(){
        super.onResume();
        ((CalculationAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_calc_list,menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_calc:
                Calculation calc = new Calculation();
                CalculationLab.get(getActivity()).addCalculation(calc);
                Intent i = new Intent(getActivity(), CalculationPagerActivity.class);
                i.putExtra(CalculationFragment.EXTRA_CALCULATION_ID, calc.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null){
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible =true;
                    item.setTitle(R.string.hide_subtitle);
                }else{
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible =false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.calc_list_item_context, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        int position =info.position;
        CalculationAdapter adapter = (CalculationAdapter)getListAdapter();
        Calculation calculation = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_calc:
                CalculationLab.get(getActivity()).deleteCalculation(calculation);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = super.onCreateView(inflater,parent,savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (mSubtitleVisible){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return v;
    }

}
