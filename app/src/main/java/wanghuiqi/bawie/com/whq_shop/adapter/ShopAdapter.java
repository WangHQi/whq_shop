package wanghuiqi.bawie.com.whq_shop.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wanghuiqi.bawie.com.whq_shop.R;
import wanghuiqi.bawie.com.whq_shop.model.bean.Shop;
import wanghuiqi.bawie.com.whq_shop.view.MyAddSub;

/**
 * date:2018-11-22
 * author:王慧琦(琦小妹i)
 * function:
 */
public class ShopAdapter extends BaseExpandableListAdapter {
    private List<Shop.DataBean> shopData;

    public ShopAdapter(List<Shop.DataBean> shopData) {
        this.shopData = shopData;
    }

    //1.有多少按钮
    @Override
    public int getGroupCount() {
        return shopData.size();
    }

    //2.一个组里有多少子条目
    @Override
    public int getChildrenCount(int groupPosition) {
        return shopData.get(groupPosition).getList().size();
    }


    ////////P.组布局////////
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //P1.获取组的下标
        Shop.DataBean dataBean = shopData.get(groupPosition);
        ParentHolder parentHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.parent_item, null);
            parentHolder = new ParentHolder(convertView);
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        //P2.获取商家名称
        parentHolder.textParent.setText(dataBean.getSellerName());
        //P3.根据当前商家的所有商品，确定checkbox是否选中
        boolean parentAllSelect = isParentAllSelect(groupPosition);
        //P4.1根据boolean判断是否选中
        parentHolder.boxParent.setChecked(parentAllSelect);
        //P5.设置点击checkbox的点击事件，接口回调
        parentHolder.boxParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCartListChangeListener != null) {
                    mOnCartListChangeListener.onParentCheckedChange(groupPosition);
                }
            }
        });
        return convertView;
    }


    ////////////C.子布局///////////
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Shop.DataBean dataBean = shopData.get(groupPosition);
        List<Shop.DataBean.ListBean> list = dataBean.getList();
        //C1.拿到list集合里具体商品
        Shop.DataBean.ListBean listBean = list.get(childPosition);
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.child_item, null);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        //C2.截取图片picasso
        String images = listBean.getImages();
        String[] strings = images.split("!");
        Picasso.with(parent.getContext()).load(strings[0]).into(childHolder.imageChild);
        //C.获取商品名称
        childHolder.childText.setText(listBean.getTitle());
        //C.单价
        childHolder.childPrice.setText(listBean.getPrice() + "");
        //C.设置子条目复选框是否选中
        childHolder.boxChild.setChecked(listBean.getSelected() == 1);
        //C.设置加减器内部数量
        childHolder.addSub.setNumber(listBean.getNum());

        //C3.设置商品checkbox的点击事件，接口回调
        childHolder.boxChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCartListChangeListener != null) {
                    mOnCartListChangeListener.onChildCheckedChange(groupPosition, childPosition);
                }
            }
        });

        //C4.设置加减器的点击事件，接口回调
        childHolder.addSub.setOnNumberChangeInterface(new MyAddSub.OnNumberChangeInterface() {
            @Override
            public void onNumberChang(int num) {
                if (mOnCartListChangeListener != null) {
                    mOnCartListChangeListener.onAddSubNumberChange(groupPosition, childPosition, num);
                }
            }
        });

        return convertView;
    }

    //--------判断当前商品是否被选中--------
    public boolean isParentAllSelect(int groupPosition) {
        //拿到组的数据
        Shop.DataBean dataBean = shopData.get(groupPosition);
        //拿到商家所有商品，集合
        List<Shop.DataBean.ListBean> list = dataBean.getList();
        for (int i = 0; i < list.size(); i++) {
            //判断这个组所有商品是否被选中，如有一个未选中就都不选中
            if (list.get(i).getSelected() == 0) {
                return false;
            }
        }
        return true;
    }

    //------底部全选按钮逻辑判断------
    public boolean isAllSelected() {
        for (int i = 0; i < shopData.size(); i++) {
            Shop.DataBean dataBean = shopData.get(i);
            List<Shop.DataBean.ListBean> list = dataBean.getList();
            for (int j = 0; j < list.size(); j++) {
                //判断组的商品是否被选中
                if (list.get(j).getSelected() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //-----计算商品总数量-----
    public int TotalNumber() {
        int totalNumber = 0;
        for (int i = 0; i < shopData.size(); i++) {
            Shop.DataBean dataBean = shopData.get(i);
            List<Shop.DataBean.ListBean> list = dataBean.getList();
            for (int j = 0; j < list.size(); j++) {
                //商品数量，选中的
                if (list.get(j).getSelected() == 1) {
                    //拿到商品的数量
                    int num = list.get(j).getNum();
                    totalNumber += num;
                }
            }
        }
        return totalNumber;
    }

    //-------计算商品总价格-------
    public float TotalPrice() {
        float totalPrice = 0;
        for (int i = 0; i < shopData.size(); i++) {
            Shop.DataBean dataBean = shopData.get(i);
            List<Shop.DataBean.ListBean> list = dataBean.getList();
            for (int j = 0; j < list.size(); j++) {
                //商品价格，选中的
                if (list.get(j).getSelected() == 1) {
                    //拿到商品数量
                    double price = list.get(j).getPrice();
                    int num = list.get(j).getNum();
                    totalPrice += price + num;
                }
            }
        }
        return totalPrice;
    }

    //--------根据选择，更改选框里状态------
    public void changeSellerAllProduct(int groupPosition, boolean isSelected) {
        Shop.DataBean dataBean = shopData.get(groupPosition);
        List<Shop.DataBean.ListBean> list = dataBean.getList();
        for (int i = 0; i < list.size(); i++) {
            Shop.DataBean.ListBean listBean = list.get(i);
            listBean.setSelected(isSelected ? 1 : 0);
        }
    }

    //---------当子条目选中，更新组选框的状态-------
    public void changeChild(int groupPosition, int childPosition) {
        Shop.DataBean dataBean = shopData.get(groupPosition);
        List<Shop.DataBean.ListBean> list = dataBean.getList();
        Shop.DataBean.ListBean listBean = list.get(childPosition);
        listBean.setSelected(listBean.getSelected() == 0 ? 1 : 0);
    }

    //---------当最底部全选框选中，更新所有选框的状态
    public void changAllCheckBox(boolean selected) {
        for (int i = 0; i < shopData.size(); i++) {
            Shop.DataBean dataBean = shopData.get(i);
            List<Shop.DataBean.ListBean> list = dataBean.getList();
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setSelected(selected ? 1 : 0);
            }
        }
    }
    //----------当加减器被点击时，改变商品数量--------
    public void changProductNumber(int groupPosition, int childPosition, int number){
        Shop.DataBean dataBean = shopData.get(groupPosition);
        List<Shop.DataBean.ListBean> list = dataBean.getList();
        Shop.DataBean.ListBean listBean = list.get(childPosition);
        listBean.setNum(number);
    }

    //==============Viewhodler============
    //组的Viewholder
    public static class ParentHolder {

        private final CheckBox boxParent;
        private final TextView textParent;

        public ParentHolder(View rootView) {
            boxParent = rootView.findViewById(R.id.box_parent);
            textParent = rootView.findViewById(R.id.text_parent);
        }
    }

    public static class ChildHolder {

        private final CheckBox boxChild;
        private final ImageView imageChild;
        private final TextView childText;
        private final TextView childPrice;
        private final MyAddSub addSub;

        public ChildHolder(View rootView) {
            boxChild = rootView.findViewById(R.id.child_box);
            imageChild = rootView.findViewById(R.id.child_image);
            childText = rootView.findViewById(R.id.child_text);
            childPrice = rootView.findViewById(R.id.child_price);
            addSub = rootView.findViewById(R.id.add_sub);
        }
    }

    //=============创建接口===========
    public interface onCartListChangeListener {
        /*当组的checkbox点击时回调*/
        void onParentCheckedChange(int groupPosition);

        /*当点击子条目商品的checkbox回调*/
        void onChildCheckedChange(int groupPosition, int childPosition);

        /*当点击加减按钮的回调*/
        void onAddSubNumberChange(int groupPosition, int childPosition, int number);
    }

    private onCartListChangeListener mOnCartListChangeListener;

    public void setOnCartListChangeListener(onCartListChangeListener onCartListChangeListener) {
        mOnCartListChangeListener = onCartListChangeListener;
    }

    //不管这些
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
