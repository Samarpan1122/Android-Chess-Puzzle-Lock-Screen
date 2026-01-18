package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/*
 * This class allows the user to enter a password and determine if he/she wants
 * to see notifications. These settings are encrypted and saved to a file.
 * When a new password is selected, the Mastermind class will generate challenge categories.
 * Those challenge categories will then be saved to a file from here.
 */

public class Settings extends Activity {
    CheckBox checkBox;
    private static final String TAG = "TAG";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "448AFBF228EC9AZX"; // Ensure this key is securely managed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Save the checkBox
        checkBox = findViewById(R.id.checkBox);
        if (LS_Service.showNotifications_) {
            checkBox.setChecked(true);
        }
        Log.d(TAG, "Settings onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent lockscreenActivity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // When the user changes the checkbox check
    public void onCheck(View view) {
        if (checkBox.isChecked()) {
            LS_Service.showNotifications_ = true;
            // Go to where the setting needed to do this is located
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            Log.d(TAG, "Notifications are on");
        } else {
            Log.d(TAG, "Notifications are off");
            LS_Service.showNotifications_ = false;
        }

        // Update file
        updateFile();
    }

    // Saves all settings to file
    private void updateFile() {
        File file = new File(this.getFilesDir(), "appData");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] pw = LS_Service.password_.getBytes(StandardCharsets.UTF_8);
            byte[] buffer = new byte[pw.length + 1];

            // Put the password bytes in the buffer
            System.arraycopy(pw, 0, buffer, 0, pw.length);

            // Put the show notifications byte at the end of the buffer
            buffer[buffer.length - 1] = (byte) (LS_Service.showNotifications_ ? 0 : 1);

            try {
                // Get a cipher
                Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

                // Put the secret key into the this class
                Key secretKeySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM);

                // Initialize the cipher
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

                // Encrypt the data
                byte[] encryptedData = cipher.doFinal(buffer);

                // Write it to file
                fos.write(encryptedData);

            } catch (Exception e) {
                Log.e(TAG, "Encryption error", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "File not created", e);
        }
    }

    // This is called when the user hits the set password button
    public void onSetPassword(View view) {
        // Get the password from the text box
        EditText editText = findViewById(R.id.editText);
        String pw = editText.getText().toString();
        // Make sure it is acceptable
        if (pw.length() == 4) {
            // Save the password
            LS_Service.password_ = pw;
            editText.setText("");

            // Update the password file
            updateFile();

            // Save the challenge codes
            Settings.this.finish();

            Log.d(TAG, LS_Service.password_);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}