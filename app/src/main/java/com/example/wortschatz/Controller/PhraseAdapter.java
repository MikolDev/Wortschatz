package com.example.wortschatz.Controller;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

        holder.textPlural.setText(phrase.getPlural());
        holder.textTranslation.setText(phrase.getTranslation());

        if (phrase.isHard()) {
            SpannableString underlined = new SpannableString(phrase.getSingular());
            underlined.setSpan(new UnderlineSpan(), 0, underlined.length(), 0);
            holder.textSingular.setText(underlined);
        } else {
            holder.textSingular.setText(phrase.getSingular());
        }
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