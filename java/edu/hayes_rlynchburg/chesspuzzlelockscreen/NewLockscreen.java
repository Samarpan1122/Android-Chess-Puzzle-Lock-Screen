package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Hayes on 10/9/2016.
 */
public class NewLockscreen extends AppCompatActivity {
    private static final String TAG = "TAG";

    private View layoutOfLS_;

    // Parameters of the layout of the lockscreen
    public static WindowManager.LayoutParams layoutParamsOfLS_;

    // Scrollable list of notifications
    public static ScrollView scrollView_;

    // EditText used to input password on lockscreen
    public static EditText ed_;

    public static int MAX_MOVES = 3;

    public static GridView gridView_;

    // Linear layout of notifications
    public static LinearLayout listOfNotifications_;

    public static WindowManager windowManager_;
    public static RelativeLayout relativeLayout_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Protolock onCreate Start");
        // Lockscreen is active
        // Save access to this activity
        LS_Service.lockscreenActivity_ = this;

        initializeChessLayout();
        if (LS_Service.showNotifications_ && LS_Service.notifications_ != null)
            showNotifications();

        windowManager_.addView(relativeLayout_, layoutParamsOfLS_);

        setContentView(R.layout.activity_chess_lockscreen);

        Log.d(TAG, "Protolock onCreate end");

