# ViewModelDemo

App Architecture with Jetpack
Part 1: View Model
Part 2: Live Data
Objectives:

    Implement ViewModel to separate data handling from user interface related code
    Use ViewModel to sustain data when configuration is changed
    Utilize LiveData to wrap data in ViewModel and make it observable
    Use LiveData to keep data displayed on the UI up to the value in View Model


Follow Google app architecture guidelines to create a currency converter app. This app allows user to put in dollar amount and click submit button for the app to convert to another currency. This app assumes the conversion rate is 0.74.
In order to reduce compilation error, I upgrade IDE and SDK to the latest version including Gradle plugin.
My current Android Gradle Plugin is 7.3.1
On the IDE toward the bottom you will see a link to “Upgrade Assistant”. Click on it you will find some hints.
You also want to create an emulator that are with API 33 for this lab.
Step 1: Use “Fragment + ViewModel” template to create a project named “ViewModelDemo” Make sure Java is the language and the minimum API level setting to, at least, API 26.
Step 1.1: Run the app to verify your app is fine.
I encourage you to set up your emulator for API 33. At least, for API 32.
On the top of your editor, .click Tools > AGP Upgrade Assistant…, you can upgrade Gradle Plugin if needed.

Part 1: View Model

Step 2: Edit build.gradle(Module: ViewModelDemo.app) under Gradle Scripts. Put thebuildFeature element to enable view binding and click “Sync Now” link at the top of the editor panel.
android {
…
buildFeatures {
viewBinding true
}
…
}
Step 3: View the project
Step 3.1: Open activity_main.xml. In Design mode, you will find there is a container. In Code mode, you find FrameLayout is the placeholder for screens during runtime.
Step 3.2: Since we use Fragment as part of the template, you can find the first fragment is created by opening fragment_main.xml. You will find there is a TextView with text “MainFragment” in ConstraintLayout.
Step 3,3: Open MainActivity.java.You find R.layout.activity_main is the argument for setContentView. Then an instance of MainFragment will be executed in the if statement following the setContentView statement.
Step 3.4: Open MainFragment.java and you will find R.layout.fragment_main will be inflated within the container.
Step 3.5: Since ViewModel is part of the template, you can open MainViewModel.java to find you can implement the ViewModel there later on.
Step 3.6: Run the app to validate what we just said above.
I found that if your environment is not up to date as mentioned above you may face some errors. You can google and find solutions by modifying dependencies in the gradle file. It is strongly recommend to upgrade your system instead.
Step 4: Design the Fragment Layout
Step 4.1: Open fragment_main.xml in Design mode.
Step 4.2: Change the id for the TextView from “message” to “resultText” in the Attributes panel. Accept to Refactor in the pop up dialog.
Step 4.3: Drag a Number(Decimal) under Text in Palette and position it above the TextView. Assign dollarText as its id in the Attributes panel. Accept to Refactor in the pop up dialog. In the Component Tree move dollarText above resultText as the first element in “main”
Step 4.4: Drag a Button onto the layout and position it below the resultText TextView. Assign convertButton as its id and change its text to “Convert” in the Attributes panel.
You need to agree to Refactor at Steps 4.2, 4.3, 4.4.
Pay attention to the Component Tree to make sure dollarText is the first element.
Now make sure you apply your skills to rearrange constraints for the three widgets. For example, you can apply vertical chain to them
Need to resolve any errors shown in the Component Tree before moving on to next step.
You can run your app to see if you like the layout or need to work on it.
Now we have designed the layout/interface. Let’s work on data model for the view model.
Step 5: Implement View Model
Step 5.1: Open MainViewModel.java
Step 5.2: Define variables to store value (i.e., dollarText) and converted amount with getter and setter within the class as follows
private static final Float rate = 0.74F;
private String dollarText = "";
private Float result = 0F;

public void setAmount(String value){
this.dollarText = value;
result = Float.parseFloat(dollarText)*rate;
}

public Float getResult(){
return result;
}

Step 6: Associate Fragment with View Model
This step will allow Fragment to access and observe data change.
Step 6.1: Open MainFragment.java. You will find that Android Studio uses ViewModelProvider to create an instance of ViewModel.
Step 6.2: Modify Fragment to react to button and extract current dollar amount and display converted currency amount
Step 6.2.1: import your project followed by databinding.FragmentMainBinding. Since I keep the default so my code will be:

import com.example.viewmodeldemo.databinding.FragmentMainBinding;


If you change the parameter when you created the project, you need to change it accordingly.

Step 6.2.2: define a local variable for binding class:

private FragmentMainBinding binding;


Step 6.2.3: Change the onCreateView method by comment out the existing return statement and inflate the fragment and get the root.
So the method should look like as follows:

//return inflater.inflate(R.layout.fragment_main, container, false);
binding = FragmentMainBinding.inflate(inflater, container, false);

return binding.getRoot();


Step 6.2.4: Create onDestroyView() for Lifecycle awareness

@Override

public void onDestroyView(){

    super.onDestroyView();

    binding = null;

}


Step 6.2.5: Create onViewCreated() method and define listener for button by putting the following code in onViewCreated() method

