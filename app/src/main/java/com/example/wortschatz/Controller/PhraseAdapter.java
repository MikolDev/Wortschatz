package com.example.wortschatz.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wortschatz.Model.Phrase;
import com.example.wortschatz.R;

import java.util.ArrayList;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.ViewHolder> {
    Context context;
    ArrayList<Phrase> phrasesList;
    LayoutInflater layoutInflater;

    public PhraseAdapter(Context context, ArrayList<Phrase> phrasesList) {
        this.context = context;
        this.phrasesList = phrasesList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_phrase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phrase phrase = phrasesList.get(position);

        holder.textSingular.setText(phrase.getSingular());
        holder.textPlural.setText(phrase.getPlural());
        holder.textTranslation.setText(phrase.getTranslation());
        Log.v("Adapter", phrase.getSingular());
    }

    @Override
    public int getItemCount() {
        return phrasesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSingular;
        TextView textPlural;
        TextView textTranslation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSingular = itemView.findViewById(R.id.item_singular);
            textPlural = itemView.findViewById(R.id.item_plural);
            textTranslation = itemView.findViewById(R.id.item_translation);
        }
    }

}
//
//public class PhraseAdapter extends BaseAdapter {
//    Context context;
//    ArrayList<Phrase> phrasesList;
//    LayoutInflater layoutInflater;
//
//    TextView textSingular;
//    TextView textPlural;
//    TextView textTranslation;
//
//    public PhraseAdapter(Context context, ArrayList<Phrase> phrasesList) {
//        this.context = context;
//        this.phrasesList = phrasesList;
//        this.layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return phrasesList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return phrasesList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        Phrase phrase = phrasesList.get(position);
//
//        view = layoutInflater.inflate(R.layout.item_phrase, null);
//        textSingular = view.findViewById(R.id.item_singular);
//        textPlural = view.findViewById(R.id.item_plural);
//        textTranslation = view.findViewById(R.id.item_translation);
//
//        textSingular.setText(phrase.getSingular());
//        textPlural.setText(phrase.getPlural());
//        textTranslation.setText(phrase.getTranslation());
//
//        return view;
//    }
//}
