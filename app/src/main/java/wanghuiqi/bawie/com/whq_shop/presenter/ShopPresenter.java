package wanghuiqi.bawie.com.whq_shop.presenter;

import com.google.gson.Gson;

import java.util.List;

import wanghuiqi.bawie.com.whq_shop.model.HttpUtils;
import wanghuiqi.bawie.com.whq_shop.model.bean.Shop;

/**
 * date:2018-11-22
 * author:王慧琦(琦小妹i)
 * function:
 */
public class ShopPresenter {
    public void ShopData(String url) {
        HttpUtils.getInstance().doGet(url, new HttpUtils.OKhttpInterface() {
            @Override
            public void Failed(Exception e) {
                mOnshopInterface.failed(e);
            }

            @Override
            public void Success(String data) {
                Shop shop = new Gson().fromJson(data, Shop.class);
                List<Shop.DataBean> shopData = shop.getData();
                mOnshopInterface.success(shopData);
            }
        });
    }

    public interface onShopInterface {
        void failed(Exception e);

        void success(List<Shop.DataBean> shopData);
    }

    private onShopInterface mOnshopInterface;

    public void setOnshopInterface(onShopInterface onshopInterface) {
        mOnshopInterface = onshopInterface;
    }
}
