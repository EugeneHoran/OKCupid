package exercise.okcupid.com.util;


import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class BindingUtil {
    /**
     * Load Image into ImageView with Glide
     *
     * @param imageView      View - ImageView
     * @param imageReference Sting - Image Url
     */
    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView imageView, String imageReference) {
        if (TextUtils.isEmpty(imageReference)) return;
        Glide.with(imageView.getContext())
                .load(imageReference)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
