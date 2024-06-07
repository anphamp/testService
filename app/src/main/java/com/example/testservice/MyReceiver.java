package com.example.testservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;

public class MyReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        int str = intent.getIntExtra("action",0);
        Intent intentService = new Intent(context ,MyService.class);
        intent.putExtra("action_service",str);
        context.startService(intentService);
    }
}
