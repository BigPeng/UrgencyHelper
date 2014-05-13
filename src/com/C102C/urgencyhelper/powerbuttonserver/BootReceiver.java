package com.C102C.urgencyhelper.powerbuttonserver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent bootActivityIntent = new Intent(context,
					PowerButtonWacther.class);
			bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(bootActivityIntent);
		}

	}

}
