package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.dmitryteplyakov.sportdiary.Core.Day.DayStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Exercise.ExerciseStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Nutrition.NutritionStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.Nutrition.NutritionListFragment;
import com.app.dmitryteplyakov.sportdiary.Programs.ProgramExerciseListFragment;
import com.app.dmitryteplyakov.sportdiary.Programs.ProgramsListFragment;
import com.app.dmitryteplyakov.sportdiary.R;
import com.app.dmitryteplyakov.sportdiary.Timer.TimerTemplatesListFragment;
import com.app.dmitryteplyakov.sportdiary.Weight.WeightListFragment;

import java.util.UUID;

/**
 * Created by Dmitry on 30.07.2017.
 */

public class DeleteFragment extends DialogFragment {
    private TextView mTitleTraining;
    private static final String ARG_TRAINING_UUID_DELETE = "com.app.arg_training_uuid_delete";
    public static final String EXTRA_RETURN_DELETE_UUID = "com.app.deletefragment.extra_return_delete_uuid";
    public static final int RESULT_DELETE_DAY_WITH_COMPTRAINING = 12;
    private int countOfDays;

    public static DeleteFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAINING_UUID_DELETE, id);
        DeleteFragment fragment = new DeleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);
        UUID id = (UUID) getArguments().getSerializable(ARG_TRAINING_UUID_DELETE);
        Log.d("DF", "CATCH: " + id.toString());
        mTitleTraining = (TextView) v.findViewById(R.id.delete_dialog_training_title);
        String alertString;
        countOfDays = DayStorage.get(getActivity()).getDaysByTrainingId((UUID) getArguments().getSerializable(ARG_TRAINING_UUID_DELETE)).size();
        if(getTargetRequestCode() == ProgramsListFragment.REQUEST_TRAINING_DELETE) {
            if (countOfDays != 0)
                alertString = getResources().getQuantityString(R.plurals.confirm_delete_training_with_day_dialog_plural, countOfDays, countOfDays);
            else
                alertString = getString(R.string.confirm_delete_training_dialog);
        }
        else
            alertString = getString(R.string.confirm_delete_training_dialog);

        mTitleTraining.setText(alertString);
        String dialogTitle = null;
        if(getTargetRequestCode() == ProgramsListFragment.REQUEST_TRAINING_DELETE)
            dialogTitle = TrainingStorage.get(getActivity()).getTraining(id).getTitle();
        else if(getTargetRequestCode() == ProgramExerciseListFragment.REQUEST_DELETE_EXERCISE) {
            Log.d("DF", "ID FOR TITLE: " + id.toString());
            dialogTitle = ExerciseStorage.get(getActivity()).getExercise(id).getTitle();
        } else if(getTargetRequestCode() == NutritionListFragment.REQUEST_DELETE_NUTRITION)
            dialogTitle = NutritionStorage.get(getActivity()).getNutrition(id).getProductTitle();
        else if(getTargetRequestCode() == WeightListFragment.REQUEST_WEIGHT_DELETE)
            dialogTitle = getString(R.string.confirm_delete_training_dialog);
        else if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_DELETE_TEMPLATE)
            dialogTitle = getString(R.string.confirm_delete_training_dialog);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            sendResult(Activity.RESULT_OK);

                    }
                })
                .setNegativeButton(R.string.no, null)
                .create();
    }

    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) return;
        Intent data = null;
        if(resultCode == Activity.RESULT_OK) {
            UUID id = (UUID) getArguments().getSerializable(ARG_TRAINING_UUID_DELETE);
            Log.d("DF", "SEND: " + id.toString());
            data = new Intent();
            data.putExtra(EXTRA_RETURN_DELETE_UUID, id);
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
    }
}
