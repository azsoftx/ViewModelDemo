package com.nain.viewmodeldemo.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Float> result = new MutableLiveData<>();

    private static final Float rate = 0.74F;
    private String dollarText = "";

    public void setAmount(String value){
        this.dollarText = value;
        result.setValue(Float.parseFloat(dollarText)*rate);
    }

    public MutableLiveData<Float> getResult(){
        return result;
    }


}