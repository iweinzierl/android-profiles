package de.iweinzierl.easyprofiles.widget.recyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.R;

public class CardItemRemoveListener extends ItemTouchHelper.SimpleCallback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(CardItemRemoveListener.class.getName());

    private final RecyclerView recyclerView;
    private final int cardViewDeleteBackgroundColor;
    private final int cardViewDeleteTextColor;
    private final String cardViewDeleteMessage;

    public CardItemRemoveListener(RecyclerView recyclerView, int cardViewDeleteBackgroundColor, int cardViewDeleteTextColor, String cardViewDeleteMessage) {
        super(0, ItemTouchHelper.RIGHT);
        this.recyclerView = recyclerView;
        this.cardViewDeleteBackgroundColor = cardViewDeleteBackgroundColor;
        this.cardViewDeleteTextColor = cardViewDeleteTextColor;
        this.cardViewDeleteMessage = cardViewDeleteMessage;
    }

    @Override
    public boolean onMove(RecyclerView profileListView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (recyclerView.getAdapter() instanceof Removable) {
            Removable removable= (Removable) recyclerView.getAdapter();
            removable.remove(viewHolder);
        } else {
            LOG.warn("RecyclerView's adapter does not implement Removable: {}", recyclerView.getAdapter().getClass());
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;

            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            Paint p = new Paint();
            Bitmap icon = BitmapFactory.decodeResource(
                    recyclerView.getContext().getResources(),
                    R.drawable.ic_delete_forever_white_24dp);

            if (dX > 0) {
                p.setColor(cardViewDeleteBackgroundColor);
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);

                RectF iconDest = new RectF(
                        (float) itemView.getLeft() + width,
                        (float) itemView.getTop() + width,
                        (float) itemView.getLeft() + 2 * width,
                        (float) itemView.getBottom() - width);

                c.drawBitmap(icon, null, iconDest, p);

                Paint textColor = new Paint();
                textColor.setColor(cardViewDeleteTextColor);
                textColor.setTextSize(32);
                c.drawText(
                        cardViewDeleteMessage,
                        iconDest.right + 16,
                        iconDest.top - (iconDest.top - iconDest.bottom) / 2 + 16,
                        textColor);
            }
        }
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
