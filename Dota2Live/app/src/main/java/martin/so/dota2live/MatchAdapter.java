package martin.so.dota2live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {


    private Context context;
    private List<Match> matchList;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.match_list, null);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match match = matchList.get(position);

        holder.textViewRadiantTeam.setText(match.getRadiantTeam());
        holder.textViewDireTeam.setText(match.getDireTeam());
    }

    @Override
    public int getItemCount() {
        if (matchList != null && !matchList.isEmpty()) {
            return matchList.size();
        } else {
            return 0;
        }
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRadiantTeam, textViewDireTeam;

        public MatchViewHolder(View itemView) {
            super(itemView);

            textViewRadiantTeam = itemView.findViewById(R.id.textViewRadiantTeam);
            textViewDireTeam = itemView.findViewById(R.id.textViewDireTeam);
        }
    }
}