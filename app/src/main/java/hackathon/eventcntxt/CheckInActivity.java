package hackathon.eventcntxt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by donnie on 10/2/16.
 */
public class CheckInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        ActionBar a = getSupportActionBar();
        a.hide();

        setContentView(R.layout.activity_checkin);

        TextView t = (TextView)findViewById(R.id.checkin_message);
        t.setText("Thank you for checking in \n Please show this screen to the attendant");
    }
}
