package com.example.customview.flow;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoya on 2017/7/14.
 * 流程图
 */

public class FlowView extends LinearLayout{

    private List<FlowPoint> flowPointList = new ArrayList<>();
    private LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    public FlowView(Context context) {
        this(context, null);
    }

    public FlowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
    }

    private void initView() {
        for (int i = 0; i < flowPointList.size(); i++){
            FlowPoint flowPoint = flowPointList.get(i);
            FlowPointView flowPointView = new FlowPointView(getContext());
            flowPointView.getTitleTV().setText(flowPoint.getTitle());
            flowPointView.getContentTV().setText(flowPoint.getContent());
            this.addView(flowPointView, params);
            if (i == flowPointList.size() -1) { // 最后一项,不显示线
                flowPointView.getLineView().setVisibility(GONE);
            }
        }
    }

    public void setFlowPointList(List<FlowPoint> flowPointList) {
        this.flowPointList.clear();
        this.removeAllViews();

        this.flowPointList.addAll(flowPointList);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
