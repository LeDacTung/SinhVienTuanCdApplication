package com.example.sinhvienapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.model.Person;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.utils.image.ImageUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpinnerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Person> mPersons;

    public SpinnerAdapter(Context mContext, ArrayList<Person> mPersons) {
        this.mContext = mContext;
        this.mPersons = mPersons;
    }

    public void addPersons(Person person){
        mPersons.add(person);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPersons.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Person person = mPersons.get(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.person_adapter, null);
        CircleImageView imageView =  view.findViewById(R.id.image);
        TextView nameTv = (TextView) view.findViewById(R.id.name_tv);
        ImageUtils.loadImage(mContext, imageView, person.getImage());
        nameTv.setText( person.getName()  );
        return view;
    }
}
