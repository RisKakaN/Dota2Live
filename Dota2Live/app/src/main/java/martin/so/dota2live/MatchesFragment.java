package martin.so.dota2live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchesFragment extends Fragment {

    private static final String API_URL = "https://api.opendota.com/api/proMatches";

    private List<Match> matchList;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerView = view.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchList = new ArrayList<>();
        matchAdapter = new MatchAdapter(getActivity(), matchList);
        recyclerView.setAdapter(matchAdapter);
        loadMatches();

        return view;
    }

    private void loadMatches() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < 10; i++) {
                        JSONObject object = (JSONObject) response.get(i);
                        String radiantTeam = object.getString("radiant_name");
                        String direTeam = object.getString("dire_name");
                        matchList.add(new Match(radiantTeam, direTeam));
                    }
                    matchAdapter = new MatchAdapter(getActivity(), matchList);
                    recyclerView.setAdapter(matchAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq.add(jsonObjectRequest);
    }
}