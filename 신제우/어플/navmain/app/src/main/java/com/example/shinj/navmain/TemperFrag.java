package com.example.shinj.navmain;
//
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
public class TemperFrag extends Fragment {
//
//    private Spinner tempSpinner1, tempSpinner2, phSpinner1, phSpinner2;
//    private String tempNnumber, phNumber;
//
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);
//
//        tempSpinner1 = (Spinner)getActivity().findViewById(R.id.tempRange1);
//        tempSpinner2 = (Spinner)getActivity().findViewById(R.id.tempRange2);
//        phSpinner1 = (Spinner)getActivity().findViewById(R.id.pHRange1);
//        phSpinner2 = (Spinner)getActivity().findViewById(R.id.pHRange2);
//
//        final ArrayList<String> tempList = new ArrayList<>(); // 0도부터 40도까지 온도 리스트
//        final ArrayList<String> phList = new ArrayList<>(); // 0도부터 40도까지 ph 리스트
//
//        // 스피너에 온도 설정 범위를 0도부터 40도까지 설정
//        for(int i=0; i<41; i++) {
//            tempNnumber = String.valueOf(i);
//            tempList.add(tempNnumber);
//        }
//
//        // 스피너에 ph 범위를 1.0~14.0 까지 설정
//        for(double i=1.0; i<=14.0; i=i+0.1) {
//            phNumber = String.format("%.1f",i);
//            phList.add(phNumber);
//        }
//
//        ArrayAdapter<String> spinnerAdapter_temp = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.support_simple_spinner_dropdown_item,
//                tempList
//        );
//        ArrayAdapter<String> spinnerAdapter_ph = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.support_simple_spinner_dropdown_item,
//                phList
//        );
//        tempSpinner1.setAdapter(spinnerAdapter_temp);
//        tempSpinner2.setAdapter(spinnerAdapter_temp);
//        phSpinner1.setAdapter(spinnerAdapter_ph);
//        phSpinner2.setAdapter(spinnerAdapter_ph);
//
//        tempSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        tempSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        phSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        phSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        Button btn_saveTemper = (Button)getView().findViewById(R.id.temperSaveButton);
//        btn_saveTemper.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(getActivity(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
////        public void saveTemperButton(View v) {
////            Toast.makeText(getActivity(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
////            return view;
////        }

        return view;
    }
}