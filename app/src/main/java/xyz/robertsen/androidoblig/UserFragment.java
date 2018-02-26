package xyz.robertsen.androidoblig;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends DialogFragment {

    private static final String ARG_PLAYER_NUMBER = "number_of_players";
    private static final String ARG_LAYOUT_ID = "layout";
    private int players;
    private int layoutId;

    private OnFragmentInteractionListener mListener;


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * @param players The current number of players
     * @return A new instance of fragment UserFragment.
     */
    public static UserFragment newInstance(int players, int layoutId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_NUMBER, players);
        args.putInt(ARG_LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            players = getArguments().getInt(ARG_PLAYER_NUMBER);
            layoutId = getArguments().getInt(ARG_LAYOUT_ID);
        }
    }

    @Override
    public void onResume() {
        if (getDialog().getWindow() != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.8);
                int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.6);
                getDialog().getWindow().setLayout(width, height);
            } else {
                int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.6);
                int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.8);
                getDialog().getWindow().setLayout(width, height);
            }
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(layoutId, container, false);

        if (layoutId == R.layout.fragment_user) {
            v.findViewById(R.id.button_user_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginButtonPressed(
                            ((EditText)v.findViewById(R.id.text_user_name)).getText().toString(),
                            ((EditText)v.findViewById(R.id.text_user_pwd)).getText().toString()
                    );
                }
            });
            v.findViewById(R.id.button_user_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onLoginButtonPressed(String usr, String pwd) {
        if (mListener != null) {
            mListener.onLoginButtonPressed(usr, pwd);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLoginButtonPressed(String usr, String pwd);
    }
}