@Override

public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



binding.convertButton.setOnClickListener(new View.OnClickListener() {

    @Override

    public void onClick(View view) {

        

    }

});

}


Step 6.2.6: import Locale to support local currency
import java.util.Locale;

Next two steps will focus on accessing  ViewModel data

Step 6.2.7: Populate value to result TextView from ViewModel. Put the statement in onViewCreate() (not in onCreate() or in onCreateView)

binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", mViewModel.getResult()));


Step 6.2.8: React when button is clicked. Put the following  code in onClick()

if (!binding.dollarText.getText().toString().equals("")){

    mViewModel.setAmount(String.format(Locale.ENGLISH, "%s", binding.dollarText.getText()));

    binding.resultText.setText(String.format(Locale.ENGLISH, "%f", mViewModel.getResult()));

} else {

    binding.resultText.setText("No Value");

}


Step 6.3: Test your app by entering a dollar amount in the textview and clicking the button to see the converted amount. Then rotate your emulator to see the data is not lost. Take a screenshot.

Notice: MainFragment.java is listed below for your reference


package com.example.viewmodeldemo.ui.main;



import androidx.lifecycle.ViewModelProvider;



import android.os.Bundle;



import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;



import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;



import com.example.viewmodeldemo.R;



import com.example.viewmodeldemo.databinding.FragmentMainBinding;



import java.util.Locale;



public class MainFragment extends Fragment {



    private MainViewModel mViewModel;

    private FragmentMainBinding binding;



    public static MainFragment newInstance() {

        return new MainFragment();

    }



    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // TODO: Use the ViewModel
     }



    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        super.onViewCreated(view, savedInstanceState);



        binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", mViewModel.getResult()));



        binding.convertButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {



                if (!binding.dollarText.getText().toString().equals("")){

                    mViewModel.setAmount(String.format(Locale.ENGLISH, "%s", binding.dollarText.getText()));

                    binding.resultText.setText(String.format(Locale.ENGLISH, "%f", mViewModel.getResult()));

                } else {

                    binding.resultText.setText("No Value");

                }



            }

        });



    }



    @Nullable

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_main, container, false);
         binding = FragmentMainBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }



    @Override

    public void onDestroyView(){

        super.onDestroyView();

        binding = null;

    }


Part 2: LiveData
Wrap the result and implement observer
Step 7: Wrap result as mutable
Step 7.1: Open MainViewModel.java
Step 7.2: Wrap result as mutable by modifying the code as follows:
Step 7.2.1: Add one additional import statement

import androidx.lifecycle.MutableLiveData;


Step 7.2.2: Change the declaration for result as follows:

private MutableLiveData<Float> result = new MutableLiveData<>();


Step 7.2.3: Change the setter, setAmount by using setValue() as follows

public void setAmount(String value){

     this.dollarText = value;
      result.setValue(Float.parseFloat(dollarText)*rate);

}


Step 7.2.4: Change the return type for the getter, getResult() as follows

public MutableLiveData<Float> getResult(){

    return result;

}


Step 7.3: Implement observer with UI Controller
Step 7.3.1: Open MainFragment.java
Step 7.3.2: Create an Observer instance, resultObserver in onViewCreated() method
Step 7.3.2.1: Add an import statement

import androidx.lifecycle.Observer;



Step 7.3.2.2: In onViewCreated() method create an observer to replace binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", mViewModel.getResult()));

The original idea for that line is to show data for rotation. Now since LiveData is used this line to setText on resultText is not needed. So take it off. And put in the following code:

final Observer<Float> resultObserver = new Observer<Float>(){

    @Override

    public void onChanged(@Nullable final Float result){

        binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", result));

    }

};


When you type Observer you may be asked to import library. That is why I ask you to type in the code and not just copy and paste. By doing that, you might miss something.
Step 7.3.2.3: Add observer to the result LiveData object, a reference obtained by calling getResult(). Put the code following the code defined at Step 7.3.2.2.

mViewModel.getResult().observe(getViewLifecycleOwner(), resultObserver);


Step 7.3.2.4: Since observer will be notified and onChanged() will be executed, there is no need in the onClick() to set the text for resultText.
We just need to set the Amount of ViewModel. So let’s delete the following statement in onClick():
binding.resultText.setText(String.format(Locale.ENGLISH, “%f”, mViewModel.getResult()));

Your onViewCreated() method should look like as follows:


@Override

public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    super.onViewCreated(view, savedInstanceState);
 
     final Observer<Float> resultObserver = new Observer<Float>(){

        @Override

        public void onChanged(@Nullable final Float result){

            binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", result));

        }

    };



    mViewModel.getResult().observe(getViewLifecycleOwner(), resultObserver);



    binding.convertButton.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View view) {



            if (!binding.dollarText.getText().toString().equals("")){

                mViewModel.setAmount(String.format(Locale.ENGLISH, "%s", binding.dollarText.getText()));
             } else {

                binding.resultText.setText("No Value");

            }



        }

    });



}


Step 7.4: Run your app by entering 500 to the dollar amount to get the result. Take a screenshot.
Submission: Submit all screennshots