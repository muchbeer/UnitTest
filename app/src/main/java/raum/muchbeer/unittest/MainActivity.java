package raum.muchbeer.unittest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import raum.muchbeer.unittest.reminder.ExamsReminder;

public class MainActivity extends AppCompatActivity {

    private static final String EXAM_DAY = "exam_day_key";
    private static final String EXAM_PERSON = "exam_person_key" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reminder:

                Intent intent = new Intent(this, ExamsReminder.class);

                intent.putExtra(EXAM_DAY, "Monday");
                intent.putExtra(EXAM_PERSON, "Geroge");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long alarmcurrentTime = SystemClock.elapsedRealtime();
                long HOUR = 60*60*1000;
                long THIRTY_SECOND=30*1000;

                long requiredtime = alarmcurrentTime+THIRTY_SECOND;
                alarmManager.set(AlarmManager.RTC, requiredtime, pendingIntent);
                    return  true;
            default :
                return super.onOptionsItemSelected(item);

        }

    }
}
