package league.funny.com.funnyleague.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import league.funny.com.funnyleague.R;

public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.TextRecyclerHolder> {

    private List<String> datas;
    private LayoutInflater inflater;

    public TextRecyclerAdapter(Context context, List<String> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    public class TextRecyclerHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public TextRecyclerHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    @Override
    public TextRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextRecyclerHolder holder = new TextRecyclerHolder(inflater.inflate(R.layout.recycler_text_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(TextRecyclerHolder holder, int position) {
        holder.title.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}