package com.app.dmitryteplyakov.sportdiary.Programs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Dialogs.DeleteFragment;
import com.app.dmitryteplyakov.sportdiary.Dialogs.TitlePickerFragment;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by Dmitry on 23.07.2017.
 */

public class ProgramsListFragment extends Fragment {
    private ProgramsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyListTextView;
    private FloatingActionButton mFab;
    private static final String DIALOG_TRAINING_TITLE = "com.app.dialog_training_title";
    private static final String DIALOG_TRAINING_DELETE = "com.app.dialog_training_delete";
    private static final int REQUEST_TRAINING_TITLE = 3;
    public static final int REQUEST_TRAINING_DELETE = 4;
    private static final int REQUEST_TRAINING_EDIT_TITLE = 6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_programs, container, false);

        mEmptyListTextView = (TextView) v.findViewById(R.id.fragment_list_programs_empty_text);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_programs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFab = (FloatingActionButton) v.findViewById(R.id.fragment_list_programs_add_program_action_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                Training training = new Training();
                TrainingStorage.get(getActivity()).addTraining(training);
                TitlePickerFragment dialog = TitlePickerFragment.newInstance(training.getId());
                dialog.setTargetFragment(ProgramsListFragment.this, REQUEST_TRAINING_TITLE);
                dialog.show(manager, DIALOG_TRAINING_TITLE);

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

    private class ProgramsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView groupTitle;
        private Training mTraining;
        private CharSequence options[];
        public ProgramsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            groupTitle = (TextView) itemView.findViewById(R.id.group_name);
            options = new CharSequence[] {getString(R.string.edit_training_title_menu), getString(R.string.menu_delete_item)};
        }

        public void bindTraining(Training training) {
            mTraining = training;
            groupTitle.setText(training.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProgramExerciseListActivity.newIntent(getActivity(), mTraining.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder optionsDialog = new AlertDialog.Builder(getActivity());
            optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0) {
                        FragmentManager manager = getFragmentManager();
                        TitlePickerFragment optionsDialog = TitlePickerFragment.newInstance(mTraining.getId());
                        optionsDialog.setTargetFragment(ProgramsListFragment.this, REQUEST_TRAINING_EDIT_TITLE);
                        optionsDialog.show(manager, DIALOG_TRAINING_TITLE);
                    } else if(which == 1) {
                        FragmentManager manager = getFragmentManager();
                        DeleteFragment optionsDialog = DeleteFragment.newInstance(mTraining.getId());
                        optionsDialog.setTargetFragment(ProgramsListFragment.this, REQUEST_TRAINING_DELETE);
                        optionsDialog.show(manager, DIALOG_TRAINING_DELETE);
                    }
                }
            });
            optionsDialog.show();
            return true;
        }
    }

    private class ProgramsAdapter extends RecyclerView.Adapter<ProgramsHolder> {
        private List<Training> mTrainings;

        public ProgramsAdapter(List<Training> trainings) {
            mTrainings = trainings;
        }

        @Override
        public ProgramsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.programs_list_item, parent, false);
            return new ProgramsHolder(view);
        }

        @Override
        public void onBindViewHolder(ProgramsHolder holder, int position) {
            Training training = mTrainings.get(position);
            holder.bindTraining(training);
        }

        @Override
        public int getItemCount() {
            return mTrainings.size();
        }

        public void setTrainings(List<Training> trainings) {
            mTrainings = trainings;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUI();
    }

    private void updateUI() {
        List<Training> programList = TrainingStorage.get(getActivity()).getTrainings();
        if(mAdapter == null) {
            mAdapter = new ProgramsAdapter(programList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTrainings(programList);
            mAdapter.notifyDataSetChanged();
        }
        if(programList.size() != 0) {
            mEmptyListTextView.setVisibility(View.GONE);
        } else  mEmptyListTextView.setVisibility(View.VISIBLE);
    }
    private void updateUI(boolean isAdd, int num) {
        List<Training> programList = TrainingStorage.get(getActivity()).getTrainings();
        mAdapter.setTrainings(programList);
        if(isAdd)
            mAdapter.notifyItemInserted(num);
        else
            mAdapter.notifyItemRemoved(num);
        if(programList.size() != 0) {
            mEmptyListTextView.setVisibility(View.GONE);
        } else  mEmptyListTextView.setVisibility(View.VISIBLE);
        if(!mRecyclerView.canScrollVertically(1)) { // 1 - down direction
            mFab.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UUID id = null;
        if(requestCode == REQUEST_TRAINING_DELETE) {
            id = (UUID) data.getSerializableExtra(DeleteFragment.EXTRA_RETURN_DELETE_UUID);
            Training training = TrainingStorage.get(getActivity()).getTraining(id);
            int num = TrainingStorage.get(getActivity()).getTrainings().indexOf(training);
            ExerciseStorage.get(getActivity()).deleteExercisesByParentId(id);
            TrainingStorage.get(getActivity()).deleteTraining(id);
            updateUI(false, num);
        } else if(requestCode == REQUEST_TRAINING_TITLE) {
            if(resultCode == Activity.RESULT_CANCELED) {
                id = (UUID) data.getSerializableExtra(TitlePickerFragment.EXTRA_NEW_UUID);
                ExerciseStorage.get(getActivity()).deleteExercisesByParentId(id);
                TrainingStorage.get(getActivity()).deleteTraining(id);
            } else {
                id = (UUID) data.getSerializableExtra(TitlePickerFragment.EXTRA_NEW_UUID);
                Training training = TrainingStorage.get(getActivity()).getTraining(id);
                int num = TrainingStorage.get(getActivity()).getTrainings().indexOf(training);
                updateUI(true, num);
            }
        } else if((resultCode == Activity.RESULT_CANCELED) && (requestCode != REQUEST_TRAINING_EDIT_TITLE)) {
            TrainingStorage.get(getActivity()).deleteTraining(id);
            updateUI();
            Log.d("ProgramsListFragment", "Training is delete!");
        } else if(requestCode == REQUEST_TRAINING_EDIT_TITLE) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });
        }
    }
}
