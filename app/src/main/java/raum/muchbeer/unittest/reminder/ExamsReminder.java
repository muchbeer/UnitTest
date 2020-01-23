package raum.muchbeer.unittest.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExamsReminder extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        String examDay = "Monday";
        String examPerson= "George";
        // TODO: This method is called when the BroadcastReceiver is receiving

        ExamsReminderNotification.notify(context, examDay,examPerson);
    }
}
