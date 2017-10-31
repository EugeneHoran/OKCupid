package exercise.okcupid.com.util;


import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import exercise.okcupid.com.R;

public class BindingUtil {
    /**
     * Load Image into ImageView with Glide
     *
     * @param imageView      View - ImageView
     * @param imageReference Sting - Image Url
     */
    @BindingAdapter("bind:loadImage")
    public static void loadCircleImage(ImageView imageView, String imageReference) {
        if (TextUtils.isEmpty(imageReference)) return;
        Glide.with(imageView.getContext())
                .load(imageReference)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * Set CardView background color dependent on isLiked
     *
     * @param cardView View - CardView
     * @param isLiked  boolean - is the user liked
     */
    @BindingAdapter("bind:setCardBackgroundColor")
    public static void setCardBackgroundColor(CardView cardView, boolean isLiked) {
        int white = ContextCompat.getColor(cardView.getContext(), R.color.white);
        int yellow = ContextCompat.getColor(cardView.getContext(), R.color.yellow);
        cardView.setCardBackgroundColor(isLiked ? yellow : white);
    }

    /**
     * Set RecyclerView item decorator
     *
     * @param recycler View - RecyclerView
     * @param span     int - Span Count
     */
    @BindingAdapter("bind:setItemDecorator")
    @SuppressWarnings("all") // Span is always 2 but requires second val for xml
    public static void setRecyclerItemDecorator(RecyclerView recycler, int span) {
        Resources res = recycler.getContext().getResources();
        recycler.addItemDecoration(new ItemDecorationSearchColumns(res.getDimensionPixelSize(R.dimen.space_8), span));
    }
}
