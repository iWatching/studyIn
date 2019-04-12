package com.example.event;

public class View {

    public String name;

    @Override
    public String toString() {
        return "" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int left;
    private int top;
    private int right;
    private int bottom;

    private OnTouchListener mOnTouchListener;
    private OnClickListener onClickListener;

    public void setOnTouchListener(OnTouchListener mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * @param x
     * @param y
     * @return 是否处于View点击区域内
     */
    public boolean isContainer(int x, int y) {
        if (x >= left && x < right && y >= top && y < bottom) {
            return true;
        }
        return false;
    }

    // 接受分发的代码
    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(name + " dispatchTouchEvent ");
        // 消费
        boolean result = false;
        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, event)) {
            result = true;
        }
        if (!result && onTouchEvent(event)) {
            result = true;
        }

        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        System.out.println(name + " onTouchEvent ");

        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }

}
