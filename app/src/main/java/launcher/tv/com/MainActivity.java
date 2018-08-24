package launcher.tv.com;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import launcher.tv.com.activitys.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "---------------";
    private PackageManager packageManager;
    private List<ResolveInfo> mApps;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        gridView = (GridView) findViewById(R.id.gv_view);
        packageManager = this.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

/*        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MAIN");
        filter.addCategory("android.intent.category.HOME");
        filter.addCategory("android.intent.category.DEFAULT");
        Context context = getApplicationContext();
        ComponentName component = new ComponentName(context.getPackageName(), MainActivity.class.getName());
        ComponentName[] components = new ComponentName[] {new ComponentName("com.miui.home", "com.miui.home.com.miui.home.launcher.Launcher"), component};
        packageManager.clearPackagePreferredActivities("com.miui.home");
        packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_EMPTY, components, component);*/
        //来获取一个关于App信息（ResolveInfo）的集合，这个ResolveInfo对象中就包含了应用程序的程序名、包名、入口类名等信息。
        mApps = this.packageManager.queryIntentActivities(mainIntent, 0);
        gridView.setAdapter(new gridAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = mApps.get(position);
                String pkg = info.activityInfo.packageName;
                String cls = info.activityInfo.name;
                ComponentName componentName = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });
    }


    public void clickMe(View view) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    class gridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mApps.size();
        }
        @Override
        public Object getItem(int position) {
            return mApps.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_apps, null);
                holder.imageView = convertView.findViewById(R.id.item_ic);
                holder.textView = convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ResolveInfo info = mApps.get(position);
            holder.imageView.setImageDrawable(info.activityInfo.loadIcon(packageManager));
            String appLabel = (String) info.loadLabel((packageManager));
            holder.textView.setText(appLabel);
            return convertView;
        }
    }

    class ViewHolder {
          ImageView imageView;
          TextView textView;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "监听返回键", Toast.LENGTH_SHORT).show();
    }
}
