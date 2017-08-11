package com.app.dmitryteplyakov.sportdiary.Programs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.Exercise;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.R;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dmitry on 27.07.2017.
 */

public class ProgramExerciseListFragment extends Fragment {

    public static final String ARGS_EXERCISE_PARENT_ID = "com.app.args_exercise_parent_id";
    private static final String SAVE_UUID = "com.app.save_uuid";
    public static final int REQUEST_DELETE_EXERCISE = 7;
    private static final String DIALOG_EXERCISE_DELETE = "com.app.programexerciselistfragment.dialog_exercise_delete";
    private UUID parentUUID;
    private FloatingActionButton mFab;
    private ProgramExerciseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private static UUID trainingUuid = null;

    public static ProgramExerciseListFragment newInstance(UUID parentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_EXERCISE_PARENT_ID, parentId);
        ProgramExerciseListFragment fragment = new ProgramExerciseListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(SAVE_UUID, parentUUID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentUUID = (UUID) getArguments().getSerializable(ARGS_EXERCISE_PARENT_ID);
        if(parentUUID == null) {
             parentUUID = trainingUuid;
            Log.d("PELF", "NULL");
        }
        else {
            trainingUuid = parentUUID;
            Log.d("PELF", "NOT NULL");
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(TrainingStorage.get(getActivity()).getTraining(parentUUID).getTitle());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mEmptyTextView.setText(getString(R.string.fragment_list_programs_empty_text_exercises));
        mFab = (FloatingActionButton) v.findViewById(R.id.fragment_list_programs_add_program_action_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                exercise.setParentUUID(parentUUID);
                ExerciseStorage.get(getActivity()).addExercise(exercise);
                Intent intent = ProgramActivity.newIntent(getActivity(), exercise.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    mFab.hide();
                else if(dy < 0)
                    mFab.show();
            }
        });
        updateUI();
        return v;
    }

    private class ProgramExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView exerciseTitle;
        private Exercise mExercise;
        private CharSequence options[];
        public ProgramExerciseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            exerciseTitle = (TextView) itemView.findViewById(R.id.group_name);
            options = new CharSequence[] {getString(R.string.menu_delete_item)};
        }

        public void bindExercise(Exercise exercise) {
            mExercise = exercise;
            exerciseTitle.setText(exercise.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProgramActivity.newIntent(getActivity(), mExercise.getId());
            Log.d("PELF AD", "EID: " + mExercise.getId().toString());
            Log.d("PELF AD", "PID " + mExercise.getParentUUID().toString());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("PELF", "EX TITLE: " + mExercise.getTitle() + " UUID: " + mExercise.getId().toString());
            AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0) {
                        FragmentManager manager = getFragmentManager();
                        DeleteFragment optionDialog = DeleteFragment.newInstance(mExercise.getId());
                        optionDialog.setTargetFragment(ProgramExerciseListFragment.this, REQUEST_DELETE_EXERCISE);
                        optionDialog.show(manager, DIALOG_EXERCISE_DELETE);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }
    }

    private class ProgramExerciseAdapter extends RecyclerView.Adapter<ProgramExerciseHolder> {
        private List<Exercise> mExercises;

        public ProgramExerciseAdapter(List<Exercise> exercises) {
            mExercises = exercises;
        }

        @Override
        public ProgramExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new ProgramExerciseHolder(view);
        }

        @Override
        public void onBindViewHolder(ProgramExerciseHolder holder, int position) {
            Exercise exercise = mExercises.get(position);
            holder.bindExercise(exercise);
        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }

        public void setExercises(List<Exercise> exercises) {
            mExercises = exercises;
        }
    }

    private void updateUI() {
        List<Exercise> exerciseList = ExerciseStorage.get(getActivity()).getExercisesByParentId(parentUUID);
        if(mAdapter == null) {
            mAdapter = new ProgramExerciseAdapter(exerciseList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setExercises(exerciseList);
            mAdapter.notifyDataSetChanged();
        }
        if(exerciseList.size() != 0)    mEmptyTextView.setVisibility(View.GONE);
        else    mEmptyTextView.setVisibility(View.VISIBLE);
    }

    private void updateUI(boolean isAdd, int num) {
        List<Exercise> exerciseList = ExerciseStorage.get(getActivity()).getExercisesByParentId(parentUUID);
        mAdapter.setExercises(exerciseList);
        if(isAdd)
            mAdapter.notifyItemInserted(num);
        else
            mAdapter.notifyItemRemoved(num);
        Log.d("PELF", "REMOVE/ADD POS: " + Integer.toString(num));
        if(exerciseList.size() != 0)    mEmptyTextView.setVisibility(View.GONE);
        else    mEmptyTextView.setVisibility(View.VISIBLE);
        if(!mRecyclerView.canScrollVertically(1)) { // 1 - down direction
            mFab.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PELF", "CODE " + Integer.toString(requestCode));
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_DELETE_EXERCISE) {
                UUID id = (UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID);
                Exercise exercise = ExerciseStorage.get(getActivity()).getExercise(id);
                int num = ExerciseStorage.get(getActivity()).getExercisesByParentId(exercise.getParentUUID()).indexOf(exercise);
                ExerciseStorage.get(getActivity()).deleteExerciseById(id);
                updateUI(false, num);
                Log.d("PELF", "DELETE EXERCISE " + exercise.getTitle());
            } else
                updateUI();
        }
    }
}
