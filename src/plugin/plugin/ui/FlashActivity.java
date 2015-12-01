package plugin.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.common.R;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobo.yuan
 * Date: 12-8-17
 * Time: 上午10:39
 */
public class FlashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final int resId = intent.getIntExtra("resId", R.drawable.default_head_image);
        final long delay = intent.getLongExtra("delay", 0);
        final PendingIntent pendingIntent = intent.getParcelableExtra("pendingIntent");
        setContentView(R.layout.flash_page);
        ImageView imageView = (ImageView)findViewById(R.id.flash_image);
        imageView.setImageResource(resId);

        if(pendingIntent != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        pendingIntent.send();
                        FlashActivity.this.finish();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }, delay);
        }
    }
}
