package com.example.bms;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel{
    private final MutableLiveData<UserEmailPhoneIds> data = new MutableLiveData<>();
    private UserEmailPhoneIds userEmailPhoneIds = new UserEmailPhoneIds();

    public void setData(UserEmailPhoneIds data1) {
        userEmailPhoneIds.addPhoneNumbers(data1.getPhoneNumbers());
        userEmailPhoneIds.addEmails(data1.getEmails());
        userEmailPhoneIds.addUserIds(data1.getUserIds());
        data.setValue(userEmailPhoneIds);
    }

    public MutableLiveData<UserEmailPhoneIds> getUserEmailPhoneIds() {
        return data;
    }

}
