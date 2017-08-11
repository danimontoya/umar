package com.uma.umar.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uma.umar.R;
import com.uma.umar.utils.FirebaseConstants;

/**
 * Created by danieh on 8/10/17.
 */

public class LanguageFragment extends AppCompatDialogFragment implements RadioGroup.OnCheckedChangeListener {

    public static final String TAG = "LanguageFragment";

    private static final String LANG_CODE = "langCode";
    private String mLangCode;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioSpanish;
    private RadioButton mRadioEnglish;
    private LanguageListener mListener;

    public LanguageFragment() {
    }

    public static LanguageFragment newInstance(String langCode) {
        Bundle args = new Bundle();
        args.putString(LANG_CODE, langCode);
        LanguageFragment fragment = new LanguageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_layout, container, false);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        mRadioSpanish = (RadioButton) v.findViewById(R.id.radioButtonEs);
        mRadioEnglish = (RadioButton) v.findViewById(R.id.radioButtonEn);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLangCode = getArguments().getString(LANG_CODE);
        mRadioSpanish.setChecked(FirebaseConstants.LANGUAGE_ES.equals(mLangCode));
        mRadioEnglish.setChecked(FirebaseConstants.LANGUAGE_EN.equals(mLangCode));
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    public void setListener(LanguageListener listener) {
        mListener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        dismiss();
        if (mListener != null) {
            if (i == mRadioSpanish.getId()) {
                mListener.onLanguageSelected(FirebaseConstants.LANGUAGE_ES);
            } else {
                mListener.onLanguageSelected(FirebaseConstants.LANGUAGE_EN);
            }
        }
    }

    public interface LanguageListener {
        void onLanguageSelected(String langCode);
    }
}
