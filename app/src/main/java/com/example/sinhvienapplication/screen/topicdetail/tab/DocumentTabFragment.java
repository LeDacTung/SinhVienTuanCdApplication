package com.example.sinhvienapplication.screen.topicdetail.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.screen.topicdetail.TopicDetailActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DocumentTabFragment extends BaseFragment<TopicDetailActivity> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_document;
    }

    public static DetailTabFragment newInstance(Topic topic) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.Intent.TOPIC_DETAIL, topic);
        DetailTabFragment fragment = new DetailTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Topic mTopic;
    PDFView pdfView;
    TextView pageTv;
    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTopic = (Topic) bundle.getSerializable(Constant.Intent.TOPIC_DETAIL);
        }

        pdfView = getRootView().findViewById(R.id.pdf_view);
        pageTv = getRootView().findViewById(R.id.page_tv);
        pdfView.zoomTo(3);
        pdfView.setMaxZoom(3);
        pdfView.setMinZoom(2);
        pdfView.setPageFling(true);
        new RetrivePDFfromUrl().execute(mTopic.getFileUrl());
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .fitEachPage(true)
                    .swipeHorizontal(true)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            int currentPage = page+1;
                            pageTv.setText(currentPage + " / " +pageCount);
                        }
                    })
                    .enableAnnotationRendering(true)
                    .load();
        }
    }
}

