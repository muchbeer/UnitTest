package raum.muchbeer.unittest.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import raum.muchbeer.unittest.R;

public class ExamsReminderNotification {
    private static final String NOTIFICATION_TAG = ExamsReminderNotification.class.getSimpleName();
private static final int NOTIFICATION_ID = 24;
private static final String CHANNEL_ID = "Google certification";
private static Context mContext;
private static ExamsReminderNotification instance;


    public ExamsReminderNotification(Context mContext) {
        this.mContext = mContext;    }

    public static ExamsReminderNotification getInstance(Context context) {
        if (instance ==null) {
            instance = new ExamsReminderNotification(context);
        }
        return instance;
    }


    public static void notify(final Context context,
                              final String noteTitle, final String noteText) {

        //for later version call the Channel especially start version 24 or 26
        createNotificationChannel();


        Intent notificationClass = new Intent(context, NotificationMain.class);
        notificationClass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , notificationClass,0);

        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                // notification title, and text.
                .setSmallIcon(R.drawable.dog_icon)
                .setContentTitle(noteTitle)
                .setContentText(noteText)
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)
                .setStyle(
                        new NotificationCompat.BigPictureStyle()
                                .bigPicture(picture)
                                .bigLargeIcon(null)
                ).setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);

    }

    private static void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = CHANNEL_ID;
            String description= "Exams must pass";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
