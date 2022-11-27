package com.example.sinhvienapplication.screen.detail.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.screen.detail.DetailActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DocumentTabFragment extends BaseFragment<DetailActivity> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_document;
    }

    PDFView pdfView;
    TextView textView ;
    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();

         textView = getRootView().findViewById(R.id.text);
        String pdf = "https://firebasestorage.googleapis.com/v0/b/work-826a1.appspot.com/o/file%2F306f7eb1-fd6b-4d6c-bda6-536397d35903.docx?alt=media&token=e22badd2-1ee8-445c-bf08-888fd56d9395";
        String doc = "https://firebasestorage.googleapis.com/v0/b/work-826a1.appspot.com/o/file%2Fdc6f73d1-2683-442b-9d9c-8da95a5a8f08.pdf?alt=media&token=a4324475-0faa-40ca-88b1-8700b5727676";
        pdfView = getRootView().findViewById(R.id.pdf_view);
        pdfView.zoomTo(3);
        pdfView.setMaxZoom(3);
        pdfView.setMinZoom(2);
        pdfView.setPageFling(true);
        new RetrivePDFfromUrl().execute(pdf);

        Uri webpage = Uri.parse(pdf);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        startActivity(intent);
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                textView.setText("Load");
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    textView.setText("200");
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
                            textView.setText("Page "+(page+1));
                        }
                    })
                    .enableAnnotationRendering(true)
                    .load();
        }
    }
}

