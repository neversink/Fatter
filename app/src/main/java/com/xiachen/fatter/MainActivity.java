package com.xiachen.fatter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AddItemDialog.itemsUpdateListener {

    private RecyclerView mRecyclerView;
    private List<Bean> mBeans;
    private ItemsAdapter mAdapter;
    private Snackbar mSnackbar;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        okHttpClient = new OkHttpClient();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemDialog dialog = new AddItemDialog();
                dialog.show(getSupportFragmentManager(), "AddItem");
                dialog.setCallback(MainActivity.this);
            }
        });

        mAdapter = new ItemsAdapter(this);
        mAdapter.setOnItemClickLitener(new ItemsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
//                Utils.showToast(MainActivity.this, position + "click!");
                AddItemDialog dialog = new AddItemDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.GET_BEAN, mAdapter.get(position));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "EditItem");
                dialog.setCallback(MainActivity.this);
            }

            @Override
            public void onItemLongClick(final int position) {
//                Utils.showToast(MainActivity.this, position + "long click!");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.dialog_posi, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.removeItem(position);
                                Utils.showSnackbar(mRecyclerView, R.string.snackbar_delete, R.string.snackbar_undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mAdapter.restore();
                                    }
                                });
                            }
                        }).setNegativeButton(R.string.dialog_nega, null);
                builder.show();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_statistics:
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                return true;
            case R.id.action_upload:


                File f = new File(getFilesDir().getParent() + File.separator + "databases" + File.separator + Constant.DATABASE_NAME);
                RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), f);
                RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                        .addPart(Headers.of("Content-Disposition", "form-data; name=\"timestamp\""), RequestBody.create(null, Utils.formatDate(new Date()) + ""))
                        .addPart(Headers.of("Content-Disposition", "form-data; name=\"file\"; filename =\"Fatter.db\""), filebody)
                        .build();
                Request request = new Request.Builder().url(Constant.SERVER_URL).post(requestBody).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Utils.showLog(getText(R.string.sync_failure).toString());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Utils.showLog(getText(R.string.sync_success).toString());

                    }
                });

                Utils.showLog("上传文件绝对路径" + f.getAbsolutePath());
                Utils.showLog("上传服务器地址" + Constant.SERVER_URL);
                Utils.showLog("上传文件大小：" + f.length() + "");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(int position) {
        mAdapter.addItem(position);
    }

    @Override
    public void refresh(Bean bean) {
        mAdapter.refresh(bean);
    }

}
