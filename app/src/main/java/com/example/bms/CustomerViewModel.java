package com.example.bms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CustomerViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Customer>> customers = new MutableLiveData<>();
    public void setCustomers(ArrayList<Customer> customerArrayList) {
        customers.setValue(customerArrayList);
    }
    public LiveData<ArrayList<Customer>> getCustomers() {
        return customers;
    }
}
