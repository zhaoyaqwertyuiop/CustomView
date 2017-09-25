package com.example.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.customview.flow.FlowPoint;
import com.example.customview.flow.FlowView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowView flowView = (FlowView) super.findViewById(R.id.flowView);
        List<FlowPoint> flowPointList = new ArrayList<>();
        flowPointList.add(new FlowPoint.Builder().setTitle("下单").setContent("商家 1212313").build());
        flowPointList.add(new FlowPoint.Builder().setTitle("接单").setContent("商家  121231313 ").build());
        flowPointList.add(new FlowPoint.Builder().setTitle("服务").setContent("阿萨法  安慰法啊").build());
        flowView.setFlowPointList(flowPointList);

        RingView ringView = (RingView) super.findViewById(R.id.ringView);
        ringView.setValue(50);
    }
}
