package com.shizhefei.test.controllers.mvchelpers.cool;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCCoolHelper;
import com.shizhefei.test.models.datasource.okhttp.BooksOkHttp_AsyncDataSource;
import com.shizhefei.test.models.enties.Book;
import com.shizhefei.test.view.adapters.ReBooksAdapter;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.header.DefaultHeader;
import com.shizhefei.view.mvc.demo.R;

import java.util.List;


/**
 * Created by LuckyJayce on 2016/11/29.
 */

public class StateHeaderFragment extends LazyFragment implements RefreshEvent {
    private CoolRefreshView coolRefreshView;
    private RecyclerView recyclerView;
    private MVCCoolHelper<List<Book>> mvcHelper;
    private ReBooksAdapter adapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);
        coolRefreshView = (CoolRefreshView) findViewById(R.id.recyclerview_funnyRefreshView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_recyclerView);

        coolRefreshView.setPullHeader(new DefaultHeader());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mvcHelper = new MVCCoolHelper<>(coolRefreshView);
        mvcHelper.setDataSource(new BooksOkHttp_AsyncDataSource());
//        mvcHelper.setAdapter(adapter = new ReBooksAdapter(getContext()));
        //这里模拟viewAdapter 和 dataAdapter不是同一个的情况
        mvcHelper.setAdapter2(adapter = new ReBooksAdapter(getContext()), new IDataAdapter<List<Book>>() {
            @Override
            public void notifyDataChanged(List<Book> books, boolean isRefresh) {
                if (books.size() > 5) {
                    adapter.notifyDataChanged(books.subList(0, 5), isRefresh);
                } else {
                    adapter.notifyDataChanged(books, isRefresh);
                }
            }

            @Override
            public List<Book> getData() {
                return adapter.getData();
            }

            @Override
            public boolean isEmpty() {
                return adapter.isEmpty();
            }
        });
        mvcHelper.refresh();
    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        mvcHelper.destory();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        coolRefreshView.setRefreshing(refreshing);
    }

}
