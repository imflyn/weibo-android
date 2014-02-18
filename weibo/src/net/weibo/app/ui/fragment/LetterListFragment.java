package net.weibo.app.ui.fragment;

import java.util.ArrayList;

import net.weibo.api.LettersImpl;
import net.weibo.app.R;
import net.weibo.app.adapter.LettersListViewApapter;
import net.weibo.app.asynctask.MyAsyncTask;
import net.weibo.app.bean.Letters;
import net.weibo.app.bean.LettersData;
import net.weibo.app.bean.LettersList;
import net.weibo.app.ui.MyFollowActivity;
import net.weibo.app.ui.SendLetterActivity;
import net.weibo.app.widget.PullToRefreshListView;
import net.weibo.common.TimeTool;
import net.weibo.common.UIUtils;
import net.weibo.common.Utility;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LetterListFragment extends AbstractAppFragment
{
    private PullToRefreshListView  pullToRefreshListView;
    private LettersListViewApapter adapter;
    private ArrayList<Letters>     lettersList   = new ArrayList<Letters>();
    private TextView               news_footer_more;
    private View                   news_footer;
    private ProgressBar            news_footer_progress;
    private LoadLettersTask        loadLettersTask;

    public static final int        CHOOSE_PEOPLE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_letters, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (null != savedInstanceState)
        {
            lettersList = (ArrayList<Letters>) savedInstanceState.getSerializable("lettersList");
        } else
        {

        }
        initView();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (lettersList.size() <= 0 && Utility.isTaskStopped(loadLettersTask))
        {

            loadLettersTask = new LoadLettersTask();
            loadLettersTask.execute();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Utility.cancelTasks(loadLettersTask);
        adapter.clear();
        lettersList.clear();
    }

    private void initView()
    {

        pullToRefreshListView = (PullToRefreshListView) getActivity().findViewById(R.id.letters_pullToRefreshListView);

        news_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        news_footer_more = (TextView) news_footer.findViewById(R.id.listview_foot_more);
        news_footer_progress = (ProgressBar) news_footer.findViewById(R.id.listview_foot_progress);

        if (null == adapter)
        {
            adapter = new LettersListViewApapter(getActivity(), lettersList);
        }

        pullToRefreshListView.addFooterView(news_footer);
        pullToRefreshListView.setAdapter(adapter);

        news_footer.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (Utility.isTaskStopped(loadLettersTask) && !news_footer_more.getText().equals("数据已拉取完毕"))
                {
                    Letters letter = lettersList.get(lettersList.size() - 1);
                    loadLettersTask = new LoadLettersTask();
                    loadLettersTask.execute(String.valueOf(letter.getPubtime()), String.valueOf(letter.getTweetid()), "1");
                }

            }
        });

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Utility.cancelTasks(loadLettersTask);
                loadLettersTask = new LoadLettersTask();
                loadLettersTask.execute();

            }
        });

        pullToRefreshListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int id, long position)
            {
                Letters letters = lettersList.get((int) position);
                String name = "";
                String nick = "";
                if (letters == null)
                {
                    System.out.println("null");
                }

                if (letters.getMsgbox() == 1)// 收件
                {
                    name = letters.getToname();
                    nick = letters.getTonick();
                } else if (letters.getMsgbox() == 2)
                {
                    name = letters.getName();
                    nick = letters.getNick();
                }

                SendLetterActivity.startActivity(getActivity(), name, nick);

            }

        });

        getActivity().findViewById(R.id.btn_writeLetter).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), MyFollowActivity.class);
                intent.putExtra("type", CHOOSE_PEOPLE);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null)
            return;

        String name = data.getStringExtra("name");
        String nick = data.getStringExtra("nick");
        if (!TextUtils.isEmpty(name))
        {
            SendLetterActivity.startActivity(getActivity(), name, nick);
        }

    }

    private class LoadLettersTask extends MyAsyncTask<String, String, LettersList>
    {
        private String pageflag = "0";
        private String pagetime;
        private String reqnum   = "10";
        private String lastid;
        private String listtype = "0";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            setFootView("正在加载", View.VISIBLE, View.VISIBLE, View.VISIBLE);
        }

        @Override
        protected LettersList doInBackground(String... params)
        {
            if (isCancelled())
                return null;
            if (null != params && params.length > 1)
            {
                pagetime = params[0];
                lastid = params[1];
                pageflag = params[2];
            }

            LettersImpl impl = new LettersImpl();

            LettersData letterData = impl.getLetters(pageflag, pagetime, reqnum, lastid, listtype);
            if (null != letterData)
            {
                LettersList letters = letterData.getData();
                if (letters == null)
                    return null;
                else
                    return letters;
            } else
                return null;

        }

        @Override
        protected void onPostExecute(LettersList result)
        {
            super.onPostExecute(result);
            if (null == result)
            {
//                UIUtils.ToastMessage(getActivity().getApplicationContext(), "服务器不稳定,暂时无法加载最新评论!");
            } else
            {
                if (result.getHasnext() == 0)
                {
                    setFootView("加载更多", View.VISIBLE, View.GONE, View.VISIBLE);
                } else
                {
                    setFootView("数据已拉取完毕", View.VISIBLE, View.GONE, View.VISIBLE);
                }

                if (!pageflag.equals("1"))
                {
                    lettersList.clear();
                }
                lettersList.addAll(result.getInfo());
            }
            adapter.setList(lettersList);
            adapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete("上次更新:" + TimeTool.getListTime(System.currentTimeMillis()));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("lettersList", lettersList);
    }

    public void setFootView(String text, int tvVisivle, int pbVisivle, int footVisible)
    {
        news_footer_more.setText(text);

        news_footer.setVisibility(View.GONE);
        news_footer_more.setVisibility(tvVisivle);
        news_footer_progress.setVisibility(pbVisivle);

    }

}
