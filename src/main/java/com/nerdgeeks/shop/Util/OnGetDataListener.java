package com.nerdgeeks.shop.Util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetDataListener {
    //make new interface for call back
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure(DatabaseError databaseError);
}