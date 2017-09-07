package com.uma.umar.ui.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uma.umar.R;

public class UmARSearchView extends RelativeLayout implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {

    private EditText mEditText;
    private ImageView mRightImageView;

    private SearchInteractionListener mListener;

    private int mCurrentImageResId = R.mipmap.ic_search_icon;
    private int mEmptyImageResId = R.mipmap.ic_search_icon;

    public UmARSearchView(Context context) {
        super(context);
        init(context);
    }

    public UmARSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UmARSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.umar_search_view, this);
        mEditText = (EditText) findViewById(R.id.input_edittext);
        mRightImageView = (ImageView) findViewById(R.id.right_icon_imageview);

        mEditText.addTextChangedListener(this);
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnClickListener(this);
        mRightImageView.setOnClickListener(this);
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.toString().trim().isEmpty()) {
            //Don't allow the user to start a search with spaces,
            s.clear();
        }

        String textTrim = s.toString().trim();

        if (textTrim.isEmpty()) {
            flipImageTo(mEmptyImageResId);
            if (mListener != null) mListener.onSearchCleared();
        } else {
            flipImageTo(R.mipmap.ic_search_cross);
            if (textTrim.length() >= 1 && s.length() > 0) {
                if (mListener != null) mListener.onInitiateSearch(s.toString(), false);
            } else {
                if (mListener != null) mListener.onSearchCleared();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_icon_imageview) {
            onIconClicked();
        } else if (v.getId() == R.id.input_edittext) {
            onEditTextClicked();
        }
    }

    public void onEditTextClicked() {
        if (!isEditing())
            if (mListener != null) mListener.onTouch(false);
    }

    public void onIconClicked() {
        if (mListener != null) mListener.onTouch(isEditing());
        mEditText.setText("");
        flipImageTo(mEmptyImageResId);
    }

    public void setInteractionListener(SearchInteractionListener listener) {
        this.mListener = listener;
    }

    public void removeInteractionListener() {
        mListener = null;
    }

    public boolean isEditing() {
        return mEditText.getText().toString().length() > 0;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NEXT) {
            String searchText = mEditText.getText().toString();
            String searchTextTrim = searchText.trim();
            if (searchTextTrim.length() > 0) {
                if (mListener != null)
                    mListener.onInitiateSearch(mEditText.getText().toString(), true);
            } else {
                if (mListener != null) mListener.onInitiateSearch(null, true);
            }
        }
        return true;
    }

    private void flipImageTo(final int imageResourceId) {
        if (mCurrentImageResId == imageResourceId) return;
        mCurrentImageResId = imageResourceId;

        mRightImageView.animate().scaleX(0).setDuration(150).withEndAction(new Runnable() {
            @Override
            public void run() {
                mRightImageView.setImageResource(imageResourceId);
                mRightImageView.animate().scaleX(1).setDuration(150);
            }
        });
    }

    public void reset() {
        if (!mEditText.getText().toString().isEmpty()) {
            mEditText.setText("");
        }
    }

    /**
     * Set the icon that is displayed on the right of the search container
     * when there is no input
     *
     * @param emptyImageResId
     */
    public void setEmptyStateDrawable(int emptyImageResId) {
        this.mEmptyImageResId = emptyImageResId;
        mRightImageView.setImageResource(emptyImageResId);
    }

    public interface SearchInteractionListener {
        void onInitiateSearch(String like, boolean onEnterPressed);

        void onSearchCleared();

        void onTouch(boolean clickOnCross);
    }
}
