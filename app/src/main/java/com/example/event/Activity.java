package com.example.event;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by com.trident, All rights reserved.
 * -----------------------------------------------------------------
 * <p>
 * Author: aaron
 * <p>
 * Create: 2019/4/11 9:21 PM
 * <p>
 * Description: 模拟事件分发流程
 */
public class Activity {

    public static void main(String[] arg) {

        // 顶级容器ViewGroup(构造函数传递左上，右下坐标)
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶级容器");

        // 二级容器ViewGroup，也是定义两个坐标
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
        viewGroup1.setName("第二级容器");

        // 模拟初始化View以及放置在ViewGroup层级中
        View view = new View(0, 0, 200, 200);
        view.setName("子View");

        viewGroup1.addView(view);
        viewGroup.addView(viewGroup1);

        viewGroup.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("顶级的OnTouch事件");
                return false;
            }
        });

        viewGroup1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("第二级容器的OnTouch事件");
                return false;
            }
        });

//        view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("子iew的onClick事件");
//            }
//        });
//
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("子view的OnTouch事件");
                return false;
            }
        });

        // 模拟事件分发(点击里面View坐标点为：(100,100))
        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);

        // 顶级容器分发
        viewGroup.dispatchTouchEvent(motionEvent);
    }

}
