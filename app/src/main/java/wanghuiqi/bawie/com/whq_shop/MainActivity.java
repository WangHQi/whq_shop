package wanghuiqi.bawie.com.whq_shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import wanghuiqi.bawie.com.whq_shop.adapter.ShopAdapter;
import wanghuiqi.bawie.com.whq_shop.model.bean.Shop;
import wanghuiqi.bawie.com.whq_shop.presenter.ShopPresenter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListView ex_listView;
    private CheckBox all_box;
    private TextView all_price;
    private Button jiesuan_btn;
    private ShopAdapter shopAdapter;
    private String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //A.获取控件
        initView();
        //B.获取数据
        initData();
    }

    //B.获取数据
    private void initData() {
        ShopPresenter shopPresenter = new ShopPresenter();
        shopPresenter.ShopData(url);
        shopPresenter.setOnshopInterface(new ShopPresenter.onShopInterface() {

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void success(List<Shop.DataBean> shopData) {
                //B1.获取适配器
                shopAdapter = new ShopAdapter(shopData);
                //B2.对适配器设置监听（加减器，组，子条目复选框改变）
                shopAdapter.setOnCartListChangeListener(new ShopAdapter.onCartListChangeListener() {
                    //------B2.1.组的复选框被点击------
                    @Override
                    public void onParentCheckedChange(int groupPosition) {
                        boolean parentAllSelect = shopAdapter.isParentAllSelect(groupPosition);
                        shopAdapter.changeSellerAllProduct(groupPosition, !parentAllSelect);
                        shopAdapter.notifyDataSetChanged();
                        //刷新底部
                        refreshAllShop();

                    }

                    //-------B2.2.子条目的复选框被点击-------
                    @Override
                    public void onChildCheckedChange(int groupPosition, int childPosition) {
                        shopAdapter.changeChild(groupPosition, childPosition);
                        shopAdapter.notifyDataSetChanged();
                        refreshAllShop();
                    }

                    //-------B2.3.加减器被点击------
                    @Override
                    public void onAddSubNumberChange(int groupPosition, int childPosition, int number) {
                        shopAdapter.changProductNumber(groupPosition, childPosition, number);
                        shopAdapter.notifyDataSetChanged();
                        refreshAllShop();
                    }
                });
                //B3.设置adapter对象
                ex_listView.setAdapter(shopAdapter);
                //B4.展开二级列表
                for (int i=0;i<shopData.size();i++){
                    ex_listView.expandGroup(i);
                }
            }
        });
    }


    //-------底部全选框的点击事件--------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_box:
                boolean allSelected = shopAdapter.isAllSelected();
                shopAdapter.changAllCheckBox(!allSelected);
                shopAdapter.notifyDataSetChanged();
                //刷新底部的数据显示
                refreshAllShop();
                break;
        }
    }

    //--------刷新底部---------
    private void refreshAllShop() {
        //判断是否所有商品都被选中
        boolean allSelected = shopAdapter.isAllSelected();
        //设置给checkbox
        all_box.setChecked(allSelected);
        //计算总计
        float price = shopAdapter.TotalPrice();
        all_price.setText("总计" + price);
        //计算数量
        int number = shopAdapter.TotalNumber();
        jiesuan_btn.setText("去结算（" + number + ")");

    }


    //A.初始化控件
    private void initView() {
        ex_listView = findViewById(R.id.ex_listView);
        all_box = findViewById(R.id.all_box);
        all_price = findViewById(R.id.all_price);
        jiesuan_btn = findViewById(R.id.jiesuan_btn);

        all_box.setOnClickListener(this);
    }
}