        this.startService(new Intent(this, LS_Service.class));
    }

    private void initializeNumericLockscreen(int layout) {
        relativeLayout_.removeAllViewsInLayout();

        setContentView(layout);

        // Get an inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the layout
        layoutOfLS_ = inflater.inflate(layout, relativeLayout_);

        // Get EditText
        ed_ = (EditText) layoutOfLS_.findViewById(R.id.editText);

        // Get ScrollView
        scrollView_ = (ScrollView) layoutOfLS_.findViewById(R.id.scrollView);

        listOfNotifications_ = new LinearLayout(this);
        listOfNotifications_.setOrientation(LinearLayout.VERTICAL);
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private void initializeChessLayout() {
        // Alert window disables the home button
        layoutParamsOfLS_ = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY); // Use TYPE_APPLICATION_OVERLAY for modern Android versions
        windowManager_ = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
        relativeLayout_ = new RelativeLayout(getBaseContext());

        // Add the parameters to the window
        getWindow().setAttributes(layoutParamsOfLS_);

        // Get an inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        Log.d("STATE", "The layout is: " + LS_Service.initialLayout_);

        setContentView(R.layout.activity_chess_lockscreen);

        // Inflate the layout
        layoutOfLS_ = inflater.inflate(R.layout.activity_chess_lockscreen, relativeLayout_);

        gridView_ = (GridView) layoutOfLS_.findViewById(R.id.gridview);
        final Board updater = new Board(this);
        readPuzzles();
        if (LS_Service.initialLayout_ != null) {
            updater.placePieces(LS_Service.initialLayout_);
            gridView_.setAdapter(updater);

            // Get EditText
            ed_ = (EditText) layoutOfLS_.findViewById(R.id.editText);

            // Get ScrollView
            scrollView_ = (ScrollView) layoutOfLS_.findViewById(R.id.scrollView);

            listOfNotifications_ = new LinearLayout(this);
            listOfNotifications_.setOrientation(LinearLayout.VERTICAL);

            gridView_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                int lastTile = -1;
                int moves = 0;
                Tile.Color currentColor = Tile.Color.WHITE;

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (lastTile != -1
                            && (!updater.board_[position].isOccupied()
                            || !updater.board_[position].getColor().equals(updater.board_[lastTile].getColor()))
                            && updater.board_[lastTile].getColor() == currentColor) {
                        if (updater.board_[lastTile].isValid(position)) {
                            Context cxt = getApplication();

                            updater.swap(lastTile, position, cxt);

                            if (Board.promotionCandidates[position] && Board.board_[position].getName().equals("pawn")) {
                                Board.board_[position].pawnPromote(position, NewLockscreen.this);
                            }

                            gridView_.setAdapter(updater);

                            currentColor = currentColor.equals(Tile.Color.WHITE) ? Tile.Color.BLACK : Tile.Color.WHITE;

                            Log.d("STATE", "The correct answer is: " + LS_Service.finalLayout_);
                            if (Board.evaluate(LS_Service.finalLayout_)) {
                                Toast.makeText(NewLockscreen.this, "WIN!!!", Toast.LENGTH_SHORT).show();
                                NewLockscreen.this.finish();
                            } else if ((MAX_MOVES - moves != 1)) {
                                updater.clear();
                                updater.placePieces(LS_Service.initialLayout_);
                                gridView_.setAdapter(updater);
                                currentColor = Tile.Color.WHITE;
                                moves++;
                                ed_.setText("Wrong move. You have " + (MAX_MOVES - moves) + " attempts left");
                            } else {
                                initializeNumericLockscreen(R.layout.activity_numeric_lockscreen);
                            }
                        } else {
                            ed_.setText("Illegal Move. Try Again");
                        }
                        lastTile = -1;
                    } else {
                        lastTile = position;
                    }
                }
            });
        } else {
            initializeNumericLockscreen(R.layout.activity_no_chess_puzzles_lockscreen);
            TextView box = (TextView) findViewById(R.id.challenge);
            box.setText("Waiting for Puzzles");
        }
    }

    void showNotifications() {
        // Convert Notifications into views to be displayed
        for (int i = 0; i < NotificationListener.numberOfNotifications; ++i) {
            RemoteViews remoteViews = LS_Service.notifications_[i].contentView;

            View notification = remoteViews.apply(LS_Service.lockscreenActivity_, NewLockscreen.scrollView_);

            notification.setClickable(true);
            notification.setId(View.generateViewId());
            notification.setOnClickListener(NewLockscreen.notifClicker);
            NewLockscreen.listOfNotifications_.addView(notification, i, NewLockscreen.layoutParamsOfLS_);
        }
        // Add the list of notifications onto the scroll view
        scrollView_.addView(listOfNotifications_);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Protoclock destroy");
        // Clean up
        relativeLayout_.removeAllViewsInLayout();
        windowManager_.removeView(relativeLayout_);
        super.onDestroy();
    }

    public void buttonOnClick(View view) {
        // If the unlock button was pressed
        if (view.getId() == R.id.unlock) {
            Log.d(TAG, "Protolock button pressed");

            Log.d(TAG, "Entered Password: " + ed_.getText().toString());
            Log.d(TAG, "Actual Password: " + LS_Service.password_);
            if (LS_Service.password_.equals(ed_.getText().toString())) {
                // Unlock phone
                Log.d(TAG, "Phone Unlocking");
                NewLockscreen.this.finish();
            }
        }

        // If the user hit the emergency button
        if (view.getId() == R.id.emergency) {
            // Open the emergency dialer
            Log.d(TAG, "Emergency clicked");
            Intent emergency = new Intent("android.intent.action.MAIN");
            emergency.setComponent(ComponentName.unflattenFromString("com.android.phone/.EmergencyDialer"));
            emergency.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(emergency);
            NewLockscreen.this.finish();
        }
    }

    public void readPuzzles() {
        try {
            File file = new File(this.getFilesDir(), LS_Service.puzzlesFileName_);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            // Do reading, usually loop until end of file reading
            String mLine = reader.readLine();

            int currentIndex = Integer.parseInt(mLine);
            boolean hasRead = false;
            List<String> fileLines = new ArrayList<>();
            fileLines.add(mLine);
            mLine = reader.readLine();
            fileLines.add(mLine); // Add amount of puzzles to file lines

            int counter = 2;

            while (mLine != null) { // While we haven't read through the file
                if (counter == currentIndex && !hasRead) { // And we haven't read in init and final layout
                    mLine = reader.readLine();
                    fileLines.add(mLine);
                    LS_Service.initialLayout_ = mLine; // Process line
                    mLine = reader.readLine();
                    fileLines.add(mLine);
                    LS_Service.finalLayout_ = mLine;
                    currentIndex += 2;
                    hasRead = true;
                }
                counter++;
                mLine = reader.readLine();
                fileLines.add(mLine);
            }

            reader.close();

            if (hasRead) {
                String newLine = "\n";
                fileLines.set(0, Integer.toString(currentIndex));

                FileOutputStream fos = new FileOutputStream(new File(this.getFilesDir(), LS_Service.puzzlesFileName_));

                for (String line : fileLines) {
                    Log.d(TAG, "readPuzzles: " + line);
                    if (line != null) {
                        fos.write(line.getBytes());
                        fos.write(newLine.getBytes());
                    }
                }
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LS_Service.alarmAlert_ = false;
    }

    // Disables the back button
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Protolock back pressed");
        return;
    }

    // This handles what happens when a notification was hit
    public static View.OnClickListener notifClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get a list of all notifications
            ArrayList<View> items = listOfNotifications_.getTouchables();

            int i = 0;
            // Find the index location for where the view is in the list of all views
            while (i < items.size() && items.get(i).getId() != v.getId()) {
                ++i;
            }
            try {
                // Launch the application that view is attached to from the list of all notifications
                LS_Service.notifications_[i].contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.e(TAG, "Notification click failed", e);
            }
        }
    };
}