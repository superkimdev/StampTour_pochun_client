package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.http.RequestPath;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseCode;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseKey;
import com.thatzit.kjw.stamptour_kyj_client.http.ResponseMsg;
import com.thatzit.kjw.stamptour_kyj_client.http.StampRestClient;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.RankDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.RankHeaderDTO;
import com.thatzit.kjw.stamptour_kyj_client.main.adapter.RankRecyclerAdapter;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.util.ProgressWaitDaialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RankingFragment extends Fragment {

    private View view;
    private final String TAG = "RankingFragment";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RankRecyclerAdapter rankRecyclerAdapter;
    private ProgressWaitDaialog progressWaitDaialog;
    private PreferenceManager preferenceManager;
    private LoggedInInfo loggedin_info;
    private int page=0;
    private String req_url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rank_fragment, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        preferenceManager = new PreferenceManager(view.getContext());
        progressWaitDaialog = new ProgressWaitDaialog(view.getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.rank_scrollable_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        rankRecyclerAdapter = new RankRecyclerAdapter(view.getContext());

        request_Ranking_info();

    }

    private void request_Ranking_info() {
        req_url = RequestPath.req_url_user_rank.getPath();
        loggedin_info = preferenceManager.getLoggedIn_Info();
        RequestParams params = new RequestParams();
        params.put(ResponseKey.NICK.getKey(),loggedin_info.getNick());
        params.put(ResponseKey.TOKEN.getKey(),loggedin_info.getAccesstoken());
        params.put(ResponseKey.PAGE.getKey(),page);
        progressWaitDaialog.show();
        StampRestClient.post(req_url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressWaitDaialog.dismiss();
                try {
                    String res_code = response.getString(ResponseKey.CODE.getKey());
                    String message = response.getString(ResponseKey.MESSAGE.getKey());
                    recyclerView.setAdapter(rankRecyclerAdapter);
                    Log.e(TAG,response.toString());
                    if(res_code.equals(ResponseCode.SUCCESS.getCode())&&message.equals(ResponseMsg.SUCCESS.getMessage())){
                        JSONObject resultData = response.getJSONObject(ResponseKey.RESULTDATA.getKey());
                        JSONObject myrank = resultData.getJSONObject(ResponseKey.MYRANK.getKey());
                        JSONArray res_ranklist = resultData.getJSONArray(ResponseKey.RANKLIST.getKey());
                        RankHeaderDTO headerDTO = new RankHeaderDTO(myrank.getString(ResponseKey.NICK.getKey())
                                    ,myrank.getInt(ResponseKey.RANK.getKey())+"",
                                    myrank.getInt(ResponseKey.MYSTAMPCOUNT.getKey())+"",
                                    myrank.getInt(ResponseKey.TOTAL.getKey())+"");
                        rankRecyclerAdapter.additem(headerDTO);
                        for(int i = 0 ; i<res_ranklist.length();i++){
                            JSONObject rank_obj = res_ranklist.getJSONObject(i);
                            rankRecyclerAdapter.additem(new RankDTO(rank_obj.getString(ResponseKey.NICK.getKey())
                                    ,rank_obj.getInt(ResponseKey.RANK.getKey())+""
                                    ,rank_obj.getInt(ResponseKey.MYSTAMPCOUNT.getKey())+""));
                        }
                        if(res_ranklist.length() == 0){
                            rankRecyclerAdapter.additem(new RankDTO("더이상 표시할 랭킹이 없습니다.","",""));
                        }
                        rankRecyclerAdapter.notifyDataSetChanged();
                    }else if(res_code.equals(ResponseCode.NOTENOUGHDATA.getCode())&&message.equals(ResponseMsg.INVALIDACCESSTOKEN.getMessage())){
                        //accesstoken invalid LoginActivity go

                    }else{
                        //server not good

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressWaitDaialog.dismiss();

            }
        });
    }
}