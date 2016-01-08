package com.example.ipcam.camer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.R;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.MainExitDialog;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;


public class MainFragment extends FragmentActivity {
    private IndicatorViewPager indicatorViewPager;
    public static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager
                .setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(4);
    }


    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {Util.getString(MainFragment.this, R.string.buttom_devicelist), Util.getString(MainFragment.this, R.string.buttom_alarm), Util.getString(MainFragment.this, R.string.buttom_audio), Util.getString(MainFragment.this, R.string.buttom_setting)};
        private int[] tabIcons = {R.drawable.maintab_1_selector,
                R.drawable.maintab_2_selector, R.drawable.maintab_3_selector,
                R.drawable.maintab_4_selector};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            if (convertView == null) {
                convertView = (TextView) inflater.inflate(R.layout.tab_main,
                        container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            textView.setCompoundDrawablesWithIntrinsicBounds(0,
                    tabIcons[position], 0, 0);
            return textView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            System.out.println(position);

            switch (position) {
                case 0:
                    fragment = new DeviceLayerFragment();
                    break;
                case 1:
                    fragment = new NewsLayerFragment();
                    break;
                case 2:
                    fragment = new AudioLayerFragment();
                    break;
                case 3:
                    fragment = new SettingLayerFragment();
                    break;

                default:
                    break;
            }
            return fragment;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();

    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次,后台运行",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            final MainExitDialog dialog = new MainExitDialog(this);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth() * 0.9); //
            dialog.getWindow().setAttributes(p);



            dialog.setExitListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setExitBoundListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    BridgeService.isBackground = true;
                    Intent localIntent = new Intent(MainFragment.this,
                            BridgeService.class);
                    MainFragment.this.startService(localIntent);
                    moveTaskToBack(true);
                }
            });
            dialog.setExitDimssListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

}
