package wanghuiqi.bawie.com.whq_shop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wanghuiqi.bawie.com.whq_shop.R;

/**
 * date:2018-11-22
 * author:王慧琦(琦小妹i)
 * function:
 */
public class MyAddSub extends LinearLayout implements View.OnClickListener {

    private TextView addText;
    private TextView subText;
    private TextView numText;
    private int number = 1;

    public MyAddSub(Context context) {
        this(context, null);
    }

    public MyAddSub(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAddSub(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.add_sub_item, this);
        addText = view.findViewById(R.id.add_text);
        subText = view.findViewById(R.id.sub_text);
        numText = view.findViewById(R.id.num_text);

        addText.setOnClickListener(this);
        subText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                ++number;
                numText.setText(number + "");
                if (mOnNumberChangeInterface != null) {
                    mOnNumberChangeInterface.onNumberChang(number);
                }
                break;
            case R.id.sub_text:
                //判断如果数字比1大就减，比1小就吐司
                if (number > 1) {
                    --number;
                    numText.setText(number + "");
                    if (mOnNumberChangeInterface != null) {
                        mOnNumberChangeInterface.onNumberChang(number);
                    }
                } else {
                    Toast.makeText(getContext(), "不能再少了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        numText.setText(number + "");
    }

    //创建接口
    public interface OnNumberChangeInterface {
        void onNumberChang(int num);
    }

    //声明接口名
    private OnNumberChangeInterface mOnNumberChangeInterface;

    //暴露方法
    public void setOnNumberChangeInterface(OnNumberChangeInterface onNumberChangeInterface) {
        mOnNumberChangeInterface = onNumberChangeInterface;
    }
}
