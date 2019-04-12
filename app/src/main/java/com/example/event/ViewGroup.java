package com.example.event;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {

    // 子View个数
    private View[] mChildren = new View[0];

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    List<View> childList = new ArrayList<>();


    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    private TouchTarget mFirstTouchTarget;

    // 事件分发的入口
    public boolean dispatchTouchEvent(MotionEvent event) {
        //
        System.out.println(name + " dispatchTouchEvent ");

        boolean handled = false;
        boolean intercepted = onInterceptTouchEvent(event);

        // TouchTarget  模式 内存缓存   move up
        TouchTarget newTouchTarget = null;
        int actionMasked = event.getActionMasked();

        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted) {
            // 不拦截情况下，开始处理Down事件
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                final View[] children = mChildren;
                //  遍历ViewGroup中所有子View
                for (int i = children.length - 1; i >= 0; i--) {
                    View child = mChildren[i];
                    // View能够接收到事件
                    if (!child.isContainer(event.getX(), event.getY())) {
                        continue;
                    }
                    // 能够接受事件  child   分发给他
                    if (dispatchTransformedTouchEvent(event, child)) {
                        // View[]  采取了 Message 的方式进行  链表结构
                        handled = true;
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }
                }
            }
            // 当前的ViewGroup  dispatchTransformedTouchEvent
            if (mFirstTouchTarget == null) {
                handled = dispatchTransformedTouchEvent(event, null);
            }
        }
        return handled;
    }

    private TouchTarget addTouchTarget(View child) {
        final TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }


    // 回收池策略·
    private static final class TouchTarget {

        public View child;//当前缓存的View

        // 回收池（一个对象）
        private static TouchTarget sRecycleBin;

        private static final Object sRecycleLock = new Object[0];

        public TouchTarget next;

        // size
        private static int sRecycledCount;

        // up事件
        public static TouchTarget obtain(View child) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycledCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        public void recycle() {

            if (child == null) {
                throw new IllegalStateException("已经被回收过了");
            }
            synchronized (sRecycleLock) {

                if (sRecycledCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount += 1;
                }
            }
        }
    }

    //分发处理 子控件  View
    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        boolean handled = false;
        // 当前View消费了
        if (child != null) {
            handled = child.dispatchTouchEvent(event);
        } else {
            handled = super.dispatchTouchEvent(event);
        }
        return handled;
    }

    /**
     * @param ev
     * @return 是否拦截点击事件
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
