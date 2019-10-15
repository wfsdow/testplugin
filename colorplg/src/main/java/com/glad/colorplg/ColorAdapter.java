package com.glad.colorplg;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {
    private List<List<String>> colorsList;
    private Context context;

    public ColorAdapter(List<List<String>> colorsList, Context context) {
        this.colorsList = colorsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ColorHolder holder = new ColorHolder(
                LayoutInflater.from(context).inflate(R.layout.color_item, viewGroup, false)
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder colorHolder, int i) {
        List<String> colors = colorsList.get(i);
        if (colors != null && colors.size() == 5) {
            colorHolder.color1.setBackgroundColor(Color.parseColor(colors.get(0)));
            colorHolder.color2.setBackgroundColor(Color.parseColor(colors.get(1)));
            colorHolder.color3.setBackgroundColor(Color.parseColor(colors.get(2)));
            colorHolder.color4.setBackgroundColor(Color.parseColor(colors.get(3)));
            colorHolder.color5.setBackgroundColor(Color.parseColor(colors.get(4)));

            colorHolder.color1.setText(colors.get(0));
            colorHolder.color2.setText(colors.get(1));
            colorHolder.color3.setText(colors.get(2));
            colorHolder.color4.setText(colors.get(3));
            colorHolder.color5.setText(colors.get(4));
        }
    }

    @Override
    public int getItemCount() {
        return colorsList == null ? 0 : colorsList.size();
    }

    class ColorHolder extends RecyclerView.ViewHolder{
        private TextView color1;
        private TextView color2;
        private TextView color3;
        private TextView color4;
        private TextView color5;
        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            color1 = itemView.findViewById(R.id.color1);
            color2 = itemView.findViewById(R.id.color2);
            color3 = itemView.findViewById(R.id.color3);
            color4 = itemView.findViewById(R.id.color4);
            color5 = itemView.findViewById(R.id.color5);

        }
    }
}
