package com.zy.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zy.pulltorefreshrecyclerview.R;


/**
 * Created by zhaoya on 2016/10/27.
 * 用SwipeRefreshLayout实现下拉刷新,自动加载更多的RecyclerView
 * 使用时需要:
 * setPullToRefreshListener 上拉下拉事件
 * setAdapter(IRecyclerViewAdapter adapter) 配置adapter
 * 刷新完成后需要调用setRefreshFinish()
 * 加载完成后需要调用setLoadMoreFinish()
 * 数据加载完成需要调用setHasMore(boolean hasMore) 来设置是否还要更多数据
 */
public class PullToRefreshRecyclerView extends LinearLayout {

    private static final String TAG = PullToRefreshRecyclerView.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadMoreRecyclerAdapter adapter;
    private IRecyclerViewAdapter iAdapter;
    private PullToRefreshListener pullToRefreshListener;

    private int footerBottomPosition = 1; // 显示到倒数第几项时自动加载

    private boolean isLoading = false; // 是否正在加载
    private boolean hasMore = true; // 是否有更多数据

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        initView();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_recyclerview, this);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.SwipeRefreshLayout);
        this.mRecyclerView = (RecyclerView) this.findViewById(R.id.RecyclerView);

        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pullToRefreshListener != null) {
                    pullToRefreshListener.onRefresh();
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL); // 垂直滑动
        this.mRecyclerView.setLayoutManager(manager);
        this.adapter = new LoadMoreRecyclerAdapter(); //创建adapter对象

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mRecyclerView.getAdapter().getItemCount();
                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
//                Log.d("test", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + footerBottomPosition) && hasMore) {
                    View view = manager.findViewByPosition(totalItemCount - 1);
                    if (null != mRecyclerView.getChildViewHolder(view)) {
                        LoadMoreRecyclerAdapter.FooterViewHolder footer = (LoadMoreRecyclerAdapter.FooterViewHolder) mRecyclerView.getChildViewHolder(view);
                        footer.footerTV.performClick(); // 模拟点击事件
                    }
                }
            }
        });
    }

    /** 设置adapter */
    public void setAdapter(IRecyclerViewAdapter adapter) {
        this.iAdapter = adapter;
    }

    /** 设置上拉下拉事件 */
    public void setPullToRefreshListener(PullToRefreshListener pullToRefreshListener){
        this.pullToRefreshListener = pullToRefreshListener;
    }

    /**
     * 设置更多
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        this.adapter.notifyItemChanged( this.adapter.getItemCount() - 1);
    }

    /** 获取RecyclerView */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public interface PullToRefreshListener {
        /** 刷新 */
        void onRefresh();

        /** 加载更多 */
        void onLoadMore();
    }

    /** 显示刷新动画 */
    public void setRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    /** 设置刷新完成 */
    public void setRefreshFinish() {
        mSwipeRefreshLayout.setRefreshing(false); // 隐藏刷新的view
        this.adapter.notifyDataSetChanged();
    }

    /** 设置加载完成 */
    public void setLoadMoreFinish() {
        isLoading = false;
        this.adapter.notifyDataSetChanged();
    }

    class LoadMoreRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2; //底部

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foot_loading, parent, false);
                return new FooterViewHolder(view);
            } else {
                return iAdapter.getViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof FooterViewHolder) {
                final FooterViewHolder footer = (FooterViewHolder) holder;
                if (hasMore) {
                    footer.footerTV.setText("更多");
                    footer.progressBar.setVisibility(View.GONE);
                    footer.footerTV.setClickable(true);
                    footer.footerTV.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pullToRefreshListener != null && isLoading == false) {
                                pullToRefreshListener.onLoadMore(); // 自动加载更多
                                footer.footerTV.setText("加载中");
                                footer.progressBar.setVisibility(View.VISIBLE);
                                footer.footerTV.setClickable(false);
                                isLoading = true;
                            }
                        }
                    });
                } else {
                    footer.footerTV.setText("暂无项目");
                    footer.progressBar.setVisibility(View.GONE);
                    footer.footerTV.setClickable(false);
                }
            } else {
                iAdapter.setViewHolder(adapter, holder, position);
            }
        }

        // RecyclerView的count设置为数据总条数+ 1（footerView）
        @Override
        public int getItemCount() {
            return iAdapter.getCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            // 最后一个item设置为footerView
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        /**
         * 底部ViewHolder
         */
        public class FooterViewHolder extends RecyclerView.ViewHolder {

            public ProgressBar progressBar;
            public TextView footerTV;

            public FooterViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
                footerTV = (TextView) itemView.findViewById(R.id.footerTV);
            }
        }
    }

    /** 外部建adapter需要实现的方法 */
    public interface IRecyclerViewAdapter {
        /** ItemCount数量 */
        int getCount();

        /** 设置item的holder */
        RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType);

        /** 为holder配置属性 */
        void setViewHolder(RecyclerView.Adapter adapter, RecyclerView.ViewHolder holder, int position);
    }

}
