package com.example.bms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CustomerViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Customer>> customers = new MutableLiveData<>();
    private final MutableLiveData<Customer> customerMutableLiveData = new MutableLiveData<>();
    public void setCustomers(ArrayList<Customer> customerArrayList) {
        customers.setValue(customerArrayList);
    }

    public void setCustomer(Customer customer){
        customerMutableLiveData.setValue(customer);
    }
    public LiveData<ArrayList<Customer>> getCustomers() {
        return customers;
    }

    public LiveData<Customer> getCustomer() { return customerMutableLiveData; }

}
