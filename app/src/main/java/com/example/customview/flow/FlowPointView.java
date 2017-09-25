package com.example.customview.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customview.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class FlowPointView extends LinearLayout {

    private TextView titleTV, contentTV;
    private View lineView;
    private static final int lineHeight = 100; // 线的高度

    public FlowPointView(Context context) {
        this(context, null);
    }

    public FlowPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_flow_point, this);
        this.titleTV = (TextView) this.findViewById(R.id.titleTV);
        this.contentTV = (TextView) this.findViewById(R.id.contentTV);
        this.lineView = this.findViewById(R.id.lineView);
        titleTV.setText("下单");

        lineView.getLayoutParams().height = lineHeight;

        // OnGlobalLayoutListener view加载完成时回调
        titleTV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lineView.animate().translationX(titleTV.getLeft() + titleTV.getWidth()/2 - lineView.getWidth() / 2 - 1).setDuration(0);
            }
        });
    }

    public TextView getTitleTV() {
        return titleTV;
    }

    public TextView getContentTV() {
        return contentTV;
    }

    public View getLineView() {
        return lineView;
    }
}
