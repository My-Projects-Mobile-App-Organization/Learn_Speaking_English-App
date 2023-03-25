package com.example.myapplication.fragmentUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    public static final String TAG = EditProfileFragment.class.getName();

    EditText edtOldPass,edtNewPass,edtConfirmPass;
    ImageView imgChangePass, imgBackProfileFrag;
    View mView;

    boolean showChangePassMenu = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        imgChangePass = mView.findViewById(R.id.imgChangePass);
        imgBackProfileFrag = mView.findViewById(R.id.imgBackProfileFrag);
        edtConfirmPass = mView.findViewById(R.id.edtConfirmPass);
        edtNewPass = mView.findViewById(R.id.edtNewPass);
        edtOldPass = mView.findViewById(R.id.edtOldPass);

        imgBackProfileFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
            }
        });

        imgChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showChangePassMenu){
                    imgChangePass.setImageResource(R.drawable.down_arrow_icon);
                    edtConfirmPass.setVisibility(View.VISIBLE);
                    edtNewPass.setVisibility(View.VISIBLE);
                    edtOldPass.setVisibility(View.VISIBLE);
                    showChangePassMenu = true;
                }else if(showChangePassMenu){
                    imgChangePass.setImageResource(R.drawable.right_arrow_icon);
                    edtConfirmPass.setVisibility(View.GONE);
                    edtNewPass.setVisibility(View.GONE);
                    edtOldPass.setVisibility(View.GONE);
                    showChangePassMenu = false;
                }
            }
        });

        return mView;
    }
}