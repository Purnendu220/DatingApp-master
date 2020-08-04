package com.quintus.labs.datingapp.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.User;


/**
 * Grocery App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterGender extends AppCompatActivity {

    String password;
    User user;
   // Boolean male = true;
    private Button genderContinueButton;
    private Button maleSelectionButton;
    private Button otherSelectionButton;
    private Button femaleSelectionButton;
    private int selectedValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        genderContinueButton = findViewById(R.id.genderContinueButton);
        otherSelectionButton = findViewById(R.id.otherSelectionButton);




        otherSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherButtonSelected();
            }
        });
        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
            }
        });

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedValue>-1){
                    openPreferenceEntryPage();

                }else{
                    ToastUtils.show(RegisterGender.this,"Please select your gender");
                }
            }
        });

    }
public void otherButtonSelected(){
    selectedValue = 3;
    toggleView(femaleSelectionButton,otherSelectionButton,maleSelectionButton);

}
    public void maleButtonSelected() {
        selectedValue = 1;
        toggleView(femaleSelectionButton,maleSelectionButton,otherSelectionButton);

    }

    public void femaleButtonSelected() {
        selectedValue = 2;
        toggleView(maleSelectionButton,femaleSelectionButton,otherSelectionButton);

    }
    private void toggleView(Button view,Button view1,Button view2){
        view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        view2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(getResources().getDrawable(R.drawable.background_rounded_border));
            view2.setBackground(getResources().getDrawable(R.drawable.background_rounded_border));

        }
        else{
            view.setBackgroundResource(R.drawable.background_rounded_border);
            view2.setBackgroundResource(R.drawable.background_rounded_border);

        }

        view1.setTextColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view1.setBackground(getResources().getDrawable(R.drawable.white_rounded_button));
        }
        else{
            view1.setBackgroundResource(R.drawable.white_rounded_button);

        }



    }

    public void openPreferenceEntryPage() {
        String ownSex =null ;
        String defaultPhoto = null;

        switch (selectedValue){
             case 1:
                 ownSex = "Male";
                 defaultPhoto = "defaultMale";
                 break;
             case 2:
                 ownSex = "Female";
                 defaultPhoto = "defaultFemale";


                 break;
             case 3:
                 ownSex = "Other";
                 defaultPhoto = "defaultOther";

                 break;

         }
        user.setSex(ownSex);
        //set default photo
        user.setProfileImageUrl(defaultPhoto);

        Intent intent = new Intent(this, RegisterGenderPrefection.class);
        intent.putExtra("password", password);
        intent.putExtra("classUser", user);
        startActivity(intent);
    }
}
