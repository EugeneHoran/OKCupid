package exercise.okcupid.com.image.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.ActivityImageViewBinding;
import exercise.okcupid.com.util.BindingUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class ImageViewActivity extends AppCompatActivity {
    public static final String ARG_IMAGE_URL = "arg_image_url";
    public String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            imageUrl = getIntent().getStringExtra(ARG_IMAGE_URL);
        }
        supportPostponeEnterTransition();
        ActivityImageViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_view);
        binding.setActivity(this);
        supportStartPostponedEnterTransition();
    }

    public Function0<Unit> function = () -> {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
        return null;
    };

    public View.OnClickListener listener = v -> supportFinishAfterTransition();
}
