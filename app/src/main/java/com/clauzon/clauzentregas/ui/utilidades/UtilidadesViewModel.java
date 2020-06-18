package com.clauzon.clauzentregas.ui.utilidades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UtilidadesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UtilidadesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}