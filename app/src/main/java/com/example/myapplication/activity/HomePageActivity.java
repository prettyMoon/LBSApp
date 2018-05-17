package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.fragment.PublishFragment;
import com.example.myapplication.fragment.TaskFragment;


/**
 * Created by 孙福来 on 2018/4/15.
 */

public class HomePageActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout taskLayout, publishLayout, mineLayout;
    private ImageView[] tagImg = new ImageView[3];
    private TextView[] tagText = new TextView[3];
    private TextView tvLeft;
    private FrameLayout container;
    private FragmentManager fm;
    private Fragment taskFragment, publishFragment, mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
        initListener();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, taskFragment, null).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task:
                selectTag(0);
                fm.beginTransaction().replace(R.id.container, taskFragment).commit();
                break;
            case R.id.publish:
                selectTag(1);
                //fm.beginTransaction().replace(R.id.container, publishFragment).commit();
                Intent intent = new Intent(HomePageActivity.this, PublishTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.mine:
                selectTag(2);
                fm.beginTransaction().replace(R.id.container, mineFragment).commit();
                break;
        }
    }

    private void initView() {
        tvLeft = this.findViewById(R.id.left);
        tvLeft.setText("");
        taskLayout = (LinearLayout) this.findViewById(R.id.task);
        publishLayout = (LinearLayout) this.findViewById(R.id.publish);
        mineLayout = (LinearLayout) this.findViewById(R.id.mine);
        tagImg[0] = (ImageView) this.findViewById(R.id.img_task);
        tagImg[1] = (ImageView) this.findViewById(R.id.img_publish);
        tagImg[2] = (ImageView) this.findViewById(R.id.img_mine);
        tagText[0] = (TextView) this.findViewById(R.id.tv_task);
        tagText[1] = (TextView) this.findViewById(R.id.tv_publish);
        tagText[2] = (TextView) this.findViewById(R.id.tv_mine);
        container = (FrameLayout) this.findViewById(R.id.container);
        taskFragment = new TaskFragment();
        publishFragment = new PublishFragment();
        mineFragment = new MineFragment();
    }

    private void initListener() {
        taskLayout.setOnClickListener(this);
        publishLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
    }

    private void selectTag(int tag) {
        switch (tag) {
            case 0:
                tagImg[0].setImageResource(R.drawable.task_pressed);
                tagImg[1].setImageResource(R.drawable.publish);
                tagImg[2].setImageResource(R.drawable.my);
                tagText[0].setTextColor(this.getResources().getColor(R.color.themeYellow));
                tagText[1].setTextColor(this.getResources().getColor(R.color.textGrey));
                tagText[2].setTextColor(this.getResources().getColor(R.color.textGrey));
                break;
            case 1:
                tagImg[0].setImageResource(R.drawable.task);
                tagImg[1].setImageResource(R.drawable.publish_pressed);
                tagImg[2].setImageResource(R.drawable.my);
                tagText[1].setTextColor(this.getResources().getColor(R.color.themeYellow));
                tagText[0].setTextColor(this.getResources().getColor(R.color.textGrey));
                tagText[2].setTextColor(this.getResources().getColor(R.color.textGrey));
                break;
            case 2:
                tagImg[0].setImageResource(R.drawable.task);
                tagImg[1].setImageResource(R.drawable.publish);
                tagImg[2].setImageResource(R.drawable.my_pressed);
                tagText[2].setTextColor(this.getResources().getColor(R.color.themeYellow));
                tagText[1].setTextColor(this.getResources().getColor(R.color.textGrey));
                tagText[0].setTextColor(this.getResources().getColor(R.color.textGrey));
                break;
        }
    }
}
