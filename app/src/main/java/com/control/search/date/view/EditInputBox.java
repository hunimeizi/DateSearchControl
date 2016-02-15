package com.control.search.date.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.control.search.date.R;


/**
 * 通用输入Edit<br/>
 * 可自定义是否显示清空按钮
 */
public class EditInputBox extends RelativeLayout {

    /**
     * hint 提示信息
     */
    private String inputHint;

    /**
     * 左边Image
     */
    private Drawable markIcon;

    /**
     * 清空按钮
     */
    private Drawable clearIcon;

    /**
     * 背景
     */
    private int editBgResId;

    private String dis;
    private EditText inputEditText;
    private ImageButton inputClear;
    private View inputBg;

    private InputMethodManager inputMethodManager;
    private OnInputContentChangeListener mContentChangeListener;

    private int textColor;

    private float textSize;

    private boolean isBoolean = false;

    private int textHintColor;

    public EditInputBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditInputBox, 0, 0);
        try {

            DisplayMetrics dm = getResources().getDisplayMetrics();

            textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSize, dm);

            inputHint = a.getString(R.styleable.EditInputBox_inputHint);
            markIcon = a.getDrawable(R.styleable.EditInputBox_leftMarkIcon);
            clearIcon = a.getDrawable(R.styleable.EditInputBox_rightClearIcon);
            editBgResId = a.getResourceId(R.styleable.EditInputBox_editbg, 0);
            dis = a.getString(R.styleable.EditInputBox_digits);
            textColor = a.getColor(R.styleable.EditInputBox_textColor, 0);
            textSize = a.getDimension(R.styleable.EditInputBox_textSize, textSize);
            isBoolean = a.getBoolean(R.styleable.EditInputBox_inputIsPWd, false);
            textHintColor = a.getColor(R.styleable.EditInputBox_inputHintColor, context.getResources().getColor(R.color.white));

        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_input_box, this, true);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public EditInputBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditInputBox(Context context) {
        this(context, null);
    }

    /**
     * 注册内容监听器
     *
     * @param listener 自定义输入框内容监控接口
     */
    public void setOnInputContentChangeListener(OnInputContentChangeListener listener) {
        mContentChangeListener = listener;
    }

    public void setInputEditTextPaddingLeft() {
        inputEditText.setPadding(0, inputEditText.getPaddingTop(), inputEditText.getPaddingRight(), inputEditText.getPaddingBottom());
    }

    /**
     * Interface definition for a callback be invoked when the input content has changed.
     */
    public interface OnInputContentChangeListener {
        /**
         * Will run when the input content has changed.
         *
         * @param inputBox The input {@link EditText} view.
         * @param content  The content input.
         */
        void onContentChanged(View inputBox, Editable content);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inputEditText = (EditText) findViewById(R.id.input_edittext);
        inputEditText.setOnFocusChangeListener(inputFocusChange);
        inputEditText.setOnTouchListener(touchInputBox);
        inputEditText.addTextChangedListener(inputContentChange);
        inputEditText.setHint(inputHint);
        inputEditText.setHintTextColor(textHintColor);
        if (!TextUtils.isEmpty(dis))
            inputEditText.setKeyListener(DigitsKeyListener.getInstance(dis));
        if (markIcon != null) {
            inputEditText.setCompoundDrawablesWithIntrinsicBounds(markIcon, null, null, null);
        }

        inputClear = (ImageButton) findViewById(R.id.input_clear);
        inputClear.setOnClickListener(clearInputContentListener);
        if (clearIcon != null) {
            inputClear.setImageDrawable(clearIcon);
        }

        inputBg = findViewById(R.id.input_bg);
        if (editBgResId > 0) {
            inputBg.setBackgroundResource(editBgResId);
        }

        if (textColor != 0) {
            inputEditText.setTextColor(textColor);
        }

        if (isBoolean) {
            inputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());//将文本框的内容以密码显示
        }
    }

    public void setEnabled(boolean enabled) {
        inputEditText.setEnabled(enabled);
        inputClear.setEnabled(enabled);
    }

    public String getInputContent() {
        return null != inputEditText ? inputEditText.getText().toString() : null;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }

    /**
     * 清空输入框内容
     */
    public void clearInputContent() {
        if (null == inputEditText) {
            return;
        }
        inputEditText.setText(null);
    }

    /**
     * 输入框赋值
     *
     * @param content 显示的内容
     */
    public void setContent(String content) {
        if (null == inputEditText || TextUtils.isEmpty(content)) {
            return;
        }
        inputEditText.setText(content);
        inputEditText.setSelection(content.length());
    }

    public void setBoxBackground(int resid) {
        this.setBackgroundResource(resid);
    }

    /**
     * Set the input type. One of the input type defined in {@link InputType}.
     *
     * @param type must define in InputType class
     */
    public void setInputType(int type) {
        if (inputEditText == null) {
            return;
        }
        inputEditText.setInputType(type);
    }

    public void setInputHint(String inputHint) {
        if (inputEditText == null) {
            return;
        }
        inputEditText.setHint(inputHint);
    }

    /**
     * 设置输入字体大小
     *
     * @param size
     */
    public void setInputEditTextSize(float size) {
        if (size <= 0 || inputEditText == null) {
            return;
        }
        inputEditText.setTextSize(size);
    }

    /**
     * 设置输入框字体颜色
     *
     * @param id 颜色Id
     */
    public void setInputEditTextColor(int id) {
        if (null == inputEditText || id < 0) {
            return;
        }
        ColorStateList colorStateList = getContext().getResources().getColorStateList(id);
        if (null == colorStateList) {
            return;
        }
        inputEditText.setTextColor(colorStateList);
    }

    /**
     * 设置输入框hint字体颜色
     *
     * @param color 颜色Id
     */
    public void setInputEditHintTextColor(int color) {
        if (inputEditText == null || color < 0) {
            return;
        }
        inputEditText.setHintTextColor(color);
    }

    /**
     * 设置允许输入的最大字符长度
     *
     * @param length 最大长度
     */
    public void seInputEdittMaxLength(int length) {
        if (inputEditText == null) {
            return;
        }
        InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(length)};
        inputEditText.setFilters(filters);
    }

    public void setLeftMarkIcon(Drawable markIcon) {
        if (inputEditText == null) {
            return;
        }
        inputEditText.setCompoundDrawablesWithIntrinsicBounds(markIcon, null, null, null);
    }

    public void setLeftMarkIcon(int resId) {
        if (inputEditText == null) {
            return;
        }
        inputEditText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public void setRightClearIcon(Drawable clearIcon) {
        if (inputClear == null) {
            return;
        }
        inputClear.setImageDrawable(clearIcon);
    }

    public void setRightClearIcon(int resId) {
        if (inputClear == null) {
            return;
        }
        inputClear.setImageResource(resId);
    }

    public void showSoftKeyBoard() {
        if (null == inputEditText) {
            return;
        }
        inputEditText.requestFocus();
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(inputEditText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public void hideSoftKeyBoard() {
        if (inputMethodManager != null && inputMethodManager.isActive() && inputEditText != null) {
            inputMethodManager.hideSoftInputFromWindow(inputEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private OnClickListener clearInputContentListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            clearInputContent();
        }
    };

    private OnTouchListener touchInputBox = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            inputEditText.setFocusable(true);
            inputEditText.setFocusableInTouchMode(true);
            return false;
        }
    };

    private OnFocusChangeListener inputFocusChange = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (!TextUtils.isEmpty(inputEditText.getText().toString())) {
                    inputClear.setVisibility(View.VISIBLE);
                }
            } else {
                inputClear.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher inputContentChange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                inputClear.setVisibility(View.GONE);
                inputEditText.setSelected(false);
            } else {
                inputClear.setVisibility(View.VISIBLE);
                inputEditText.setSelected(true);
            }
            if (mContentChangeListener != null) {
                mContentChangeListener.onContentChanged(EditInputBox.this, s);
            }
        }
    };

    public void setInputClearVisibility(int visibility) {
        inputClear.setVisibility(visibility);
    }
}

