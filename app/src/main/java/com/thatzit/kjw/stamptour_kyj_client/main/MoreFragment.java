package com.thatzit.kjw.stamptour_kyj_client.main;

/**
 * Created by kjw on 16. 8. 22..
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thatzit.kjw.stamptour_kyj_client.R;
import com.thatzit.kjw.stamptour_kyj_client.hide.HideListActivity;
import com.thatzit.kjw.stamptour_kyj_client.login.LoggedInCase;
import com.thatzit.kjw.stamptour_kyj_client.main.action.Case_by_loggedout;
import com.thatzit.kjw.stamptour_kyj_client.main.action.Check_return_loggedincase;
import com.thatzit.kjw.stamptour_kyj_client.more.GiftManageActivity;
import com.thatzit.kjw.stamptour_kyj_client.more.SurveyActivity;
import com.thatzit.kjw.stamptour_kyj_client.preference.LoggedInInfo;
import com.thatzit.kjw.stamptour_kyj_client.preference.PreferenceManager;
import com.thatzit.kjw.stamptour_kyj_client.user.User;
import com.thatzit.kjw.stamptour_kyj_client.user.normal.NormalUser;
import com.thatzit.kjw.stamptour_kyj_client.user.social.SocialUser;

public class MoreFragment extends Fragment implements View.OnClickListener,Check_return_loggedincase,Case_by_loggedout
{
    private View view;
    private RelativeLayout account_view_container;
    private RelativeLayout hidemanage_view_container;
    private RelativeLayout present_view_container;
    private RelativeLayout research_view_container;
    private RelativeLayout logout_view_container;
    private PreferenceManager preferenceManager;
    private final int MOREREQUESTNO = 1000;
    private final int USERINFOCHANGED = 1001;
    private final String TAG = "MoreFragment";
    private User user;
    private SocialUser socialuser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more_fragment, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        preferenceManager = new PreferenceManager(getActivity());
        account_view_container = (RelativeLayout) view.findViewById(R.id.account_view_container);
        hidemanage_view_container = (RelativeLayout) view.findViewById(R.id.hidemanage_view_container);
        present_view_container = (RelativeLayout) view.findViewById(R.id.present_view_container);
        research_view_container = (RelativeLayout) view.findViewById(R.id.research_view_container);
        logout_view_container = (RelativeLayout) view.findViewById(R.id.logout_view_container);
        account_view_container.setOnClickListener(this);
        hidemanage_view_container.setOnClickListener(this);
        present_view_container.setOnClickListener(this);
        research_view_container.setOnClickListener(this);
        logout_view_container.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_view_container:
                Intent intent = new Intent(getActivity(),MyinfoActivity.class);
                getActivity().startActivityForResult(intent,MOREREQUESTNO);
                break;
            case R.id.hidemanage_view_container:
                intent = new Intent(getActivity(),HideListActivity.class);
                getActivity().startActivityForResult(intent,MOREREQUESTNO);
                break;
            case R.id.present_view_container:
                intent = new Intent(getActivity(),GiftManageActivity.class);
                getActivity().startActivityForResult(intent,MOREREQUESTNO);
                break;
            case R.id.research_view_container:
                intent = new Intent(getActivity(),SurveyActivity.class);
                getActivity().startActivityForResult(intent,MOREREQUESTNO);
                break;
            case R.id.logout_view_container:
                Case_by_loggedout(check_return_loggedincase());
                break;
        }
    }

    @Override
    public String check_return_loggedincase() {
        if (preferenceManager.getLoggedIn_Info().getLoggedincase().equals("")){
            return "-1";
        }
        return preferenceManager.getLoggedIn_Info().getLoggedincase();
    }

    @Override
    public void Case_by_loggedout(String loggedincase) {
        if(loggedincase.equals("-1")){
            Toast.makeText(getActivity(),"잘못된 로그인 정보입니다 다시 로그인해주세요",Toast.LENGTH_LONG).show();
            return;
        }
        String temp = " User";
        LoggedInInfo loggedin_info = preferenceManager.getLoggedIn_Info();
        if(loggedincase.equals(LoggedInCase.NORMAL.getLogin_case())){
            user = new NormalUser(loggedin_info.getAccesstoken(),getActivity());
            ((NormalUser)user).LoggeOut();
        }else if(loggedincase.equals(LoggedInCase.FBLogin.getLogin_case())){
            socialuser = new SocialUser(loggedin_info.getAccesstoken(),getActivity());
            socialuser.LoggeOut();
        }else if(loggedincase.equals(LoggedInCase.KAKAOLogin.getLogin_case())){
            socialuser = new SocialUser(loggedin_info.getAccesstoken(),getActivity());
            socialuser.LoggeOut();
        }
    }


}